package _0522;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import _0522.DTO.*;

public class QueryList {
// 쿼리문 DB 전달을 위한 변수
	Connection con = null;
	Statement stmt = null;
	PreparedStatement pps = null;
	ResultSet rs = null;
	String sb = null;

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

	QueryList(Connection con) {
		this.con = con;
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

	public void member() {
		String memberid;
		String name;
		String sex;
		int birth;
		int phone;

		sb = ("select  memberid, name, phone, sex, birth from member");
		try {
			while (rs.next()) {
				rs.getString("memberid");
				rs.getString("name");
				rs.getString("phone");
				rs.getString("sex");
				rs.getInt("birth");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void setDTOData() {
		String getStoreInfo = "SELECT * FROM STOREINFO WHERE STORENO = ?";
		String getStaffInfo = "SELECT * FROM STAFF WHERE STORENO = ?";
		String getOrderListInfo = "SELECT * FROM ORDERLIST WHERE STORENO = ?";

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

			pps = con.prepareStatement(getStaffInfo);
			pps.setString(1, store.getStoreId());
			rs = pps.executeQuery();
			while (rs.next()) {
				staff.setId(rs.getString(1));
				staff.setName(rs.getString(2));
				staff.setJoinDate(rs.getString(3));
				staff.setLeaveDate(rs.getString(4));
				staff.setPhone(rs.getInt(5));
				staff.setSex(rs.getString(7));
				staff.setWorkstyle(rs.getString(8));
			}

			System.out.println("staff 정보 갱신완료");

			pps = con.prepareStatement(getOrderListInfo);
			pps.setString(1, store.getStoreId());
			rs = pps.executeQuery();
			while (rs.next()) {
				orderList.setId(rs.getString(2));
				orderList.setMemberId(rs.getString(3));
				orderList.setOrderDate(rs.getString(4));
				orderList.setOrderPrice(rs.getInt(5));

			}

			System.out.println("orderList 정보 갱신완료");

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

	public StaffDTO[] searchStaff(String staffName){
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
			int i =0;
			while(rs.next()) {
				searchResult[i] = new StaffDTO();
				searchResult[i].setId(rs.getString(1));
				searchResult[i].setName(rs.getString(2));
				searchResult[i].setPhone(rs.getInt(3));
				//System.out.println(searchResult[i].getId()+searchResult[i].getName());
				System.out.println("검색중"+i);
				i++;
			}
			return searchResult;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	
	public void updateStaffInfo() {
		sb=("stmt");
	}
	
	
	
	// 혜영===========================================

	// 매장정보>정보수정
	public void storeInfoMotify(String num, String name, String owner, String open, String close, String phone,
			String addr) {
		// update 할 내용 적어주기 동시 에 업데이트 됐어도
	}

	// 매장관리>재고관리>입고(원재료)
	public void rawstock(String id, String name, String category, String stock, String cost) {

	}

	// 매장관리>재고관리>입고(비품)
	public void matestock(String id, String name, String stock, String cost) {

	}

}