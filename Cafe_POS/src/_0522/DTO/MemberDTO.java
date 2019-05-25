package _0522.DTO;

public class MemberDTO{
	private String memberID;
	private String name;
	private int phone;
	private String sex;
	private String birth;
	
	public MemberDTO() {
		
	}
	public MemberDTO(String memberID,String name,int phone,String sex,String birth) {
		this.memberID=memberID;
		this.name=name;
		this.phone=phone;
		this.sex=sex;
		this.birth=birth;
	}
	
	public String getMemberID() {
		return memberID;
	}
	public void setMemberID(String memberID) {
		this.memberID = memberID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPhone() {
		return phone;
	}
	public void setPhone(int phone) {
		this.phone = phone;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getBirth() {
		return birth;
	}
	public void setBirth(String birth) {
		this.birth = birth;
	}
}
