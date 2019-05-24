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
		this.userId=userId;
		this.material=material;
		this.member=member;
		this.menu=menu;
		this.menuItem=menuItem;
		this.part=part;
		this.regular=regular;
		this.raw=raw;
		this.staff=staff;
		this.stock=stock;
		this.store=store;
		this.orderList = orderList;

	}

	QueryList(Connection con) {
		this.con = con;
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
				return true;
			} else
				return false;

		} catch (SQLException e) {
			return false;
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
			while(rs.next())
			{
				store.setName(rs.getString(2));
				store.setOwner(rs.getString(3));
				store.setOpendate(rs.getInt(4));
				store.setClosedate(rs.getInt(5));
				store.setPhone(rs.getInt(6));
				store.setAddress(rs.getString(7));
			}
			System.out.println("store 정보 갱신완료");
			
			
			pps = con.prepareStatement(getStaffInfo);
			pps.setString(1, store.getStoreId());
			rs = pps.executeQuery();
			while(rs.next())
			{
				staff.setId(rs.getString(1));
				staff.setName(rs.getString(2));
				staff.setJoinDate(rs.getInt(3));
				staff.setLeaveDate(rs.getInt(4));
				staff.setPhone(rs.getInt(5));
				staff.setSex(rs.getString(7));
				staff.setWorkstyle(rs.getString(8));
			}
			
			System.out.println("staff 정보 갱신완료");
			
			pps = con.prepareStatement(getOrderListInfo);
			pps.setString(1, store.getStoreId());
			rs = pps.executeQuery();
			while(rs.next())
			{
				orderList.setId(rs.getString(2));
				orderList.setMemberId(rs.getString(3));
				orderList.setOrderdate(rs.getInt(4));
				orderList.setOrderprice(rs.getInt(5));

			}
			
			System.out.println("orderList 정보 갱신완료");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}