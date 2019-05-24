package _0522;

import java.sql.Connection;
import java.util.Iterator;
import _0522.DTO.IdVO;

/*
 * View에 해당하는 Scripts 클래스와 실제로 각종 기능을 수행하는 QueryList 클래스를 연결해주는 클래스입니다 
 */
public class Pos_controller {
	Scripts scripts;
	QueryList query;
	
	public void setScripts(Scripts scripts) {
		this.scripts = scripts;
	}
	
	public void setQueryList(Connection con) {
		this.query = new QueryList(con);
	}
	
	// pos_main 클래스가 가동하면 프로그램 최초로 실행되는 메소드
	public void start() {
		System.out.println("Pos_controller.start() start");
		//view 역할인 scripts에서 적절한 메소드를 호출한다
		//(로그인이 성공할 때까지 반복되도록 while문을 걸어주었다)
		while(true) {
			if(Pos_main.isClientAccess() == false)
				break;
		scripts.logIn();
		}
	}
	
	// scripts.logIn()의 실행결과 호출되는 메소드
	public void checkLogin(String id, String password) {
		// ID 및 Password 정보를 QueryList 클래스의 getLogInInfo 메서드로 넘깁니다
		boolean logInResult = query.getLogInInfo(id, password);
		// 로그인 정보 검증 결과를 해당하는 스크립트 메서드로 넘기어 사용자에게 알맞은 메세지를 보냅니다
		if(logInResult)
			scripts.logInSuccess();
		else if(!logInResult)
			scripts.logInFail();
		
	/*	죽은 코드입니다.
	 *  필요없는 부분이나, 혹시 몰라 주석으로 남겨두어습니다.
	 * 
	 * 
		// 넘겨받은 인자를 QueryList 클래스 중 한 메서드로 다시 넘겨야하지만
		// 점장 List는 프로그램 내에서 관리하기로 했기에 일단 checkLogin 메서드 안에서 처리합니다
		// 추후 점장List DB도 오라클로 넘긴다면, QueryList 클래스에 적절한 메서드를 생성해야 합니다
		Iterator<IdVO> itr = ServerMain.list.iterator();
		while(itr.hasNext()) {
			IdVO temp = itr.next();
			if(temp.getId().equals(id)) {
				if(temp.getPassword().equals(password)) {
					//성공시 유저에게 성공 메세지 보내는 View 메서드를 실행
					System.out.println("Client logIn success");
					scripts.logInSuccess();
				}else {
					//실패시 유저에게 실패메세지 보내는 View 메서드를 실행
					System.out.println("Client logIn Fail:type_password");
					scripts.logInFailTypePassword();

				}
				//실패시 유저에게 실패메세지 보내는 View 메서드를 실행
			}			
			System.out.println("Client logIn Fail:type_id");
			scripts.logInFailTypeId();
		}
		System.out.println("No More data");
		scripts.noData();
		*/
	}
	
	public void setDTOdata() {
		query.setDTOData();
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
