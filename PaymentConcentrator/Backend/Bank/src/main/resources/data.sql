insert into account (id, first_name, funds, last_name, merchant_id, merchant_password) values (10001,'Pera',2000,'Peric','84074cf2-3d74-11eb-9d51-0242ac130002','123');
insert into account (id, first_name, funds, last_name, merchant_id, merchant_password) values (10002,'Mika',2000,'Mikic','a5524b38-42e6-11eb-b378-0242ac130002','123');

insert into card (id, card_holder_name, exp_date, "number", security_code, account) values (10001,'Pera Peric','09/2025','9582901023452134','321',10001);
insert into card (id, card_holder_name, exp_date, "number", security_code, account) values (10002,'Mika Mikic','10/2027','9582901052151531','922',10002);
