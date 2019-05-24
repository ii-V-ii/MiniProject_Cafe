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
	IdVO userId = new IdVO();
	MaterialDTO material = new MaterialDTO();
	MemberDTO member = new MemberDTO();
	MenuDTO menu = new MenuDTO();
	MenuItemDTO menuItem = new MenuItemDTO();
	PartTimeStaffDTO part = new PartTimeStaffDTO();
	RegularStaffDTO regular = new RegularStaffDTO();
	RawMaterialDTO raw = new RawMaterialDTO();
	StaffDTO staff = new StaffDTO();
	StockDTO stock = new StockDTO();
	StoreDTO store = new StoreDTO();

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
				System.out.println(check+"/"+storeID);
				System.out.println("check03");
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
	
	public void member() {
		String memberid;
		String name;
		String sex;
		int birth;
		int phone;
		
		sb=("select  memberid, name, phone, sex, birth from member");
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
		
	}
	
	
}