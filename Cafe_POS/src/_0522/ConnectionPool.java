package _0522;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

public class ConnectionPool {
	static {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	ArrayList<Connection> free; // 미사용 Connection 객체
	ArrayList<Connection> used; // 사용 중 Connecton 객체
	String url;
	String user;
	String password;
	int initialCons = 0; // 초기 커넥션 수
	int maxCons = 0; // 최대 커넥션 수
	int numCons = 0; // 현재 커넥션 수

	/*
	 * Singleton 패턴 데이터베이스 접근처럼 일원적으로 관리되어야 할 업무는 여러군데서 접근객체를 만들도록 허용하지않고 단 하나의 객체만
	 * 만들도록 강요하고 이 한 개의 객체로만 사용할 것을 강요(유도)하는 프로그래밍 방식이다. 1) static으로 클래스 변수를 선언한다 2)
	 * 생성자를 private으로 선언한다 3) 일반적으로 클래스 객체를 리턴하는 getInstance() 메서드를 만든다 4)
	 * getInstance()에서는 최초로 한번만 객체를 생성하도록 한다 5) 외부 클래스에서 이 클래스 객체를 요청할 때는 반드시
	 * getInstance() 만을 사용하여 객체를 얻을 수 있다 6) 아무리 많은 스레드에서 요청해도 반환하는 객체는 유일하고 동일한 객체이다
	 */

	static ConnectionPool cp = null; // Singleton 패턴 사용

	public static ConnectionPool getInstance(String url, String user, String password, int initCons, int maxCons) {
		try {
			if (cp == null) {
				// static 메서드에서 동기화하는 방법
				synchronized (ConnectionPool.class) {
					// ConnectionPool.class는 ConnectionPool클래스 자체를 의미한다. 인스턴스를 의미하는 this.를 사용하지 못하기
					// 때문에 사용
					cp = new ConnectionPool(url, user, password, initCons, maxCons);
				}
				return cp;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cp;
	}

	private ConnectionPool(String url, String user, String password, int initCons, int maxCons)
			throws SQLException {
		this.url = url; // 오라클 주소
		this.user = user; // 관리자 id
		this.password = password; // 관리자 password
		this.initialCons = initCons; // 최초에 connection을 몇개 만들지
		this.maxCons = maxCons; // 최대로 몇개까지 만들지

		if (initialCons < 0) // -1을 주면 알아서 결정해라
			this.initialCons = 5;
		if (maxCons < 0)
			this.maxCons = 10;

		// 초기 커넥션 개수를 각각의 ArrayList에 저장할 수 있도록 초기 커넥션 수많큼 ArrayList를 생성한다
		free = new ArrayList<Connection>(this.initialCons);
		used = new ArrayList<Connection>(this.initialCons);

		// 초기 커넥션 개수만큼 Connection 객체를 생성하자
		while (this.numCons < this.initialCons) {
			// 1개씩 객체를 추가하는 메서드
			addConnection();
		}

	}// Connection Pool

	private void addConnection() throws SQLException {
		free.add(getNewConnection());
	}

	private Connection getNewConnection() throws SQLException {
		Connection con = null;
		con = DriverManager.getConnection(this.url, this.user, this.password);
		// System.out.println("About to connect to " + con);
		++this.numCons;

		return con;
	}

	// 외부에서 오라클 연결객체를 요구하는 메서드
	public synchronized Connection getConnection() throws SQLException {
		if (free.isEmpty()) {
			System.out.println("connection 추가 개설 시작");
			while (this.numCons < maxCons) {
				addConnection();
			}
		}
		Connection _con = free.get(free.size() - 1);
		free.remove(_con);
		used.add(_con);
		// System.out.println("겟커넥션:"+used.size());
		return _con;
	}// getConnection()

	// 사용중인 오라클 연결 객체를 반납하는 메서드
	// used->free
	public synchronized void releaseConnection(Connection _con) {
		try {
			if (used.contains(_con)) {
				used.remove(_con);
				free.add(_con);
			} else {
				throw new SQLException("ConnectionPool:에 있지 않습니다");
			}
		} catch (SQLException e) {
			System.out.println("e.getMessage()");
		}
	}

	// 모든 Connection 을 종료한다
	// 서비스 종료시 ConnectionPool을 그만 사용한다
	public void closeAll() {
		// used에 있는 커넥션을 모두 삭제하고 닫는다
		for (int i = 0; i < used.size(); i++) {
			Connection _con = (Connection) used.get(i);
			try {
				used.remove(i--);
				_con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		// free에 있는 커넥션을 모두 삭제하고 닫는다
		for (int i = 0; i < free.size(); i++) {
			Connection _con = (Connection) free.get(i);
			try {
				free.remove(i--);
				_con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	// 최대 연결 개수
	public int getMaxCons() {
		return this.maxCons;
	}

	// 현재 할당(연결)된 개수
	public int getNumCons() {
		return this.numCons;
	}
	
	public int getFreeCons() {
		return this.free.size();
	}
	
	public int getUsedCons() {
		return this.used.size();
	}

}// public class
