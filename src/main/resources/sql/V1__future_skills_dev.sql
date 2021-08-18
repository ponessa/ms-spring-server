DROP TRIGGER SKILL_DEV.FUTURE_SKILLS_T1;
DROP TRIGGER SKILL_DEV.FUTURE_SKILLS_T2;
DROP TRIGGER SKILL_DEV.FUTURE_SKILLS_T3;
DROP TRIGGER SKILL_DEV.FUTURE_SKILLS_T4;
ALTER TABLE SKILL_DEV.FUTURE_SKILLS DROP CONSTRAINT FUTURE_SKILLS_PK;
DROP VIEW SKILL_DEV.FUTURE_SKILLS_V;
DROP VIEW SKILL_DEV.FUTURE_SKILLS_SCD_V;
DROP TABLE SKILL_DEV.FUTURE_SKILLS;
DROP TABLESPACE TS_FUTURE_SKILL2;
COMMIT WORK;

CREATE TABLESPACE TS_FUTURE_SKILL2 MANAGED BY AUTOMATIC STORAGE;

CREATE TABLE SKILL_DEV.FUTURE_SKILLS (
      FUTURE_SKILLS_ID         INTEGER  NOT NULL GENERATED ALWAYS AS IDENTITY ( START WITH 1 INCREMENT BY 1 MINVALUE 1 MAXVALUE 2147483647 NO CYCLE CACHE 20 NO ORDER )
  ,   CNUM                     VARCHAR(10) NOT NULL
  ,   TARGET_JRS               VARCHAR(802) NOT NULL
  ,   TYPE_OF_SKILLING         VARCHAR(15) NOT NULL
  ,   CURRENT_BUSINESS_VALUE   VARCHAR(20)
  ,   TARGET_BUSINESS_VALUE    VARCHAR(20)
  ,   SK_EMPLOYEE              INTEGER
  ,   STATUS                   VARCHAR(50)
  ,   COMPLETION_DATE          DATE
  ,   SK_COMPLETION_DATE       INTEGER
  ,   BATCH_NAME               VARCHAR(512)
  ,   BATCH_STATUS             VARCHAR(32)
  ,   COMPLETION_PERCENTAGE    INTEGER
  ,   PLAN_NAME                VARCHAR(200)
  ,   JRS_CHANGED              VARCHAR(32)
  ,   REPORTING_YEAR           INTEGER
  ,   LEARNING_ADVISOR         VARCHAR(192)
  ,   ENROLLMENT_DATE          DATE
  ,   SK_ENROLLMENT_DATE       INTEGER
  ,   SOURCE_JRS               VARCHAR(1000)
  ,   MANAGEMENT_ORGANIZATION  VARCHAR(128)
  ,   CHANNEL                  VARCHAR(64)
  ,   START_MONTH              VARCHAR(32)
  ,   ARCHIVED_STATUS          VARCHAR(32)
  ,   CURRENT_INDICATOR        INTEGER
  ,   EFFECTIVE_DATE           DATE
  ,   SK_EFFECTIVE_DATE        INTEGER
  ,   EXPIRATION_DATE          DATE
  ,   SK_EXPIRATION_DATE       INTEGER
  ,   ETL_TIMESTAMP            TIMESTAMP
  ,   AUDIT_TIMESTAMP          TIMESTAMP
  ,   ROW_STATUS_CD            CHAR(1) NOT NULL DEFAULT 'I'
  ,   VERSION_NUM              INTEGER NOT NULL DEFAULT 1
  ,   EFF_TMS                  TIMESTAMP NOT NULL
  ,   EXPIR_TMS                TIMESTAMP NOT NULL
  )
  IN TS_FUTURE_SKILL2
  DATA CAPTURE NONE 
  COMPRESS NO;
  
 --Alter table to add primary key for Surrogate Id
ALTER TABLE SKILL_DEV.FUTURE_SKILLS ADD CONSTRAINT FUTURE_SKILLS_PK PRIMARY KEY (FUTURE_SKILLS_ID);

--Create UNIQUE INDEX based on the natural key and SCD attributes
CREATE UNIQUE INDEX SKILL_DEV.FUTURE_SKILLS_UX1 
    ON SKILL_DEV.FUTURE_SKILLS (
      CNUM  ASC
    , TARGET_JRS  ASC
    , TYPE_OF_SKILLING  ASC
    , EXPIR_TMS
    )
    MINPCTUSED 0
    DISALLOW REVERSE SCANS
    PAGE SPLIT SYMMETRIC
    COLLECT SAMPLED DETAILED STATISTICS
    COMPRESS NO;
   
COMMENT ON TABLE SKILL_DEV.FUTURE_SKILLS IS 'Train Track future skills from WF360.';

--Add comments to the columns
COMMENT ON COLUMN SKILL_DEV.FUTURE_SKILLS.FUTURE_SKILLS_ID IS 'Surrogate key for SKILL_DEV.FUTURE_SKILLS.';

COMMENT ON COLUMN SKILL_DEV.FUTURE_SKILLS.ROW_STATUS_CD IS 'Row Status code. ''I'' - Insert, ''U'' - Update, ''D'' - Delete.';
COMMENT ON COLUMN SKILL_DEV.FUTURE_SKILLS.VERSION_NUM IS 'Version number of the record''s state.';
COMMENT ON COLUMN SKILL_DEV.FUTURE_SKILLS.EFF_TMS IS 'Timestamp indicating when the particular record became effective within the slow changing dimension. Note that each record of a SCD represents a ''state'' of the object/data.';
COMMENT ON COLUMN SKILL_DEV.FUTURE_SKILLS.EXPIR_TMS IS 'Timestamp indicating when the particular record has expired (is no longer in effect) within the slow changing dimension.. 9999-12-31-23.59.59.999999 represents the current state of the record.';

--Views
CREATE VIEW SKILL_DEV.FUTURE_SKILLS_V
AS
SELECT FUTURE_SKILLS_ID
     , CNUM
     , TARGET_JRS
     , TYPE_OF_SKILLING
     , CURRENT_BUSINESS_VALUE
     , TARGET_BUSINESS_VALUE
     , SK_EMPLOYEE
     , STATUS
     , COMPLETION_DATE
     , SK_COMPLETION_DATE
     , BATCH_NAME
     , BATCH_STATUS
     , COMPLETION_PERCENTAGE
     , PLAN_NAME
     , JRS_CHANGED
     , REPORTING_YEAR
     , LEARNING_ADVISOR
     , ENROLLMENT_DATE
     , SK_ENROLLMENT_DATE
     , SOURCE_JRS
     , MANAGEMENT_ORGANIZATION
     , CHANNEL
     , START_MONTH
     , ARCHIVED_STATUS
     , CURRENT_INDICATOR
     , EFFECTIVE_DATE
     , SK_EFFECTIVE_DATE
     , EXPIRATION_DATE
     , SK_EXPIRATION_DATE
     , ETL_TIMESTAMP
     , AUDIT_TIMESTAMP
FROM   SKILL_DEV.FUTURE_SKILLS
WHERE  EXPIR_TMS = '9999-12-31-23.59.59.999999';

CREATE VIEW SKILL_DEV.FUTURE_SKILLS_SCD_V
AS
SELECT *
FROM   SKILL_DEV.FUTURE_SKILLS;

--Create Triggers to manage SCD
--T1 Set Effective and Expiration Timestamps on new/inserted records
CREATE TRIGGER SKILL_DEV.FUTURE_SKILLS_T1
    BEFORE INSERT ON SKILL_DEV.FUTURE_SKILLS
    REFERENCING  NEW AS NEWVAL
    FOR EACH ROW
SET EFF_TMS       = CURRENT TIMESTAMP
  ,  EXPIR_TMS     = '9999-12-31-23.59.59.999999'
  ,  ROW_STATUS_CD = 'I';

--T2 Expire previous record for the natural key (EXPIR_TMS = N.EFF_TMS - 1 MICROSECOND)
CREATE TRIGGER SKILL_DEV.FUTURE_SKILLS_T2
    AFTER INSERT ON SKILL_DEV.FUTURE_SKILLS
    REFERENCING  NEW AS N
    FOR EACH ROW
UPDATE SKILL_DEV.FUTURE_SKILLS
  SET EXPIR_TMS     = N.EFF_TMS - 1 MICROSECOND 
    , ROW_STATUS_CD = 'H'
WHERE CNUM = N.CNUM
  AND TARGET_JRS = N.TARGET_JRS
  AND TYPE_OF_SKILLING = N.TYPE_OF_SKILLING
--  AND VERSION_NUM   = N.VERSION_NUM-1 
  AND EXPIR_TMS     = '9999-12-31-23.59.59.999999'
  AND EFF_TMS       < N.EFF_TMS;
  
--T3 Change DELETE transaction to expire previous record for the natural key.
CREATE TRIGGER SKILL_DEV.FUTURE_SKILLS_T3
    INSTEAD OF DELETE ON SKILL_DEV.FUTURE_SKILLS_SCD_V
    REFERENCING  OLD AS O
    FOR EACH ROW
UPDATE SKILL_DEV.FUTURE_SKILLS
  SET EXPIR_TMS     = CURRENT TIMESTAMP
    , ROW_STATUS_CD = 'D'
WHERE CNUM = O.CNUM
  AND TARGET_JRS = O.TARGET_JRS
  AND TYPE_OF_SKILLING = O.TYPE_OF_SKILLING
  AND EXPIR_TMS     = '9999-12-31-23.59.59.999999';
  
--T4 Change UPDATE transaction to insert a new record.
-- The insert will invoke T1 to set the effective and expiration timestamp and
-- T2 to expire previous record for the natural key.
CREATE TRIGGER SKILL_DEV.FUTURE_SKILLS_T4
    INSTEAD OF UPDATE ON SKILL_DEV.FUTURE_SKILLS_SCD_V
    REFERENCING  NEW AS N
    FOR EACH ROW
INSERT INTO SKILL_DEV.FUTURE_SKILLS
 (CNUM, TARGET_JRS, TYPE_OF_SKILLING, CURRENT_BUSINESS_VALUE, TARGET_BUSINESS_VALUE, SK_EMPLOYEE, STATUS, COMPLETION_DATE, SK_COMPLETION_DATE, BATCH_NAME, BATCH_STATUS, COMPLETION_PERCENTAGE, PLAN_NAME, JRS_CHANGED, REPORTING_YEAR, LEARNING_ADVISOR, ENROLLMENT_DATE, SK_ENROLLMENT_DATE, SOURCE_JRS, MANAGEMENT_ORGANIZATION, CHANNEL, START_MONTH, ARCHIVED_STATUS, CURRENT_INDICATOR, EFFECTIVE_DATE, SK_EFFECTIVE_DATE, EXPIRATION_DATE, SK_EXPIRATION_DATE, ETL_TIMESTAMP, AUDIT_TIMESTAMP)
VALUES
 (N.CNUM, N.TARGET_JRS, N.TYPE_OF_SKILLING, N.CURRENT_BUSINESS_VALUE, N.TARGET_BUSINESS_VALUE, N.SK_EMPLOYEE, N.STATUS, N.COMPLETION_DATE, N.SK_COMPLETION_DATE, N.BATCH_NAME, N.BATCH_STATUS, N.COMPLETION_PERCENTAGE, N.PLAN_NAME, N.JRS_CHANGED, N.REPORTING_YEAR, N.LEARNING_ADVISOR, N.ENROLLMENT_DATE, N.SK_ENROLLMENT_DATE, N.SOURCE_JRS, N.MANAGEMENT_ORGANIZATION, N.CHANNEL, N.START_MONTH, N.ARCHIVED_STATUS, N.CURRENT_INDICATOR, N.EFFECTIVE_DATE, N.SK_EFFECTIVE_DATE, N.EXPIRATION_DATE, N.SK_EXPIRATION_DATE, N.ETL_TIMESTAMP, N.AUDIT_TIMESTAMP);
 
grant select on table SKILL_DEV.FUTURE_SKILLS_V to role read_access_dev;
grant select on table SKILL_DEV.FUTURE_SKILLS_SCD_V to role read_access_dev;

