package _0522.DTO;

public class StaffDTO {
	private String id;
	private String name;
	private int joinDate;
	private int leaveDate;
	private int phone;
	private String sex;
	private String workstyle;
	private String storeId;
	
	
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getWorkstyle() {
		return workstyle;
	}
	public void setWorkstyle(String workstyle) {
		this.workstyle = workstyle;
	}
	public String getStoreId() {
		return storeId;
	}
	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getJoinDate() {
		return joinDate;
	}
	public void setJoinDate(int joinDate) {
		this.joinDate = joinDate;
	}
	public int getLeaveDate() {
		return leaveDate;
	}
	public void setLeaveDate(int leaveDate) {
		this.leaveDate = leaveDate;
	}
	public int getPhone() {
		return phone;
	}
	public void setPhone(int phone) {
		this.phone = phone;
	}
	
}
