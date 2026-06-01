CREATE TABLE IF NOT EXISTS app_sequences (
  sequence_name VARCHAR(80) PRIMARY KEY,
  next_value INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS app_metadata (
  metadata_key VARCHAR(80) PRIMARY KEY,
  metadata_value TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS merchandise_catalog (
  merchandise_code VARCHAR(40) PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS units (
  unit_code VARCHAR(20) PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS import_requests (
  request_id VARCHAR(30) PRIMARY KEY,
  status VARCHAR(30) NOT NULL,
  created_at TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS import_request_items (
  request_id VARCHAR(30) NOT NULL REFERENCES import_requests(request_id) ON DELETE CASCADE,
  line_no INTEGER NOT NULL,
  merchandise_code VARCHAR(40) NOT NULL,
  quantity_ordered INTEGER NOT NULL,
  unit VARCHAR(20) NOT NULL,
  desired_delivery_date DATE NOT NULL,
  PRIMARY KEY (request_id, line_no)
);

CREATE TABLE IF NOT EXISTS import_sites (
  site_code VARCHAR(40) PRIMARY KEY,
  site_name VARCHAR(160) NOT NULL,
  delivery_days_by_ship INTEGER NOT NULL,
  delivery_days_by_air INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS site_catalog (
  site_code VARCHAR(40) NOT NULL REFERENCES import_sites(site_code) ON DELETE CASCADE,
  merchandise_code VARCHAR(40) NOT NULL,
  PRIMARY KEY (site_code, merchandise_code)
);

CREATE TABLE IF NOT EXISTS inventory_records (
  site_code VARCHAR(40) NOT NULL REFERENCES import_sites(site_code) ON DELETE CASCADE,
  merchandise_code VARCHAR(40) NOT NULL,
  in_stock_quantity INTEGER NOT NULL,
  unit VARCHAR(20) NOT NULL,
  PRIMARY KEY (site_code, merchandise_code)
);

CREATE TABLE IF NOT EXISTS allocation_plans (
  plan_id VARCHAR(30) PRIMARY KEY,
  request_id VARCHAR(30) NOT NULL REFERENCES import_requests(request_id)
);

CREATE TABLE IF NOT EXISTS allocation_lines (
  plan_id VARCHAR(30) NOT NULL REFERENCES allocation_plans(plan_id) ON DELETE CASCADE,
  line_no INTEGER NOT NULL,
  request_id VARCHAR(30) NOT NULL,
  merchandise_code VARCHAR(40) NOT NULL,
  site_code VARCHAR(40) NOT NULL,
  quantity_ordered INTEGER NOT NULL,
  unit VARCHAR(20) NOT NULL,
  delivery_means VARCHAR(20) NOT NULL,
  PRIMARY KEY (plan_id, line_no)
);

CREATE TABLE IF NOT EXISTS overseas_orders (
  order_id VARCHAR(30) PRIMARY KEY,
  site_code VARCHAR(40) NOT NULL,
  status VARCHAR(30) NOT NULL,
  acknowledgement_token TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS order_lines (
  order_id VARCHAR(30) NOT NULL REFERENCES overseas_orders(order_id) ON DELETE CASCADE,
  line_no INTEGER NOT NULL,
  merchandise_code VARCHAR(40) NOT NULL,
  quantity_ordered INTEGER NOT NULL,
  unit VARCHAR(20) NOT NULL,
  delivery_means VARCHAR(20) NOT NULL,
  PRIMARY KEY (order_id, line_no)
);

CREATE TABLE IF NOT EXISTS goods_receipts (
  receipt_id VARCHAR(30) PRIMARY KEY,
  order_reference VARCHAR(30) NOT NULL REFERENCES overseas_orders(order_id),
  received_at TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS receipt_lines (
  receipt_id VARCHAR(30) NOT NULL REFERENCES goods_receipts(receipt_id) ON DELETE CASCADE,
  line_no INTEGER NOT NULL,
  merchandise_code VARCHAR(40) NOT NULL,
  ordered_quantity INTEGER NOT NULL,
  received_quantity INTEGER NOT NULL,
  discrepancy_quantity INTEGER NOT NULL,
  discrepancy_type VARCHAR(30) NOT NULL,
  discrepancy_note TEXT NOT NULL,
  PRIMARY KEY (receipt_id, line_no)
);

CREATE TABLE IF NOT EXISTS warehouse_stock (
  merchandise_code VARCHAR(40) PRIMARY KEY,
  quantity_in_stock INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS user_accounts (
  user_id VARCHAR(30) PRIMARY KEY,
  username VARCHAR(80) NOT NULL UNIQUE,
  email VARCHAR(160) NOT NULL,
  status VARCHAR(30) NOT NULL,
  actor_roles TEXT NOT NULL,
  password_hash TEXT NOT NULL,
  password_salt TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS operation_logs (
  log_id VARCHAR(30) PRIMARY KEY,
  operator_id VARCHAR(80) NOT NULL,
  action_type VARCHAR(80) NOT NULL,
  timestamp TIMESTAMP NOT NULL,
  details TEXT NOT NULL
);

INSERT INTO merchandise_catalog (merchandise_code) VALUES
  ('MH-001'), ('MH-002'), ('MH-003'), ('MH-004'), ('MH-005'), ('MH-014')
ON CONFLICT DO NOTHING;

INSERT INTO units (unit_code) VALUES ('box'), ('piece'), ('kg')
ON CONFLICT DO NOTHING;

INSERT INTO import_sites (site_code, site_name, delivery_days_by_ship, delivery_days_by_air) VALUES
  ('SITE-SEA-01', 'Singapore Sea Site', 18, 5),
  ('SITE-EU-04', 'Europe Import Site', 28, 7),
  ('SITE-AIR-02', 'Air Express Site', 35, 4)
ON CONFLICT DO NOTHING;

INSERT INTO site_catalog (site_code, merchandise_code) VALUES
  ('SITE-SEA-01', 'MH-001'), ('SITE-SEA-01', 'MH-002'), ('SITE-SEA-01', 'MH-005'),
  ('SITE-EU-04', 'MH-001'), ('SITE-EU-04', 'MH-003'), ('SITE-EU-04', 'MH-005'), ('SITE-EU-04', 'MH-014'),
  ('SITE-AIR-02', 'MH-002'), ('SITE-AIR-02', 'MH-004'), ('SITE-AIR-02', 'MH-014')
ON CONFLICT DO NOTHING;

INSERT INTO app_sequences (sequence_name, next_value) VALUES
  ('import_requests', 1),
  ('allocation_plans', 1),
  ('overseas_orders', 1),
  ('goods_receipts', 1),
  ('user_accounts', 1),
  ('operation_logs', 1)
ON CONFLICT DO NOTHING;
