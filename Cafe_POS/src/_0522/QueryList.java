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

	public void setDTO(IdVO userId, MaterialDTO material, MemberDTO member, MenuDTO menu, MenuItemDTO menuItem,
			PartTimeStaffDTO part, RegularStaffDTO regular, RawMaterialDTO raw, StaffDTO staff, StockDTO stock,
			StoreDTO store) {
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
				return true;
			} else
				return false;

		} catch (SQLException e) {
			return false;
		}

	}

	public void setDTOData() {

	}
	
	// 혜영===========================================

	//매장정보>정보수정
	public void storeInfoMotify(String num, String name, String owner, String open, String close, String phone, String addr) {
		//update 할 내용 적어주기 동시 에 업데이트 됐어도 
	}
	//매장관리>재고관리>입고(원재료)
	public void rawstock(String id, String name, String category, String stock, String cost) {
		
	}
	//매장관리>재고관리>입고(비품)
	public void matestock(String id, String name, String stock, String cost) {
		
	}

}