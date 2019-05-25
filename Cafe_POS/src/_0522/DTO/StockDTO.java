package _0522.DTO;

public class StockDTO {
	protected String storeId;
	protected String stockId;	// 입고 일련번호
	private int inputDate;
	private int sellByDate;
	private int amount;		//입고량
	
	
	
	public String getStoreId() {
		return storeId;
	}
	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}
	public String getStockId() {
		return stockId;
	}
	public void setStockId(String stockId) {
		this.stockId = stockId;
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
