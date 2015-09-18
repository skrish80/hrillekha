/* STATUS*/
insert into status(status_code,description, version) values ('Active','Active State', 1);
insert into status(status_code,description, version) values ('Disabled','Disabled State', 1);

/*BANK*/
insert into bank(bank_code,bank_name,description, version) values ('SBI','State Bank of India','State Bank of India', 1);
insert into bank(bank_code,bank_name,description, version) values ('SC','Standard Chartered','Standard Chartered', 1);
insert into bank(bank_code,bank_name,description, version) values ('HSBC','Honkkong Shanghai Bank Corporation','Honkkong Shanghai Bank Corporation', 1);
insert into bank(bank_code,bank_name,description, version) values ('RBS','Royal Bank of Scotland','Royal Bank of Scotland', 1);

/*CURRENCY*/
insert into currency(currency_code,description, version) values ('INR','Indian Rupees', 1);

/*LOCATION*/
insert into location(location,description, version) values ('Thiruvarur','Thiruvarur City', 1);
insert into location(location,description, version) values ('Mannargudi','Mannargudi city', 1);
insert into location(location,description, version) values ('Chennai-1','Thuraipakkam', 1);

/*DEPARTMENT*/
insert into department(department_name,description, version) values ('Admin','Administration', 1);
insert into department(department_name,description, version) values ('HR','Human Resource', 1);
insert into department(department_name,description, version) values ('Sales','Sales ', 1);

/*DESIGNATION*/
insert into designation (designation,description, version) values ('MD','Managing Director', 1);
insert into designation (designation,description, version) values ('SR','Sales Rep', 1);
insert into designation (designation,description, version) values ('SM','Sales Manager', 1);
insert into designation (designation,description, version) values ('GM','General Manager', 1);
insert into designation (designation,description, version) values ('CFO','Chief Financial Operator', 1);

/*CROWN ENTITY*/
insert into crown_entity(entity_name,description, entity_type, version) values ('Retail','Dummy Retail Entity', 'RT', 1);
insert into crown_entity(entity_name,description, entity_type, version) values ('Thiruvarur','Thiruvarur Entity', 'WS', 1);
insert into crown_entity(entity_name,description, entity_type, version) values ('Mannargudi','Mannargudi Entity', 'WS', 1);
insert into crown_entity(entity_name,description, entity_type, version) values ('Chennai-1','Thuraipakkam Entity', 'WS', 1);

/*UNIT OF MEASURE*/
insert into unit_of_measure(uom_name,description, version) values ('PCS','Pieces', 1);
insert into unit_of_measure(uom_name,description, version) values ('PCK','Pack', 1);

/*COMPANY TYPE*/
insert into company_type (company_type,description, version) values ('Wholesale','Wholesale', 1);
insert into company_type (company_type,description, version) values ('Retail','Retail', 1);
insert into company_type (company_type,description, version) values ('Import','Import', 1);

/*MOVE STATUS*/
insert into move_status(move_status,description, version) values ('MOVED','Moved', 1);
insert into move_status(move_status,description, version) values ('RETURNED','Returned', 1);
insert into move_status(move_status,description, version) values ('ACCEPTED','Accepted', 1);
insert into move_status(move_status,description, version) values ('PARTIAL_ACCEPT','Partial Accept', 1);
insert into move_status(move_status,description, version) values ('CANCELLED','Cancelled', 1);
insert into move_status(move_status,description, version) values ('CLOSED','Closed', 1);

/*PAYMENT MODE*/
insert into payment_mode(payment_mode,description, version) values ('Cash','Cash', 1);
insert into payment_mode(payment_mode,description, version) values ('Cheque','Cheque', 1);
insert into payment_mode(payment_mode,description, version) values ('Credit Note','Credit Note', 1);
insert into payment_mode(payment_mode,description, version) values ('Receipt','Receipt', 1);
insert into payment_mode(payment_mode,description, version) values ('Credit Card','Credit Card', 1);

/*PAYMENT STATUS*/
insert into payment_status(payment_status,description, version) values ('FULL_PAYMENT','Full Payment', 1);
insert into payment_status(payment_status,description, version) values ('PARTIAL_PAYMENT','Partial Payment', 1);
insert into payment_status(payment_status,description, version) values ('CREDIT_SALES','No Payment/Credit Sales', 1);

/*INVOICE STATE*/
insert into invoice_state(state,description, version) values ('NEW','Invoice Created', 1);
insert into invoice_state(state,description, version) values ('PRINTED','Original Invoice Printed', 1);
insert into invoice_state(state,description, version) values ('DELIVERED','Order Delivered', 1);
insert into invoice_state(state,description, version) values ('RETURNED','Items Returned', 1);
insert into invoice_state(state,description, version) values ('CANCELLED','Order cancelled/Items shall be completely returned', 1);
insert into invoice_state(state,description, version) values ('PARTIAL_DELIVERY','Order Partially Delivered', 1);
insert into invoice_state(state,description, version) values ('CREDIT_NOTE','Credit Note released', 1);
insert into invoice_state(state,description, version) values ('CLOSED','Invoice Closed. No further process', 1);

/*AGENT TYPE*/
insert into agent_type (agent_type, description, version) values ('Internal Known','Internal Known', 1);
insert into agent_type (agent_type, description, version) values ('External Known','External Known', 1);
insert into agent_type (agent_type, description, version) values ('External Unknown','External Unknown', 1);

/*ALLOCATION TYPE*/
insert into allocation_type (allocation_type,description, version) values ('UOM','Unit of Measure', 1);
insert into allocation_type (allocation_type,description, version) values ('Item','Item Wise', 1);

/*ALLOCATION STATE*/
insert into allocation_state (state,description, version) values ('ALLOCATED','Stock Allocated', 1);
insert into allocation_state (state,description, version) values ('RELEASED','Stock Released', 1);
insert into allocation_state (state,description, version) values ('DELIVERED','Stock Delivered and not in scope of allocation', 1);
insert into allocation_state (state,description, version) values ('STOLEN_DAMAGED','Stock Stolen or Damaged', 1);

/*CUSTOMER TYPE*/
insert into customer_type (customer_type,description, version) values ('Internal','Internal Customer', 1);
insert into customer_type (customer_type,description, version) values ('External','External Customer', 1);

/*DISCOUNT YTPE*/
insert into discount_type (discount,description, version) values ('Percentage','Percentage', 1);
insert into discount_type (discount,description, version) values ('Fixed','Fixed', 1);

/*ROLE*/
insert into role(role_name,role_description, version) values ('SYSTEM_ADMIN','System Administrator', 1);
insert into role(role_name,role_description, version) values ('STOCK_MANAGER','Stock Manager', 1);
insert into role(role_name,role_description, version) values ('WHOLESALE_INCHARGE','Wholesale Incharge', 1);
insert into role(role_name,role_description, version) values ('RETAIL_INCHARGE','Retail Incharge', 1);

/*AUDIT ACTION*/
insert into audit_action(action_code, description, version) values ('CREATE', 'Create Action', 1);
insert into audit_action(action_code, description, version) values ('UPDATE', 'Update Action', 1);
insert into audit_action(action_code, description, version) values ('DELETE', 'Delete Action', 1);
insert into audit_action(action_code, description, version) values ('LOGIN', 'Login Action', 1);

/*ADMIN USER username=sysadmin; password=crownadmin*/
INSERT INTO crown_user(
            employee_no, username, password, first_name, last_name, email, designation, department, "role", 
            "location", address, phone, mobile, date_of_birth, joining_date, status, version)
    VALUES ('EMP000', 'sysadmin', '7605e61df214c04eb1a9155ce8c8c319', 'System', 'Administrator', 'sysadmin@crown.com', 1, 1, 1, 1, 
            'KK Nagar', '044-23711304', '9444322723', '05-SEP-1978', '20-AUG-2011', 1, 1);

/*
GRANT INSERT, UPDATE, SELECT, DELETE ON ALL TABLES IN SCHEMA public TO gvgreat;
GRANT UPDATE, SELECT ON ALL SEQUENCES IN SCHEMA public TO gvgreat;
*/