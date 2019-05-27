package _0522.DTO;

public class OrderListDTO {
	private String Id;
	private String memberId;
	private int orderdate;
	private int orderprice;
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public int getOrderdate() {
		return orderdate;
	}
	public void setOrderdate(int orderdate) {
		this.orderdate = orderdate;
	}
	public int getOrderprice() {
		return orderprice;
	}
	public void setOrderprice(int orderprice) {
		this.orderprice = orderprice;
	}
}
