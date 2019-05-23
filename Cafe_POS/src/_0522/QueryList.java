package _0522;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QueryList {
// 쿼리문 DB 전달을 위한 변수
	Connection con = null;
	Statement stmt = null;
	PreparedStatement pps = null;
	ResultSet rs = null;
	String sb = null;

	QueryList(Connection con) {
		this.con = con;
	}

	public boolean getLogInInfo(String id, String password) {
		int check = 0;
		sb = ("SELECT rownum FROM masterList WHERE masterid = ? and masterPassword = ?");
		try {
			pps = con.prepareStatement(sb);
			pps.setString(1, id);
			pps.setString(2, password);
			rs = pps.executeQuery();
			while (rs.next()) {
				check = rs.getInt("rownum");
			}
			if (check == 1)
				return true;
			else
				return false;

		} catch (SQLException e) {
			return false;
		}

	}
}