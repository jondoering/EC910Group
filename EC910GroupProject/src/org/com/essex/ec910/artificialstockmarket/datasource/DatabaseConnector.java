package org.com.essex.ec910.artificialstockmarket.datasource;


public class DatabaseConnector 
{
	/*
	 * //input order table
	 
=======
//input order table
>>>>>>> refs/remotes/origin/master
create table[Order Drived Market]([limitB]numeric,[volumnB]numeric,[cumvolB]numeric,[ABSB]numeric,
[price]int,[ABSS]numeric,[cumvolS]numeric,[volumeS]numeric,[limitS]numeric)

alter table Order Drived Market
drop ABSS

insert into table.a. Share Database(price,volume),table.b.Order Database(price,volume), table.c. Trading Output(moneyB,moneyS)


//input each column needed
select price,ABSB,cumvolB,cumvolS from Order Drived Market  union all


//get new price at minABS, market maker provide liquidty if there is none
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

<<<<<<< HEAD
select ...
volumnB*price
when price>
money？...
 :(
		 
}
=======
//get undealed shares database
select price,volume
if limitB<price or limitS>price
group by price,volume

select price
volumeB=ABSB where cumvolB>cumvolS
else volumeB=0
group by//priceB,volumeB

select price
volumeS=ABSB where cumvolS>cumvolB
else volume=o
group by//priceS,volumeS

//get money from dealed shares
select volumesB
where limitB>price
money=price*volume
group by moneyB

select volumesS
where limitS<price
money=price*volume
group by moneyS/
*/
	
}
