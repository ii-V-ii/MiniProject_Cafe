package _0522;

import java.util.Iterator;

import _0522.DTO.IdVO;

/*
 * View에 해당하는 Scripts 클래스와 실제로 각종 기능을 수행하는 QueryList 클래스를 연결해주는 클래스입니다 
 */
public class Pos_controller {
	static Pos_controller posControl = null;
	Scripts scripts;
	
	public void setScripts(Scripts scripts) {
		this.scripts = scripts;
	}

	private Pos_controller(){
	}
	
	public static Pos_controller getPosInstance() {
		if(posControl==null)
			posControl = new Pos_controller();
		return posControl;		
	}
	
	// pos_main 클래스가 가동하면 프로그램 최초로 실행되는 메소드
	public void start() {
		//view 역할인 scripts에서 적절한 메소드를 호출한다
		//(로그인이 성공할 때까지 반복되도록 while문을 걸어주었다)
		while(true) {
		scripts.logIn();
		}
	}
	
	
	// scripts.logIn()의 실행결과 호출되는 메소드
	public void checkLogin(String id, String password) {
		// 넘겨받은 인자를 QueryList 클래스 중 한 메서드로 다시 넘겨야하지만
		// 점장 List는 프로그램 내에서 관리하기로 했기에 일단 checkLogin 메서드 안에서 처리합니다
		// 추후 점장List DB도 오라클로 넘긴다면, QueryList 클래스에 적절한 메서드를 생성해야 합니다
		Iterator<IdVO> itr = ServerMain.list.iterator();
		while(itr.hasNext()) {
			IdVO temp = itr.next();
			if(temp.getId().equals(id)) {
				if(temp.getPassword().equals(password)) {
					//성공시 유저에게 성공 메세지 보내는 View 메서드를 실행
					scripts.logInSuccess();
				}else {
					//실패시 유저에게 실패메세지 보내는 View 메서드를 실행
					scripts.logInFailTypePassword();
				}
				//실패시 유저에게 실패메세지 보내는 View 메서드를 실행
				scripts.logInFailTypeId();
			}			
		}
	}

}
