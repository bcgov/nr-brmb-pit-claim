CREATE TABLE ccs.process_failovr_ownership (
  process_failovr_ownership_guid varchar(32)    DEFAULT GEN_RANDOM_UUID() NOT NULL,
  process_name                   varchar(64)    NOT NULL,
  service_node                   varchar(64)    NOT NULL,
  expiry_timestamp               timestamp(6)   NOT NULL,
  revision_count                 numeric(10)    DEFAULT 0 NOT NULL,
  create_user                    varchar(64)    NOT NULL,
  create_date                    timestamp(0)   DEFAULT CURRENT_TIMESTAMP NOT NULL,
  update_user                    varchar(64)    NOT NULL,
  update_date                    timestamp(0)   DEFAULT CURRENT_TIMESTAMP NOT NULL
) TABLESPACE pg_default
;



COMMENT ON TABLE ccs.process_failovr_ownership IS 'Dispatch Synchronization Ownership is used to track the active node that will process synchronization jobs between Claims Calculator and CIRRAS. Redundant processing nodes are set up to process synchronization jobs. Only one process node is active at any time, so a single process node will be declared by this table as being the active node for processing jobs. If the active node goes down for some reason, then it will not be able to extend the expiry date in the table to show the node still has ownership of processing jobs. If node ownerships expires, another node will be promoted as the new active node for processing synchronization jobs and will be assigned a new expiry date.'
;
COMMENT ON COLUMN ccs.process_failovr_ownership.process_failovr_ownership_guid IS 'PROCESS_FAILOVR_OWNERSHIP_GUID is a unique identifier for the record.'
;
COMMENT ON COLUMN ccs.process_failovr_ownership.process_name IS 'Process Name is the name of the process that requires failover support.'
;
COMMENT ON COLUMN ccs.process_failovr_ownership.service_node IS 'Service Node is the name of the current active service node.'
;
COMMENT ON COLUMN ccs.process_failovr_ownership.expiry_timestamp IS 'Expiry Timestamp is when the current active service node expires and either the current service node renews the expiry timestamp, or another service node is promoted as the current service node and a new expiry timestamp is issued.'
;
COMMENT ON COLUMN ccs.process_failovr_ownership.revision_count IS 'REVISION_COUNT is the number of times that the row of data has been changed. The column is used for optimistic locking via application code.'
;
COMMENT ON COLUMN ccs.process_failovr_ownership.create_user IS 'CREATE_USER is an audit column that indicates the user that created the record.'
;
COMMENT ON COLUMN ccs.process_failovr_ownership.create_date IS 'CREATE_DATE is the date and time the row of data was created.'
;
COMMENT ON COLUMN ccs.process_failovr_ownership.update_user IS 'UPDATE_USER is an audit column that indicates the user that updated the record.'
;
COMMENT ON COLUMN ccs.process_failovr_ownership.update_date IS 'UPDATE_DATE is the date and time the row of data was updated.'
;

ALTER TABLE ccs.process_failovr_ownership ADD
    CONSTRAINT pk_flv PRIMARY KEY (process_failovr_ownership_guid) USING INDEX TABLESPACE pg_default
;

ALTER TABLE ccs.process_failovr_ownership ADD
    CONSTRAINT uk_flv UNIQUE (process_name) USING INDEX TABLESPACE pg_default
;