package org.com.essex.ec910.artificialstockmarket.datasource;


public class DatabaseConnector 
{
	/*
	 * //input order table
	 
create table[Order Drived Market]([limitB]numeric,[volumnB]numeric,[cumvolB]numeric,[ABSB]numeric,
[price]int,[ABSS]numeric,[cumvolS]numeric,[volumeS]numeric,[limitS]numeric)

alter table Order Drived Market
drop limitB, volumnB, ABSS, volumeS, limitS

insert into table.a. Share Database(volume,price), table.b. Trading Output(money)


//input each column needed
select price,ABSB,cumvolB,cumvolS from Order Drived Market  union all


//get new price at minABS, market maker provide fullency if there is none
select price
 
count price from Order Drived Market where min(ABSB) as Np//(number of this same price)
(case when Np>‘1’ then select rand(price) from Share Database)

else
group by price


//get volume at new price
select cumvolB 
from Order Drived Market 
where min(ABSB) and cumvolB<cumvolS
group by cumvolB

else select comvolS
group by cumvolS

select ...
volumnB*price
when price>
money？...
 :(
		 */
}
