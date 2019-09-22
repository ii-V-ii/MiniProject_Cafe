package _0522.DTO;

public class RowMaterialDTO {
	private String id;
	private String name;
	private String category;
	private int stock;
	private int cost;
	
	
	
	public String getId() {
		return id;
	}
	public void setId(String rawmatID) {
		this.id = rawmatID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public int getStock() {
		return stock;
	}
	public void setStock(int stock) {
		this.stock = stock;
	}
	public int getCost() {
		return cost;
	}
	public void setCost(int cost) {
		this.cost = cost;
	}
	
	
}
