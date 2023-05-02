INSERT INTO `user` (name,lastname,username,userRole,password,employeeNumber)
VALUES
	("Max","Musterman","admin","ADMINISTRATOR","Sonne",1),
    ("Martin","Spörl","owner","OWNER","Sonne",2),
    ("Dimitrios","Simeonidis","management","MANAGEMENT","Sonne",3);
  
INSERT INTO `budget` (expirationDate,plannedAmount,budgetDescription,employeeNumber,archivated)
VALUES
	('2022-01-01',10000,"Smartwork Geo",2,0),
    ('2025-01-01',2000,"IVS",2,0),
    ('2021-03-23',200,"TeamBuilding",2,1);

INSERT INTO `history` (description,value,date,budgetId)
VALUES
	("Monitor",220,'2019-05-22',1),
	("Schreibtisch",50,'2019-05-12',1),
	("Schreibtischstuhl",150,'2019-05-22',1),
    ("Marketing",120,'2021-10-29',2),
    ("Wasser",10,'2009-07-22',3),
	("Kekse",20,'2015-12-23',3), 
    ("Geschäftsessen",120,'2020-05-22',3),
    ("Bowling",45,'2021-11-22',3);
    
INSERT INTO `forecast` (description,value,date,budgetId)
VALUES
    ("Sitzsack",125,'2022-02-24',1),
	("Strom",1200,'2022-12-22',1),
    ("Aktenschrank",256,'2022-01-22',1),
    ("Tastatur",25,'2021-12-22',1),
	("Marketing",250,'2023-01-22',2),
	("Marketing",250,'2022-01-22',2);