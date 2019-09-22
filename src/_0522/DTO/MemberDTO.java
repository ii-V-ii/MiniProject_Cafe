
package _0522.DTO;

public class MemberDTO{
	private String memberID;
	private String name;
	private int phone;
	private String sex;
	private int birth;
	private int point;
	
	public MemberDTO() {
		
	}
	public MemberDTO(String memberID,String name,int phone,String sex,int birth, int point) {
		this.memberID=memberID;
		this.name=name;
		this.phone=phone;
		this.sex=sex;
		this.birth=birth;
		this.point = point;
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
	public int getBirth() {
		return birth;
	}
	public void setBirth(int birth) {
		this.birth = birth;
	}
	public int getPoint() {
		return point;
	}
	public void setPoint(int point) {
		this.point = point;
	}

}
