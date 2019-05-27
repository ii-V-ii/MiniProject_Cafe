
package _0522;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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

	public MemberDTO[] showMembers() {

		String memberid;
		String name;
		String sex;
		String birth;
		int phone;

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
						rs.getString("sex"), rs.getString("birth"));
			}
			return memberList;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;

	}

//	public MemberDTO[] searchMember() {//일단주석처리 
//		String memberid;
//		String name;
//		String sex;
//		String birth;
//		int phone;
//		
//		try {
//			pps = con.prepareStatement("SELECT COUNT(rownum) FROM member WHERE name = ?");
//			
//			
//			
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		
//		
//	}
///////////////////////////////////////////////////////////////////////////////
	public void setDTOData() {
		String getStoreInfo = "SELECT * FROM STOREINFO WHERE STORENO = ?";

//		String getStaffInfo = "SELECT * FROM STAFF WHERE STORENO = ?";
//		String getOrderListInfo = "SELECT * FROM ORDERLIST WHERE STORENO = ?";
//		

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

//			pps = con.prepareStatement(getStaffInfo);
//			pps.setString(1, store.getStoreId());
//			rs = pps.executeQuery();
//			while(rs.next())
//			{
//				staff.setId(rs.getString(1));
//				staff.setName(rs.getString(2));
//				staff.setJoinDate(rs.getString(3));
//				staff.setLeaveDate(rs.getString(4));
//				staff.setPhone(rs.getInt(5));
//				staff.setSex(rs.getString(7));
//				staff.setWorkstyle(rs.getString(8));
//			}
//			
//			System.out.println("staff 정보 갱신완료");
//			
//			pps = con.prepareStatement(getOrderListInfo);
//			pps.setString(1, store.getStoreId());
//			rs = pps.executeQuery();
//			while(rs.next())
//			{
//				orderList.setId(rs.getString(2));
//				orderList.setMemberId(rs.getString(3));
//				orderList.setOrderDate(rs.getString(4));
//				orderList.setOrderPrice(rs.getInt(5));
//
//			}
//			
//			System.out.println("orderList 정보 갱신완료");

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
			String getStaffInfo = "SELECT * FROM STAFF WHERE STORENO = ?";
			pps = con.prepareStatement(getStaffInfo);

			pps.setString(1, store.getStoreId());
			rs = pps.executeQuery();
			for (int i = 0; rs.next(); i++) {
				staff.setId(rs.getString(1));
				staff.setName(rs.getString(2));
				staff.setJoinDate(rs.getString(3));
				staff.setLeaveDate(rs.getString(4));
				staff.setPhone(rs.getInt(5));
				staff.setBirth(rs.getString(6));
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

			pps = con.prepareStatement("Select staffno, name, phone from staff where storeno = ? and name = ?");
			pps.setString(1, store.getStoreId());
			pps.setString(2, staff.getName());
			rs = pps.executeQuery();
			int i = 0;
			while (rs.next()) {
				searchResult[i] = new StaffDTO();
				searchResult[i].setId(rs.getString(1));
				searchResult[i].setName(rs.getString(2));
				searchResult[i].setPhone(rs.getInt(3));
				// System.out.println(searchResult[i].getId()+searchResult[i].getName());
				System.out.println("검색중" + i);
				i++;
			}
			return searchResult;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void updateStaffInfo() {
		sb = ("stmt");
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
		int resultInt = 0;
		StoreDTO orgDto = new StoreDTO();
		try {
			pps = con.prepareStatement("select storeno from STOREINFO where storeno = ?");
			pps.setString(1, orgDto.getStoreId());
			rs = pps.executeQuery();
			while (rs.next()) {
				orgDto.setStoreId(rs.getString(1));
			}

			if (sDto.getStoreId().equals(orgDto.getStoreId())) {
				pps = con.prepareStatement(
						"UPDATE STOREINFO SET name = ? , owner = ?, opendate = TO_CHAR(?,'YYYYMMDD') ,closedate = TO_CHAR(?,'YYYYMMDD'), phone = ? ,address = ? WHERE storeno = ?");
				pps.setString(1, sDto.getName());
				pps.setString(2, sDto.getOwner());
				pps.setString(3, sDto.getOpendate());
				pps.setString(4, sDto.getClosedate());
				pps.setInt(5, sDto.getPhone());
				pps.setString(6, sDto.getAddress());
				pps.setString(7, sDto.getStoreId());
				resultInt = pps.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}// storeInfoMotify

	// 매장관리>재고관리>현재비품 현황
	public void showstockList() {
		try {
			stmt = con.createStatement();

			String getStockInfo = "SELECT * FROM stockview";
			rs = stmt.executeQuery(getStockInfo);

			for (int i = 0; rs.next(); i++) {
				scripts.send("" + rs.getString(1) + "|" + rs.getString(2) + "|" + rs.getInt(3) + "|" + rs.getInt(4));
				// stockList[i] = new StockDTO();
			}
			// return stockList;
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
			if (eDto.getMenuId().equals(orgMe.getMenuId())) {
				System.out.println("기존에 있는 메뉴입니다.");
				System.out.println("메뉴정보에서 수정해주세요.");
			} else {
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
/*
 	int resultInt = 0;
		StoreDTO orgDto = new StoreDTO();
		try {
			pps = con.prepareStatement("select storeno from STOREINFO where storeno = ?");
			pps.setString(1, orgDto.getStoreId());
			rs = pps.executeQuery();
			while (rs.next()) {
				orgDto.setStoreId(rs.getString(1));
			}

			if (sDto.getStoreId().equals(orgDto.getStoreId())) {
				pps = con.prepareStatement(
						"UPDATE STOREINFO SET name = ? , owner = ?, opendate = TO_CHAR(?,'YYYYMMDD') ,closedate = TO_CHAR(?,'YYYYMMDD'), phone = ? ,address = ? WHERE storeno = ?");
				pps.setString(1, sDto.getName());
				pps.setString(2, sDto.getOwner());
				pps.setString(3, sDto.getOpendate());
				pps.setString(4, sDto.getClosedate());
				pps.setInt(5, sDto.getPhone());
				pps.setString(6, sDto.getAddress());
				pps.setString(7, sDto.getStoreId());
				resultInt = pps.executeUpdate();
			}
 
 
 */
	// 메뉴관리>메뉴정보>수정
	public void menuModify(MenuItemDTO mDto) {
		int resultInt = 0;
		MenuItemDTO orgMe = new MenuItemDTO();
		//바로 받아와서 바로 수정
		try {
			pps = con.prepareStatement("SELECT menuid FROM menu WHERE menuid = ?");
			pps.setString(1, mDto.getMenuId());
			rs = pps.executeQuery();
			while(rs.next()) {
				orgMe.setMenuId(rs.getString(1));
			}
			// 기존에 있는거 수정
			if (mDto.getMenuId().equals(orgMe.getMenuId())) {
//				mDto.setPrice(orgMe.getPrice());
//				mDto.setActivation(orgMe.getActivation());
				pps = con.prepareStatement("UPDATE menu SET price = ?, activation = ? WHERE menuid = ?");
				pps.setInt(1, mDto.getPrice());
				pps.setString(2, mDto.getActivation());
				pps.setString(3, mDto.getMenuId());
				resultInt = pps.executeUpdate();
			}
			// 새로 입력해야한다?
//			else {
//				System.out.println("기존의 메뉴가 없습니다.");
//				System.out.println("메뉴관리에서 메뉴등록을 해주세요.");
//			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

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

}
