\connect ishtech_dev_db;

-- ===== Common =====
CREATE USER booksapp_dev_user        PASSWORD 'booksapp_dev_pass'        NOSUPERUSER;
CREATE USER booksapp_dev_flyway_user PASSWORD 'booksapp_dev_flyway_pass' NOSUPERUSER;

GRANT CONNECT ON DATABASE ishtech_dev_db TO booksapp_dev_user;
GRANT CONNECT ON DATABASE ishtech_dev_db TO booksapp_dev_flyway_user;

CREATE SCHEMA booksapp_dev_schema;
CREATE SCHEMA booksapp_dev_aud_schema;

-- ===== App User =====
GRANT USAGE ON SCHEMA public                 TO booksapp_dev_user;
GRANT USAGE ON SCHEMA booksapp_dev_schema     TO booksapp_dev_user;
GRANT USAGE ON SCHEMA booksapp_dev_aud_schema TO booksapp_dev_user;

-- For existing tables
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA booksapp_dev_schema     TO booksapp_dev_user;
GRANT SELECT, INSERT                 ON ALL TABLES IN SCHEMA booksapp_dev_aud_schema TO booksapp_dev_user;

-- For existing sequences
GRANT USAGE, SELECT, UPDATE ON ALL SEQUENCES IN SCHEMA booksapp_dev_schema     TO booksapp_dev_user;
GRANT USAGE, SELECT, UPDATE ON ALL SEQUENCES IN SCHEMA booksapp_dev_aud_schema TO booksapp_dev_user;

-- For future tables and sequences created by booksapp_dev_flyway_user
ALTER DEFAULT PRIVILEGES FOR ROLE booksapp_dev_flyway_user IN SCHEMA booksapp_dev_schema     GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO booksapp_dev_user;
ALTER DEFAULT PRIVILEGES FOR ROLE booksapp_dev_flyway_user IN SCHEMA booksapp_dev_aud_schema GRANT SELECT, INSERT                 ON TABLES TO booksapp_dev_user;

ALTER DEFAULT PRIVILEGES FOR ROLE booksapp_dev_flyway_user IN SCHEMA booksapp_dev_schema     GRANT USAGE, SELECT, UPDATE ON SEQUENCES TO booksapp_dev_user;
ALTER DEFAULT PRIVILEGES FOR ROLE booksapp_dev_flyway_user IN SCHEMA booksapp_dev_aud_schema GRANT USAGE, SELECT, UPDATE ON SEQUENCES TO booksapp_dev_user;

-- ===== Flyway User =====
GRANT USAGE         ON SCHEMA public                 TO booksapp_dev_flyway_user;
GRANT USAGE, CREATE ON SCHEMA booksapp_dev_schema     TO booksapp_dev_flyway_user;
GRANT USAGE, CREATE ON SCHEMA booksapp_dev_aud_schema TO booksapp_dev_flyway_user;

-- For existing tables
GRANT SELECT ON ALL TABLES IN SCHEMA booksapp_dev_schema     TO booksapp_dev_flyway_user;
GRANT SELECT ON ALL TABLES IN SCHEMA booksapp_dev_aud_schema TO booksapp_dev_flyway_user;

-- For existing sequences
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA booksapp_dev_schema     TO booksapp_dev_flyway_user;
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA booksapp_dev_aud_schema TO booksapp_dev_flyway_user;

-- For future tables and sequences created by booksapp_dev_flyway_user
ALTER DEFAULT PRIVILEGES FOR ROLE booksapp_dev_flyway_user IN SCHEMA booksapp_dev_schema     GRANT SELECT ON TABLES TO booksapp_dev_flyway_user;
ALTER DEFAULT PRIVILEGES FOR ROLE booksapp_dev_flyway_user IN SCHEMA booksapp_dev_aud_schema GRANT SELECT ON TABLES TO booksapp_dev_flyway_user;

ALTER DEFAULT PRIVILEGES FOR ROLE booksapp_dev_flyway_user IN SCHEMA booksapp_dev_schema     GRANT USAGE, SELECT ON SEQUENCES TO booksapp_dev_flyway_user;
ALTER DEFAULT PRIVILEGES FOR ROLE booksapp_dev_flyway_user IN SCHEMA booksapp_dev_aud_schema GRANT USAGE, SELECT ON SEQUENCES TO booksapp_dev_flyway_user;

-- Note: DROP, INSERT, UPDATE, DELETE for Flyway can be added later if needed
-- Note: CREATE USER is alias for CREATE ROLE WITH LOGIN
