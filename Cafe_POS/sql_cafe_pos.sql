CREATE TABLE customer(
customID VARCHAR2(30),
MemberID VARCHAR2(30),
CONSTRAINT customer_pk_customID PRIMARY KEY(customID),
CONSTRAINT customer_fk_member FOREIGN KEY(MemberID) REFERENCES member(MemberID)
);

CREATE TABLE member(
MemberID VARCHAR2(30),
name VARCHAR2(30) CONSTRAINT member_nu_name NOT NULL,
phone NUMBER,
sex VARCHAR2(30),
birth NUMBER,
CONSTRAINT member_pk_MemberID PRIMARY KEY(MemberID),
CONSTRAINT member_ch_sex CHECK(sex IN('여','남'))
);

CREATE TABLE orderList(
orderID VARCHAR2(30),
customID VARCHAR2(30),
orderDate DATE,
orderPrice NUMBER CONSTRAINT orderList_nu_orderPrice NOT NULL,
CONSTRAINT orderList_pk_orderID PRIMARY KEY(orderID),
CONSTRAINT orderList_fk_customer FOREIGN KEY(customID)REFERENCES customer(customID),
CONSTRAINT orderList_ch_orderPrice CHECK(orderPrice>0)
);

CREATE TABLE orderDetail(
orderID VARCHAR2(30),
menuID VARCHAR2(30),
count  NUMBER CONSTRAINT orderDetail_nu_count NOT NULL,
sumPrice  NUMBER CONSTRAINT orderDetail_nu_sumPrice NOT NULL,
CONSTRAINT orderDetail_pk_orderID PRIMARY KEY(orderID,menuID),
CONSTRAINT orderDetail_fk_orderList FOREIGN KEY(orderID) REFERENCES orderList(orderID),
CONSTRAINT orderDetail_fk_menu FOREIGN KEY(menuID) REFERENCES menu(menuID),
CONSTRAINT orderDetail_ch_count CHECK(count>=1),
CONSTRAINT orderDetail_ch_sumPrice CHECK(sumPrice>=1)
);

CREATE TABLE staff(
staffno VARCHAR2(30),
name VARCHAR2(30) CONSTRAINT staff_nu_name NOT NULL,
joindate DATE CONSTRAINT staff_nu_joindate NOT NULL,
leavedate DATE,
phone NUMBER,
birth NUMBER,
sex VARCHAR2(3),
workstyle VARCHAR2(30),
storeno VARCHAR2(30),
CONSTRAINT staff_pk_staffno PRIMARY KEY (staffno),
CONSTRAINT staff_pk_storeInfo FOREIGN KEY (storeno) REFERENCES storeInfo(storeno),
CONSTRAINT staff_ch_sex CHECK(sex IN('여','남')),
CONSTRAINT staff_ch_workstyle CHECK(workstyle IN('정직원','파트타임'))
);

CREATE TABLE staff_part(
staffno VARCHAR2(30),
workday NUMBER CONSTRAINT staff_part_nu_workday NOT NULL,
hour NUMBER CONSTRAINT staff_part_nu_hour NOT NULL,
pay_per_hour NUMBER,
CONSTRAINT staff_part_pk_staffno PRIMARY KEY(staffno),
CONSTRAINT staff_part_fk_staff FOREIGN KEY(staffno) REFERENCES staff(staffno),
CONSTRAINT staff_part_ch_workday CHECK(workday<=7),
CONSTRAINT staff_part_ch_hour CHECK(hour<=8),
CONSTRAINT staff_part_ch_pay_per_hour CHECK(pay_per_hour>8350)
);

CREATE TABLE staff_all(
staffno VARCHAR2(30),
workday NUMBER CONSTRAINT staff_all_nu_workday NOT NULL,
sal NUMBER CONSTRAINT staff_all_nu_sal NOT NULL,
CONSTRAINT staff_all_pk_staffno PRIMARY KEY(staffno),
CONSTRAINT staff_all_fk_staff FOREIGN KEY (staffno)REFERENCES staff(staffno)
);

CREATE TABLE storeInfo(
storeno VARCHAR2(30),
name VARCHAR2(30) CONSTRAINT storeInfo_nu_name NOT NULL,
owner VARCHAR2(30) CONSTRAINT storeInfo_nu_owner NOT NULL,
opendate DATE CONSTRAINT storeInfo_nu_opendate NOT NULL,
closedate DATE,
phone NUMBER,
address VARCHAR2(30),
CONSTRAINT storeInfo_pk_storeno PRIMARY KEY(storeno)
);

CREATE TABLE rawmaterial(
rawmateID VARCHAR2(30),
name VARCHAR2(30) CONSTRAINT rawmaterial_nu_name NOT NULL,
category VARCHAR2(30),
stock NUMBER,
cost NUMBER CONSTRAINT rawmaterial_nu_cost NOT NULL,
CONSTRAINT rawmaterial_pk_rawmateID PRIMARY KEY (rawmateID),
CONSTRAINT rawmaterial_ch_cost CHECK(cost>0)
);

CREATE TABLE Material(
MateID VARCHAR2(30),
name VARCHAR2(30) CONSTRAINT Material_nu_name NOT NULL,
stock NUMBER,
cost NUMBER CONSTRAINT Material_nu_cost NOT NULL,
CONSTRAINT Material_pk_MateID PRIMARY KEY(MateID),
CONSTRAINT Material_ch_cost CHECK(cost>0)
);

CREATE TABLE menu(
menuID VARCHAR2(30),
name VARCHAR2(30) CONSTRAINT menu_nu_name NOT NULL,
price NUMBER CONSTRAINT menu_nu_price NOT NULL,
recipe VARCHAR2(30) CONSTRAINT menu_nu_recipe NOT NULL,
category VARCHAR2(30),  
CONSTRAINT menu_pk_menuID PRIMARY KEY(menuID),
CONSTRAINT menu_ch_price CHECK(price>0)
);

CREATE TABLE recipe(
menuID VARCHAR2(30),
rawmateID VARCHAR2(30),
CONSTRAINT recipe_fk_menu FOREIGN KEY(menuID) REFERENCES menu(menuID),
CONSTRAINT recipe_fk_rawmaterial FOREIGN KEY(rawmateID) REFERENCES rawmaterial(rawmateID)
);

CREATE TABLE buyingData(
onebiID VARCHAR2(30),
rawmateID VARCHAR2(30),
MateID VARCHAR2(30),
inputdate DATE,
sellbydate DATE,
amount NUMBER CONSTRAINT buyingData_nu_amount NOT NULL,
CONSTRAINT buyingData_pk_onebiID PRIMARY KEY(onebiID),
CONSTRAINT buyingData_fk_rawmaterial FOREIGN KEY (rawmateID) REFERENCES rawmaterial(rawmateID),
CONSTRAINT buyingData_fk_Material FOREIGN KEY (MateID) REFERENCES Material(MateID),
CONSTRAINT buyingData_ch_MateID CHECK((MateID!=NULL AND rawmateId=null)OR((MateID=NULL AND rawmateId!=null))),
CONSTRAINT buyingData_ch_amount CHECK(amount >=0)
);



