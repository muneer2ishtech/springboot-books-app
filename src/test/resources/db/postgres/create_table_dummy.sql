CREATE TABLE bookapp_dev_schema.t_dummy (
  id          BIGSERIAL     PRIMARY KEY,
  is_active   BOOLEAN       NOT NULL DEFAULT true,
  description TEXT              NULL
);
