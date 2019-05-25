package _0522;

import java.sql.Connection;

import _0522.DTO.IdVO;
import _0522.DTO.MaterialDTO;
import _0522.DTO.MemberDTO;
import _0522.DTO.MenuDTO;
import _0522.DTO.MenuItemDTO;
import _0522.DTO.OrderListDTO;
import _0522.DTO.PartTimeStaffDTO;
import _0522.DTO.RawMaterialDTO;
import _0522.DTO.RegularStaffDTO;
import _0522.DTO.StaffDTO;
import _0522.DTO.StockDTO;
import _0522.DTO.StoreDTO;

/*
 * View에 해당하는 Scripts 클래스와 실제로 각종 기능을 수행하는 QueryList 클래스를 연결해주는 클래스입니다 
 */
public class Pos_controller {
	Scripts scripts;
	QueryList query;
	IdVO userId;
	MaterialDTO material;
	MemberDTO member;
	MenuDTO menu;
	MenuItemDTO menuItem;
	PartTimeStaffDTO part;
	RegularStaffDTO regular;
	RawMaterialDTO raw;
	StaffDTO staff;
	StockDTO stock;
	StoreDTO store;

	OrderListDTO orderList;


	public void setScripts(Scripts scripts) {
		this.scripts = scripts;
	}

	public void setQueryList(Connection con) {
		this.query = new QueryList(con);
		query.setDTO(userId, material, member, menu, menuItem, part, regular, raw, staff, stock, store, orderList);
	}

	public void setDTO(IdVO userId, MaterialDTO material, MemberDTO member, MenuDTO menu, MenuItemDTO menuItem,
			PartTimeStaffDTO part, RegularStaffDTO regular, RawMaterialDTO raw, StaffDTO staff, StockDTO stock,
			StoreDTO store, OrderListDTO orderList) {

		this.userId = userId;
		this.material = material;
		this.member = member;
		this.menu = menu;
		this.menuItem = menuItem;
		this.part = part;
		this.regular = regular;
		this.raw = raw;
		this.staff = staff;
		this.stock = stock;
		this.store = store;
		this.orderList = orderList;

	}

	// pos_main 클래스가 가동하면 프로그램 최초로 실행되는 메소드
	public void start() {
		System.out.println("Pos_controller.start() start");
		// view 역할인 scripts에서 적절한 메소드를 호출한다
		// (로그인이 성공할 때까지 반복되도록 while문을 걸어주었다)
		while (true) {
			if (Pos_main.isClientAccess() == false)
				break;
			scripts.logIn();
		}
	}

	// scripts.logIn()의 실행결과 호출되는 메소드
	public void checkLogin(String id, String password) {
		// ID 및 Password 정보를 QueryList 클래스의 getLogInInfo 메서드로 넘깁니다
		boolean logInResult = query.getLogInInfo(id, password);
		// 로그인 정보 검증 결과를 해당하는 스크립트 메서드로 넘기어 사용자에게 알맞은 메세지를 보냅니다
		if (logInResult)
			scripts.logInSuccess();
		else if (!logInResult)
			scripts.logInFail();



		/*
		 * 죽은 코드입니다. 필요없는 부분이나, 혹시 몰라 주석으로 남겨두어습니다.
		 * 
		 * 
		 * // 넘겨받은 인자를 QueryList 클래스 중 한 메서드로 다시 넘겨야하지만 // 점장 List는 프로그램 내에서 관리하기로 했기에
		 * 일단 checkLogin 메서드 안에서 처리합니다 // 추후 점장List DB도 오라클로 넘긴다면, QueryList 클래스에 적절한
		 * 메서드를 생성해야 합니다 Iterator<IdVO> itr = ServerMain.list.iterator();
		 * while(itr.hasNext()) { IdVO temp = itr.next(); if(temp.getId().equals(id)) {
		 * if(temp.getPassword().equals(password)) { //성공시 유저에게 성공 메세지 보내는 View 메서드를 실행
		 * System.out.println("Client logIn success"); scripts.logInSuccess(); }else {
		 * //실패시 유저에게 실패메세지 보내는 View 메서드를 실행
		 * System.out.println("Client logIn Fail:type_password");
		 * scripts.logInFailTypePassword();
		 * 
		 * } //실패시 유저에게 실패메세지 보내는 View 메서드를 실행 }
		 * System.out.println("Client logIn Fail:type_id"); scripts.logInFailTypeId(); }
		 * System.out.println("No More data"); scripts.noData();
		 */
	}

	public StaffDTO[] showStaffList() {
		return query.showStaffList();
	}
	public StaffDTO[] searchStaff(String staffName) {
		return query.searchStaff(staffName);
	}
	public void staffEnroll() {
		//직원관리>직원 등록
		
	}
	public void staffSchedule() {
		//직원관리>스케쥴관리
	}

	public void setDTOdata() {
		query.setDTOData();
	}
	//매장정보>정보수정
	public void storeInfoMotify(String num, String name, String owner, String open, String close, String phone, String addr) {
		
	}
	//매장관리>재고관리>입고(원재료)
	public void rawstock(String id, String name, String category, String stock, String cost) {
		
	}
	//매장관리>재고관리>입고(비품)
	public void matestock(String id, String name, String stock, String cost) {
		
	}
	
	public void updateStaffInfo() {
		query.updateStaffInfo();
	}

	
	
	
	
	public MemberDTO[] showMember() {
		return query.showMembers();
	}
	
//	public MemberDTO[] searchMember() {/일단주석처리
//		return query.searchMember();
//	}
}
