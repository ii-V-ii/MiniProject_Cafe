
package _0522;

// 동기화 확인용 주석
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

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
 * 1차메뉴 (매장,메뉴,고객,직원관리)와 2차 메뉴(매장관리>매장정보, 매출정보, 재고관리) 는 인터페이스로 관리
 * 3차메뉴 (매장정보>기본정보, 수정, 수입확인, 지출확인)는 scripts 클래스 내의 메소드로 관리합니다
 * 
 */

// 유저에게 보여줄 메뉴 인터페이스(1차, 2차 메뉴)
interface MainMenu {
	String STORE = "1", MENU = "2", CUSTOMER = "3", STAFF = "4";
}

/* 매장관리 */
interface StoreMenu {
	String STOREINFO = "1", SALESINFO = "2", STOCK = "3";
}

/* 메뉴관리 */
interface menuMenu {
	String MENUINFO = "1", MENUENROLL = "2", SEARCH = "3";
}

/* 고객관리 */
interface customerMenu {
	String CUSTINFO = "1", CUSTENROLL = "2", HISTORY = "3";
}

/* 직원관리 */
interface staffMenu {

	String STAFFINFO = "1", STAFFENROLL = "2", SCHEDULE = "3", PAY = "4";

}

public class Scripts {
	Socket socket;
	BufferedReader br;
	PrintWriter pw;
	Pos_controller posControl;

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

	String choose;

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

	public void setPosControl(Pos_controller posControl) {
		this.posControl = posControl;
	}

	Scripts(Socket socket) {
		this.socket = socket;
		try {
			this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 유저에게 메세지 보내는 용도의 pw 메서드
	public void send(String msg) {
		pw.println(msg);
		pw.flush();
	}

	// 유저에게서 선택 받아오는 용도의 br 메서드
	// Scanner 대신 쓰세요!
	public String receive() {
		String line = null;
		try {
			if ((line = br.readLine()) != null)
				return line;
		} catch (IOException e) {
			System.out.println("Client Exit");
			Pos_main.setClientAccess(false);
		}
		return null;
	}

	
	public int receiveInt() {
		int line = -1;
		try {
			line = br.read();
				return line;
		} catch (IOException e) {
			System.out.println("Client Exit");
			Pos_main.setClientAccess(false);
		}
		return -1;
	}



//	public int receiveInt() {
//		int line = -1;
//		try {
//			line = br.read();
//			return line;
//		} catch (IOException e) {
//			System.out.println("Client Exit");
//			Pos_main.setClientAccess(false);
//		}
//		return -1;
//	}


	// 최초 프로그램 실행시 로그인 기능
	// 메서드 완성할 떄의 예시로 봐주세요
	public void logIn() {
		// 유저에게 메세지를 전달하고 유저의 입력을 받는다
		send("카페관리 프로그램을 시작합니다");
		send("아이디를 입력하세요");
		String id = receive();
		send("패스워드를 입력하세요");
		String password = receive();
		// 유저의 입력을 controller 소속의 적절한 메소드로 넘긴다
		posControl.checkLogin(id, password);
	}

	public void logInSuccess() {
		send("로그인에 성공하였습니다");
		// 원래라면 controller의 메서드를 호출해야하나
		// 편의를 위해 view 메서드 -> view 메서드 의 이동은 scripts 클래스내에서
		// 직접적으로 이루어지도록 하겠습니다
		posControl.setDTOdata();
		mainMenu();
	}

	public void logInFail() {
		send("로그인에 실패했습니다");
		send("id 또는 password를 확인해주세요");
	}

	// 유저에게서 메인메뉴를 보여주고 선택받는다
	public void mainMenu() {
		send("1. 매장관리");
		send("2. 메뉴관리");
		send("3. 고객관리");
		send("4. 직원관리");
		send("5. 프로그램 종료");
		send(">>선택 :");


		choose = receive();

		switch (choose) {
		case MainMenu.STORE:
			storeMenu();
			break;
		case MainMenu.MENU:
			menuMenu();
			break;
		case MainMenu.CUSTOMER:
			customerMenu();
			break;
		case MainMenu.STAFF:
			staffMenu();
			break;
		case "5":
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		default:
			send("다시선택하세요~");
			mainMenu();
			break;
		}// switch



	


	}// mainMenu

	// 유저에게서 매장관리 메뉴를 보여주고 선택받는다
	public void storeMenu() {
		send("1. 매장정보");
		send("2. 매출정보");
		send("3. 재고관리");
		send("선택>>");
		choose = receive();


		switch (choose) {
		case StoreMenu.STOREINFO:
			storeInfo();
			break;
		case StoreMenu.SALESINFO:
			saleInfo();
			break;
		case StoreMenu.STOCK:
			stock();
			break;
		default:
			send("다시선택하세요~");
			mainMenu();
			break;
		}// switch

	}

	// 유저에게서 메뉴관리 메뉴를 보여주고 선택받는다
	public void menuMenu() {
		send("1. 메뉴정보");
		send("2. 메뉴등록");
		send("3. 메뉴검색");
		send("선택>>");
		choose = receive();


		switch (choose) {
		case menuMenu.MENUINFO:
			menuInfo();
			break;
		case menuMenu.MENUENROLL:
			menuEnroll();
			break;
		case menuMenu.SEARCH:
			search();
			break;
		default:
			send("다시선택하세요~");
			mainMenu();
			break;
		}// switch


	}
/////////////////////////////////////////////////////////////////////
	// 유저에게서 고객관리 메뉴를 보여주고 선택받는다
	public void customerMenu() {

		send("1.회원정보");
		send("2.회원등록");
		send("3.고객구매이력");
		choose = receive();

		while (true) {
			switch (choose) {
			case customerMenu.CUSTINFO:
				custInfo();//1.회원정보>1.모두보기
				break;
			case customerMenu.CUSTENROLL:
				searchMember();//1.회원정보>2.고객정보
				break;	
			case customerMenu.HISTORY:
				showMemberDetail();//1.회원정보>3.정보보기
				break;	
			}
		}
	}
	
	public void custInfo() {
		send("1.모두보기");
		send("2.고객검색");
		send("3.정보보기");
		choose = receive();
		switch (choose) {
		case customerMenu.CUSTINFO:
			custInfo();
			break;
		}
	}
	public void custenroll() {
		send("1.모두보기");
		send("2.고객검색");
		send("3.정보보기");
		choose = receive();
		switch (choose){
		case customerMenu.CUSTENROLL:
			searchMember();
			break;
		}
	}
	public void history() {
		send("1.모두보기");
		send("2.고객검색");
		send("3.정보보기");
		choose = receive();
		
		switch (choose) {
		case customerMenu.HISTORY:
			showMemberDetail();
			break;

		}
	}
	// 유저에게서 직원관리 메뉴를 보여주고 선택받는다


	public void staffMenu() {
		// String STAFFINFO = "1", STAFFENROLL = "2", SCHEDULE = "3";
		send("1. 직원정보 확인");
		send("2. 직원 등록");
		send("3. 스케쥴 관리");
		send("4.급여관리");//보류
		choose = receive();

		switch (choose) {
		case staffMenu.STAFFINFO:
			staffDefaultInfo();
			break;
		case staffMenu.STAFFENROLL:
			posControl.staffEnroll();
			break;
		case staffMenu.SCHEDULE:
			posControl.staffSchedule();
			break;
//		case staffMenu.PAY:
//		break;
		default:
			break;
		}
	}
	
	public void staffDefaultInfo() {
		StaffDTO[] staffList = posControl.showStaffList();
		send("=====직원 명단 =====");
		for(int i = 0;i<staffList.length;i++) {
			send(""+staffList[i].getName());
			send(""+staffList[i].getPhone());
			send(""+staffList[i].getSex());
			send(""+staffList[i].getBirth());
			send(""+staffList[i].getJoinDate());
			send(""+staffList[i].getLeaveDate());
			send(""+staffList[i].getWorkstyle());
		}
	}	
	
	

	// ==2차메뉴
	// 메서드=====================================================================
	// 매장관리 > 매장정보
	public void storeInfo() {
		send("1. 기본정보"); // storeInfoDefault()
		send("2. 수정");// storeInfoMotify()
		send("3. 수입확인");// checkIncome()
		send("4. 지출확인");// checkOutcome()
		send("선택 : ");
		choose = receive();

		switch (choose) {
		case "1":
			storeInfoDefault();
			break;
		case "2":
			storeInfoMotify();
			break;
		case "3":
			checkIncome();
			break;
		case "4":
			checkOutcome();
			break;
		default:
			send("다시선택하세요.");
			mainMenu();
			break;
		}// switch

	}// storeInfo

	// 매장관리 > 매출정보
	public void saleInfo() {
		send("1. 기본정보"); // salesInfoDefault()
		send("2. 시간별 검색"); // salesSearchTimes()
		send("3. 메뉴별 검색"); // salesSearchMenus()
		send("선택 :");
		choose = receive();

		switch (choose) {
		case "1":
			salesInfoDefault();
			break;
		case "2":
			salesSearchTimes();
			break;
		case "3":
			salesSearchMenus();
			break;
		default:
			send("다시선택하세요.");
			mainMenu();
			break;
		}// switch

	}// saleInfo

	// 매장관리 > 재고관리
	public void stock() {
		send("1. 비품 재고"); // stockNow()
		send("2. 입고 관리"); // stockManage()
		send("선택 :");
		String choose = receive();

		switch (choose) {
		case "1":
			stockNow();
			break;
		case "2":
			stockManage();
			break;
		default:
			send("다시 선택하세요.");
			mainMenu();
			break;
		}// switch

	}// stock

	// 메뉴관리 > 메뉴정보
	public void menuInfo() {

		send("1. 기본정보"); // menuInfoDefault()
		send("2. 수정"); // menuModify()
		send("3. 삭제"); // menuDelete()
		send("4. 메뉴활성화");// menuOnOff
		send("선택 :");
		choose = receive();

		switch (choose) {
		case "1":
			menuInfoDefault();
			break;
		case "2":
			menuModify();
			break;
		case "3":
			menuDelete();
			break;
		case "4":
			menuOnOff();
			break;
		default:
			send("다시 선택하세요.");
			mainMenu();
			break;
		}// switch
	}// menuInfo

	// 메뉴관리 > 메뉴등록
	public void menuEnroll() {
		// sql이랑 연결해야해 ========================================

	}// menuEnroll

	// 메뉴관리 > 메뉴검색
	public void search() {
		send("1. 메뉴명");
		send("2. 메뉴 종류");
		send("선택 :");
		choose = receive();

		switch (choose) {
		case "1":
			searchMenuName();
			break;
		case "2":
			searchMenuCategory();
			break;
		default:
			send("다시선택하세요.");
			mainMenu();
			break;

		}// switch

	}// search


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
///////////////////////////////////////////////////////
	// 고객관리>회원정보 내부 메뉴
	public void showMembers() {
		send("1.모두보기");
		send("2.고객검색");
		send("3.정보보기");
		
		while(true) {
			//QueryList.member();
			
		}
		
		
	}

	public void searchMember() {
	
		
		
	}

	public void showMemberDetail() {
		
		
	}

	// 고객관리>회원정보>정보보기 내부 메뉴(1.수정)
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