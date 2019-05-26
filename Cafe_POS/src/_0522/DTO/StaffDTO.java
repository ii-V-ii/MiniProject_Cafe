package _0522.DTO;

public class StaffDTO {
	private String id;
	private String name;
	private String joinDate;
	private String leaveDate;
	private int phone;
	private int birth;
	private String sex;
	private String workstyle;
	private String storeId;
	
	public StaffDTO(){}
	public StaffDTO(String id, String name, String joinDate, String leaveDate, int phone, int birth, String sex, String workstyle){
		this.id = id;
		this.name = name;
		this.joinDate = joinDate;
		this.leaveDate = leaveDate;
		this.phone = phone;
		this.birth = birth;
		this.sex = sex;
		this.workstyle = workstyle;
	}
	
	public int getBirth() {
		return birth;
	}
	public void setBirth(int birth) {
		this.birth = birth;
	}
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
	public String getJoinDate() {
		return joinDate;
	}
	public void setJoinDate(String joinDate) {
		this.joinDate = joinDate;
	}
	public String getLeaveDate() {
		return leaveDate;
	}
	public void setLeaveDate(String leaveDate) {
		this.leaveDate = leaveDate;
	}
	public int getPhone() {
		return phone;
	}
	public void setPhone(int phone) {
		this.phone = phone;
	}
	
}
