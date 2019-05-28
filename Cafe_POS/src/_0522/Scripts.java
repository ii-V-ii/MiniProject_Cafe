
package _0522;

// 동기화 확인용 주석
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
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
 * 1차메뉴 (매장,메뉴,고객,직원관리)와 2차 메뉴(매장관리>매장정보, 매출정보, 재고관리) 는 인터페이스로 관리 3차메뉴
 * (매장정보>기본정보, 수정, 수입확인, 지출확인)는 scripts 클래스 내의 메소드로 관리합니다
 */

// 유저에게 보여줄 메뉴 인터페이스(1차, 2차 메뉴)
interface MainMenu {
	String SALES = "0", STORE="1",MENU="2",CUSTOMER="3",STAFF="4";
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
	String STAFFINFO = "1", STAFFENROLL = "2", PAY = "3";

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
	boolean paycheck = false;
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

	// 최초 프로그램 실행시 로그인 기능 메서드 완성할 떄의 예시로 봐주세요
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

// 1차==========================================================================
	// 유저에게서 메인메뉴를 보여주고 선택받는다
	public void mainMenu() {
		send("0. 판매");
		send("1.매장관리  |  2.메뉴관리  |  3.고객관리  |  4.직원관리  |  5.프로그램 종료");
		send("선택 >>");
		choose = receive();

		switch (choose) {
		case MainMenu.SALES:
			sale();
			break;

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
			send("다시선택하세요.");
			mainMenu();
			break;
		}// switch
	}// mainMenu

	// 유저에게서 매장관리 메뉴를 보여주고 선택받는다
	public void storeMenu() {
		send("1.매장정보  |  2.매출정보  |  3.재고관리  |  4.되돌아가기");
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
		case "4":
			mainMenu();
			break;
		default:
			send("다시선택하세요.");
			storeMenu();
			break;
		}// switch
	}

	// 유저에게서 메뉴관리 메뉴를 보여주고 선택받는다
	public void menuMenu() {
		send("1.메뉴정보  |  2.메뉴등록  |  3.메뉴검색  |  4. 돌아가기");
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
		case "4":
			mainMenu();
			break;
		default:
			send("다시선택하세요.");
			menuMenu();
			break;
		}// switch
	}

/////////////////////////////////////////////////////////////////////
/////// 판매메뉴 추가
/////////////////////////////////////////////////////////////////////
	public void sale() {

		MenuDTO.resetOrder();
		send("카테고리");
		while (true) {
			menu = new MenuDTO();
			String showMenu = null;
			if (MenuDTO.getOrderedMenu().size() > 0)
				showMenu = "4. PAYING	0.CANCEL";
			else {
				showMenu = "0. CANCLE";
			}
			send("1.COFFEE	2.BEVERGE	3.BAKERY	" + showMenu);
			send("선택>>");
			choose = receive();
			switch (choose) {
			case "1":
				choiceCoffee();
				break;
			case "2":
				choiceBeverge();
				break;
			case "3":
				choiceBakery();
				break;
			case "4":
				if (MenuDTO.getOrderedMenu().size() > 0) {
					showOrderedMenu();
				}
				;
				break;
			case "0":
				send("주문을 취소합니다");
				MenuDTO.resetOrder();
				;
				return;
			default:
				send("잘 못 선택하셨습니다");
				break;
			}
			if (paycheck) {
				break;
			}
		}
	}

	public void choiceCoffee() {
		send("메뉴를 고르세요");
		send("1.아메리카노	2.카페라떼		3.카페모카		4.돌체라떼		0.돌아가기");
		choose = receive();
		if (choose.contentEquals("0"))
			return;
		switch (choose) {
		case "1":
			menu.setMenuId("me1");
			menu.setName("아메리카노");
			break;
		case "2":
			menu.setMenuId("me2");
			menu.setName("카페라떼");
			break;
		case "3":
			menu.setMenuId("me3");
			menu.setName("카페모카");
			break;
		case "4":
			menu.setMenuId("me4");
			menu.setName("돌체라떼");
			break;
		case "0":
			send("이전 메뉴로 돌아갑니다");
			return;
		default:
			send("잘 못 입력하였습니다");
			return;
		}

		choiceCount(menu);
	}

	public void choiceBeverge() {
		send("메뉴를 고르세요");
		send("1.핫초코	2.딸기바나나	0.돌아가기");
		choose = receive();
		if (choose.contentEquals("0"))
			return;
		switch (choose) {
		case "1":
			menu.setMenuId("me5");
			menu.setName("핫초코");

			break;
		case "2":
			menu.setMenuId("me6");
			menu.setName("딸기바나나");
			break;
		case "0":
			send("이전 메뉴로 돌아갑니다");
			return;
		default:
			send("잘 못 입력하였습니다");
			return;
		}
		choiceCount(menu);

	}

	public void choiceBakery() {
		send("메뉴를 고르세요");
		send("1.베이글	2.스콘	0.돌아가기");
		choose = receive();
		if (choose.contentEquals("0"))
			return;
		switch (choose) {
		case "1":
			menu.setMenuId("me7");
			menu.setName("베이글");

			break;
		case "2":
			menu.setMenuId("me8");
			menu.setName("스콘");
			break;
		case "0":
			send("이전 메뉴로 돌아갑니다");
			return;
		default:
			send("잘 못 입력하였습니다");
			return;
		}
		choiceCount(menu);
	}

	public void choiceCount(MenuDTO menu) {
		int count = 0;
		do {
			send("몇개 주문하시겠습니까?");
			choose = receive();
			if (choose.equals("")) {
				send("주문 갯수를 입력하세요");
				continue;
			}
			count = Integer.parseInt(choose);
			if (count < 1) {
				send("주문 갯수는 1보다 작을 수 없습니다");
			}
		} while (count < 1);

		menu.setAmount(count);
		menu.putOrder(menu);

		send(menu.getName() + " " + menu.getAmount() + "개를 주문서에 추가했습니다");
		send("1. 계속 주문하기	2. 결제하기");
		choose = receive();
		switch (choose) {
		case "1":
			break;
		case "2":
			showOrderedMenu();
			break;
		default:
			send("잘못 입력하셨습니다");
			break;
		}

	}

	public void showOrderedMenu() {
		posControl.calcualteOrder();

		send("주문메뉴\t주문개수\t주문금액");
		int orderPrice = 0;
		for (MenuDTO menu : MenuDTO.getOrderedMenu()) {
			send(menu.getName() + "\t" + menu.getAmount() + "\t" + menu.getSumprice());
			orderPrice += menu.getSumprice();
		}
		send("*************");
		send("총 결제 금액 : " + orderPrice);
		send("이대로 결제하시겠습니까? y/n");
		choose = receive();
		switch (choose) {
		case "y":

			posControl.createPayment(pointPlus());
			break;
		case "n":
			send("결제를 취소합니다");
			MenuDTO.resetOrder();
			return;
		default:
			break;
		}
		send("결제가 완료되었습니다");
		paycheck = true;
	}

	public MemberDTO pointPlus() {
		send("포인트를 적립하십니까? y/n");
		choose = receive();
		switch (choose) {
		case "y":
			send("고객의 전화번호를 입력하세요");
			member.setPhone(Integer.parseInt(receive()));
			break;
		case "n":
			member.setMemberID("0");
			member.setPhone(0);
			break;
		default:
			break;
		}
		return member;
	}
	//////////////////////////////////////////////////////////////////////////////////////////////
	// 유저에게서 고객관리 메뉴를 보여주고 선택받는다
	public void customerMenu() {
		while (true) {
			send("1.회원정보  |  2.회원등록  |  3.고객구매이력  |  4. 돌아가기");
			send("선택 >>");
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
			default:
				send("다시선택하세요.");
				customerMenu();
				break;
			}
		}
	}

	// 직원관리
	public void staffMenu() {
		send("1.직원정보 확인  |  2.직원등록  |  3.급여관리  |  4.돌아가기");
		send("선택 >>");
		choose = receive();

		switch (choose) {
		case staffMenu.STAFFINFO:
			staffMenuFirst();
			break;
		case staffMenu.STAFFENROLL:
			staffEnroll();
			break;
		case staffMenu.PAY:
			staffSalaryManage();
			break;
		case "4":
			mainMenu();
			break;
		default:
			send("잘못 입력하셨습니다");
			staffMenu();
			break;
		}
	}

//2차=======================================================
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

	// 고객관리>회원정보
	public void custInfo() {
		send("2. 고객 검색");
		send("3. 상세 고객 정보");
		send("4. 되돌아가기");
		send("5. 이전 메뉴로 되돌아가기");// customerMenu();
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

	// 고객관리>회원등록
	public void custenroll() {

	}

	// 고객관리>고객구매이력
	public void history() {
		send("1.최신 구매이력  |  2.최다 구매이력  |  3.돌아가기");
		send("선택>>");
		choose = receive();

		switch (choose) {
		case "1":
			lastBuyingRecord();
			break;
		case "2":
			MostBuyingRecord();
			break;
		case "3":
			customerMenu();
			break;
		default:
			send("잘못 입력하셨습니다");
			history();
			break;
		}
	}

	// 직원관리>직원정보

	public void staffMenuFirst() {
		send("1.직원기본정보  |  2.직원정보수정  |  3.직원정보삭제  |  4.돌아가기 ");
		send("선택>>");
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
		case "4":
			staffMenu();
			break;
		default:
			send("잘못 입력하셨습니다");
			staffMenuFirst();
			break;
		}
	}

	// 직원관리>직원정보>기본정보
	public void staffDefaultInfo() {
		StaffDTO[] staffList = posControl.showStaffList();

		send("이름 \t전화번호\t\t성별\t생년월일\t입사일\t\t퇴사일\t\t고용형태");
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
		} while (!(newData.contentEquals("") || newData.equals("여") || newData.equals("남")));

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

		while (true) {
			send("* 고용형태 : ");
			send("'정직원' 또는 '파트타임'으로 입력하세요");
			choose = receive();
			if (choose.contentEquals("")) {
				send("고용형태는 필수 입력사항입니다. 다시 입력하세요");
			} else {
				staff.setWorkstyle(choose);
				break;
			}
		}
		staff.setStoreId(store.getStoreId());

		posControl.staffEnroll(staff);
		send("등록이 완료되었습니다");
	}

	// 직원관리>급여관리 내부 메뉴
	public void staffSalaryManage() {
		send("1.직원급여현황  |  2.직원급여수정  |  3.돌아가기");
		send("선택>>");
		choose = receive();

		switch (choose) {
		case "1":
			showSalaryOption();
			break;
		case "2":
			updateSalaryOption();
			break;
		case "3":
			staffMenu();
			break;
		default:
			send("잘못 입력하셨습니다");
			staffSalaryManage();
			break;
		}
	}

	// 직원관리>직원급여현황
	public void showSalaryOption() {
		ArrayList<String[]> optionList = posControl.showSalaryOption();
		send("직원번호\t직원명\t고용형태\t근무일수\t근무시간\t환산시급\t환산월급");
		for (String[] option : optionList) {
			send(option[0] + "\t" + option[1] + "\t" + option[2] + "\t" + option[3] + "\t" + option[4] + "\t"
					+ option[5] + "\t" + option[6]);
		}
	}

	public void updateSalaryOption() {
		send("급여 설정을 변경할 직원의 이름을 입력하세요");
		choose = receive();
		StaffDTO[] staffList = posControl.searchStaff(choose);
		for (StaffDTO staff : staffList) {
			send("" + staff.getId() + "\t" + staff.getName() + "\t" + staff.getPhone() + "\t" + staff.getJoinDate());
		}
		if (staffList.length == 1) {
			staff = staffList[0];
			send("위 직원의 급여 설정을 변경하시겠습니까? y/n");
			choose = receive();
			switch (choose) {
			case "y":

				updateSalaryOptionDetail(staff);
				break;
			case "n":
				send("취소하셨습니다");
				break;
			default:
				send("다시 입력하세요");
				break;
			}
		} else if (staffList.length > 1) {
			send("동명이인이 있습니다. 원하시는 직원의 직원번호를 입력하세요");
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
				send("위 직원의 급여정보를 변경하시겠습니까? y/n");
				choose = receive();
				switch (choose) {
				case "y":
					updateSalaryOptionDetail(staff);
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

	public void updateSalaryOptionDetail(StaffDTO staff) {
		send("해당 직원의 근무정보 중 변경을 원하시는 부분을 선택하세요");
		String pay = null;
		if (staff.getWorkstyle().contentEquals("정직원")) {
			send("1. 고용형태\t2. 근무일수\t3. 월급");
		} else if (staff.getWorkstyle().contentEquals("파트타임")) {
			send("1. 고용형태\t2. 근무일수\t3. 일 근무시간\t4. 시급");
		}
		choose = receive();
		switch (choose) {
		case "1":
			changeTime(staff);
			break;
		case "2":
			changeWorkday(staff);
			break;
		case "3":
			if (staff.getWorkstyle().contentEquals("정직원")) {
				changePayMonth(staff);
			} else if (staff.getWorkstyle().contentEquals("파트타임")) {
				changeWorktime(staff);
			}
			break;
		case "4":
			if (staff.getWorkstyle().contentEquals("파트타임")) {
				changePayHour(staff);
			} else {
				send("잘 못 입력하셨습니다");
			}
			break;
		default:
			send("잘 못 입력하셨습니다");
			break;
		}
	}

	public void changeTime(StaffDTO staff) {
		int workday = 0;
		int workTime = 0;
		int pay = 0;
		if (staff.getWorkstyle().contentEquals("정직원")) {
			send("고용형태를 파트타임으로 바꾸시겠습니까? y/n");
			choose = receive();
			switch (choose) {
			case "y":
				send("새로운 주당 근무 일수를 입력하세요 (7 이하 정수 입력)");
				while (true) {
					choose = receive();
					if (choose != "") {
						workday = Integer.parseInt(choose);
						if (workday < 8 && workday > 0) {
							break;
						} else {
							send("1~7 사이의 정수를 입력하세요");
						}

					} else {
						send("값을 입력하지 않았습니다");
					}
				}
				send("새로운 하루 근무시간을 입력하세요 (8이하 정수 입력)");
				while (true) {
					choose = receive();
					if (choose != "") {
						workTime = Integer.parseInt(choose);
						if (workTime < 1 || workTime > 8) {
							send("1~8 사이의 정수를 입력하세요");
						} else {
							break;
						}

					} else {
						send("값을 입력하지 않았습니다");
					}
				}
				send("시급을 입력하세요 (8351 이상의 정수 입력)");
				while (true) {
					choose = receive();
					if (choose != "") {
						pay = Integer.parseInt(choose);
						if (pay <= 8350) {
							send("8350보다 큰 값을 입력하세요");
						} else {
							break;
						}

					} else {
						send("값을 입력하지 않았습니다");
					}
				}
				posControl.changeTime(staff, workday, workTime, pay);
				break;
			case "n":
				send("취소되었습니다");
				break;
			default:
				send("잘 못 입력하셨습니다");
				break;
			}
		} else if (staff.getWorkstyle().contentEquals("파트타임")) {
			send("고용형태를 정직원으로 바꾸시겠습니까? y/n");
			choose = receive();
			switch (choose) {
			case "y":
				send("주당 근무일 수를 입력하세요");
				while (true) {
					choose = receive();
					if (choose != "") {
						workday = Integer.parseInt(choose);
						if (workday < 8 && workday > 0) {
							break;
						} else {
							send("1~7 사이의 정수를 입력하세요");
						}

					} else {
						send("값을 입력하지 않았습니다");
					}
				}
				send("월급을 입력하세요(단위: 10000원)");
				while (true) {
					choose = receive();
					if (choose != "") {
						pay = Integer.parseInt(choose);
						if (pay <= 0) {
							send("0보다 큰 값을 입력하세요");
						} else {
							break;
						}

					} else {
						send("값을 입력하지 않았습니다");
					}
				}
				posControl.changeTime(staff, workday, 8, pay);
				break;
			case "n":
				send("취소되었습니다");
				break;
			default:
				send("잘 못 입력하셨습니다");
				break;
			}
		}
		send("업데이트가 완료되었습니다");
	}

	public void changeWorkday(StaffDTO staff) {
		send("주당 근무일수를 변경하시겠습니까? y/n");
		choose = receive();
		switch (choose) {
		case "y":
			int workDay = 0;
			send("새로운 주당 근무일수를 입력하세요");
			while (true) {
				choose = receive();
				if (choose != "") {
					workDay = Integer.parseInt(choose);
					if (workDay < 8 && workDay > 0) {
						break;
					} else {
						send("1~7 사이의 정수를 입력하세요");
					}

				} else {
					send("값을 입력하지 않았습니다");
				}
			}
			posControl.changeWorkday(staff, workDay);
			send("업데이트가 완료되었습니다");
			break;
		case "n":
			send("취소되었습니다");
			break;
		default:
			send("잘 못 입력하셨습니다");
			break;
		}

	}

	public void changeWorktime(StaffDTO staff) {
		send("일당 근무시간을 변경하시겠습니까? y/n");
		choose = receive();
		switch (choose) {
		case "y":
			int workTime = 0;
			send("새로운 일당 근무시간을 입력하세요");
			while (true) {
				choose = receive();
				if (choose != "") {
					workTime = Integer.parseInt(choose);
					if (workTime < 9 && workTime > 0) {
						break;
					} else {
						send("1~8 사이의 정수를 입력하세요");
					}

				} else {
					send("값을 입력하지 않았습니다");
				}
			}

			posControl.changeWorkTime(staff, workTime);
			send("업데이트가 완료되었습니다");
			break;

		case "n":
			send("취소되었습니다");
			break;

		default:
			send("잘 못 입력하셨습니다");
			break;

		}

	};

	public void changePayHour(StaffDTO staff) {
		send("시급 액수를 변경하겠습니까? y/n");
		choose = receive();
		switch (choose) {
		case "y":
			int pay = 0;
			send("새로운 월급 액수를 입력하세요(8351 이상의 정수)");
			while (true) {
				choose = receive();
				if (choose != "") {
					pay = Integer.parseInt(choose);
					if (pay > 8350) {
						break;
					} else {
						send("8350보다 큰 정수를 입력하세요");
					}

				} else {
					send("값을 입력하지 않았습니다");
				}
			}

			posControl.changePayHour(staff, pay);
			send("업데이트가 완료되었습니다");
			break;

		case "n":
			send("취소되었습니다");
			break;

		default:
			send("잘 못 입력하셨습니다");
			break;

		}
	}

	public void changePayMonth(StaffDTO staff) {
		send("월급 액수를 변경하겠습니까? y/n");
		choose = receive();
		switch (choose) {
		case "y":
			int pay = 0;
			send("새로운 월급 액수를 입력하세요(10000원 단위)");
			while (true) {
				choose = receive();
				if (choose != "") {
					pay = Integer.parseInt(choose);
					if (pay > 0) {
						break;
					} else {
						send("0보다 큰 정수를 입력하세요");
					}

				} else {
					send("값을 입력하지 않았습니다");
				}
			}

			posControl.changePayMonth(staff, pay);
			send("업데이트가 완료되었습니다");
			break;

		case "n":
			send("취소되었습니다");
			break;

		default:
			send("잘 못 입력하셨습니다");
			break;

		}
	}

	// ==2차메뉴
	// 메서드=====================================================================

	// 매장관리 > 매출정보
	public void saleInfo() {
		send("1. 기본정보"); // salesInfoDefault()
		send("2. 기간별 검색"); // salesSearchTimes()
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
			menuActivation();
			break;
		default:
			send("다시 선택하세요.");
			mainMenu();
			break;
		}// switch
	}// menuInfo

	// 메뉴관리 > 메뉴등록
	public void menuEnroll() {

		MenuItemDTO eDto = new MenuItemDTO();

		send("====메뉴입력====");
		send("메뉴번호 : ");
		eDto.setMenuId(receive());
		send("메뉴명 : ");
		eDto.setName(receive());
		send("가격 : ");
		eDto.setPrice(Integer.parseInt(receive()));
		send("분류 : ");
		eDto.setCategory(receive());
		send("활성화 여부 (Y,N) : ");
		eDto.setActivation(receive());
		send("입력완료");

		posControl.menuEnroll(eDto);

		// 상위 메뉴로 돌아가기
		menuMenu();

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
		StoreDTO showStoreList = posControl.storeInfoDefault();

		send("======= 매장 정보 =======");
		send("" + showStoreList.getName());
		send("" + showStoreList.getOwner());
		send("" + showStoreList.getOpendate());
		send("" + showStoreList.getClosedate());
		send("" + showStoreList.getPhone());
		send("" + showStoreList.getAddress());
	}

	// 매장관리>매장정보>수정
	public void storeInfoMotify() {
		// 매장 정보 수정 합니다~
		StoreDTO sDto = new StoreDTO();
		send("매장 정보 수정해주세요.");
		send("지점번호 : ");
		sDto.setStoreId(receive());
		send("지점명 : ");
		sDto.setName(receive());
		send("지점담당자 : ");
		sDto.setOwner(receive());
		send("개업일 : ");
		sDto.setOpendate(receive()); // *** int로 입력되야함
		send("폐업일 : ");
		sDto.setClosedate(receive()); // *** int로 입력되야함
		send("매장전화번호 : ");
		sDto.setPhone(Integer.parseInt(receive())); // *** int로 입력되야함
		send("매장주소 : ");
		sDto.setAddress(receive());

		send("입력완료");

		// 유저의 입력을 store 소속의 적절한 메소드로 넘긴다
		posControl.storeInfoMotify(sDto);

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

		send("최근 7일간의 매출입니다");
		String[][] salesData = posControl.salesInfoDefault();
		send("날짜\t\t총 매출액");
		for (String[] data : salesData) {
			send(data[0] + "\t" + data[1]);
		}
		send("=================================");

	}

	// 매장관리>매출정보>시간별 검색
	public void salesSearchTimes() {// 쿼리에서 작성해서 보여야함

		send("기간별 검색을 시작합니다");
		send("검색 시작 날짜를 입력하세요(yy/mm/dd)");
		String startDate = receive();
		send("검색 마지막 날짜를 입력하세요(yy/mm/dd)");
		String finishDate = receive();
		String[] searchDate = { startDate, finishDate };
		ArrayList<String[]> salesData = posControl.salesInfoDefault(searchDate);
		send("날짜\t\t총 매출액");
		String sum = null;
		for (String[] data : salesData) {
			send(data[0] + "\t" + data[1]);
			sum = data[2];
		}
		send("=============================");
		send("매출 총 합계  : " + sum);
		send("=============================");

	}

	// 매장관리>매출정보>메뉴별 검색
	public void salesSearchMenus() {// 쿼리에서 작성해서 보여야함

		send("메뉴별 검색을 시작합니다");
		send("검색할 메뉴 이름을 입력하세요");
		choose = receive();
		String[][] salesData = posControl.salesMenuDate(choose);
		send("최근 7일간 판매량==================");
		send("날짜\t\t판매량\t수입");
		String sum = null;
		for (String[] data : salesData) {
			send(data[0] + "\t" + data[1] + "\t" + data[2]);
			sum = data[3];
		}
		send("=============================");
		send("최근 7일간 매출	:" + sum);
		send("최근 30일 간 매출\t:" + posControl.salesMenuDate30(choose));
		send("전 기간 매출\t\t:" + posControl.salesMenuDate365(choose));
		send("=============================");

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

	}

	// 매장관리>매출정보>입고관리
	public void stockManage() {
		send("1. 원재료 입고");
		send("2. 비품입고");
		choose = receive();

		if (choose.equals("1")) {
			RawMaterialDTO temp = new RawMaterialDTO();
			send("원재료 입고를 작성하세요.");
			send("원재료 ID : ");
			temp.setId(receive());

			send("원재료명 : ");
			temp.setName(receive());

			send("분류 : ");
			temp.setCategory(receive());

			send("수량 : ");
			temp.setStock(Integer.parseInt(receive()));

			send("원가 : ");
			temp.setCost(Integer.parseInt(receive()));

			posControl.temp(temp);

		} else if (choose.equals("2")) {
			MaterialDTO temp = new MaterialDTO();
			send("비품 입고를 작성하세요.");
			send("비품 ID : ");
			temp.setId(receive());

			send("비품명 : ");
			temp.setName(receive());

			send("수량 : ");
			temp.setStock(Integer.parseInt(receive())); // *** int로 입력되야함

			send("원가 : ");
			temp.setCost(Integer.parseInt(receive())); // *** int로 입력되야함

			posControl.matestock(temp);
		}

		send("더 추가하시겠습니까?  y/n");
		choose = receive();
		if (choose.equals("Y") || choose.equals("y")) {
			this.stockManage();
		} else if (choose.equals("N") || choose.equals("n")) {
			mainMenu();
		}
	}// stockManage

	// 메뉴관리>메뉴정보>기본정보
	public void menuInfoDefault() {

		send("메뉴번호\t메뉴이름\t가격\t카테고리");
		MenuItemDTO[] menuList = posControl.menuInfoDefault();
		for (MenuItemDTO menu : menuList) {
			send(menu.getMenuId() + "\t" + menu.getName() + "\t" + menu.getPrice() + "\t" + menu.getCategory());
		}
	}// menuInfoDefault

	// 메뉴관리>메뉴정보>수정
	public void menuModify() {

		MenuItemDTO mDto = new MenuItemDTO();
		menuInfoDefault();
		send("====메뉴수정====");
		send("메뉴번호 : ");
		mDto.setMenuId(receive());
		send("메뉴명 : ");
		mDto.setName(receive());
		send("가격 : ");
		mDto.setPrice(Integer.parseInt(receive()));
		send("분류 : ");
		mDto.setCategory(receive());
		send("활성화 여부 (Y,N) : ");
		mDto.setActivation(receive());
		send("입력완료");

		posControl.menuModify(mDto);

//			send("계속 하시겠습니까?");
//			choose = receive();
//			if (choose.equals("Y") || choose.equals("y")) {
//				menuModify();
//			} else if (choose.equals("N") || choose.equals("n")) {
		menuMenu();
//			}
	} // menuModify

	// 메뉴관리>메뉴정보>삭제
	public void menuDelete() {
		MenuDTO menu = new MenuDTO();
		menuInfoDefault();
		send("삭제할 메뉴ID을 입력하세요.");
		menu.setMenuId(receive());

		posControl.menuDelete(menu);

		menuMenu();
	}

	// 메뉴관리>메뉴정보>활성화,비활성화
	public void menuActivation() {

		send("====판매가능한 메뉴확인====");
		send("1. 판매가능한 메뉴\t| 2.시즌메뉴");
		choose = receive();

		if (choose.equals("1")) {
			send("메뉴번호\t메뉴이름\t가격\t카테고리");
			posControl.menuActivation("y");

		} else if (choose.equals("2")) {
			send("메뉴번호\t메뉴이름\t가격\t카테고리");
			posControl.menuActivation("n");

		} else {
			menuInfo();
		}
	}

	// 메뉴관리>메뉴검색>이름
	public void searchMenuName() {

		menuInfoDefault();
		send("메뉴를 입력하세요(원재료확인)");
		choose = receive();

		posControl.searchMenuName(choose);
	}

	// 메뉴관리>메뉴검색>종류
	public void searchMenuCategory() {
		send("====종류검색====");
		send("1.커피음료 \t| 2.음료\t| 3.베이커리\t| 4.차");
		choose = receive();

		if (choose.equals("1")) {
			send("메뉴번호\t메뉴이름\t가격\t카테고리");
			posControl.searchMenuCategory("커피음료");
		} else if (choose.equals("2")) {
			send("메뉴번호\t메뉴이름\t가격\t카테고리");
			posControl.searchMenuCategory("음료");
		} else if (choose.equals("3")) {
			send("메뉴번호\t메뉴이름\t가격\t카테고리");
			posControl.searchMenuCategory("베이커리");
		} else if (choose.equals("4")) {
			send("메뉴번호\t메뉴이름\t가격\t카테고리");
			posControl.searchMenuCategory("차");
		} else {
			send("잘못입력했습니다~");
			searchMenuCategory();
		}

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
			send("포인트 :"+showMembersList[i].getPoint());
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