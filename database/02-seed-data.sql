-- ====================================================================
-- SEED DATA: Realistic Tiruppur Knitwear Operations
-- ====================================================================

-- 0. Plant Personnel & Roles (Passwords: admin123, super123, qc123, yarn123, dispatch123)
INSERT INTO users (username, password, full_name, role, active) VALUES
('admin', '$2a$10$8MjTPtR2BpPweOOc0MuKXenBKBxomS0mwVNDgokYq9jpaWtyt/S3O', 'Ramasamy (Managing Director)', 'ROLE_ADMIN', true),
('supervisor', '$2a$10$3Vw7MKn43pnb5aiWG7OciOLiwgaxbWTthho6qvXx3AudWhyCvNiAK', 'Muruganandam (Knitting & Sewing Head)', 'ROLE_KNITTING_SUPERVISOR', true),
('qc_lead', '$2a$10$ZeAE5HK89RGLu6FGjI8Qt.yz3eUlKDBBd6uiE6cMhHFQXGiWg1NiO', 'Senthil Kumar (Quality Head)', 'ROLE_QC_INSPECTOR', true),
('yarn_mgr', '$2a$10$sJLlMr2xNHvAt4aZx8B9O.QynOyLMlst/k95uQBy1oQrEMQ5O/jWm', 'Palanisamy (Yarn Procurement Head)', 'ROLE_YARN_MANAGER', true),
('dispatch_officer', '$2a$10$bol.LvJ2VNJTZ.28.B2zE.3LthDnJGHFJxvBNz36sj.DBwJTzsWJi', 'Karthik (Logistics & Shipping Officer)', 'ROLE_DISPATCH_OFFICER', true);

-- 1. Buyers
INSERT INTO buyers (name, buyer_code, country, contact_person, contact_email) VALUES
('Zara Sourcing Europe', 'BUYER-ZARA-01', 'Spain', 'Carlos Mendez', 'carlos@inditex.com'),
('Decathlon Sports India', 'BUYER-DECATH-02', 'France', 'Anand Kumar', 'anand.k@decathlon.in'),
('Myntra Fashion Brands', 'BUYER-MYNTRA-03', 'India', 'Priya Sharma', 'priya.s@myntra.com');

-- 2. Production Orders
INSERT INTO production_orders (order_number, buyer_id, style_name, target_gsm, color_shade, total_quantity, delivery_date, status) VALUES
('PO-2026-TRP-001', 1, 'Men Bio-Wash Crew Neck T-Shirt', 180, 'Navy Blue (19-4024 TPX)', 10000, '2026-12-15', 'IN_PROGRESS'),
('PO-2026-TRP-002', 2, 'Dry-Fit Performance Gym Tee', 160, 'Anthracite Grey (19-3908 TPX)', 5000, '2026-12-28', 'PLANNED'),
('PO-2026-TRP-003', 3, 'Oversized Streetwear Heavy Tee', 240, 'Sage Green (16-0421 TPX)', 4000, '2027-01-10', 'PLANNED');

-- 3. Yarn Stock (Tiruppur Mills)
INSERT INTO yarn_stock (mill_name, lot_number, yarn_count, yarn_type, total_bags, net_weight_kg, rate_per_kg, received_date) VALUES
('KPR Mill Ltd, Coimbatore', 'LOT-KPR-30SC-892', '30s Combed Cotton', '100% Cotton', 50, 2500.00, 285.00, '2026-11-01'),
('Ramco Industries, Rajapalayam', 'LOT-RMC-34SL-114', '34s Lycra Spun', '95% Cotton 5% Spandex', 30, 1500.00, 340.00, '2026-11-03'),
('Bannari Amman Spinning Mills', 'LOT-BASM-24SM-042', '24s Melange Cotton', 'Melange', 40, 2000.00, 310.00, '2026-11-05');

-- 4. Knitting Programs
INSERT INTO knitting_programs (order_id, program_number, machine_dia, gauge, target_gsm, fabric_type, loop_length, allocated_yarn_kg, status) VALUES
(1, 'KNIT-PO001-01', 30, 24, 180, 'Single Jersey', 2.85, 2450.00, 'COMPLETED');

-- 5. Fabric Rolls produced from Knitting
INSERT INTO fabric_rolls (knitting_program_id, roll_number, roll_weight_kg, actual_gsm, quality_grade, inspected_by) VALUES
(1, 'ROLL-TRP-001', 24.50, 182, 'A', 'Senthil Kumar (QC)'),
(1, 'ROLL-TRP-002', 25.10, 180, 'A', 'Senthil Kumar (QC)'),
(1, 'ROLL-TRP-003', 24.80, 179, 'A', 'Senthil Kumar (QC)'),
(1, 'ROLL-TRP-004', 23.90, 184, 'B', 'Senthil Kumar (QC)');

-- 6. Dyeing & Processing Batch (Job Work to Perundurai/SIPCOT)
INSERT INTO dyeing_batches (order_id, batch_number, dyeing_unit_name, outward_dc_number, sent_weight_kg, received_weight_kg, pantone_shade, finish_type, status, sent_date, received_date) VALUES
(1, 'DYE-BATCH-2026-88', 'SIPCOT Eco-Friendly Processing, Perundurai', 'DC-OUT-9921', 2400.00, 2330.00, 'Navy Blue (19-4024 TPX)', 'Bio-Polish & Silicon Softener', 'RECEIVED', '2026-11-08', '2026-11-12');

-- 7. Cutting Lay & Cut Bundles
INSERT INTO cutting_orders (order_id, lay_number, total_plies, total_cut_pieces, fabric_consumed_kg, cutting_wastage_kg) VALUES
(1, 'LAY-01-NAVY-MENS', 120, 10000, 2250.00, 80.00);

INSERT INTO cut_bundles (cutting_order_id, bundle_code, size, quantity, current_stage) VALUES
(1, 'BNDL-PO001-S-01', 'S', 25, 'SEWING'),
(1, 'BNDL-PO001-M-01', 'M', 25, 'SEWING'),
(1, 'BNDL-PO001-L-01', 'L', 25, 'PRINTING'),
(1, 'BNDL-PO001-XL-01', 'XL', 25, 'CUT');

-- 8. Sewing Lines & Logs
INSERT INTO sewing_lines (line_name, supervisor_name, hourly_target_pieces) VALUES
('Line 1 - Overlock/Flatlock Assembly', 'Muruganandam (Supervisor)', 120),
('Line 2 - Polo Collar & Placket Special', 'Velusamy (Supervisor)', 90);

INSERT INTO sewing_production_log (line_id, bundle_id, operator_name, passed_pieces, reworked_pieces, rejected_pieces) VALUES
(1, 1, 'Kavitha R', 24, 1, 0),
(1, 2, 'Revathi M', 25, 0, 0);

-- 9. Carton Packaging
INSERT INTO carton_packings (order_id, carton_number, packing_type, size_breakdown, total_garments, gross_weight_kg, cbm, is_dispatched) VALUES
(1, 'CTN-PO001-001', 'ASSORTED', 'S:10, M:20, L:20, XL:10', 60, 14.50, 0.0850, false),
(1, 'CTN-PO001-002', 'ASSORTED', 'S:10, M:20, L:20, XL:10', 60, 14.60, 0.0850, false);
