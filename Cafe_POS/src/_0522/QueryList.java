

package _0522;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
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
	ResultSetMetaData rsmd= null;
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
////////////////////////////////////////////소미파트 
	public MemberDTO[] showMembers()  {//고객정보 >모두보기 에서 수정,삭제파트 추가하기


		try {
			stmt = con.createStatement();
			rs=stmt.executeQuery( "SELECT COUNT(rownum) FROM member ");
			rs.next();
			int memberCount=rs.getInt(1);
			MemberDTO[]memberList = new MemberDTO[memberCount];
			String getMemberInfo = "SELECT * FROM member";
			rs = stmt.executeQuery(getMemberInfo);
			int i = 0;
			while (rs.next()) {
				rs.getString("memberid");
				rs.getString("name");
				rs.getInt("phone");
				rs.getString("sex");
				rs.getString("birth");
				memberList[i++]=new MemberDTO(rs.getString("memberid"),rs.getString("name"),rs.getInt("phone"),rs.getString("sex"),rs.getInt("birth"));
			}return memberList;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;

	}
	//고객관리수정,소미파트
	public  MemberDTO[] modifyshowMember(MemberDTO modifymember)  {
		sb="UPDATE member SET name = ?, phone = ?, sex = ?, birth = ? WHERE memberid = ?";
		MemberDTO[] modifyshowMember = null;
		try {
			pps = con.prepareStatement(sb);
			System.out.println(modifymember.getName()+modifymember.getPhone()+modifymember.getSex()+modifymember.getBirth()+modifymember.getMemberID());
			pps.setString(1, modifymember.getName());
			pps.setInt(2, modifymember.getPhone());
			pps.setString(3,modifymember.getSex());
			pps.setInt(4, modifymember.getBirth());
			pps.setString(5,modifymember.getMemberID());
			pps.executeUpdate();
			System.out.println("member_info modify finish~");
		} catch (SQLException e) {
			e.printStackTrace();
			
			//System.out.println("format 불일치로인한 error.");
		}return modifyshowMember;
	}
	
	//고객관리삭제, 소미파트
	public MemberDTO[] deleteshowMember(MemberDTO deletemember){
		sb="DELETE member SET name = ?, phone = ?, sex = ?, birth = ? WHERE memberid =?";
		MemberDTO[]deleteshowMember = null;
		try {
			pps = con.prepareStatement(sb);
			System.out.println(deletemember.getName()+deletemember.getPhone()+deletemember.getSex()+deletemember.getBirth()+deletemember.getMemberID());
			pps.setString(1,deletemember.getName());
			pps.setInt(2,deletemember.getPhone());
			pps.setString(3,deletemember.getSex());
			pps.setInt(4,deletemember.getBirth());
			pps.setString(5,deletemember.getMemberID());
			System.out.println("member_info delete finish~");
		} catch (SQLException e) {
			e.printStackTrace();
		}return deleteshowMember;
	}
	
	//고객검색
	public MemberDTO[] searchMember(String memberName) {//일단주석처리 
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
			while(rs.next()) {
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
	public OrderListDTO[] lastBuyingData(MemberDTO member) {
		sb = "select * from (select * from orderlist where memberid = ? order by orderdate desc) where rownum<11";
		OrderListDTO[] orderList = new OrderListDTO[10];
		try {
			pps = con.prepareStatement(sb);
			pps.setString(1, member.getMemberID());
			rs = pps.executeQuery();
			for(int i = 0; rs.next();i++) {
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
sb="select * from (select mn.menuid, mn.name, sum(od.count) \"총 주문 갯수\" from member m, orderlist ol, orderdetail od, menu mn where m.memberid = ol.memberid and ol.orderid = od.orderid and od.menuid=mn.menuid and m.memberid = ? group by  mn.menuid, mn.name order by  sum(od.count) desc) where rownum <6";
		MenuDTO[] menuList = new MenuDTO[5];
		try {
			pps = con.prepareStatement(sb);
			pps.setString(1, member.getMemberID());
			rs = pps.executeQuery();
			for(int i = 0; rs.next(); i++) {
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
			String getStaffInfo = "SELECT * FROM STAFF WHERE STORENO = ? order by staffno";
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
			
			
			pps = con.prepareStatement("Select * from staff where storeno = ? and name = ? order by staffno");
			pps.setString(1, store.getStoreId());
			pps.setString(2, staff.getName());
			rs = pps.executeQuery();
			int i =0;
			while(rs.next()) {
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

	//직원정보수정(소미가주석담)추후공부하고주석삭제할예정
	public void updateStaffInfo(StaffDTO staff) {
		sb = "UPDATE staff SET name = ?, joindate = ?, leavedate = ?, phone = ?, birth = ?, sex = ?, workstyle = ? WHERE staffno = ?";
		try {
			pps = con.prepareStatement(sb);
			System.out.println(staff.getName()+staff.getJoinDate()+staff.getLeaveDate()+staff.getPhone()+staff.getBirth()+staff.getSex()+staff.getWorkstyle()+staff.getId());
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
	
	//직원정보 삭제(소미가주석담)추후공부하고주석삭제할예정
	public void deleteStaffInfo(StaffDTO staff) {
		sb = ("DELETE FROM staff WHERE staffno = ?");
		try {
			pps = con.prepareStatement(sb);
			pps.setString(1, staff.getId());
			pps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
	//새로운직원등록(소미가주석담)추후공부하고주석삭제할예정
	public void staffEnroll(StaffDTO staff) {
		sb="INSERT INTO staff VALUES (staff_seq.nextval, ?, ?,?,?,?,?,?,?)";
		try {
			pps = con.prepareStatement(sb);
			pps.setString(1, staff.getName());
			pps.setString(2, staff.getJoinDate());
			pps.setString(3, staff.getLeaveDate());
			pps.setInt(4, staff.getPhone());
			pps.setInt(5,staff.getBirth());
			pps.setString(6, staff.getSex());
			pps.setString(7, staff.getWorkstyle());
			pps.setString(8, staff.getStoreId());
			pps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
	}
	
	public MenuItemDTO[] menuInfoDefault() {
		MenuItemDTO[] itemList = null;
		try {
			rs = stmt.executeQuery("SELECT COUNT(menuId) FROM menu");
			rs.next();
			int count = rs.getInt(1);
			itemList = new MenuItemDTO[count];
			rs = stmt.executeQuery("SELECT * FROM menu");
			int i =0;
			while(rs.next()) {
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
	
	
	// 혜영===========================================

	// 매장정보>정보수정
	public void storeInfoMotify(String num, String name, String owner, String open, String close, String phone,
			String addr) {
		// update 할 내용 적어주기 동시 에 업데이트 됐어도
	}

	// 매장관리>재고관리>입고(비품)
	public void matestock(String id, String name, String stock, String cost) {

	}

	// 매장관리>재고관리>입고(원재료)
	public void rawstock(String id, String name, String category, int stock, int cost) {
		String rawid = id;
		String rawname = name;
		String cate = category;
		int stoc = stock;
		int cos = cost;
		int check = 0;
		boolean isCommit = false;

		try {
			con.setAutoCommit(false);

			while (true) {// 입력받아온 값 크기만큼
				sbr.append("INSERT INTO rawmaterial VALUES(");
				sbr.append(rawid);
				sbr.append("," + rawname);
				sbr.append("," + cate);
				sbr.append("," + stoc);
				sbr.append("," + cos);
				sbr.append(")");

				stmt.addBatch(sbr.toString());
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

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

	}

}
