create table OAUTH_CLIENT_DETAILS (
  client_id VARCHAR(256) PRIMARY KEY,
  resource_ids VARCHAR(256),
  client_secret VARCHAR(256),
  scope VARCHAR(256),
  authorized_grant_types VARCHAR(256),
  web_server_redirect_uri VARCHAR(256),
  authorities VARCHAR(256),
  access_token_validity INTEGER,
  refresh_token_validity INTEGER,
  additional_information VARCHAR(4096),
  autoapprove VARCHAR(256)
);

create table oauth_client_token (
	token_id VARCHAR(255),
	token bytea ,
	authentication_id VARCHAR(255),
	user_name VARCHAR(255),
	client_id VARCHAR(255)
);

create table oauth_access_token (
	token_id VARCHAR(255),
	token bytea,
	authentication_id VARCHAR(255),
	user_name VARCHAR(255),
	client_id VARCHAR(255),
	authentication bytea,
	refresh_token VARCHAR(255)
);

create table oauth_refresh_token (
	token_id VARCHAR(255),
	token bytea,
	authentication bytea
);

create table oauth_code (
	code VARCHAR(255), authentication bytea
);

create table tanistan_user (id varchar(255) not null, created_by varchar(255), created_date timestamp not null, modified_by varchar(255), modified_date timestamp, account_name varchar(255), account_phrase varchar(4096), attempt_type varchar(255), birth_day date, email_address varchar(255), last_name varchar(255), login_attempt integer, middle_name varchar(255), name varchar(255), phone_number varchar(255), secret_answer varchar(255), secret_question varchar(255), test_suite_id varchar(255), primary key (id));
create table test_suite_test_case_rel (id varchar(255) not null, test_suite_id varchar(255) not null, primary key (id, test_suite_id));
create table test_case (id varchar(255) not null, created_by varchar(255), created_date timestamp not null, modified_by varchar(255), modified_date timestamp, name varchar(255), test_commands varchar(32768), user_entity_id varchar(255), primary key (id));
create table test_case_instance_runner (id varchar(255) not null, created_by varchar(255), created_date timestamp not null, modified_by varchar(255), modified_date timestamp, running boolean not null, test_case_id varchar(255), user_entity_id varchar(255), primary key (id));
create table test_step (id varchar(255) not null, created_by varchar(255), created_date timestamp not null, modified_by varchar(255), modified_date timestamp, execution_time timestamp, result varchar(4096), run_status boolean not null, status integer, test_case_instance_runner_id varchar(255), primary key (id));
create table test_suite (id varchar(255) not null, created_by varchar(255), created_date timestamp not null, modified_by varchar(255), modified_date timestamp, name varchar(255), parent_id varchar(255), primary key (id));
create table user_auth_relation (id varchar(255) not null, user_entity_id varchar(255) not null);
create table user_authorization (id varchar(255) not null, created_by varchar(255), created_date timestamp not null, modified_by varchar(255), modified_date timestamp, authority varchar(255), primary key (id));
alter table tanistan_user add constraint UK6qqisgona1v2qvewkxfwh05b6 unique (account_name, email_address);
alter table user_authorization add constraint UK6873pmw0w87wf757hu8hndkjr unique (authority);
alter table tanistan_user add constraint FKhrcohw01gix0s1bvcidoqys55 foreign key (test_suite_id) references test_suite;
alter table test_suite_test_case_rel add constraint FKkr5l04bode5ifscpa4k3eyapv foreign key (test_suite_id) references test_suite;
alter table test_suite_test_case_rel add constraint FKbm6d6wvk6xfatjqydw04mrkt0 foreign key (id) references test_case;
alter table test_case add constraint FKa1mwq0sjngbjd0loo7p6fxxaf foreign key (user_entity_id) references tanistan_user;
alter table test_case_instance_runner add constraint FKqs4aeh1is6p9wdqh5wvai4las foreign key (test_case_id) references test_case;
alter table test_case_instance_runner add constraint FKlu5geeboyl4r5tcccws4n97w7 foreign key (user_entity_id) references tanistan_user;
alter table test_step add constraint FKfjne503h1ep49qxk0qi06u29g foreign key (test_case_instance_runner_id) references test_case_instance_runner;
alter table test_suite add constraint FKnlhm016gjf8gvev8d73k0l4hy foreign key (parent_id) references test_suite;
alter table user_auth_relation add constraint FKpagb5sre20f1w8tc3h7lp167e foreign key (user_entity_id) references tanistan_user;
alter table user_auth_relation add constraint FKqftc29nrwf0ust4rc2vk9a0na foreign key (id) references user_authorization;
-- Create root folder
insert into test_suite (id, created_by, created_date, modified_by, modified_date, name, parent_id) VALUES ('0','anyUser',CURRENT_TIMESTAMP, 'anyUser',CURRENT_TIMESTAMP,'Root',null);