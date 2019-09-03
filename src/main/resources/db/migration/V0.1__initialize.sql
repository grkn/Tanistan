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

-- Create root folder
-- insert into test_suite (id, created_by, created_date, modified_by, modified_date, name, parent_id) VALUES ('0','anyUser',CURRENT_TIMESTAMP, 'anyUser',CURRENT_TIMESTAMP,'Root',null);