package _0522.DTO;

public class StockDTO {
	protected String storkId;
	private int inputDate;
	private int sellByDate;
	private int amount;
	
	public String getStorkId() {
		return storkId;
	}
	public void setStorkId(String storkId) {
		this.storkId = storkId;
	}
	public int getInputDate() {
		return inputDate;
	}
	public void setInputDate(int inputDate) {
		this.inputDate = inputDate;
	}
	public int getSellByDate() {
		return sellByDate;
	}
	public void setSellByDate(int sellByDate) {
		this.sellByDate = sellByDate;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
}
