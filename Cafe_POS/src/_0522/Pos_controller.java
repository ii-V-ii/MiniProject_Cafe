package _0522;

import java.sql.Connection;

import java.sql.Date;
import java.util.ArrayList;

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

import oracle.sql.DATE;

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

	public void setQueryList(Connection con, Scripts scripts) {
		this.query = new QueryList(con, scripts);
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

	}
	//////////////////////////////////////////////////////////////////////////////
		////판매
	//////////////////////////////////////////////////////////////////////////////

	public void calcualteOrder() {
		query.calcualteOrder();
	}

	public void createPayment(MemberDTO member) {
		query.createPayment(member);
	}
	//////////////////////////////////////////////////////////////////////////////
	public StaffDTO[] showStaffList() {
		return query.showStaffList();

	}

	public StaffDTO[] searchStaff(String staffName) {
		return query.searchStaff(staffName);
	}


	public void staffEnroll(StaffDTO staff) {
		// 직원관리>직원 등록
		query.staffEnroll(staff);

	}

	public void changeTime(StaffDTO staff, int workDay, int workTime, int pay) {
		query.changeTime(staff, workDay, workTime, pay);
	}

	public void changeWorkday(StaffDTO staff, int workDay) {
		query.changeWorkday(staff, workDay);
	}
	
	public void changeWorkTime(StaffDTO staff, int workTime) {
		query.changeWorkTime(staff, workTime);
		
	}
	
	public void changePayMonth(StaffDTO staff, int pay) {
		query.changePayMonth(staff, pay);
	}
	
	public void changePayHour(StaffDTO staff, int pay) {
		query.changePayHour(staff, pay);
	}
	public MenuItemDTO[] menuInfoDefault() {
		return query.menuInfoDefault();
	}

	public void setDTOdata() {
		query.setDTOData();
	}


	public String[][] salesInfoDefault() {
		return query.salesInfoDefault();
	}

	public ArrayList<String[]> salesInfoDefault(String[] date) {
		return query.salesInfoDefault(date);

	}

	public String[][] salesMenuDate(String menuName) {
		return query.salesMenuDate(menuName);
	}

	public String salesMenuDate30(String menuName) {

		return query.salesMenuDate30(menuName);
	}

	public String salesMenuDate365(String menuName) {

		return query.salesMenuDate365(menuName);
	}

	// 혜영
	// =============================================================================
	// 매장정보>기본정보
	public StoreDTO storeInfoDefault() {
		return query.storeInfoDefault();
	}

	// 매장정보>정보수정
	public void storeInfoMotify(StoreDTO sDto) {
		query.storeInfoMotify(sDto);
	}

	// 매장관리>재고관리>현재비품현황
	public void showstockList() {
		query.showstockList();
	}


	// 매장관리>재고관리>입고(원재료)
	public void temp(RawMaterialDTO raw) {
		query.rawstock(raw);
	}

	// 매장관리>재고관리>입고(비품)
	public void matestock(MaterialDTO raw) {
		query.matestock(raw);
	}

	// 메뉴관리>메뉴정보>수정
	public void menuModify(MenuItemDTO mDto) {
		query.menuModify(mDto);
	}

	public void menuEnroll(MenuItemDTO eDto) {
		query.menuEnroll(eDto);
	}

// ============================================================================
	public void updateStaffInfo(StaffDTO staff) {
		query.updateStaffInfo(staff);
	}


	public void deleteStaffInfo(StaffDTO staff) {
		query.deleteStaffInfo(staff);
	}
	public ArrayList<String[]> showSalaryOption() {
		return query.showSalaryOption();
	}


// ============================================================================
	public MemberDTO[] showMember() {
		return query.showMembers();
	}


	public MemberDTO[] searchMember(String memberName) {// 일단주석처리
		return query.searchMember(memberName);
	}

	public OrderListDTO[] lastBuyingData(MemberDTO member) {
		return query.lastBuyingData(member);
	}


	public MenuDTO[] mostBuyingData(MemberDTO member) {
		return query.mostBuyingData(member);
	}

}
