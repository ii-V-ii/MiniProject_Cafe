
package _0522;

// 동기화 확인용 주석
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
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
	String CUSTINFO = "1", CUSTENROLL = "2", HISTORY = "3", EXIT = "4", RETURN_CUSTOMERMENU = "5";
}

/* 직원관리 */
interface staffMenu {
	String STAFFINFO = "1", STAFFENROLL = "2", SCHEDULE = "3", PAY = "4";

}

public class Scripts {

	private static final String String = null;

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

	String text;

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
		while (true) {
			mainMenu();
		}
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

/////////////////////////////////////////////////////////////////////내꺼

	// 유저에게서 고객관리 메뉴를 보여주고 선택받는다
	public void customerMenu() {

		while (true) {
			send("1.회원정보");
			send("2.회원등록");
			send("3.고객구매이력");
			send("4.이전 메뉴로 되돌아가기");
			send("선택 >> ");

			choose = receive();
			switch (choose) {
			case customerMenu.CUSTINFO:
				custInfo();// 1.회원정보>1.모두보기
				break;
			case customerMenu.CUSTENROLL:
				custenroll();
				break;
			case customerMenu.HISTORY:
				history();
				break;
			case customerMenu.EXIT:
				return;
			}
		}
	}

	public void custInfo() {
		send("1. 모든 고객 보기");
		send("2. 고객 검색");
		send("3. 상세 고객 정보");
		send("4.되돌아가기");
		send("5.이전 메뉴로 되돌아가기");// customerMenu();
		send("선택 >> ");
		choose = receive();

		switch (choose) {
		case "1":
			showMembers();
			break;
		case "2":
			break;
		case "3":
			break;
		case customerMenu.EXIT:
			send("다시선택하세요>>");
			custInfo();
			break;
		case customerMenu.RETURN_CUSTOMERMENU:
			send("처음화면으로 되돌아갑니다.");
			customerMenu();
			break;
		}
	}

	public void custenroll() {

	}

	public void history() {
		send("1. 최신 구매이력");
		send("2. 최다 구매이력");
		choose = receive();
		switch (choose) {
		case "1":
			lastBuyingRecord();
			break;
		case "2":
			MostBuyingRecord();
			break;
		default:
			send("잘못 입력하셨습니다");
			break;

		}
	}

	// 유저에게서 직원관리 메뉴를 보여주고 선택받는다

	public void staffMenu() {
		// String STAFFINFO = "1", STAFFENROLL = "2", SCHEDULE = "3";
		send("1. 직원정보 확인");
		send("2. 직원 등록");
		send("3. 스케쥴 관리");
		send("4.급여관리");// 보류
		send("선택 >> ");

		choose = receive();

		switch (choose) {
		case staffMenu.STAFFINFO:
			staffMenuFirst();

			break;
		case staffMenu.STAFFENROLL:
			staffEnroll();
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

	public void staffMenuFirst() {
		send("1. 모든 직원을 본다");
		send("2. 직원정보 수정");
		send("3. 직원정보 삭제");
		choose = receive();
		switch (choose) {
		case "1":
			staffDefaultInfo();
			break;
		case "2":
			staffInfoModify();
			break;
		case "3":
			staffInfoDelete();
			break;
		default:
			send("잘 못 입력하셨습니다");
			break;
		}
	}

	public void staffDefaultInfo() {
		StaffDTO[] staffList = posControl.showStaffList();
		send("이름 \t전화번호\t\t성별\t생년월일\t입사일\t\t\t퇴사일\t\t\t고용형태");
		for (int i = 0; i < staffList.length; i++) {
			send("" + staffList[i].getName() + "\t" + staffList[i].getPhone() + "\t" + staffList[i].getSex() + "\t"
					+ staffList[i].getBirth() + "\t" + staffList[i].getJoinDate() + "\t" + staffList[i].getLeaveDate()
					+ "\t" + staffList[i].getWorkstyle());
		}
	}

	public void staffInfoModify() {
		send("수정 할 직원의 이름을 입력하세요");
		choose = receive();
		StaffDTO[] staffList = posControl.searchStaff(choose);
		for (StaffDTO staff : staffList) {
			send("" + staff.getId() + "\t" + staff.getName() + "\t" + staff.getPhone() + "\t" + staff.getJoinDate());
		}
		if (staffList.length == 1) {
			staff = staffList[0];
			send("위 직원의 정보를 수정하시겠습니까? y/n");
			choose = receive();
			switch (choose) {
			case "y":
				staffInfoModifyDetail();
				break;
			case "n":
				send("취소하셨습니다");
				break;
			default:
				send("다시 입력하세요");
				break;
			}
		} else if (staffList.length > 1) {
			send("동명이인이 있습니다. 수정하려는 직원의 직원번호를 입력하세요");
			choose = receive();
			boolean check = false;
			for (StaffDTO temp : staffList) {
				if (choose.contentEquals(temp.getId())) {
					staff = temp;
					check = true;
				}
			}
			if (!check)
				send("직원 번호를 다시 확인해주세요");
			else if (check) {
				send("위 직원의 정보를 수정하시겠습니까? y/n");
				choose = receive();
				switch (choose) {
				case "y":
					staffInfoModifyDetail();
					break;
				case "n":
					send("변경을 취소했습니다");
					break;
				default:
					send("잘 못 입력하셨습니다");
					break;
				}
			} else
				send("error:입력을 다시 확인해주세요");

		} else {
			send("일치하는 직원이 없습니다");
		}

	}

	public void staffInfoModifyDetail() {

		send("새로운 정보를 입력하세요(변경을 원치 않으시면 enter를 입력하세요)");
		String newData = null;
		send("이름: ");
		newData = receive();
		if (!newData.contentEquals("")) {
			staff.setName(newData);
			System.out.println("해당 직원의 이름을 " + staff.getName() + "로 변경완료");
		}

		send("입사일: ");
		send("YY/MM/DD 의 형태로 입력하세요");
		newData = receive();
		if (!newData.contentEquals("")) {
			staff.setJoinDate(newData);
			System.out.println("해당 직원의 입사일을 " + staff.getJoinDate() + "로 변경완료");
		}

		send("퇴사일: ");
		send("YY/MM/DD 의 형태로 입력하세요");
		newData = receive();
		if (!newData.contentEquals("")) {
			staff.setLeaveDate(newData);
			System.out.println("해당 직원의 퇴사일을 " + staff.getLeaveDate() + "로 변경완료");
		}

		send("전화번호: ");
		newData = receive();
		if (!newData.contentEquals("")) {
			int newPhone = Integer.parseInt(newData);
			staff.setPhone(newPhone);
			System.out.println("해당 직원의 전화번호을 " + staff.getPhone() + "로 변경완료");
		}

		send("생일: ");
		newData = receive();
		if (!newData.contentEquals("")) {
			int newBirth = Integer.parseInt(newData);
			staff.setBirth(newBirth);
			System.out.println("해당 직원의 생일을 " + staff.getBirth() + "로 변경완료");

		}

		send("성별: ");
		do {
			newData = receive();
			if (!newData.contentEquals("")) {
				if (!newData.equals("남") && !newData.equals("여"))
					send("'남' 또는 '여'를 입력하세요");

				else {
					staff.setSex(newData);
					System.out.println("해당 직원의 성별을" + staff.getSex() + "로 변경완료");
				}

			}
		} while (!(newData.contentEquals("") || newData.equals("여") ||newData.equals("남")));

		send("고용형태: ");
		do {
			newData = receive();
			if (!newData.contentEquals("")) {
				if (!newData.equals("정직원") && !newData.equals("파트타임"))
					send("'정직원' 또는 '파트타임'를 입력하세요");

				else {
					staff.setWorkstyle(newData);
					System.out.println("해당 직원의 고용형태를" + staff.getWorkstyle() + "로 변경완료");
				}

			}
		} while (!(newData.equals("정직원") || newData.equals("파트타임") || newData.contentEquals("")));

		posControl.updateStaffInfo(staff);
		send("변경이 완료되었습니다");
	}

	public void staffInfoDelete() {
		send("삭제 할 직원의 이름을 입력하세요");
		choose = receive();
		StaffDTO[] staffList = posControl.searchStaff(choose);
		for (StaffDTO staff : staffList) {
			send("" + staff.getId() + "\t" + staff.getName() + "\t" + staff.getPhone() + "\t" + staff.getJoinDate());
		}
		if (staffList.length == 1) {
			staff = staffList[0];
			send("위 직원의 정보를 삭제하시겠습니까? y/n");
			choose = receive();
			switch (choose) {
			case "y":
				posControl.deleteStaffInfo(staff);
				send("해당 직원의 정보가 삭제되었습니다");
				break;
			case "n":
				send("취소하셨습니다");
				break;
			default:
				send("다시 입력하세요");
				break;
			}
		} else if (staffList.length > 1) {
			send("동명이인이 있습니다. 삭제하려는 직원의 직원번호를 입력하세요");
			choose = receive();
			boolean check = false;
			for (StaffDTO temp : staffList) {
				if (choose.contentEquals(temp.getId())) {
					staff = temp;
					check = true;
				}
			}
			if (!check)
				send("직원 번호를 다시 확인해주세요");
			else if (check) {
				send("위 직원의 정보를 삭제하시겠습니까? y/n");
				choose = receive();
				switch (choose) {
				case "y":
					posControl.deleteStaffInfo(staff);
					send("해당 직원의 정보가 삭제되었습니다");
					break;
				case "n":
					send("변경을 취소했습니다");
					break;
				default:
					send("잘 못 입력하셨습니다");
					break;
				}
			} else
				send("error:입력을 다시 확인해주세요");

		} else {
			send("일치하는 직원이 없습니다");
		}

	}

	public void staffEnroll() {
		send("새로운 직원을 등록합니다");
		send("아래의 정보를 맞게 입력하세요");
		send(" * 이 붙은 항목은 필수 입력사항입니다");
		while (true) {
			send("* 이름 : ");
			choose = receive();
			if (choose.contentEquals(""))
				send("이름은 필수 입력사항입니다. 다시 입력하세요");
			else {
				staff.setName(choose);
				break;
			}
		}
		while (true) {
			send("* 입사일: ");
			send("(yy/mm/dd 형식으로 입력하세요)");
			choose = receive();
			if (choose.contentEquals(""))
				send("입사일은 필수 입력사항입니다. 다시 입력하세요");
			else {
				staff.setJoinDate(choose);
				break;
			}
		}
		send("퇴사일 : ");
		send("(yy/mm/dd 형식으로 입력하세요)");
		choose = receive();
		if (choose.contentEquals(""))
			staff.setLeaveDate("");
		else {
			staff.setLeaveDate(choose);
		}
		send("전화번호 : ");
		choose = receive();
		if (choose.contentEquals("")) {
			staff.setPhone(1000000000);
		} else {
			int phoneNumber = Integer.parseInt(choose);
			staff.setPhone(phoneNumber);
		}
		send("생년월일 : ");
		choose = receive();
		if (choose.contentEquals("")) {
			staff.setBirth(900101);
		} else {
			int birth = Integer.parseInt(choose);
			staff.setBirth(birth);
		}
		send("성별 : ");
		send("'남' 또는 '여' 로 입력하세요");
		choose = receive();
		if (choose.contentEquals("")) {
			staff.setSex("");
		} else {
			staff.setSex(choose);
		}
		send("성별 : ");
		send("'정직원' 또는 '파트타임'으로 입력하세요");
		choose = receive();
		if (choose.contentEquals("")) {
			staff.setWorkstyle("");
		} else {
			staff.setWorkstyle(choose);
		}
		staff.setStoreId(store.getStoreId());

		posControl.staffEnroll(staff);
		send("등록이 완료되었습니다");
	}

	/*
	 * // 직원관리>급여관리 내부 메뉴 public void staffSalaryManage() {
	 * 
	 * }
	 */
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
	// 매장관리>매장정보>기본정보
	public void storeInfoDefault() {
		send("지점번호 : " + store.getStoreId());
		send("지점명 : " + store.getName());
		send("지점담당자 : " + store.getOwner());
		send("개업일 : " + store.getOpendate());
		send("폐업일 : " + store.getClosedate());
		send("매장전화번호 : " + store.getPhone());
		send("매장주소 : " + store.getAddress());
	}

	// 매장관리>매장정보>수정
	public void storeInfoMotify() {
		// 매장 정보 수정 합니다~

		send("매장 정보 수정해주세요.");
		send("지점번호 : ");
		String num = receive();
		send("지점명 : ");
		String name = receive();
		send("지점담당자 : ");
		String owner = receive();
		send("개업일 : ");
		String open = receive(); // *** int로 입력되야함
		send("폐업일 : ");
		String close = receive(); // *** int로 입력되야함
		send("매장전화번호 : ");
		String phone = receive(); // *** int로 입력되야함
		send("매장주소 : ");
		String addr = receive();
		send("입력완료");

		// 유저의 입력을 store 소속의 적절한 메소드로 넘긴다
		posControl.storeInfoMotify(num, name, owner, open, close, phone, addr);

		// 메뉴로 다시 돌아가기
		storeMenu();
	}

	// 매장관리>매장정보>수입확인
	public void checkIncome() {// 쿼리에서 작성해서 보여야함

	}

	// 매장관리>매장정보>지출확인
	public void checkOutcome() {// 쿼리에서 작성해서 보여야함

	}

	// 매장관리>매출정보>기본정보
	public void salesInfoDefault() {// 쿼리에서 작성해서 보여야함

	}

	// 매장관리>매출정보>시간별 검색
	public void salesSearchTimes() {// 쿼리에서 작성해서 보여야함

	}

	// 매장관리>매출정보>메뉴별 검색
	public void salesSearchMenus() {// 쿼리에서 작성해서 보여야함

	}

	// 매장관리>재고관리>현재 비품 재고
	public void stockNow() {
		send("===재고 List====");
		send(" ID | 이름 | 갯수 | 원가 ");
		posControl.showstockList();
		send("뒤로가까? y/n");
		String yon = receive();
		if (yon == "y")
			storeMenu();
		else
			storeMenu();
//		for(int i =0;i<stocklist.length;i++) {
//			send(stocklist[i].getStockId());
//			send(""+stocklist[i].getInputDate());
//			send(""+stocklist[i].getSellByDate());
//			send(""+stocklist[i].getAmount());
//		}
//		send("지점번호 : " + stock.getStoreId());
//		send("재고일렬번호 : " + stock.getStockId());
//		send("입고날짜 : " + stock.getInputDate());
//		send("유통기한 : " + stock.getSellByDate());
//		send("재고 량 : " + stock.getAmount());
//		send("뒤로가까? y/n");
//		String yon = receive();
//		if (yon == "y")
//			storeMenu();
//		else
//			storeMenu();
	}

	// 매장관리>매출정보>입고관리
	public void stockManage() { // 수량 입력 후에 쿼리에서 합하는거 ? 그거 해줘야지
		String id;
		String name;
		String category;
		int stock;
		int cost;
		send("1. 원재료 입고");
		send("2. 비품입고");
		choose = receive();

		if (choose == "1") {

			ArrayList<RawMaterialDTO> raw = new ArrayList<>();
			RawMaterialDTO temp = null;

			while (true) {
				send("원재료 입고를 작성하세요.");
				send("원재료 ID : ");
				id = receive();
				send("원재료명 : ");
				name = receive();
				send("분류 : ");
				category = receive();
				send("수량 : ");
				stock = receiveInt(); // *** int로 입력되야함
				send("원가 : ");
				cost = receiveInt(); // *** int로 입력되야함

				temp.setId(id);
				temp.setName(name);
				temp.setCategory(category);
				temp.setStock(stock);
				temp.setCost(cost);

				raw.add(temp);

				temp = new RawMaterialDTO();
				send("더 추가하시겠습니까?  y/n");
				if (choose.equals("n"))
					break;
			}

			// 유저의 입력을 store 소속의 적절한 메소드로 넘긴다
			posControl.temp(raw);
			// posControl.rawstock(id, name, category, stock, cost);

			// 메뉴로 다시 돌아가기
			send("뒤로가까? y/n");
			String yon = receive();
			if (yon == "y")
				stockNow();
			else
				stockNow();

		} else if (choose == "2") { //
			send("비품 입고를 작성하세요.");
			send("비품 ID : ");
			id = receive();
			send("비품명 : ");
			name = receive();
			send("수량 : ");
			stock = receiveInt(); // *** int로 입력되야함
			send("원가 : ");
			cost = receiveInt(); // *** int로 입력되야함

			// 유저의 입력을 store 소속의 적절한 메소드로 넘긴다
			posControl.matestock(id, name, stock, cost);

			// 메뉴로 다시 돌아가기
			send("뒤로가까? y/n");
			String yon = receive();
			if (yon == "y")
				stockNow();
			else
				stockNow();

		} // else
	}// stockManage

	// 메뉴관리>메뉴정보>기본정보
	public void menuInfoDefault() {
		send("메뉴번호\t메뉴이름\t가격\t카테고리");
		MenuItemDTO[] menuList = posControl.menuInfoDefault();
		for (MenuItemDTO menu : menuList) {
			send(menu.getMenuId() + "\t" + menu.getName() + "\t" + menu.getPrice() + "\t" + menu.getCategory());
		}
	}

	// 메뉴관리>메뉴정보>수정
	public void menuModify() {

	}

	// 메뉴관리>메뉴정보>삭제
	public void menuDelete() {

	}

	// 메뉴관리>메뉴정보>활성화,비활성화
	public void menuOnOff() {

	}

	// 메뉴검색>메뉴검색>이름
	public void searchMenuName() {

	}

	// 메뉴검색>메뉴검색>종류
	public void searchMenuCategory() {

	}

///////////////////////////////////////////////////////소미 파트 
	// 고객관리>회원정보 내부 메뉴
	public void showMembers() {// 1.모두보기
		MemberDTO[] showMembersList = posControl.showMember();
		send("=======회원정보 모두보기=======");
		for (int i = 0; i < showMembersList.length; i++) {
			send("" + showMembersList[i].getMemberID());
			send("" + showMembersList[i].getName());
			send("" + showMembersList[i].getPhone());
			send("" + showMembersList[i].getSex());
			send("" + showMembersList[i].getBirth());
		}
	}

	public void searchMember() {// 2.고객검색

	}

	public void showMemberDetail() {// 3.정보보기

	}

	// 고객관리>회원정보>정보보기 내부 메뉴(1.수정)
	public void showMemberDetailModify() {

	}

	public void showMemberDetailDelete() {

	}

	// 고객관리>고객구매이력 내부 메뉴
	public void lastBuyingRecord() {
		send("고객의 최신 구매이력 10건을 검색합니다");
		send("검색을 원하는 고객의 이름을 입력하세요");
		choose = receive();
		MemberDTO[] memberList = posControl.searchMember(choose);

		if (memberList.length == 1) {
			member = memberList[0];
			OrderListDTO[] orderList = posControl.lastBuyingData(member);
			send(member.getName() + "님의 최신 구매이력 10건입니다");
			send("주문번호\t주문일자\t주문 총 금액");
			try {
				for (OrderListDTO order : orderList) {
					send(order.getId() + "\t" + order.getOrderDate() + "\t" + order.getOrderPrice());
				}
			} catch (NullPointerException e) {
				send("더 이상의 구매이력이 존재하지 않습니다");
			}
		} else if (memberList.length > 1) {
			send("===========고객 검색 결과 ===============");
			for (MemberDTO member : memberList) {
				send(member.getMemberID() + "\t" + member.getName() + "\t" + member.getPhone() + "\t");
			}
			send("====================================");
			send("일치하는 고객이 2인 이상입니다");
			send("위 고객 중 원하시는 고객의 번호를 입력해주세요");
			choose = receive();
			boolean check = false;
			for (MemberDTO temp : memberList) {
				if (choose.equals(temp.getMemberID())) {
					member = temp;
					OrderListDTO[] orderList = posControl.lastBuyingData(member);
					send(member.getName() + "님의 최신 구매이력 10건입니다");
					send("주문번호\t주문일자\t주문 총 금액");
					try {
						for (OrderListDTO order : orderList) {
							send(order.getId() + "\t" + order.getOrderDate() + "\t" + order.getOrderPrice());
						}
					} catch (NullPointerException e) {
						send("더 이상의 구매이력이 존재하지 않습니다");
					}
					check = true;
				}
			}
			if (check == false) {
				send("고객번호를 잘 못 입력하셨습니다");
				send("처음부터 다시 진행해주세요");
			}

		} else {
			send("일치하는 고객이 없습니다");
		}

	}

	public void MostBuyingRecord() {
		send("고객의 최다 구매물품을 검색합니다");
		send("검색을 원하는 고객의 이름을 입력하세요");
		choose = receive();
		MemberDTO[] memberList = posControl.searchMember(choose);

		if (memberList.length == 1) {
			member = memberList[0];
			MenuDTO[] menuList = posControl.mostBuyingData(member);
			send(member.getName() + "님의 최다 구매이력 5건입니다");
			send(" 메뉴번호\t메뉴명\t총 주문 수량");
			try {
				for (MenuDTO item : menuList) {
					send(item.getMenuId() + "\t" + item.getName() + "\t" + item.getAmount());
				}
			} catch (NullPointerException e) {
				send("더 이상의 구매이력이 존재하지 않습니다");
			}
		} else if (memberList.length > 1) {
			send("===========고객 검색 결과 ===============");
			for (MemberDTO member : memberList) {
				send(member.getMemberID() + "\t" + member.getName() + "\t" + member.getPhone() + "\t");
			}
			send("====================================");
			send("일치하는 고객이 2인 이상입니다");
			send("위 고객 중 검색을 원하시는 고객의 번호를 입력해주세요");
			choose = receive();
			boolean check = false;
			for (MemberDTO temp : memberList) {
				if (choose.equals(temp.getMemberID())) {
					member = temp;
					MenuDTO[] menuList = posControl.mostBuyingData(member);
					send(member.getName() + "님의 최다 구매이력 5건입니다");
					send("메뉴번호\t메뉴명\t총 주문 수량");
					try {
						for (MenuDTO item : menuList) {
							send(item.getMenuId() + "\t" + item.getName() + "\t" + item.getAmount());
						}
					} catch (NullPointerException e) {
						send("더 이상의 구매이력이 존재하지 않습니다");
					}
					check = true;
				}
			}
			if (check == false) {
				send("고객번호를 잘 못 입력하셨습니다");
				send("처음부터 다시 진행해주세요");
			}

		} else {
			send("일치하는 고객이 없습니다");
		}
	}

}