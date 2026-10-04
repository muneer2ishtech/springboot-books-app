CREATE TABLE IF NOT EXISTS booksapp_dev_schema.t_dummy (
  id          BIGSERIAL     PRIMARY KEY,
  is_active   BOOLEAN       NOT NULL DEFAULT true,
  description TEXT              NULL
);

DELETE FROM booksapp_dev_schema.t_dummy;

INSERT INTO booksapp_dev_schema.t_dummy(is_active, description) VALUES
(true, 'Test row 1'),
(true, 'Test row 2'),
(true, 'Test row 3');

GRANT SELECT, DELETE ON TABLE booksapp_dev_schema.t_dummy TO booksapp_dev_user;
