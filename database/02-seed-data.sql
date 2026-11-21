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

-- 1. Master Data: Unit of Measure (UOM)
INSERT INTO uom_master (uom_code, uom_name, category, base_uom, conversion_factor, decimal_places, active) VALUES
('KG', 'Kilogram', 'WEIGHT', true, 1.000000, 2, true),
('BAG', 'Yarn Bag (100 lbs)', 'WEIGHT', false, 45.360000, 2, true),
('BAG_50', 'Yarn Bag (50 kg)', 'WEIGHT', false, 50.000000, 2, true),
('LBS', 'Pound (lb)', 'WEIGHT', false, 0.453592, 3, true),
('PCS', 'Pieces', 'COUNT', true, 1.000000, 0, true),
('DOZ', 'Dozen (12 pcs)', 'COUNT', false, 12.000000, 1, true),
('ROLL', 'Fabric Roll (~25 kg)', 'PACKAGING', false, 25.000000, 2, true),
('CONE', 'Yarn Cone (~1.89 kg)', 'PACKAGING', false, 1.890000, 2, true),
('CTN', 'Carton Box', 'PACKAGING', true, 1.000000, 0, true),
('MTR', 'Meter', 'LENGTH', true, 1.000000, 2, true),
('YDS', 'Yard', 'LENGTH', false, 0.914400, 2, true);

-- 1A. Master Data: Manufacturing Company & Plant Divisions
INSERT INTO company_master (company_name, company_code, unit_or_division, gstin, pan, iec_code, registered_address, contact_person, contact_email, contact_phone, bank_name, bank_account_number, bank_ifsc, is_primary, active) VALUES
('Tiruppur TexFlow Garments Pvt Ltd', 'TTG-CORP-01', 'Unit 1 - Headquarters & Integrated Garment Plant', '33AAACT1234F1Z5', 'AAACT1234F', '0498765432', 'SF No. 124/2, Kangeyam Main Road, Nallur, Tiruppur, Tamil Nadu 641604', 'Ramasamy', 'contact@tiruppurtextiles.com', '+91 421 223 4567', 'State Bank of India, Overseas Branch, Tiruppur', '30123456789', 'SBIN0001234', true, true),
('TexFlow Processing & Dyeing Division', 'TTG-DYE-02', 'Unit 2 - CETP Zero-Liquid Discharge Dyeing House', '33AAACT1234F2Z4', 'AAACT1234F', '0498765432', 'SIPCOT Industrial Park, Perundurai, Erode / Tiruppur Border', 'K. Palanisamy', 'dyeing@tiruppurtextiles.com', '+91 421 245 8900', 'Canara Bank, SME Branch, Tiruppur', '12345678901234', 'CNRB0005678', false, true);

-- 1B. Master Data: Products / Garment Styles
INSERT INTO product_master (product_code, style_name, category, fabric_type, yarn_count, target_gsm, neck_type, sleeve_type, standard_smv, available_sizes, hsn_code, active) VALUES
('STY-CREW-180', 'Men Bio-Wash Crew Neck T-Shirt', 'MEN_ROUND_NECK', 'Single Jersey', '30s Combed Cotton', 180, 'Round Neck (1x1 Cotton Rib)', 'Half Sleeve', 12.50, 'S, M, L, XL, 2XL', '61091000', true),
('STY-POLO-220', 'Men Pique Polo Shirt with Flat Knit Collar', 'MEN_POLO', 'Pique / Honeycomb', '30s Combed Cotton', 220, 'Flat Knit Rib Collar with 2-Button Box Placket', 'Half Sleeve with Rib Cuff', 22.00, 'M, L, XL, 2XL, 3XL', '61051000', true),
('STY-HOOD-320', 'Unisex Heavyweight Kangaroo Pocket Hoodie', 'HOODIE', '3-Thread Fleece', '24s Melange Cotton', 320, 'Drawstring Hood with Metal Eyelets', 'Full Sleeve with 2x2 Spun Rib', 34.00, 'XS, S, M, L, XL, 2XL', '61102000', true),
('STY-JOG-260', 'Athletic Slim Fit French Terry Joggers', 'JOGGERS', 'French Terry', '30s Combed Cotton', 260, 'Elastic Rib Waistband with Drawcord', 'Ribbed Ankle Hem', 24.50, 'S, M, L, XL, 2XL', '61034200', true);

-- 2. Master Data: Customers / Buyers
INSERT INTO buyers (name, buyer_code, brand_names, country, currency, payment_terms, tax_id_gstin, contact_person, contact_email, contact_phone, billing_address, shipping_port, tolerance_percentage, active) VALUES
('Zara Sourcing Europe (Inditex Group)', 'BUY-ZARA-01', 'Zara Man, Zara Kids, TRF', 'Spain', 'EUR', 'LC at 60 Days from BL Date', 'ESB15075062', 'Carlos Mendez', 'carlos@inditex.com', '+34 981 185 400', 'Avenida de la Diputacion, Edificio Inditex, 15142 Arteixo, Spain', 'Tuticorin Port (VOCPT) -> Port of Valencia', 3.00, true),
('Decathlon Sports India Pvt Ltd', 'BUY-DECATH-02', 'Domyos, Kalenji, Quechua', 'France', 'INR', 'Net 30 Days from Invoice', '29AABCD1234F1Z5', 'Anand Kumar', 'anand.k@decathlon.in', '+91 80 4646 7000', 'Survey No 78/10, Anekal Taluk, Bengaluru 562106', 'Chennai Port / Domestic Hub', 5.00, true),
('Myntra Designs Private Limited', 'BUY-MYNTRA-03', 'Roadster, HRX, Mast & Harbour', 'India', 'INR', 'Net 45 Days', '29AAACM3210G1ZU', 'Priya Sharma', 'priya.s@myntra.com', '+91 80 4354 0000', 'Embassy Tech Village, Outer Ring Road, Bengaluru 560103', 'Tiruppur Hub Dispatch', 5.00, true);

-- 3. Master Data: Pricing Rate Cards (Job-work & Material Pricing)
INSERT INTO pricing_master (price_code, process_or_item_type, category_or_spec, uom_code, base_rate, currency, effective_from, notes, active) VALUES
('RATE-KNIT-SJ-24', 'KNITTING_JOBWORK', 'Single Jersey 24GG', 'KG', 18.00, 'INR', '2026-01-01', 'Standard 180 GSM circular knitting', true),
('RATE-KNIT-RIB-18', 'KNITTING_JOBWORK', '1x1 Rib 18GG', 'KG', 22.00, 'INR', '2026-01-01', 'Neck rib & cuff knit', true),
('RATE-KNIT-INT-28', 'KNITTING_JOBWORK', 'Interlock 28GG', 'KG', 25.00, 'INR', '2026-01-01', 'Heavy interlock double jersey', true),
('RATE-DYE-LIGHT', 'DYEING_JOBWORK', 'Light Shade (Pastel / Off-White)', 'KG', 55.00, 'INR', '2026-01-01', 'Low dye concentration', true),
('RATE-DYE-MED', 'DYEING_JOBWORK', 'Medium Shade (Sky Blue / Coral)', 'KG', 75.00, 'INR', '2026-01-01', 'Medium depth shade', true),
('RATE-DYE-DARK', 'DYEING_JOBWORK', 'Dark Shade (Navy / Olive / Maroon)', 'KG', 95.00, 'INR', '2026-01-01', 'Standard dark shade reactive dyeing', true),
('RATE-DYE-BLACK', 'DYEING_JOBWORK', 'Jet Black (Reactive)', 'KG', 110.00, 'INR', '2026-01-01', 'Extra depth reactive black with wash-off', true),
('RATE-BIO-POLISH', 'FINISHING', 'Enzyme Bio-Polishing', 'KG', 16.00, 'INR', '2026-01-01', 'Cellulase enzyme treatment for zero fuzz', true),
('RATE-SEW-CREW', 'SEWING_CM', 'Basic Crew Neck T-Shirt', 'PCS', 18.50, 'INR', '2026-01-01', 'Standard 4-thread overlock + flatlock hem', true),
('RATE-SEW-POLO', 'SEWING_CM', 'Pique Polo Tee with Placket', 'PCS', 34.00, 'INR', '2026-01-01', 'Rib collar, box placket, 2-button attachment', true),
('RATE-YARN-30SC', 'YARN_PURCHASE', '30s Combed Cotton Cone Yarn', 'KG', 285.00, 'INR', '2026-01-01', 'KPR / Ramco Mill standard 30s combed', true),
('RATE-YARN-34SL', 'YARN_PURCHASE', '34s Lycra Spun Yarn (95/5)', 'KG', 340.00, 'INR', '2026-01-01', 'Core spun Lycra for stretch tees', true);

-- 4. Production Orders
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
