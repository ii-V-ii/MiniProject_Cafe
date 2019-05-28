
package _0522;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
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

public class QueryList {
// 쿼리문 DB 전달을 위한 변수
	Connection con = null;
	Statement stmt = null;
	PreparedStatement pps = null;
	ResultSet rs = null;

	ResultSetMetaData rsmd = null;

	String sb = null;
	Scripts scripts;
	StringBuffer sbr = null;

	// DTO 클래스들 선언
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

	QueryList(Connection con, Scripts scripts) {
		this.con = con;
		this.scripts = scripts;

		try {
			stmt = con.createStatement();
		} catch (SQLException e) {
			System.out.println("QueryList 생성자, stmt = con.createStatement() 오류");
		}
	}

	public boolean getLogInInfo(String i, String pass) {
		String id = i;
		String password = pass;
		int check = 0;
		String storeID = null;
		sb = ("SELECT rownum, storeNo FROM masterList WHERE masterid = ? and masterPassword = ?");
		try {
			System.out.println("check00");

			pps = con.prepareStatement(sb);
			pps.setString(1, id);
			pps.setString(2, password);
			rs = pps.executeQuery();

			while (rs.next()) {
				check = rs.getInt("rownum");
				storeID = rs.getString("storeNo");
				System.out.println(check + "/" + storeID);

			}
			if (check == 1) {
				// userId.setId(id);
				userId.setPassword(password);
				userId.setStoreId(storeID);
				store.setStoreId(storeID);
				setDTOData();
				return true;
			} else
				return false;

		} catch (SQLException e) {
			return false;
		}

	}
	//////////////////////////////////////////////////////////////////////////////
	//// 판매
	//////////////////////////////////////////////////////////////////////////////

	public void calcualteOrder() {
		sb = "select price*? from menu where menuid = ?";
		try {
			pps = con.prepareStatement(sb);
			for (MenuDTO menu : MenuDTO.getOrderedMenu()) {
				pps.setInt(1, menu.getAmount());
				pps.setString(2, menu.getMenuId());
				rs = pps.executeQuery();
				rs.next();
				menu.setSumprice(rs.getInt(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void createPayment(MemberDTO member) {
		try {
			con.setAutoCommit(false);
			rs = stmt.executeQuery("SELECT memberid, point FROM member WHERE phone=" + member.getPhone());
			rs.next();
			member.setMemberID(rs.getString(1));
			member.setPhone(rs.getInt(2));

			// 가게번호, 주문번호, 고객, 날짜, 주문총액
			pps = con.prepareStatement(
					"INSERT INTO orderlist VALUES(?, 'or'||order_seq.nextval,?, to_date(sysdate), 1)");
			pps.setString(1, store.getStoreId());
			pps.setString(2, member.getMemberID());
			pps.executeUpdate();
			// 메뉴, 양, 합계액
			pps = con.prepareStatement("INSERT INTO orderDetail VALUES('or'||order_seq.currval, ?, ?, ?)");
			int sumprice = 0;
			for (MenuDTO menu : MenuDTO.getOrderedMenu()) {
				pps.setString(1, menu.getMenuId());
				pps.setInt(2, menu.getAmount());
				pps.setInt(3, menu.getSumprice());
				sumprice += menu.getSumprice();
				pps.addBatch();
			}
			System.out.println(pps.executeBatch());
			con.commit();
			rs = stmt.executeQuery("SELECT orderid FROM orderlist order by orderid desc");
			rs.next();
			String number = rs.getString(1);
			System.out.println(number);
			pps = con.prepareStatement("UPDATE orderlist SET orderprice=? WHERE orderid = ?");
			pps.setInt(1, sumprice);
			pps.setString(2, number);
			pps.executeUpdate();
			stmt.executeUpdate("UPDATE member SET point = " + (member.getPoint() + sumprice * 5 / 100)
					+ " WHERE memberid=" + member.getMemberID());

			con.commit();
			con.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	//////////////////////////////////////////////////////////////////////////////
	public MemberDTO[] showMembers() {

		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT COUNT(rownum) FROM member ");
			rs.next();
			int memberCount = rs.getInt(1);
			MemberDTO[] memberList = new MemberDTO[memberCount];
			String getMemberInfo = "SELECT * FROM member";
			rs = stmt.executeQuery(getMemberInfo);
			int i = 0;
			while (rs.next()) {
				rs.getString("memberid");
				rs.getString("name");
				rs.getInt("phone");
				rs.getString("sex");
				rs.getString("birth");
				memberList[i++] = new MemberDTO(rs.getString("memberid"), rs.getString("name"), rs.getInt("phone"),
						rs.getString("sex"), rs.getInt("birth"), rs.getInt("point"));

			}
			return memberList;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;

	}

	// 고객관리수정,소미파트
	public MemberDTO[] modifyshowMember(MemberDTO modifymember) {
		sb = "UPDATE member SET name = ?, phone = ?, sex = ?, birth = ? WHERE memberid = ?";
		MemberDTO[] modifyshowMember = null;
		try {
			pps = con.prepareStatement(sb);
			System.out.println(modifymember.getName() + modifymember.getPhone() + modifymember.getSex()
					+ modifymember.getBirth() + modifymember.getMemberID());
			pps.setString(1, modifymember.getName());
			pps.setInt(2, modifymember.getPhone());
			pps.setString(3, modifymember.getSex());
			pps.setInt(4, modifymember.getBirth());
			pps.setString(5, modifymember.getMemberID());
			pps.executeUpdate();
			System.out.println("member_info modify finish~");
		} catch (SQLException e) {
			e.printStackTrace();

			// System.out.println("format 불일치로인한 error.");
		}
		return modifyshowMember;
	}

	// 고객관리삭제, 소미파트
	public MemberDTO[] deleteshowMember(MemberDTO deletemember) {
		sb = "DELETE FROM member WHERE memberid =?";
		MemberDTO[] deleteshowMember = null;
		try {
			pps = con.prepareStatement(sb);
			pps.setString(1, deletemember.getMemberID());
			pps.executeUpdate();
			System.out.println("member_info delete finish~");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return deleteshowMember;
	}

	public MemberDTO[] searchMember(String memberName) {// 일단주석처리
		MemberDTO[] memberList = null;
		try {
			pps = con.prepareStatement("SELECT COUNT(rownum) FROM member WHERE name = ?");
			pps.setString(1, memberName);
			rs = pps.executeQuery();
			rs.next();
			int count = rs.getInt(1);
			memberList = new MemberDTO[count];

			pps = con.prepareStatement("SELECT * FROM member WHERE name = ?");
			pps.setString(1, memberName);
			rs = pps.executeQuery();
			int i = 0;
			while (rs.next()) {
				memberList[i] = new MemberDTO();
				memberList[i].setMemberID(rs.getString(1));
				memberList[i].setName(rs.getString(2));
				memberList[i].setPhone(rs.getInt(3));
				memberList[i].setSex(rs.getString(4));
				memberList[i].setBirth(rs.getInt(5));
				i++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return memberList;

	}

	public void enrollMember(MemberDTO member) {
		try {
			sb = "INSERT INTO member VALUES (member_seq.nextval, ?, ?,?,?,0)";

			pps = con.prepareStatement(sb);
			pps.setString(1, member.getName());
			pps.setInt(2, member.getPhone());
			pps.setString(3, member.getSex());
			pps.setInt(4, member.getBirth());
			pps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public OrderListDTO[] lastBuyingData(MemberDTO member) {
		sb = "select * from (select * from orderlist where memberid = ? order by orderdate desc) where rownum<11";
		OrderListDTO[] orderList = new OrderListDTO[10];
		try {
			pps = con.prepareStatement(sb);
			pps.setString(1, member.getMemberID());
			rs = pps.executeQuery();
			for (int i = 0; rs.next(); i++) {
				orderList[i] = new OrderListDTO();
				orderList[i].setId(rs.getString(2));
				orderList[i].setMemberId(rs.getString(3));
				orderList[i].setOrderDate(rs.getString(4));
				orderList[i].setOrderPrice(rs.getInt(5));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return orderList;
	}

	public MenuDTO[] mostBuyingData(MemberDTO member) {
		sb = "select * from (select mn.menuid, mn.name, sum(od.count) \"총 주문 갯수\" from member m, orderlist ol, orderdetail od, menu mn where m.memberid = ol.memberid and ol.orderid = od.orderid and od.menuid=mn.menuid and m.memberid = ? group by  mn.menuid, mn.name order by  sum(od.count) desc) where rownum <6";
		MenuDTO[] menuList = new MenuDTO[5];
		try {
			pps = con.prepareStatement(sb);
			pps.setString(1, member.getMemberID());
			rs = pps.executeQuery();
			for (int i = 0; rs.next(); i++) {
				menuList[i] = new MenuDTO();
				menuList[i].setMenuId(rs.getString(1));
				menuList[i].setName(rs.getString(2));
				menuList[i].setAmount(rs.getInt(3));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return menuList;
	}

///////////////////////////////////////////////////////////////////////////////
	public void setDTOData() {
		String getStoreInfo = "SELECT * FROM STOREINFO WHERE STORENO = ?";

		try {
			pps = con.prepareStatement(getStoreInfo);
			pps.setString(1, store.getStoreId());
			rs = pps.executeQuery();
			while (rs.next()) {
				store.setName(rs.getString(2));
				store.setOwner(rs.getString(3));
				store.setOpendate(rs.getString(4));
				store.setClosedate(rs.getString(5));
				store.setPhone(rs.getInt(6));
				store.setAddress(rs.getString(7));
			}
			System.out.println("store 정보 갱신완료");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public StaffDTO[] showStaffList() {
		try {
			pps = con.prepareStatement("SELECT COUNT(rownum) FROM STAFF WHERE STORENO = ?");
			pps.setString(1, store.getStoreId());
			rs = pps.executeQuery();
			rs.next();
			int staffCount = rs.getInt(1);
			StaffDTO[] staffList = new StaffDTO[staffCount];

			String getStaffInfo = "SELECT staffno, name, TO_CHAR(joindate, 'yy/mm/dd'), TO_CHAR(leavedate, 'yy/mm/dd'), phone, birth, sex, workstyle FROM STAFF WHERE STORENO = ? order by staffno";

			pps = con.prepareStatement(getStaffInfo);

			pps.setString(1, store.getStoreId());
			rs = pps.executeQuery();
			for (int i = 0; rs.next(); i++) {
				staff.setId(rs.getString(1));
				staff.setName(rs.getString(2));
				staff.setJoinDate(rs.getString(3));
				staff.setLeaveDate(rs.getString(4));
				staff.setPhone(rs.getInt(5));

				staff.setBirth(rs.getInt(6));

				staff.setSex(rs.getString(7));
				staff.setWorkstyle(rs.getString(8));
				staffList[i] = new StaffDTO(staff.getId(), staff.getName(), staff.getJoinDate(), staff.getLeaveDate(),
						staff.getPhone(), staff.getBirth(), staff.getSex(), staff.getWorkstyle());

			}
			return staffList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public StaffDTO[] searchStaff(String staffName) {
		try {
			staff.setName(staffName);
			pps = con.prepareStatement("Select count(staffno) from staff where storeno = ? and name = ?");
			pps.setString(1, store.getStoreId());
			pps.setString(2, staff.getName());
			rs = pps.executeQuery();
			rs.next();
			int result = rs.getInt(1);
			StaffDTO[] searchResult = new StaffDTO[result];

			pps = con.prepareStatement(
					"Select staffno, name, TO_CHAR(joindate, 'yy/mm/dd'), TO_CHAR(leavedate, 'yy/mm/dd'), phone, birth, sex, workstyle from staff where storeno = ? and name = ? order by staffno");

			pps.setString(1, store.getStoreId());
			pps.setString(2, staff.getName());
			rs = pps.executeQuery();
			int i = 0;
			while (rs.next()) {
				searchResult[i] = new StaffDTO();
				searchResult[i].setId(rs.getString(1));
				searchResult[i].setName(rs.getString(2));

				searchResult[i].setJoinDate(rs.getString(3));
				searchResult[i].setLeaveDate(rs.getString(4));
				searchResult[i].setPhone(rs.getInt(5));
				searchResult[i].setBirth(rs.getInt(6));
				searchResult[i].setSex(rs.getString(7));
				searchResult[i].setWorkstyle(rs.getString(8));

				i++;
			}
			return searchResult;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	// 혜영===========================================

	// 매장관리>매장정보>기본정보
	public StoreDTO storeInfoDefault() {
		StoreDTO sDto = new StoreDTO();
		try {
			pps = con.prepareStatement("SELECT * FROM STOREINFO WHERE storeno = ?");
			pps.setString(1, store.getStoreId());
			rs = pps.executeQuery();
			while (rs.next()) {
				sDto.setStoreId(rs.getString(1));
				sDto.setName(rs.getString(2));
				sDto.setOwner(rs.getString(3));
				sDto.setOpendate(rs.getString(4));
				sDto.setClosedate(rs.getString(5));
				sDto.setPhone(rs.getInt(6));
				sDto.setAddress(rs.getString(7));
			}
			return sDto;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}// storeInfoDefault

	// 매장관리>매장정보>정보수정
	public void storeInfoMotify(StoreDTO sDto) {

		StoreDTO orgDto = new StoreDTO();
		try {
			pps = con.prepareStatement("select name from STOREINFO where name = ?");
			pps.setString(1, sDto.getName());
			rs = pps.executeQuery();
			while (rs.next()) {
				orgDto.setName(rs.getString(1));
			System.out.println(0);
			}
			System.out.println(1);
			
			
			if (sDto.getName().equals(orgDto.getName())) {
				System.out.println(2);
				pps = con.prepareStatement(
						"UPDATE STOREINFO SET name=?, owner = ?, opendate = ?, closedate = ?, phone = ? , address = ? WHERE name = ?");
				System.out.println(3);
				pps.setString(1, sDto.getName());
				pps.setString(2, sDto.getOwner());
				pps.setString(3, sDto.getOpendate());
				pps.setString(4, sDto.getClosedate());
				pps.setInt(5, sDto.getPhone());
				pps.setString(6, sDto.getAddress());
				pps.setString(7, sDto.getName());
				System.out.println(4);
				pps.executeUpdate();
				System.out.println(5);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println(6);
	}// storeInfoMotify

	// 매장관리>재고관리>현재비품 현황
	public void showstockList() {
		try {
			stmt = con.createStatement();

			String getStockInfo = "SELECT * FROM stockview";
			rs = stmt.executeQuery(getStockInfo);

			for (int i = 0; rs.next(); i++) {
				scripts.send("" + rs.getString(1) + "|" + rs.getString(2) + "|" + rs.getInt(3) + "|" + rs.getInt(4));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}// showstockList

	// 메뉴관리>메뉴등록
	public void menuEnroll(MenuItemDTO eDto) {
		int resultInt = 0;
		MenuItemDTO orgMe = new MenuItemDTO();

		try {
			pps = con.prepareStatement("SELECT * FROM menu WHERE menuid = ?");
			pps.setString(1, eDto.getMenuId());
			rs = pps.executeQuery();
			rs.next();

			// 입력한 내용이 기존에 있는 데이터라면... update
			if (!eDto.getMenuId().equals(orgMe.getMenuId())) {
				pps = con.prepareStatement(
						"INSERT INTO menu (menuid,name,price,category,activation) VALUES (?,?,?,?,?)");
				pps.setString(1, eDto.getMenuId());
				pps.setString(2, eDto.getName());
				pps.setInt(3, eDto.getPrice());
				pps.setString(4, eDto.getCategory());
				pps.setString(5, eDto.getActivation());
				resultInt = pps.executeUpdate();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}// menuEnroll

	// 메뉴관리>메뉴정보>수정
	public void menuModify(MenuItemDTO mDto) {
		int resultInt = 0;
		MenuItemDTO orgMe = new MenuItemDTO();
		// 바로 받아와서 바로 수정
		try {
			pps = con.prepareStatement("SELECT menuid FROM menu WHERE menuid = ?");
			pps.setString(1, mDto.getMenuId());
			rs = pps.executeQuery();
			while (rs.next()) {
				orgMe.setMenuId(rs.getString(1));
			}
			// 기존에 있는거 수정
			if (mDto.getMenuId().equals(orgMe.getMenuId())) {
				pps = con.prepareStatement("UPDATE menu SET price = ?, activation = ? WHERE menuid = ?");
				pps.setInt(1, mDto.getPrice());
				pps.setString(2, mDto.getActivation());
				pps.setString(3, mDto.getMenuId());
				resultInt = pps.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 메뉴관리>메뉴정보>삭제
	public void menuDelete(MenuDTO menu) {
		int resultInt = 0;
		MenuDTO orgmenu = new MenuDTO();

		try {
			// 입력한 id와 쿼리에 있는거와 같은지 찾아
			pps = con.prepareStatement("SELECT menuid FROM menu where menuid= ?");
			pps.setString(1, menu.getMenuId());
			rs = pps.executeQuery();
			rs.next();
			orgmenu.setMenuId(rs.getString(1));

			if (menu.getMenuId().equals(orgmenu.getMenuId())) {
				pps = con.prepareStatement("DELETE FROM menu WHERE menuid = ?");
				pps.setString(1, menu.getMenuId());
				resultInt = pps.executeUpdate();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}// menuDelete

	// 메뉴관리>메뉴정보>활성화
	public void menuActivation(String n) {

		String actY = "SELECT * FROM menu WHERE activation = 'Y'";
		String actN = "SELECT * FROM menu WHERE activation = 'N'";
		try {
			if (n.equals("y")) {
				rs = stmt.executeQuery(actY);
				while (rs.next()) {
					scripts.send("" + rs.getString(1) + "\t" + rs.getString(2) + "\t" + rs.getString(3) + "\t"
							+ rs.getString(4));
				}
			} else if (n.equals("n")) {
				rs = stmt.executeQuery(actN);
				while (rs.next()) {
					scripts.send("" + rs.getString(1) + "\t" + rs.getString(2) + "\t" + rs.getString(3) + "\t"
							+ rs.getString(4));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}// menuActivation();

	// 메뉴관리>메뉴검색>이름
	public void searchMenuName(String str) {
		try {
			pps = con.prepareStatement(
					"SELECT name FROM recipe WHERE menuid = (SELECT menuid FROM menu WHERE name = ?)");
			pps.setString(1, str); // 가져온 단어 넣었어
			rs = pps.executeQuery();

			while (rs.next()) {
				scripts.send(rs.getString(1));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 메뉴관리>메뉴검색>종류
	public void searchMenuCategory(String str) {
		String coffee = "SELECT menuid, name, price, category FROM menu WHERE category = '커피음료'";
		String noncoffee = "SELECT menuid, name, price, category FROM menu WHERE category = '음료'";
		String bakery = "SELECT menuid, name, price, category FROM menu WHERE category = '베이커리'";
		String tea = "SELECT menuid, name, price, category FROM menu WHERE category = '차'";
		try {
			if (str.equals("커피음료")) {
				rs = stmt.executeQuery(coffee);
				while (rs.next()) {
					scripts.send("" + rs.getString(1) + "\t" + rs.getString(2) + "\t" + rs.getString(3) + "\t"
							+ rs.getString(4));
				}
			} else if (str.equals("음료")) {
				rs = stmt.executeQuery(noncoffee);
				while (rs.next()) {
					scripts.send("" + rs.getString(1) + "\t" + rs.getString(2) + "\t" + rs.getString(3) + "\t"
							+ rs.getString(4));
				}
			} else if (str.equals("베이커리")) {
				rs = stmt.executeQuery(bakery);
				while (rs.next()) {
					scripts.send("" + rs.getString(1) + "\t" + rs.getString(2) + "\t" + rs.getString(3) + "\t"
							+ rs.getString(4));
				}
			} else if (str.equals("차")) {
				rs = stmt.executeQuery(bakery);
				while (rs.next()) {
					scripts.send("" + rs.getString(1) + "\t" + rs.getString(2) + "\t" + rs.getString(3) + "\t"
							+ rs.getString(4));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}// searchMenuCategory

	// 매장관리>재고관리>입고(원재료)
	public void rawstock(RawMaterialDTO raw) {
		int resultInt = 0;
		RawMaterialDTO orgRaw = new RawMaterialDTO();
		try {
			pps = con.prepareStatement("SELECT * FROM RAWMATERIAL WHERE RAWMATEID = ?");
			pps.setString(1, raw.getId());
			rs = pps.executeQuery();
			while (rs.next()) {
				orgRaw.setId(rs.getString(1));
				orgRaw.setStock(rs.getInt(4));
				orgRaw.setCost(rs.getInt(5));
			}
			if (raw.getId().equals(orgRaw.getId())) {
				raw.setStock(orgRaw.getStock() + raw.getStock());
				raw.setCost(orgRaw.getCost() + raw.getCost());
				pps = con.prepareStatement("update RAWMATERIAL set STOCK = ? , COST = ? where RAWMATEID = ?");
				pps.setInt(1, raw.getStock());
				pps.setInt(2, raw.getCost());
				pps.setString(3, raw.getId());
				resultInt = pps.executeUpdate();
			} else {
				pps = con.prepareStatement(
						"insert into RAWMATERIAL (RAWMATEID, NAME, CATEGORY, STOCK, COST) values (?, ?, ?, ?, ?)");
				pps.setString(1, raw.getId());
				pps.setString(2, raw.getName());
				pps.setString(3, raw.getCategory());
				pps.setInt(4, raw.getStock());
				pps.setInt(5, raw.getCost());
				resultInt = pps.executeUpdate();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}// rawstock

	// 매장관리>재고관리>입고(비품)
	public void matestock(MaterialDTO raw) {
		int resultInt = 0;
		MaterialDTO orgMat = new MaterialDTO();

		try {
			pps = con.prepareStatement("SELECT * FROM material WHERE mateid=?");
			pps.setString(1, raw.getId());
			rs = pps.executeQuery();
			while (rs.next()) {
				orgMat.setId(rs.getString(1));
				orgMat.setStock(rs.getInt(3));
				orgMat.setCost(rs.getInt(4));
			}
			if (raw.getId().equals(orgMat.getId())) {
				raw.setStock(orgMat.getStock() + raw.getStock());
				raw.setCost(orgMat.getCost() + raw.getCost());
				pps = con.prepareStatement("UPDATE material SET stock = ?, cost = ? WHERE mateID = ?");
				pps.setInt(1, raw.getStock());
				pps.setInt(2, raw.getCost());
				pps.setString(3, raw.getId());
				resultInt = pps.executeUpdate();
			} else {
				pps = con.prepareStatement("insert into MATERIAL (MATEID, NAME, STOCK, COST) values (?, ?, ?, ?)");
				pps.setString(1, raw.getId());
				pps.setString(2, raw.getName());
				pps.setInt(3, raw.getStock());
				pps.setInt(4, raw.getCost());
				resultInt = pps.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}// matestock

	public void updateStaffInfo(StaffDTO staff) {
		sb = "UPDATE staff SET name = ?, joindate = ?, leavedate = ?, phone = ?, birth = ?, sex = ?, workstyle = ? WHERE staffno = ?";
		try {
			pps = con.prepareStatement(sb);
			System.out.println(staff.getName() + staff.getJoinDate() + staff.getLeaveDate() + staff.getPhone()
					+ staff.getBirth() + staff.getSex() + staff.getWorkstyle() + staff.getId());
			pps.setString(1, staff.getName());
			pps.setString(2, staff.getJoinDate());
			pps.setString(3, staff.getLeaveDate());
			pps.setInt(4, staff.getPhone());
			pps.setInt(5, staff.getBirth());
			pps.setString(6, staff.getSex());
			pps.setString(7, staff.getWorkstyle());
			pps.setString(8, staff.getId());
			pps.executeUpdate();
			System.out.println("staff info update finish");
		} catch (SQLException e) {
			System.out.println("format 불일치로 인한 error");
		}

	}

	public void deleteStaffInfo(StaffDTO staff) {

		try {
			if (staff.getWorkstyle().contentEquals("정직원")) {
				stmt.executeUpdate("DELETE FROM staff_all WHERE staffno =" + staff.getId());

			} else if (staff.getWorkstyle().contentEquals("파트타임")) {
				stmt.executeUpdate("DELETE FROM staff_part WHERE staffno =" + staff.getId());
			}
			sb = ("DELETE FROM staff WHERE staffno = ?");

			pps = con.prepareStatement(sb);
			pps.setString(1, staff.getId());
			pps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void staffEnroll(StaffDTO staff) {

		try {
			con.setAutoCommit(false);
			sb = "INSERT INTO staff VALUES (staff_seq.nextval, ?, ?,?,?,?,?,?,?)";

			pps = con.prepareStatement(sb);
			pps.setString(1, staff.getName());
			pps.setString(2, staff.getJoinDate());
			pps.setString(3, staff.getLeaveDate());
			pps.setInt(4, staff.getPhone());
			pps.setInt(5, staff.getBirth());
			pps.setString(6, staff.getSex());
			pps.setString(7, staff.getWorkstyle());
			pps.setString(8, staff.getStoreId());
			pps.executeUpdate();

			if (staff.getWorkstyle().contentEquals("정직원")) {
				sb = "INSERT INTO staff_all VALUES (staff_seq.currval, ?, ?";
				pps = con.prepareStatement(sb);
				pps.setInt(1, 5);
				pps.setInt(2, 150);
				pps.executeUpdate();
			} else if (staff.getWorkstyle().contentEquals("파트타임")) {

				sb = "INSERT INTO staff_part VALUES (staff_seq.currval, ?, ?, ?)";
				pps = con.prepareStatement(sb);

				pps.setInt(1, 5);
				pps.setInt(2, 4);
				pps.setInt(3, 8400);
				pps.executeUpdate();
			}
			con.commit();

			con.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<String[]> showSalaryOption() {
		sb = "select staffno, name, workstyle, workday, 8 직무시간, round(sal/4.35/40) 시급, sal 월급  from staff natural join staff_all where storeno = ? and workstyle = '정직원' union select staffno, name, workstyle, workday, hour, pay_per_hour, workday*hour*pay_per_hour*4.35 월급 from staff natural join staff_part where storeno = ? and workstyle = '파트타임'";
		ArrayList<String[]> optionList = new ArrayList<>();
		try {
			pps = con.prepareStatement(sb);
			pps.setString(1, store.getStoreId());
			pps.setString(2, store.getStoreId());
			rs = pps.executeQuery();
			while (rs.next()) {
				String[] staff = { rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
						Integer.toString(rs.getInt(5)), Integer.toString(rs.getInt(6)),
						Integer.toString(rs.getInt(7)) };
				optionList.add(staff);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return optionList;
	}

	public void changeTime(StaffDTO staff, int workDay, int workTime, int pay) {
		try {
			if (staff.getWorkstyle().contentEquals("정직원")) {
				stmt.executeUpdate("UPDATE staff SET workstyle = '파트타임' WHERE staffno =" + staff.getId());
				stmt.executeUpdate("DELETE FROM staff_all WHERE staffno = " + staff.getId());
				pps = con.prepareStatement("INSERT INTO staff_part VALUES(?,?,?,?)");
				pps.setString(1, staff.getId());
				pps.setInt(2, workDay);
				pps.setInt(3, workTime);
				pps.setInt(4, pay);
				pps.executeUpdate();
			} else if (staff.getWorkstyle().contentEquals("파트타임")) {
				stmt.executeUpdate("UPDATE staff SET workstyle = '정직원' WHERE staffno =" + staff.getId());
				stmt.executeUpdate("DELETE FROM staff_part WHERE staffno = " + staff.getId());
				pps = con.prepareStatement("INSERT INTO staff_all VALUES(?,?,?)");
				pps.setString(1, staff.getId());
				pps.setInt(2, workDay);
				pps.setInt(3, pay);
				pps.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void changeWorkday(StaffDTO staff, int workDay) {
		String all = "UPDATE staff_all SET workDay = ? WHERE staffno = ?";
		String part = "UPDATE staff_part SET workDay = ? WHERE staffno = ?";
		if (staff.getWorkstyle().contentEquals("정직원")) {
			sb = all;
		} else if (staff.getWorkstyle().contentEquals("파트타임")) {
			sb = part;
		}
		try {
			pps = con.prepareStatement(sb);
			pps.setInt(1, workDay);
			pps.setString(2, staff.getId());
			pps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void changeWorkTime(StaffDTO staff, int workTime) {
		try {
			stmt.execute("UPDATE staff_part SET hour = " + workTime + " WHERE staffno = " + staff.getId());
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void changePayMonth(StaffDTO staff, int pay) {
		try {
			stmt.execute("UPDATE staff_all SET SAL = " + pay + " WHERE staffno = " + staff.getId());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void changePayHour(StaffDTO staff, int pay) {
		try {
			stmt.execute("UPDATE staff_part SET pay_per_hour = " + pay + " WHERE staffno = " + staff.getId());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

//============================================================
	public MenuItemDTO[] menuInfoDefault() {
		MenuItemDTO[] itemList = null;
		try {
			rs = stmt.executeQuery("SELECT COUNT(menuId) FROM menu");
			rs.next();
			int count = rs.getInt(1);
			itemList = new MenuItemDTO[count];
			rs = stmt.executeQuery("SELECT * FROM menu");
			int i = 0;
			while (rs.next()) {
				itemList[i] = new MenuItemDTO();
				itemList[i].setMenuId(rs.getString(1));
				itemList[i].setName(rs.getString(2));
				itemList[i].setPrice(rs.getInt(3));
				itemList[i].setCategory(rs.getString(4));
				i++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return itemList;
	}

	public String[][] salesInfoDefault() {
		sb = "select * from (SELECT to_char(orderdate, 'yy/mm/dd') 날짜, sum(orderPrice) 총매출 FROM orderList WHERE storeno = ? GROUP BY orderdate ORDER BY orderdate desc) where rownum<8";
		String[][] salesData = new String[7][2];

		try {
			pps = con.prepareStatement(sb);
			pps.setString(1, store.getStoreId());
			rs = pps.executeQuery();
			for (int i = 0; rs.next(); i++) {
				salesData[i][0] = rs.getString(1);
				salesData[i][1] = Integer.toString(rs.getInt(2));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return salesData;
	}

	public ArrayList<String[]> salesInfoDefault(String[] date) {
		sb = "select * from (SELECT to_char(orderdate, 'yy/mm/dd') 날짜, sum(orderPrice) 총매출 FROM orderList WHERE storeno = ? GROUP BY orderdate HAVING orderdate between ? and ? ORDER BY orderdate desc)";
		ArrayList<String[]> salesData = new ArrayList<>();

		try {
			pps = con.prepareStatement(sb);
			pps.setString(1, store.getStoreId());
			pps.setString(2, date[0]);
			pps.setString(3, date[1]);
			rs = pps.executeQuery();
			int sum = 0;
			for (int i = 0; rs.next(); i++) {
				sum += rs.getInt(2);
				String[] temp = { rs.getString(1), Integer.toString(rs.getInt(2)), Integer.toString(sum) };
				salesData.add(temp);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return salesData;
	}

	public String[][] salesMenuDate(String menuName) {
		// sb = "select m.name, sum(o.count), sum(sumprice) from menu m, orderdetail o,
		// orderlist ol where m.menuid = o.menuid and o.orderid = ol.orderid GROUP BY
		// m.name HAVING m.name=?";

		sb = "select * from (select to_char(orderdate, 'yy/mm/dd'), sum(count), sum(sumprice) from menu m, orderdetail o, orderlist ol where m.menuid = o.menuid and o.orderid = ol.orderid and m.name = ? and storeno = ? GROUP BY orderdate order by ol.orderdate desc) where rownum<8";
		String[][] salesMenuWeek = new String[7][4];
		try {
			pps = con.prepareStatement(sb);
			pps.setString(1, menuName);
			pps.setString(2, store.getStoreId());
			rs = pps.executeQuery();
			int sum = 0;
			for (int i = 0; rs.next(); i++) {
				salesMenuWeek[i][0] = rs.getString(1);
				salesMenuWeek[i][1] = Integer.toString(rs.getInt(2));
				salesMenuWeek[i][2] = Integer.toString(rs.getInt(3));
				if (Integer.toString(rs.getInt(3)) != null) {
					sum += rs.getInt(3);
				}
			}
			salesMenuWeek[6][3] = Integer.toString(sum);
			System.out.println(salesMenuWeek[6][3]);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return salesMenuWeek;
	}

	public String salesMenuDate30(String menuName) {
		sb = "select sum(sumprice) from menu m, orderdetail o, orderList ol where m.menuid = o.menuid and o.orderid = ol.orderid and m.name =? and ol.storeno = ? and orderdate between sysdate-30 and sysdate";
		String sum30 = null;
		try {
			pps = con.prepareStatement(sb);
			pps.setString(1, menuName);
			pps.setString(2, store.getStoreId());
			rs = pps.executeQuery();
			rs.next();
			sum30 = Integer.toString(rs.getInt(1));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return sum30;

	}

	public String salesMenuDate365(String menuName) {
		sb = "select sum(sumprice) from menu m, orderdetail o, orderList ol where m.menuid = o.menuid and o.orderid = ol.orderid and m.name =? and ol.storeno = ? and orderdate between sysdate-365 and sysdate";
		String sum365 = null;
		try {
			pps = con.prepareStatement(sb);
			pps.setString(1, menuName);
			pps.setString(2, store.getStoreId());
			rs = pps.executeQuery();
			rs.next();
			sum365 = Integer.toString(rs.getInt(1));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return sum365;

	}

}
