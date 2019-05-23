/* 0523 소미의 SQL 문*/
/* 0523 +menu table에 recipe 컬럼이 생성되지 않도록 변경함
        +customer 테이블 삭제
*/
DROP USER cafe_pos CASCADE;

CREATE USER cafe_pos IDENTIFIED BY cafe_pos DEFAULT TABLESPACE users TEMPORARY TABLESPACE temp PROFILE DEFAULT;

GRANT CONNECT, RESOURCE TO cafe_pos;
ALTER USER cafe_pos ACCOUNT UNLOCK;

conn cafe_pos/cafe_pos;

DROP TABLE buyingdata;
DROP TABLE recipe;
DROP TABLE rawmaterial;
DROP TABLE material;
DROP TABLE member;
DROP TABLE menu;
DROP TABLE orderdetail;
DROP TABLE orderList;
DROP TABLE staff_all;
DROP TABLE staff_part;
DROP TABLE staff;
DROP TABLE masterList;
DROP TABLE storeInfo;


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

CREATE TABLE masterList(
storeno VARCHAR2(30),
masterID VARCHAR2(30),
masterPassword VARCHAR2(30) CONSTRAINT masterList_nu_password NOT NULL,
CONSTRAINT masterList_pk_masterID PRIMARY KEY(masterID),
CONSTRAINT masterList_fk_storeno FOREIGN KEY (storeno) REFERENCES storeInfo(storeno)
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

CREATE TABLE menu(
menuID VARCHAR2(30),
name VARCHAR2(30) CONSTRAINT menu_nu_name NOT NULL,
price NUMBER CONSTRAINT menu_nu_price NOT NULL,
--recipe VARCHAR2(30) CONSTRAINT menu_nu_recipe NOT NULL,
category VARCHAR2(30),  
CONSTRAINT menu_pk_menuID PRIMARY KEY(menuID),
CONSTRAINT menu_ch_price CHECK(price>0)
);

CREATE TABLE orderList(
orderID VARCHAR2(30),
memberID VARCHAR2(30),
orderDate DATE,
orderPrice NUMBER CONSTRAINT orderList_nu_orderPrice NOT NULL,
CONSTRAINT orderList_pk_orderID PRIMARY KEY(orderID),
CONSTRAINT orderList_fk_member FOREIGN KEY(memberID) REFERENCES member(memberID),
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

commit;


--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


REM INSERTING into STOREINFO
SET DEFINE OFF;
Insert into STOREINFO (STORENO,NAME,OWNER,OPENDATE,CLOSEDATE,PHONE,ADDRESS) values ('01','신촌점','문상환',to_date('19/02/26','RR/MM/DD'),to_date('99/12/31','RR/MM/DD'),27312229,'서울시서대문구신촌동');
Insert into STOREINFO (STORENO,NAME,OWNER,OPENDATE,CLOSEDATE,PHONE,ADDRESS) values ('02','종로점','남궁성',to_date('19/03/26','RR/MM/DD'),to_date('99/12/31','RR/MM/DD'),27772225,'서울시종로구청계천로');
Insert into STOREINFO (STORENO,NAME,OWNER,OPENDATE,CLOSEDATE,PHONE,ADDRESS) values ('03','강남점','누군지',to_date('18/12/22','RR/MM/DD'),to_date('19/05/01','RR/MM/DD'),23334444,'서울시강남구서초동');

REM INSERTING into MASTERLIST
SET DEFINE OFF;
Insert into MASTERLIST (STORENO, MASTERID, MASTERPASSWORD) values ('01', 'moon', '0000');
Insert into MASTERLIST (STORENO, MASTERID, MASTERPASSWORD) values ('02', 'test', '1111');
Insert into MASTERLIST (STORENO, MASTERID, MASTERPASSWORD) values ('03', 'test2', '2222');

REM INSERTING into MEMBER
SET DEFINE OFF;
Insert into MEMBER (MEMBERID,NAME,PHONE,SEX,BIRTH) values ('c0001','김손님',1011112,'여',850505);
Insert into MEMBER (MEMBERID,NAME,PHONE,SEX,BIRTH) values ('c0002','최손님',1011152223,'남',870605);
Insert into MEMBER (MEMBERID,NAME,PHONE,SEX,BIRTH) values ('c0003','박손님',1011612224,'여',890705);
Insert into MEMBER (MEMBERID,NAME,PHONE,SEX,BIRTH) values ('c0004','이손님',1011712225,'남',911205);




REM INSERTING into MENU
SET DEFINE OFF;
Insert into MENU (MENUID,NAME,PRICE,CATEGORY) values ('me1','아메리카노',4500,'커피음료');
Insert into MENU (MENUID,NAME,PRICE,CATEGORY) values ('me2','카페라떼',4800,'커피음료');
Insert into MENU (MENUID,NAME,PRICE,CATEGORY) values ('me3','카페모카',5000,'커피음료');
Insert into MENU (MENUID,NAME,PRICE,CATEGORY) values ('me4','돌체라떼',5300,'커피음료');
Insert into MENU (MENUID,NAME,PRICE,CATEGORY) values ('me5','핫초코',5500,'음료');
Insert into MENU (MENUID,NAME,PRICE,CATEGORY) values ('me6','딸기바나나',6000,'음료');
Insert into MENU (MENUID,NAME,PRICE,CATEGORY) values ('me7','베이글',3500,'베이커리');
Insert into MENU (MENUID,NAME,PRICE,CATEGORY) values ('me8','스콘',3500,'베이커리');

REM INSERTING into ORDERLIST
SET DEFINE OFF;
Insert into ORDERLIST (ORDERID,ORDERDATE,ORDERPRICE,MEMBERID) values ('or01',to_date('19/02/02','RR/MM/DD'),25000,'c0001');
Insert into ORDERLIST (ORDERID,ORDERDATE,ORDERPRICE,MEMBERID) values ('or02',to_date('19/02/03','RR/MM/DD'),45000,'c0001');

REM INSERTING into ORDERDETAIL
SET DEFINE OFF;
Insert into ORDERDETAIL (COUNT,SUMPRICE,ORDERID,MENUID) values (1,4500,'or01','me1');

REM INSERTING into STAFF
SET DEFINE OFF;
Insert into STAFF (STAFFNO,NAME,JOINDATE,LEAVEDATE,PHONE,BIRTH,SEX,WORKSTYLE,STORENO) values ('1000','류혜영',to_date('19/02/26','RR/MM/DD'),to_date('99/12/31','RR/MM/DD'),1012538253,900729,'여','정직원','01');
Insert into STAFF (STAFFNO,NAME,JOINDATE,LEAVEDATE,PHONE,BIRTH,SEX,WORKSTYLE,STORENO) values ('1001','한국화',to_date('19/03/26','RR/MM/DD'),to_date('99/12/31','RR/MM/DD'),1011112222,911205,'여','정직원','01');
Insert into STAFF (STAFFNO,NAME,JOINDATE,LEAVEDATE,PHONE,BIRTH,SEX,WORKSTYLE,STORENO) values ('1002','한소미',to_date('19/04/26','RR/MM/DD'),to_date('99/12/31','RR/MM/DD'),1022225555,920102,'여','정직원','01');
Insert into STAFF (STAFFNO,NAME,JOINDATE,LEAVEDATE,PHONE,BIRTH,SEX,WORKSTYLE,STORENO) values ('1003','김종현',to_date('18/01/01','RR/MM/DD'),to_date('19/03/25','RR/MM/DD'),1095558555,931212,'남','파트타임','02');
Insert into STAFF (STAFFNO,NAME,JOINDATE,LEAVEDATE,PHONE,BIRTH,SEX,WORKSTYLE,STORENO) values ('1004','황민현',to_date('17/12/25','RR/MM/DD'),to_date('18/02/08','RR/MM/DD'),1011115555,930728,'남','파트타임','02');
Insert into STAFF (STAFFNO,NAME,JOINDATE,LEAVEDATE,PHONE,BIRTH,SEX,WORKSTYLE,STORENO) values ('1005','박찬열',to_date('18/05/31','RR/MM/DD'),to_date('18/12/31','RR/MM/DD'),1055558888,921125,'남','정직원','03');
Insert into STAFF (STAFFNO,NAME,JOINDATE,LEAVEDATE,PHONE,BIRTH,SEX,WORKSTYLE,STORENO) values ('1006','변백현',to_date('19/01/01','RR/MM/DD'),to_date('99/12/31','RR/MM/DD'),1044488850,920506,'남','파트타임','03');

REM INSERTING into STAFF_ALL
SET DEFINE OFF;
Insert into STAFF_ALL (WORKDAY,SAL,STAFFNO) values (5,150,'1000');
Insert into STAFF_ALL (WORKDAY,SAL,STAFFNO) values (4,130,'1001');
Insert into STAFF_ALL (WORKDAY,SAL,STAFFNO) values (5,150,'1002');
Insert into STAFF_ALL (WORKDAY,SAL,STAFFNO) values (3,110,'1005');

REM INSERTING into STAFF_PART
SET DEFINE OFF;
Insert into STAFF_PART (WORKDAY,HOUR,PAY_PER_HOUR,STAFFNO) values (4,5,9000,'1003');
Insert into STAFF_PART (WORKDAY,HOUR,PAY_PER_HOUR,STAFFNO) values (5,5,8500,'1004');
Insert into STAFF_PART (WORKDAY,HOUR,PAY_PER_HOUR,STAFFNO) values (3,5,9500,'1006');

REM INSERTING into MATERIAL
SET DEFINE OFF;
Insert into MATERIAL (MATEID,NAME,STOCK,COST) values ('mate01','컵',10,35000);
Insert into MATERIAL (MATEID,NAME,STOCK,COST) values ('mate02','빨대',5,20000);
Insert into MATERIAL (MATEID,NAME,STOCK,COST) values ('mate03','휴지',3,15000);
Insert into MATERIAL (MATEID,NAME,STOCK,COST) values ('mate04','머그컵',4,11000);
Insert into MATERIAL (MATEID,NAME,STOCK,COST) values ('mate05','쟁반',7,15000);
Insert into MATERIAL (MATEID,NAME,STOCK,COST) values ('mate06','포크',6,15000);
Insert into MATERIAL (MATEID,NAME,STOCK,COST) values ('mate07','접시',10,2000);
Insert into MATERIAL (MATEID,NAME,STOCK,COST) values ('mate08','영수증',8,2500);

REM INSERTING into RAWMATERIAL
SET DEFINE OFF;
Insert into RAWMATERIAL (RAWMATEID,NAME,CATEGORY,STOCK,COST) values ('raw01','원두',null,5,250);
Insert into RAWMATERIAL (RAWMATEID,NAME,CATEGORY,STOCK,COST) values ('raw02','우유',null,7,350);
Insert into RAWMATERIAL (RAWMATEID,NAME,CATEGORY,STOCK,COST) values ('raw03','초코파우더',null,8,4501);
Insert into RAWMATERIAL (RAWMATEID,NAME,CATEGORY,STOCK,COST) values ('raw04','연유',null,9,5000);
Insert into RAWMATERIAL (RAWMATEID,NAME,CATEGORY,STOCK,COST) values ('raw05','베이글',null,10,5100);
Insert into RAWMATERIAL (RAWMATEID,NAME,CATEGORY,STOCK,COST) values ('raw06','스콘',null,5,4200);
Insert into RAWMATERIAL (RAWMATEID,NAME,CATEGORY,STOCK,COST) values ('raw07','시럽',null,5,8112);
Insert into RAWMATERIAL (RAWMATEID,NAME,CATEGORY,STOCK,COST) values ('raw08','딸기',null,7,216854);

REM INSERTING into RECIPE
SET DEFINE OFF;
Insert into RECIPE (RAWMATEID,MENUID) values ('raw01','me1');
Insert into RECIPE (RAWMATEID,MENUID) values ('raw01','me2');
Insert into RECIPE (RAWMATEID,MENUID) values ('raw01','me4');
Insert into RECIPE (RAWMATEID,MENUID) values ('raw02','me2');
Insert into RECIPE (RAWMATEID,MENUID) values ('raw02','me3');
Insert into RECIPE (RAWMATEID,MENUID) values ('raw02','me4');
Insert into RECIPE (RAWMATEID,MENUID) values ('raw02','me5');
Insert into RECIPE (RAWMATEID,MENUID) values ('raw02','me6');
Insert into RECIPE (RAWMATEID,MENUID) values ('raw03','me3');
Insert into RECIPE (RAWMATEID,MENUID) values ('raw03','me5');
Insert into RECIPE (RAWMATEID,MENUID) values ('raw04','me4');
Insert into RECIPE (RAWMATEID,MENUID) values ('raw05','me7');
Insert into RECIPE (RAWMATEID,MENUID) values ('raw06','me8');
Insert into RECIPE (RAWMATEID,MENUID) values ('raw07','me6');
Insert into RECIPE (RAWMATEID,MENUID) values ('raw08','me6');

REM INSERTING into BUYINGDATA
SET DEFINE OFF;
Insert into BUYINGDATA (ONEBIID,MATEID,INPUTDATE,SELLBYDATE,AMOUNT,RAWMATEID) values ('ob01','mate01',to_date('19/01/01','RR/MM/DD'),to_date('19/12/12','RR/MM/DD'),5,'raw01');
Insert into BUYINGDATA (ONEBIID,MATEID,INPUTDATE,SELLBYDATE,AMOUNT,RAWMATEID) values ('ob02','mate02',to_date('19/01/01','RR/MM/DD'),to_date('19/12/31','RR/MM/DD'),7,'raw02');
Insert into BUYINGDATA (ONEBIID,MATEID,INPUTDATE,SELLBYDATE,AMOUNT,RAWMATEID) values ('ob03','mate03',to_date('19/01/05','RR/MM/DD'),to_date('19/12/31','RR/MM/DD'),8,'raw03');
Insert into BUYINGDATA (ONEBIID,MATEID,INPUTDATE,SELLBYDATE,AMOUNT,RAWMATEID) values ('ob04','mate04',to_date('19/01/05','RR/MM/DD'),to_date('19/12/31','RR/MM/DD'),99,'raw04');
Insert into BUYINGDATA (ONEBIID,MATEID,INPUTDATE,SELLBYDATE,AMOUNT,RAWMATEID) values ('ob05','mate05',to_date('19/01/06','RR/MM/DD'),to_date('19/12/31','RR/MM/DD'),110,'raw05');
Insert into BUYINGDATA (ONEBIID,MATEID,INPUTDATE,SELLBYDATE,AMOUNT,RAWMATEID) values ('ob06','mate06',to_date('19/01/08','RR/MM/DD'),to_date('19/12/31','RR/MM/DD'),5,'raw06');
Insert into BUYINGDATA (ONEBIID,MATEID,INPUTDATE,SELLBYDATE,AMOUNT,RAWMATEID) values ('ob07','mate07',to_date('19/01/08','RR/MM/DD'),to_date('19/12/31','RR/MM/DD'),5,'raw07');
Insert into BUYINGDATA (ONEBIID,MATEID,INPUTDATE,SELLBYDATE,AMOUNT,RAWMATEID) values ('ob08','mate08',to_date('19/10/11','RR/MM/DD'),to_date('19/12/31','RR/MM/DD'),7,'raw08');
Insert into BUYINGDATA (ONEBIID,MATEID,INPUTDATE,SELLBYDATE,AMOUNT,RAWMATEID) values ('ob09','mate01',to_date('19/11/11','RR/MM/DD'),to_date('19/12/31','RR/MM/DD'),10,'raw01');
Insert into BUYINGDATA (ONEBIID,MATEID,INPUTDATE,SELLBYDATE,AMOUNT,RAWMATEID) values ('ob10','mate02',to_date('19/12/12','RR/MM/DD'),to_date('19/12/31','RR/MM/DD'),5,'raw02');
Insert into BUYINGDATA (ONEBIID,MATEID,INPUTDATE,SELLBYDATE,AMOUNT,RAWMATEID) values ('ob11','mate03',to_date('19/12/12','RR/MM/DD'),to_date('19/12/31','RR/MM/DD'),3,'raw03');
Insert into BUYINGDATA (ONEBIID,MATEID,INPUTDATE,SELLBYDATE,AMOUNT,RAWMATEID) values ('ob12','mate04',to_date('19/12/12','RR/MM/DD'),to_date('19/12/31','RR/MM/DD'),7,'raw04');
Insert into BUYINGDATA (ONEBIID,MATEID,INPUTDATE,SELLBYDATE,AMOUNT,RAWMATEID) values ('ob13','mate05',to_date('19/12/12','RR/MM/DD'),to_date('19/12/31','RR/MM/DD'),4,'raw05');
Insert into BUYINGDATA (ONEBIID,MATEID,INPUTDATE,SELLBYDATE,AMOUNT,RAWMATEID) values ('ob14','mate06',to_date('19/12/12','RR/MM/DD'),to_date('19/11/23','RR/MM/DD'),6,'raw06');
Insert into BUYINGDATA (ONEBIID,MATEID,INPUTDATE,SELLBYDATE,AMOUNT,RAWMATEID) values ('ob15','mate07',to_date('19/12/12','RR/MM/DD'),to_date('19/11/23','RR/MM/DD'),10,'raw07');
Insert into BUYINGDATA (ONEBIID,MATEID,INPUTDATE,SELLBYDATE,AMOUNT,RAWMATEID) values ('ob16','mate08',to_date('19/12/12','RR/MM/DD'),to_date('19/11/23','RR/MM/DD'),8,'raw08');


COMMIT;

