package _0522.DTO;

import java.util.ArrayList;

public class MenuDTO {

	String menuId;
	String name;
	int amount;
	int sumprice;

	static ArrayList<MenuDTO> orderedMenu = new ArrayList<>();

	public MenuDTO() {

	}

	public MenuDTO(String menuId, String name, int amount) {
		this.menuId = menuId;
		this.name = name;
		this.amount = amount;
	}

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public static void resetOrder() {
		try {
			orderedMenu.clear();
		} catch (NullPointerException e) {
		}
	}

	public void putOrder(String menuId, String name, int amount) {
		orderedMenu.add(new MenuDTO(menuId, name, amount));
	}

	public void putOrder(MenuDTO menu) {
		orderedMenu.add(menu);
	}

	public static ArrayList<MenuDTO> getOrderedMenu() {
		return orderedMenu;
	}

	public int getSumprice() {
		return sumprice;
	}

	public void setSumprice(int sumprice) {
		this.sumprice = sumprice;
	}

}