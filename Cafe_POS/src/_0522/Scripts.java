package _0522;

/*
 * 1차메뉴 (매장,메뉴,고객,직원관리)와 2차 메뉴(매장관리>매장정보, 매출정보, 재고관리) 는 인터페이스로 관리
 * 3차메뉴 (매장정보>기본정보, 수정, 수입확인, 지출확인)는 scripts 클래스 내의 메소드로 관리합니다
 * 
 */

// 유저에게 보여줄 메뉴 인터페이스(1차, 2차 메뉴)
interface MainMenu {
	int STORE = 1, MENU = 2, CUSTOMER = 3, STAFF = 4;
}

interface StoreMenu {

}

interface menuMenu {

}

interface customerMenu {

}

interface staffMenu {

}

public class Scripts {
	// 유저에게서 메인메뉴를 보여주고 선택받는다
	public void mainMenu() {

	}

	// 유저에게서 매장관리 메뉴를 보여주고 선택받는다
	public void storeMenu() {

	}

	// 유저에게서 메뉴관리 메뉴를 보여주고 선택받는다
	public void menuMenu() {

	}

	// 유저에게서 고객관리 메뉴를 보여주고 선택받는다
	public void customerMenu() {

	}

	// 유저에게서 직원관리 메뉴를 보여주고 선택받는다
	public void staffMenu() {

	}

	// -----------------------------
	// -----------------------------여기부터 3차메뉴 관리
	// 매장관리>매장정보 내부 메뉴
	public void storeInfoDefault() {

	}

	public void storeInfoMotify() {

	}

	public void checkIncome() {

	}

	public void checkOutcome() {

	}

	// 매장관리>매출정보 내부 메뉴
	public void salesInfoDefault() {

	}

	public void salesSearchTimes() {

	}

	public void salesSearchMenus() {

	}

	// 매장관리>재고관리 내부 메뉴
	public void stockNow() {

	}

	public void stockManage() {

	}

	// 메뉴관리>메뉴정보 내부 메뉴
	public void menuInfoDefault() {

	}
	public void menuModify() {
		
	}
	public void menuDelete() {
		
	}
	public void menuOnOff() {
		
	}

	// 메뉴검색>메뉴검색 내부 메뉴
	public void searchMenuName() {

	}
	public void searchMenuCategory() {

	}
	// 고객관리>회원정보 내부 메뉴
	public void showMembers() {
		
	}
	public void searchMember() {
		
	}
	public void showMemberDetail() {
		
	}
	// 고객관리>회원정보>정보보기 내부 메뉴
	public void showMemberDetailModify() {
		
	}
	public void showMemberDetailDelete() {
		
	}
	// 고객관리>고객구매이력 내부 메뉴
	public void lastBuyingRecord() {
		
	}
	public void MostBuyingRecord() {
		
	}
	// 직원관리>직원정보 내부 메뉴
	public void staffInfoDefault() {
		
	}
	public void staffInfoModify() {
		
	}
	public void staffInfoDelete() {
		
	}
	// 직원관리>급여관리 내부 메뉴
	public void staffSalaryManage() {
		
	}



}