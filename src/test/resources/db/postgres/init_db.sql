\connect ishtech_dev_db;

-- ===== Common =====
CREATE USER bookapp_dev_user        PASSWORD 'bookapp_dev_pass'        NOSUPERUSER;
CREATE USER bookapp_dev_flyway_user PASSWORD 'bookapp_dev_flyway_pass' NOSUPERUSER;

GRANT CONNECT ON DATABASE ishtech_dev_db TO bookapp_dev_user;
GRANT CONNECT ON DATABASE ishtech_dev_db TO bookapp_dev_flyway_user;

CREATE SCHEMA bookapp_dev_schema;
CREATE SCHEMA bookapp_dev_aud_schema;

-- ===== App User =====
GRANT USAGE ON SCHEMA public                 TO bookapp_dev_user;
GRANT USAGE ON SCHEMA bookapp_dev_schema     TO bookapp_dev_user;
GRANT USAGE ON SCHEMA bookapp_dev_aud_schema TO bookapp_dev_user;

-- For existing tables
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA bookapp_dev_schema     TO bookapp_dev_user;
GRANT SELECT, INSERT                 ON ALL TABLES IN SCHEMA bookapp_dev_aud_schema TO bookapp_dev_user;

-- For existing sequences
GRANT USAGE, SELECT, UPDATE ON ALL SEQUENCES IN SCHEMA bookapp_dev_schema     TO bookapp_dev_user;
GRANT USAGE, SELECT, UPDATE ON ALL SEQUENCES IN SCHEMA bookapp_dev_aud_schema TO bookapp_dev_user;

-- For future tables and sequences created by bookapp_dev_flyway_user
ALTER DEFAULT PRIVILEGES FOR ROLE bookapp_dev_flyway_user IN SCHEMA bookapp_dev_schema     GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO bookapp_dev_user;
ALTER DEFAULT PRIVILEGES FOR ROLE bookapp_dev_flyway_user IN SCHEMA bookapp_dev_aud_schema GRANT SELECT, INSERT                 ON TABLES TO bookapp_dev_user;

ALTER DEFAULT PRIVILEGES FOR ROLE bookapp_dev_flyway_user IN SCHEMA bookapp_dev_schema     GRANT USAGE, SELECT, UPDATE ON SEQUENCES TO bookapp_dev_user;
ALTER DEFAULT PRIVILEGES FOR ROLE bookapp_dev_flyway_user IN SCHEMA bookapp_dev_aud_schema GRANT USAGE, SELECT, UPDATE ON SEQUENCES TO bookapp_dev_user;

-- ===== Flyway User =====
GRANT USAGE         ON SCHEMA public                 TO bookapp_dev_flyway_user;
GRANT USAGE, CREATE ON SCHEMA bookapp_dev_schema     TO bookapp_dev_flyway_user;
GRANT USAGE, CREATE ON SCHEMA bookapp_dev_aud_schema TO bookapp_dev_flyway_user;

-- For existing tables
GRANT SELECT ON ALL TABLES IN SCHEMA bookapp_dev_schema     TO bookapp_dev_flyway_user;
GRANT SELECT ON ALL TABLES IN SCHEMA bookapp_dev_aud_schema TO bookapp_dev_flyway_user;

-- For existing sequences
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA bookapp_dev_schema     TO bookapp_dev_flyway_user;
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA bookapp_dev_aud_schema TO bookapp_dev_flyway_user;

-- For future tables and sequences created by bookapp_dev_flyway_user
ALTER DEFAULT PRIVILEGES FOR ROLE bookapp_dev_flyway_user IN SCHEMA bookapp_dev_schema     GRANT SELECT ON TABLES TO bookapp_dev_flyway_user;
ALTER DEFAULT PRIVILEGES FOR ROLE bookapp_dev_flyway_user IN SCHEMA bookapp_dev_aud_schema GRANT SELECT ON TABLES TO bookapp_dev_flyway_user;

ALTER DEFAULT PRIVILEGES FOR ROLE bookapp_dev_flyway_user IN SCHEMA bookapp_dev_schema     GRANT USAGE, SELECT ON SEQUENCES TO bookapp_dev_flyway_user;
ALTER DEFAULT PRIVILEGES FOR ROLE bookapp_dev_flyway_user IN SCHEMA bookapp_dev_aud_schema GRANT USAGE, SELECT ON SEQUENCES TO bookapp_dev_flyway_user;

-- Note: DROP, INSERT, UPDATE, DELETE for Flyway can be added later if needed
-- Note: CREATE USER is alias for CREATE ROLE WITH LOGIN
