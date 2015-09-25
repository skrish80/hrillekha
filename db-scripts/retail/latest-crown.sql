/*
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////DROP DEPENDENT TABLES//////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
*/
DROP VIEW if exists total_stock;
DROP VIEW if exists total_entity_stock;
DROP TABLE if exists stolen_stock;
DROP TABLE if exists stock_allocation;
DROP TABLE if exists invoice_return;
DROP TABLE if exists invoice_items;
DROP TABLE if exists purchase_invoice_items;
DROP TABLE if exists invoice_payment;
DROP TABLE if exists purchase_invoice_payment;
DROP TABLE if exists agent_commission;
DROP TABLE if exists warehouse_stock;
DROP TABLE if exists credit_note;
DROP TABLE if exists invoice;
DROP TABLE if exists purchase_invoice;
DROP TABLE if exists agent;
DROP TABLE if exists receipt_payment;
DROP TABLE if exists receipt;
DROP TABLE if exists customer;
DROP TABLE if exists ws_customer;
DROP TABLE if exists supplier;
DROP TABLE if exists company;
DROP TABLE if exists rights;
DROP TABLE if exists crown_audit;
DROP TABLE if exists allocation_state;
DROP TABLE if exists stock_movement_items;
DROP TABLE if exists stock_movement;
DROP TABLE if exists item;
DROP TABLE if exists warehouse;
DROP TABLE if exists expense;

/*
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////DROP INDEPENDENT TABLES////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
*/
DROP TABLE if exists crown_user;
DROP TABLE if exists role;
DROP TABLE if exists unit_of_measure;
DROP TABLE if exists item_category;
DROP TABLE if exists item_brand;
DROP TABLE if exists crown_entity;
DROP TABLE if exists designation;
DROP TABLE if exists department;
DROP TABLE if exists location;
DROP TABLE if exists currency;
DROP TABLE if exists bank;
DROP TABLE if exists expense_allocation;

/*
//////////////////////////////////////////// ENUM TABLES /////////////////////////////////////////////////////////////
*/
DROP TABLE if exists discount_type;
DROP TABLE if exists customer_type;
DROP TABLE if exists allocation_type;
DROP TABLE if exists agent_type;
DROP TABLE if exists payment_mode;
DROP TABLE if exists payment_status;
DROP TABLE if exists move_status;
DROP TABLE if exists company_type;
DROP TABLE if exists status;
DROP TABLE if exists audit_action;
DROP TABLE if exists invoice_state;


/*
//////////////////////////////////////////// INDEPENDENT TABLES //////////////////////////////////////////////////////
*/
/*
***********************************************************************************************************************
****************************************** AUDIT ACTION ***************************************************************
***********************************************************************************************************************
*/
CREATE TABLE audit_action (
	action_id		serial NOT NULL,
	version			bigint NOT NULL default 0,
	action_code		character (16) UNIQUE NOT NULL,
	description		character varying(64),
	PRIMARY KEY		(action_id)
);

/*
***********************************************************************************************************************
****************************************** STATUS *********************************************************************
***********************************************************************************************************************
*/
CREATE TABLE status (
	status_id		serial NOT NULL,
	version			bigint NOT NULL default 0,
	status_code		character (16) UNIQUE NOT NULL,
	description		character varying(64),
	PRIMARY KEY		(status_id)
);


/*
***********************************************************************************************************************
****************************************** ALLOCATION STATE ***********************************************************
***********************************************************************************************************************
*/
CREATE TABLE allocation_state (
	state_id		serial NOT NULL,
	version			bigint NOT NULL default 0,
	state			character (16) UNIQUE NOT NULL,
	description		character varying(64),
	PRIMARY KEY		(state_id)
);


/*
***********************************************************************************************************************
****************************************** INVOICE STATE **************************************************************
***********************************************************************************************************************
*/
CREATE TABLE invoice_state (
	state_id		serial NOT NULL,
	version			bigint NOT NULL default 0,
	state			character (16) UNIQUE NOT NULL,
	description		character varying(64),
	PRIMARY KEY		(state_id)
);


/*
***********************************************************************************************************************
********************************************* BANK ********************************************************************
***********************************************************************************************************************
*/
CREATE TABLE bank (
	bank_id			serial NOT NULL,
	version			bigint NOT NULL default 0,
	bank_code		character varying(10) UNIQUE NOT NULL,
	bank_name		character varying(50),
	description		character varying(250),
	PRIMARY KEY		(bank_id)
);


/*
***********************************************************************************************************************
****************************************** CURRENCY *******************************************************************
***********************************************************************************************************************
*/
CREATE TABLE currency (
	currency_code		character(3) NOT NULL,
	version			bigint NOT NULL default 0,
	description		character varying(50),
	PRIMARY KEY		(currency_code)
);


/*
***********************************************************************************************************************
****************************************** LOCATION *******************************************************************
***********************************************************************************************************************
*/
CREATE TABLE location (
	location_id		serial NOT NULL,
	version			bigint NOT NULL default 0,
	location		character varying(30) UNIQUE NOT NULL,
	description		character varying(100),
	PRIMARY KEY		(location_id)
);


/*
***********************************************************************************************************************
****************************************** DEPARTMENT *****************************************************************
***********************************************************************************************************************
*/
CREATE TABLE department (
	department_id		serial NOT NULL,
	version			bigint NOT NULL default 0,
	department_name		character varying(10) UNIQUE NOT NULL,
	description		character varying(25),
	PRIMARY KEY		(department_id)
);


/*
***********************************************************************************************************************
****************************************** DESIGNATION ****************************************************************
***********************************************************************************************************************
*/
CREATE TABLE designation (
	designation_id		serial NOT NULL,
	version			bigint NOT NULL default 0,
	designation		character varying(10) UNIQUE NOT NULL,
	description		character varying(25),
	PRIMARY KEY		(designation_id)
);


/*
***********************************************************************************************************************
********************************************* crown ENTITY ***********************************************************
***********************************************************************************************************************
*/
CREATE TABLE crown_entity (
	entity_id		serial NOT NULL,
	version			bigint NOT NULL default 0,
	entity_name		character varying(25) UNIQUE NOT NULL,
	description		character varying(50),
	entity_type		character(2) NOT NULL, /*WS or RT*/
	PRIMARY KEY		(entity_id)
);


/*
***********************************************************************************************************************
********************************************* ITEM BRAND **************************************************************
***********************************************************************************************************************
*/
CREATE TABLE item_brand (
	brand_id		serial NOT NULL,
	version			bigint NOT NULL default 0,
	brand_name		character varying(25) UNIQUE NOT NULL,
     brand_code		character varying(12) UNIQUE NOT NULL,
	description		character varying(50),
	status			int NOT NULL,
	PRIMARY KEY		(brand_id)
);

ALTER TABLE item_brand
    ADD CONSTRAINT item_brand_status_fk FOREIGN KEY (status) REFERENCES status(status_id);


/*
***********************************************************************************************************************
********************************************* ITEM CATEGORY ***********************************************************
***********************************************************************************************************************
*/
CREATE TABLE item_category (
	category_id		serial NOT NULL,
	version			bigint NOT NULL default 0,
	category_name		character varying(25) UNIQUE NOT NULL,
    category_code		character varying(12) UNIQUE NOT NULL,
	description		character varying(50),
	status			int NOT NULL,
	PRIMARY KEY		(category_id)
);

ALTER TABLE item_category
    ADD CONSTRAINT item_status_fk FOREIGN KEY (status) REFERENCES status(status_id);


/*
***********************************************************************************************************************
********************************************* UNIT OF MEASURE *********************************************************
***********************************************************************************************************************
*/
CREATE TABLE unit_of_measure (
	uom_id			serial NOT NULL,
	version			bigint NOT NULL default 0,
	uom_name		character varying(25) UNIQUE NOT NULL,
	description		character varying(50),
	PRIMARY KEY		(uom_id)
);


/*
***********************************************************************************************************************
****************************************** COMPANY TYPE ***************************************************************
***********************************************************************************************************************
*/
CREATE TABLE company_type (
	company_type_id		serial NOT NULL,
	version			bigint NOT NULL default 0,
	company_type		character varying(25) UNIQUE NOT NULL,
	description		character varying(100),
	PRIMARY KEY		(company_type_id)
);


/*
***********************************************************************************************************************
****************************************** MOVE TYPE ******************************************************************
***********************************************************************************************************************
*/
CREATE TABLE move_status (
	move_status_id		serial NOT NULL,
	version			bigint NOT NULL default 0,
	move_status		character varying(20) UNIQUE NOT NULL,
	description		character varying(50),
	PRIMARY KEY		(move_status_id)
);


/*
***********************************************************************************************************************
****************************************** PAYMENT TYPE ***************************************************************
*************************** Cash/Cheque/Credit Note/Receipt/etc  ******************************************************
***********************************************************************************************************************
*/
CREATE TABLE payment_mode (
	payment_mode_id		serial NOT NULL,
	version			bigint NOT NULL default 0,
	payment_mode		character varying(25) UNIQUE NOT NULL,
	description		character varying(25),
	PRIMARY KEY		(payment_mode_id)
);


/*
***********************************************************************************************************************
****************************************** PAYMENT STATUS ***************************************************************
****************************************** FULL/PARTIAL ***************************************************************
***********************************************************************************************************************
*/
CREATE TABLE payment_status (
	payment_status_id	serial NOT NULL,
	version			bigint NOT NULL default 0,
	payment_status		character varying(25) UNIQUE NOT NULL,
	description		character varying(25),
	PRIMARY KEY		(payment_status_id)
);


/*
***********************************************************************************************************************
****************************************** AGENT TYPE *****************************************************************
***********************************************************************************************************************
*/
CREATE TABLE agent_type (
	agent_type_id		serial NOT NULL,
	version			bigint NOT NULL default 0,
	agent_type		character varying(25) UNIQUE NOT NULL,
	description		character varying(100),
	PRIMARY KEY		(agent_type_id)
);


/*
***********************************************************************************************************************
****************************************** ALLOCATION TYPE ************************************************************
***********************************************************************************************************************
*/
CREATE TABLE allocation_type (
	allocation_type_id	serial NOT NULL,
	version			bigint NOT NULL default 0,
	allocation_type		character varying(25) UNIQUE NOT NULL,
	description		character varying(50),
	PRIMARY KEY		(allocation_type_id)
);


/*
***********************************************************************************************************************
***************************************** CUSTOMER TYPE ***************************************************************
***********************************************************************************************************************
*/
CREATE TABLE customer_type (
	customer_type_id	serial NOT NULL,
	version			bigint NOT NULL default 0,
	customer_type		character varying(25) UNIQUE NOT NULL,
	description		character varying(100),
	PRIMARY KEY		(customer_type_id)
);


/*
***********************************************************************************************************************
****************************************** DISCOUNT TYPE **************************************************************
***********************************************************************************************************************
*/
CREATE TABLE discount_type (
	discount_type_id	serial NOT NULL,
	version			bigint NOT NULL default 0,
	discount		character varying(10) UNIQUE NOT NULL,
	description		character varying(25),
	PRIMARY KEY		(discount_type_id)
);


/*
***********************************************************************************************************************
****************************************** ROLE ***********************************************************************
***********************************************************************************************************************
*/
CREATE TABLE role ( 
	role_id         	serial NOT NULL,
	version			bigint NOT NULL default 0,
	role_name       	character varying(32) UNIQUE NOT NULL,
	role_description	character varying(32) NOT NULL ,
	PRIMARY KEY		(role_id)
);


/*
/////////////////////////////////////////////////// DEPENDENT TABLES /////////////////////////////////////////////////
*/

/*
***********************************************************************************************************************
****************************************** RIGHTS *********************************************************************
***********************************************************************************************************************
*/
CREATE TABLE rights ( 
    right_id  			serial NOT NULL,
	version			bigint NOT NULL default 0,
    right_name			character varying(50) UNIQUE NOT NULL,
    description 		text,
    role_id     		int NOT NULL,
    PRIMARY KEY			(right_id)
);
ALTER TABLE rights ADD CONSTRAINT fk_rights_role FOREIGN KEY(role_id) REFERENCES role(role_id);
ALTER TABLE rights ADD CONSTRAINT UNIQUE_RIGHT UNIQUE (right_id, role_id);


/*
***********************************************************************************************************************
********************************************** crown USER ************************************************************
***********************************************************************************************************************
*/
CREATE TABLE crown_user (
	user_id			serial NOT NULL,
	version			bigint NOT NULL default 0,
	employee_no		character varying(10) UNIQUE NOT NULL,
	username         	character varying(32) NOT NULL UNIQUE,
	password     		character varying(32) NOT NULL,
	first_name       	character varying(32) NOT NULL,
	last_name        	character varying(32) NOT NULL,
	email            	character varying(64) UNIQUE NOT NULL,
	role			int,
	address			text,
	mobile			character varying(25),
	date_of_birth		date NOT NULL,
	joining_date		date,
	status			int,
	PRIMARY KEY		(user_id)
);

ALTER TABLE crown_user
    ADD CONSTRAINT user_role_fk FOREIGN KEY (role) REFERENCES role(role_id);
ALTER TABLE crown_user
    ADD CONSTRAINT user_status_fk FOREIGN KEY (status) REFERENCES status(status_id);


/*
***********************************************************************************************************************
********************************************* WAREHOUSE ***************************************************************
***********************************************************************************************************************
*/
CREATE TABLE warehouse (
	warehouse_id		serial NOT NULL,
	version			bigint NOT NULL default 0,
	warehouse_name		character varying(25) UNIQUE NOT NULL,
	description		character varying(50),
	address			text NOT NULL,
	location		int NOT NULL,
	incharge		int NOT NULL,
	entity			int NOT NULL,
	is_retail_shop		boolean default FALSE,
	status			int,
	PRIMARY KEY		(warehouse_id)
);
ALTER TABLE warehouse
    ADD CONSTRAINT warehouse_location_fk FOREIGN KEY (location) REFERENCES location(location_id);
ALTER TABLE warehouse
    ADD CONSTRAINT warehouse_entity_fk FOREIGN KEY (entity) REFERENCES crown_entity(entity_id);
ALTER TABLE warehouse
    ADD CONSTRAINT warehouse_incharge_fk FOREIGN KEY (incharge) REFERENCES crown_user(user_id);
ALTER TABLE warehouse
    ADD CONSTRAINT warehouse_status_fk FOREIGN KEY (status) REFERENCES status(status_id);


/*
***********************************************************************************************************************
********************************************** COMPANY ****************************************************************
***********************************************************************************************************************
*/
CREATE TABLE company (
	company_id		serial NOT NULL,
	version			bigint NOT NULL default 0,
	company_name		character varying(50) UNIQUE NOT NULL,
	company_type		int,
	description		character varying(100) NOT NULL,
	location		int NOT NULL,
	address			text,
	phone			character varying(25),
	status			int,
	tin			character varying(25),
	PRIMARY KEY		(company_id)
);

ALTER TABLE company
    ADD CONSTRAINT company_type_fk FOREIGN KEY (company_type) REFERENCES company_type(company_type_id);
ALTER TABLE company
    ADD CONSTRAINT status_fk FOREIGN KEY (status) REFERENCES status(status_id);
ALTER TABLE company
    ADD CONSTRAINT location_fk FOREIGN KEY (location) REFERENCES location(location_id);

/*
***********************************************************************************************************************
******************************************** RETAIL CUSTOMER **********************************************************
***********************************************************************************************************************
*/
CREATE TABLE customer (
	customer_id		serial NOT NULL,
	version			bigint NOT NULL default 0,
	customer_code		character varying(25) UNIQUE NOT NULL,
	customer_name		character varying(50),
	customer_type		int, /*Privilege or Non-Privilege*/
	address			text,
	phone			character varying(50),
	date_of_birth	date,
	anniversary		date,
	remarks			text,
	status			int,
	PRIMARY KEY		(customer_id)
);

ALTER TABLE customer
    ADD CONSTRAINT customer_type_fk FOREIGN KEY (customer_type) REFERENCES customer_type(customer_type_id);
ALTER TABLE only customer
    ADD CONSTRAINT status_fk FOREIGN KEY (status) REFERENCES status(status_id);

/*
***********************************************************************************************************************
********************************************** WHOLESALE CUSTOMER *****************************************************
***********************************************************************************************************************
*/
CREATE TABLE ws_customer (
	customer_id		serial NOT NULL,
	version			bigint NOT NULL default 0,
	customer_code		character varying(25) UNIQUE NOT NULL,
	customer_name		character varying(50),
	customer_type		int,
	address			text,
	phone			character varying(50),
	POC			character varying(50),
	TIN			character varying(25) UNIQUE NOT NULL,
	remarks			text,
	status			int,
	credit_limit		double precision,
	current_credit		double precision,
	PRIMARY KEY		(customer_id)
);

ALTER TABLE ws_customer
    ADD CONSTRAINT ws_customer_type_fk FOREIGN KEY (customer_type) REFERENCES customer_type(customer_type_id);
ALTER TABLE only ws_customer
    ADD CONSTRAINT ws_status_fk FOREIGN KEY (status) REFERENCES status(status_id);

/*
***********************************************************************************************************************
********************************************** AGENT ******************************************************************
***********************************************************************************************************************
*/
CREATE TABLE agent (
	agent_id		serial NOT NULL,
	version			bigint NOT NULL default 0,
	agent_name		character varying(50),
	agent_type		int,
	location_id		int,
	address			text,
	phone			character varying(25),
	status_id		int,
	PRIMARY KEY		(agent_id)
);

ALTER TABLE agent
    ADD CONSTRAINT agent_location_fk FOREIGN KEY (location_id) REFERENCES location(location_id);
ALTER TABLE agent
    ADD CONSTRAINT agent_status_fk FOREIGN KEY (status_id) REFERENCES status(status_id);
ALTER TABLE agent
    ADD CONSTRAINT agent_type_fk FOREIGN KEY (agent_type) REFERENCES agent_type(agent_type_id);

/*
***********************************************************************************************************************
********************************************** INVOICE ****************************************************************
***********************************************************************************************************************
*/
CREATE TABLE invoice (
	invoice_id		serial NOT NULL,
	version			bigint NOT NULL default 0,
	invoice_type		character(2) NOT NULL, /*RT for retail, WS for wholesale*/
	invoice_number		character varying(25) NOT NULL UNIQUE, /*INV <<YYYYMMDD>><<RUNNING SEQ>>*/
	agent_id		int, /*MAY BE NULL*/
	customer_id		int NOT NULL,
	entity_id		int NOT NULL, /* TO CHECK FOR ALLOCATION*/
	delivery_warehouse	int, /* TO DELIVER*/
	invoice_date		date,
	currency		character(3), /* LET THIS BE THERE*/
	invoice_state		int,
	discount_type		int,
	discount_amount		double precision,
	invoice_amount		double precision,
	return_date		date,
	delivered_date		date,
	return_amount		double precision,
	payment_status		int NOT NULL,
	status			int,
	remarks			text,
	created_by		int,
	company			int,
	terms_conditions	text,
	other_price		character varying(30),
	other_price_amount	double precision,
	PRIMARY KEY		(invoice_id)
);


ALTER TABLE invoice
    ADD CONSTRAINT invoice_currency_fk FOREIGN KEY (currency) REFERENCES currency(currency_code);
ALTER TABLE invoice
    ADD CONSTRAINT invoice_customer_fk FOREIGN KEY (customer_id) REFERENCES customer(customer_id);
ALTER TABLE invoice
    ADD CONSTRAINT invoice_agent_fk FOREIGN KEY (agent_id) REFERENCES agent(agent_id);
ALTER TABLE invoice
    ADD CONSTRAINT invoice_status_fk FOREIGN KEY (status) REFERENCES status(status_id);
ALTER TABLE invoice
    ADD CONSTRAINT invoice_state_fk FOREIGN KEY (invoice_state) REFERENCES invoice_state(state_id);
ALTER TABLE invoice
    ADD CONSTRAINT invoice_pmt_status_fk FOREIGN KEY (payment_status) REFERENCES payment_status(payment_status_id);
ALTER TABLE invoice
    ADD CONSTRAINT invoice_entity_fk FOREIGN KEY (entity_id) REFERENCES crown_entity(entity_id);
ALTER TABLE invoice
    ADD CONSTRAINT invoice_warehouse_fk FOREIGN KEY (delivery_warehouse) REFERENCES warehouse(warehouse_id);
ALTER TABLE invoice
    ADD CONSTRAINT invoice_discount_fk FOREIGN KEY (discount_type) REFERENCES discount_type(discount_type_id);
ALTER TABLE invoice
    ADD CONSTRAINT invoice_user_fk FOREIGN KEY (created_by) REFERENCES crown_user(user_id);
ALTER TABLE invoice
    ADD CONSTRAINT invoice_company_fk FOREIGN KEY (company) REFERENCES company(company_id);

/*
***********************************************************************************************************************
********************************************** ITEM *******************************************************************
***********************************************************************************************************************
*/
CREATE TABLE item (
	item_id			serial NOT NULL,
	version			bigint NOT NULL default 0,
	item_code		character varying(25) NOT NULL,/*ITM <<NAME/DESIGN/STYLE/SIZE/MRP>>*/
	item_name		character varying(50) NOT NULL,
	model_number		character varying(50),
	item_category		int,
	item_brand		int,
	uom			int,
	pieces_per_uom		int,
	currency		character(3),
	cost_price		double precision,
	uom_price		double precision,
	item_price		double precision,
	status			int,
	vat			double precision,
	reorder_level		int default 10,
	PRIMARY KEY		(item_id)
);

ALTER TABLE item
    ADD CONSTRAINT items_brand_fk FOREIGN KEY (item_brand) REFERENCES item_brand(brand_id);
ALTER TABLE item
    ADD CONSTRAINT items_category_fk FOREIGN KEY (item_category) REFERENCES item_category(category_id);
ALTER TABLE item
    ADD CONSTRAINT items_currency_fk FOREIGN KEY (currency) REFERENCES currency(currency_code);
ALTER TABLE item
    ADD CONSTRAINT items_status_fk FOREIGN KEY (status) REFERENCES status(status_id);
ALTER TABLE item
    ADD CONSTRAINT items_uom_fk FOREIGN KEY (uom) REFERENCES unit_of_measure(uom_id);

/*
***********************************************************************************************************************
********************************************** WAREHOUSE STOCK ********************************************************
***********************************************************************************************************************
*/
CREATE TABLE warehouse_stock(
	item_id			int NOT NULL,
	warehouse_id		int NOT NULL,
	uom_quantity		integer,
	item_quantity		integer,
	PRIMARY KEY		(item_id, warehouse_id)
);
ALTER TABLE warehouse_stock
    ADD CONSTRAINT wi_warehouse_fk FOREIGN KEY (warehouse_id) REFERENCES warehouse(warehouse_id);
ALTER TABLE warehouse_stock
    ADD CONSTRAINT wi_item_fk FOREIGN KEY (item_id) REFERENCES item(item_id);

/*
***********************************************************************************************************************
********************************************** AGENT COMMISSION *******************************************************
***********************************************************************************************************************
*/
CREATE TABLE agent_commission (
	invoice_id		int NOT NULL,
	agent_id		int NOT NULL,
	commission		double precision,
	status			int,
	PRIMARY KEY		(invoice_id, agent_id)
);
ALTER TABLE agent_commission
    ADD CONSTRAINT agent_comm_invoice_fk FOREIGN KEY (invoice_id) REFERENCES invoice(invoice_id);
ALTER TABLE agent_commission
    ADD CONSTRAINT agent_comm_agent_fk FOREIGN KEY (agent_id) REFERENCES agent(agent_id);


/*
***********************************************************************************************************************
********************************************** INVOICE ITEMS **********************************************************
***********************************************************************************************************************
*/
CREATE TABLE invoice_items (
	invoice_id		int NOT NULL,
	item_id			int NOT NULL,
	allocation_type		int NOT NULL,
	item_qty		int,
	amount			double precision,
	delivered_qty		int,
	remarks			text,
	PRIMARY KEY		(invoice_id, item_id, allocation_type)
);

ALTER TABLE invoice_items
    ADD CONSTRAINT invoice_item_fk FOREIGN KEY (item_id) REFERENCES item(item_id);
ALTER TABLE invoice_items
    ADD CONSTRAINT invoice_id_fk FOREIGN KEY (invoice_id) REFERENCES invoice(invoice_id);
ALTER TABLE invoice_items
    ADD CONSTRAINT allocation_type_fk FOREIGN KEY (allocation_type) REFERENCES allocation_type(allocation_type_id);


/*
***********************************************************************************************************************
********************************************** INVOICE RETURN *********************************************************
***********************************************************************************************************************
*/
CREATE TABLE invoice_return (
	return_id		serial NOT NULL,
	version			bigint NOT NULL default 0,
	invoice_id		int NOT NULL,
	item_id			int NOT NULL,
	return_type		int NOT NULL, 
	return_qty		int NOT NULL, /* RETURN QTY CANNOT BE NULL ALWAYS*/
	amount			double precision,
	return_date		date,
	remarks			text,
	PRIMARY KEY		(return_id)
);

ALTER TABLE invoice_return
    ADD CONSTRAINT invoice_id_fk FOREIGN KEY (invoice_id) REFERENCES invoice(invoice_id);
ALTER TABLE invoice_return
    ADD CONSTRAINT item_id_fk FOREIGN KEY (item_id) REFERENCES item(item_id);
ALTER TABLE invoice_return
    ADD CONSTRAINT return_type_fk FOREIGN KEY (return_type) REFERENCES allocation_type(allocation_type_id);


/*
***********************************************************************************************************************
********************************************** INVOICE PAYMENT ********************************************************
***********************************************************************************************************************
*/
CREATE TABLE invoice_payment (
	payment_id		serial NOT NULL,
	version			bigint NOT NULL default 0,
	invoice_id		int NOT NULL,
	payment_mode		int NOT NULL,
	bank_id			int, /*This is not a foreign key to bank (in case of cash payment)*/
	draft_number		character varying(25), /*Cheque or Credit Card Number*/
	cheque_date		date DEFAULT CURRENT_DATE,
	amount			double precision, 
	payer			character varying(50),
	remarks			text,
	PRIMARY KEY		(payment_id)
);

ALTER TABLE invoice_payment
    ADD CONSTRAINT invoice_id_fk FOREIGN KEY (invoice_id) REFERENCES invoice(invoice_id);
ALTER TABLE invoice_payment
    ADD CONSTRAINT invoice_pmt_fk FOREIGN KEY (payment_mode) REFERENCES payment_mode(payment_mode_id);
ALTER TABLE invoice_payment ADD CONSTRAINT fk_pmt_bank FOREIGN KEY (bank_id) REFERENCES bank (bank_id);


/*
***********************************************************************************************************************
********************************************** CREDIT NOTE ************************************************************
***********************************************************************************************************************
*/
CREATE TABLE credit_note (
	credit_note_id		serial NOT NULL,
	version			bigint NOT NULL default 0,
	note_number		character varying(25) NOT NULL UNIQUE, /*INV <<YYYYMMDD>><<RUNNING SEQ>>*/
	invoice_id		int NOT NULL,
	customer_id		int NOT NULL,
	issue_date		date NOT NULL,
	remarks			text,
	amount			double precision,
	issued			boolean,
	utilized		boolean DEFAULT false,
	PRIMARY KEY		(credit_note_id)
);

ALTER TABLE credit_note
    ADD CONSTRAINT invoice_id_fk FOREIGN KEY (invoice_id) REFERENCES invoice(invoice_id);
ALTER TABLE credit_note
    ADD CONSTRAINT customer_id_fk FOREIGN KEY (customer_id) REFERENCES customer(customer_id);


/*
***********************************************************************************************************************
********************************************** STOCK ALLOCATION *******************************************************
***********************************************************************************************************************
*/
CREATE TABLE stock_allocation (
	allocation_id		serial NOT NULL,
	version			bigint NOT NULL default 0,
	allocation_code		character varying(25) UNIQUE NOT NULL,
	item_id			int,
	entity			int,
	allocation_type		int, /*ITEM/UOM*/
	allocation_qty		integer,
	allocated_date		date,
	released_date		date,
	state			int,
	status			int,
	PRIMARY KEY		(allocation_id)
);

ALTER TABLE stock_allocation
    ADD CONSTRAINT allocation_entity_fk FOREIGN KEY (entity) REFERENCES crown_entity(entity_id);
ALTER TABLE stock_allocation
    ADD CONSTRAINT allocation_item_fk FOREIGN KEY (item_id) REFERENCES item(item_id);
ALTER TABLE stock_allocation
    ADD CONSTRAINT allocation_status_fk FOREIGN KEY (status) REFERENCES status(status_id);
ALTER TABLE stock_allocation
    ADD CONSTRAINT itemallocation_type_fk FOREIGN KEY (allocation_type) REFERENCES allocation_type(allocation_type_id);
ALTER TABLE stock_allocation
    ADD CONSTRAINT allocation_state_fk FOREIGN KEY (state) REFERENCES allocation_state(state_id);

/*
***********************************************************************************************************************
********************************************** STOLEN STOCK ***********************************************************
***********************************************************************************************************************
*/
CREATE TABLE stolen_stock (
	stolen_id		serial NOT NULL,
	version			bigint NOT NULL default 0,
	item_id			int NOT NULL,
	stolen_uom_qty		integer,
	stolen_item_qty		integer,
	warehouse		int,
	stolen_date		date NOT NULL,
	remarks			text,
	created_by		int NOT NULL,
	PRIMARY KEY		(stolen_id)
);

ALTER TABLE stolen_stock
    ADD CONSTRAINT stolen_item_fk FOREIGN KEY (item_id) REFERENCES item(item_id);
ALTER TABLE stolen_stock
    ADD CONSTRAINT stolen_warehouse_fk FOREIGN KEY (warehouse) REFERENCES warehouse(warehouse_id);
ALTER TABLE stolen_stock ADD CONSTRAINT created_user_fk FOREIGN KEY (created_by) REFERENCES crown_user (user_id);

/*
***********************************************************************************************************************
********************************************** STOCK MOVEMENT *********************************************************
***********************************************************************************************************************
*/
CREATE TABLE stock_movement (
	movement_id		serial NOT NULL,
	version			bigint NOT NULL default 0,
	move_receipt_id		character varying(20) UNIQUE NOT NULL,/*MV <<YYYYMMDD>><<RUNNING SEQ>>*/
	from_warehouse		int,
	moved_date		date,
	move_status		int,
	to_warehouse		int,
	received_date		date,
	returned_date		date,
	comments		text, /* This shall be appended with the comments on cancel, accept etc. */
	PRIMARY KEY		(movement_id)
);
ALTER TABLE stock_movement
    ADD CONSTRAINT move_status_fk FOREIGN KEY (move_status) REFERENCES move_status(move_status_id);
ALTER TABLE stock_movement
    ADD CONSTRAINT move_warehouse_fk1 FOREIGN KEY (from_warehouse) REFERENCES warehouse(warehouse_id);
ALTER TABLE stock_movement
    ADD CONSTRAINT move_warehouse_fk2 FOREIGN KEY (to_warehouse) REFERENCES warehouse(warehouse_id);


/*
***********************************************************************************************************************
********************************************** STOCK MOVEMENT ITEMS ***************************************************
***********************************************************************************************************************
*/
CREATE TABLE stock_movement_items (
	movement_id		int NOT NULL,
	item_id			int NOT NULL,
	allocation_type		int NOT NULL,
	moved_qty		integer,
	received_qty		integer,
	returned_qty		integer,
	PRIMARY KEY		(movement_id, item_id)
);
ALTER TABLE stock_movement_items
    ADD CONSTRAINT stock_movement_items_fk1 FOREIGN KEY (movement_id) REFERENCES stock_movement(movement_id);
ALTER TABLE stock_movement_items
    ADD CONSTRAINT stock_movement_items_fk2 FOREIGN KEY (item_id) REFERENCES item(item_id);
ALTER TABLE stock_movement_items
    ADD CONSTRAINT allocation_type_fk FOREIGN KEY (allocation_type) REFERENCES allocation_type(allocation_type_id);

/*
***********************************************************************************************************************
********************************************** AUDIT ******************************************************************
***********************************************************************************************************************
*/
CREATE TABLE crown_audit (
	audit_id		serial NOT NULL,
	version			bigint NOT NULL default 0,
	action			int NOT NULL,
	user_id			int NOT NULL,
	table_name		varchar(32) NOT NULL,
	description		text,
	action_time		timestamp without time zone NOT NULL,
	PRIMARY KEY		(audit_id)
);

ALTER TABLE crown_audit
    ADD CONSTRAINT audit_user_fk FOREIGN KEY (user_id) REFERENCES crown_user(user_id);
ALTER TABLE crown_audit
    ADD CONSTRAINT audit_action_fk FOREIGN KEY (action) REFERENCES audit_action(action_id);

/*
***********************************************************************************************************************
********************************************** RECEIPT ****************************************************************
***********************************************************************************************************************
*/
CREATE TABLE receipt (
	receipt_id		serial NOT NULL,
	version			bigint NOT NULL default 0,
	receipt_number		varchar(30) UNIQUE NOT NULL,
	receipt_date		date NOT NULL,
	customer		int NOT NULL,
	issuing_company		int NOT NULL,
	amount			double precision,
	used_amount		double precision,
	used_state		char(2) NOT NULL DEFAULT 'UU', /* FU - Fully Used, PU - Partially Used, UU - UnUsed */
	issued			boolean,
	remarks			text,
	PRIMARY KEY		(receipt_id)
);

ALTER TABLE receipt
    ADD CONSTRAINT customer_fk FOREIGN KEY (customer) REFERENCES customer(customer_id);
ALTER TABLE receipt
    ADD CONSTRAINT company_fk FOREIGN KEY (issuing_company) REFERENCES company(company_id);

/*
***********************************************************************************************************************
********************************************** RECEIPT PAYMENT ********************************************************
***********************************************************************************************************************
*/
CREATE TABLE receipt_payment (
	payment_id		serial NOT NULL,
	version			bigint NOT NULL default 0,
	receipt_id		int NOT NULL,
	payment_mode		int NOT NULL,
	bank_id			int, /*This is not a foreign key to bank (in case of cash payment)*/
	cheque_number		character varying(25), /*Cheque or Credit Card Number*/
	cheque_date		date DEFAULT CURRENT_DATE, /* This can be paid date as well for non cheque payments */
	amount			double precision, 
	payer			character varying(50),
	PRIMARY KEY		(payment_id)
);

ALTER TABLE receipt_payment
    ADD CONSTRAINT receipt_id_fk FOREIGN KEY (receipt_id) REFERENCES receipt(receipt_id);
ALTER TABLE receipt_payment
    ADD CONSTRAINT receipt_pmt_fk FOREIGN KEY (payment_mode) REFERENCES payment_mode(payment_mode_id);
ALTER TABLE receipt_payment ADD CONSTRAINT fk_pmt_bank FOREIGN KEY (bank_id) REFERENCES bank (bank_id);

/*
***********************************************************************************************************************
********************************************** TOTAL STOCK VIEW *******************************************************
***********************************************************************************************************************
*/
CREATE VIEW total_stock AS (
	SELECT w.item_id, uom, sum(item_quantity) as item_qty, sum(uom_quantity) as uom_qty
	 FROM warehouse_stock w, item WHERE item.item_id = w.item_id GROUP BY w.item_id, item.uom);


/*
***********************************************************************************************************************
********************************************** TOTAL ENTITY STOCK VIEW ************************************************
***********************************************************************************************************************
*/
CREATE OR REPLACE VIEW total_entity_stock AS (
	SELECT ce.entity_id, ws.item_id, sum(ws.item_quantity) AS item_qty, sum(ws.uom_quantity) AS uom_qty
	 FROM warehouse_stock ws, crown_entity ce
	 WHERE ws.warehouse_id IN 
	  ( SELECT w.warehouse_id FROM warehouse w WHERE w.entity = ce.entity_id)
	 GROUP BY ce.entity_id, ws.item_id);

/*
***********************************************************************************************************************
********************************************** SUPPLIER ***************************************************************
***********************************************************************************************************************
*/
CREATE TABLE supplier (
	supplier_id		serial NOT NULL,
	version			bigint NOT NULL default 0,
	supplier_code		character varying(25) UNIQUE NOT NULL,
	supplier_name		character varying(50),
	address			text,
	phone			character varying(50),
	POC			character varying(50),
	TIN			character varying(25),
	remarks			text,
	status			int,
	PRIMARY KEY		(supplier_id)
);

ALTER TABLE only supplier
    ADD CONSTRAINT status_fk FOREIGN KEY (status) REFERENCES status(status_id);


/*
***********************************************************************************************************************
***************************************** PURCHASE INVOICE ************************************************************
***********************************************************************************************************************
*/
CREATE TABLE purchase_invoice (
	invoice_id		serial NOT NULL,
	version			bigint NOT NULL default 0,
	invoice_number		character varying(25) NOT NULL UNIQUE, /*INV <<YYYYMMDD>><<RUNNING SEQ>>*/
	supplier_id		int NOT NULL,
	invoice_date		date,
	currency		character(3), /* LET THIS BE THERE*/
	invoice_state		char(3), /* NEW, PRT (printed) or REC (received - goods receipt) */
	invoice_amount		double precision,
	received_date		date,
	remarks			text,
	created_by		int,
	received_by		int,
	company			int,
	terms_conditions	text,
	goods_receipt_number	character varying(50),
	PRIMARY KEY		(invoice_id)
);


ALTER TABLE purchase_invoice
    ADD CONSTRAINT invoice_currency_fk FOREIGN KEY (currency) REFERENCES currency(currency_code);
ALTER TABLE purchase_invoice
    ADD CONSTRAINT supplier_fk FOREIGN KEY (supplier_id) REFERENCES supplier(supplier_id);
ALTER TABLE purchase_invoice
    ADD CONSTRAINT created_fk FOREIGN KEY (created_by) REFERENCES crown_user(user_id);
ALTER TABLE purchase_invoice
    ADD CONSTRAINT received_fk FOREIGN KEY (received_by) REFERENCES crown_user(user_id);
ALTER TABLE purchase_invoice
    ADD CONSTRAINT company_fk FOREIGN KEY (company) REFERENCES company(company_id);

/*
***********************************************************************************************************************
**************************************** PURCHASE INVOICE ITEMS *******************************************************
***********************************************************************************************************************
*/
CREATE TABLE purchase_invoice_items (
	invoice_id		int NOT NULL,
	item_id			int NOT NULL,
	allocation_type		int NOT NULL,
	item_qty		int,
	price			double precision,
	received_qty		int,
	remarks			text,
	PRIMARY KEY		(invoice_id, item_id, allocation_type)
);

ALTER TABLE purchase_invoice_items
    ADD CONSTRAINT item_fk FOREIGN KEY (item_id) REFERENCES item(item_id);
ALTER TABLE purchase_invoice_items
    ADD CONSTRAINT invoice_id_fk FOREIGN KEY (invoice_id) REFERENCES purchase_invoice(invoice_id);
ALTER TABLE purchase_invoice_items
    ADD CONSTRAINT allocation_type_fk FOREIGN KEY (allocation_type) REFERENCES allocation_type(allocation_type_id);
    
/*
***********************************************************************************************************************
********************************************** PURCHASE INVOICE PAYMENT ***********************************************
***********************************************************************************************************************
*/
CREATE TABLE purchase_invoice_payment (
	payment_id		serial NOT NULL,
	version			bigint NOT NULL default 0,
	invoice_id		int NOT NULL,
	payment_mode		int NOT NULL,
	bank_id			int, /*This is not a foreign key to bank (in case of cash payment)*/
	draft_number		character varying(25), /*Cheque or Credit Card Number*/
	cheque_date		date DEFAULT CURRENT_DATE,
	amount			double precision, 
	remarks			text,
	PRIMARY KEY		(payment_id)
);

ALTER TABLE purchase_invoice_payment
    ADD CONSTRAINT pur_invoice_id_fk FOREIGN KEY (invoice_id) REFERENCES purchase_invoice(invoice_id);
ALTER TABLE purchase_invoice_payment
    ADD CONSTRAINT pur_invoice_pmt_fk FOREIGN KEY (payment_mode) REFERENCES payment_mode(payment_mode_id);
ALTER TABLE purchase_invoice_payment ADD CONSTRAINT pur_pmt_bank_fk FOREIGN KEY (bank_id) REFERENCES bank (bank_id);

/*
***********************************************************************************************************************
**************************************** EXPENSE ALLOCATION ***********************************************************
***********************************************************************************************************************
*/
CREATE TABLE expense_allocation (
	allocation_id	serial NOT NULL,
	version			bigint NOT NULL default 0,
	allocation_date	date DEFAULT CURRENT_DATE,
	allocation_amount double precision,
	is_open			boolean default TRUE,
	valid_till		date,
	remarks			text,
	PRIMARY KEY		(allocation_id)
);
/*
***********************************************************************************************************************
**************************************** EXPENSES *********************************************************************
***********************************************************************************************************************
*/
CREATE TABLE expense (
	expense_id		serial NOT NULL,
	version			bigint NOT NULL default 0,
	allocation_id	int NOT NULL,
	expense_amount	double precision,
	voucher_number	character varying(25), /*Cheque or Credit Card Number*/
	expense_date	date DEFAULT CURRENT_DATE,
	remarks			text,
	PRIMARY KEY		(expense_id)
);
ALTER TABLE expense
    ADD CONSTRAINT alloc_id_fk FOREIGN KEY (allocation_id) REFERENCES expense_allocation(allocation_id);
