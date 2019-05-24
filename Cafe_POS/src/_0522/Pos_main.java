package _0522;

import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;

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
 * 프로그램이 시작 될 준비를 하고, 프로그램이 돌아갑니다
 */
public class Pos_main extends Thread {
	Socket socket;
	Pos_controller posControl;
	Scripts scripts;

	ConnectionPool cp = null;
	Connection con = null;
	String url = "jdbc:oracle:thin:@127.0.0.1:1521:xe";
	String user = "cafe_pos";
	String password = "cafe_pos";

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
	OrderListDTO orderList = new OrderListDTO();
	
	// (IdVo userId, MaterialDTO material, MemberDTO member, MenuDTO menu,
	// MenuItemDTO menuItem, PartTiemStaffDTO part, RegularStaffDTO
	// regular,RawMeterialDTO raw,StaffDTO staff,StockDTO stock,StoreDTO store)

	private static boolean clientAccess = true;

	public static boolean isClientAccess() {
		return clientAccess;
	}

	public static void setClientAccess(boolean clientAccess) {
		Pos_main.clientAccess = clientAccess;
	}

	public Pos_main(Socket socket) {
		this.socket = socket;
		scripts = new Scripts(socket);
		posControl = new Pos_controller();
		posControl.setScripts(scripts);
		scripts.setPosControl(posControl);
		posControl.setDTO(userId, material, member, menu, menuItem, part, regular, raw, staff, stock, store, orderList);
		scripts.setDTO(userId, material, member, menu, menuItem, part, regular, raw, staff, stock, store, orderList);
		// DB와 연결
		cp = ConnectionPool.getInstance(url, user, password, -1, -1);
		try {
			con = cp.getConnection();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		posControl.setQueryList(con);
		try {
			con = cp.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void run() {
		System.out.println("pos_main start");
		posControl.start();
	}

}
