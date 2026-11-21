-- ====================================================================
-- TIRUPPUR KNITWEAR ERP & MES: DATABASE SCHEMA (01-schema.sql)
-- Complete lifecycle from Yarn Inward to Garment Dispatch
-- ====================================================================

-- Clean up if exists
DROP TABLE IF EXISTS carton_packings CASCADE;
DROP TABLE IF EXISTS sewing_production_log CASCADE;
DROP TABLE IF EXISTS sewing_lines CASCADE;
DROP TABLE IF EXISTS cut_bundles CASCADE;
DROP TABLE IF EXISTS cutting_orders CASCADE;
DROP TABLE IF EXISTS dyeing_batches CASCADE;
DROP TABLE IF EXISTS fabric_rolls CASCADE;
DROP TABLE IF EXISTS knitting_programs CASCADE;
DROP TABLE IF EXISTS yarn_stock CASCADE;
DROP TABLE IF EXISTS production_orders CASCADE;
DROP TABLE IF EXISTS buyers CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- 0. USER & AUTHENTICATION (RBAC)
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(150) NOT NULL,
    role VARCHAR(50) NOT NULL,
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 1. BUYER MASTER & PRODUCTION ORDERS
CREATE TABLE buyers (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    buyer_code VARCHAR(50) UNIQUE NOT NULL,
    country VARCHAR(100) DEFAULT 'India',
    contact_person VARCHAR(100),
    contact_email VARCHAR(150),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE production_orders (
    id BIGSERIAL PRIMARY KEY,
    order_number VARCHAR(100) UNIQUE NOT NULL,   -- e.g., 'PO-2026-TRP-001'
    buyer_id BIGINT REFERENCES buyers(id) ON DELETE SET NULL,
    style_name VARCHAR(150) NOT NULL,            -- e.g., 'Men Bio-Wash Crew T-Shirt'
    target_gsm INT NOT NULL,                     -- e.g., 180
    color_shade VARCHAR(100) NOT NULL,           -- e.g., 'Navy Blue (19-4024 TPX)'
    total_quantity INT NOT NULL,                 -- in pieces (e.g. 10,000 pcs)
    delivery_date DATE NOT NULL,
    status VARCHAR(50) DEFAULT 'PLANNED',        -- PLANNED, IN_PROGRESS, COMPLETED, CANCELLED
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. YARN INVENTORY & INWARD RECEIPT
CREATE TABLE yarn_stock (
    id BIGSERIAL PRIMARY KEY,
    mill_name VARCHAR(150) NOT NULL,             -- e.g., 'KPR Mills', 'Ramco', 'Bannari Amman'
    lot_number VARCHAR(100) NOT NULL,
    yarn_count VARCHAR(50) NOT NULL,             -- e.g., '30s Combed Cotton', '34s Lycra', '24s Melange'
    yarn_type VARCHAR(50) NOT NULL,              -- 100% Cotton, Melange, PC Blend, Modal
    total_bags INT NOT NULL,
    net_weight_kg NUMERIC(12, 2) NOT NULL,       -- Current stock in KG
    rate_per_kg NUMERIC(10, 2) NOT NULL,
    received_date DATE DEFAULT CURRENT_DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3. KNITTING PROGRAM & GREY FABRIC ROLLS
CREATE TABLE knitting_programs (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT REFERENCES production_orders(id) ON DELETE CASCADE,
    program_number VARCHAR(100) UNIQUE NOT NULL, -- e.g., 'KNIT-PRG-001'
    machine_dia INT NOT NULL,                    -- e.g., 30 inch (circular)
    gauge INT NOT NULL,                          -- e.g., 24 GG
    target_gsm INT NOT NULL,                     -- e.g., 180 GSM
    fabric_type VARCHAR(100) NOT NULL,           -- Single Jersey, 2x2 Rib, Pique, 3-Thread Fleece
    loop_length NUMERIC(6, 2) DEFAULT 2.85,      -- in mm
    allocated_yarn_kg NUMERIC(12, 2) NOT NULL,
    status VARCHAR(50) DEFAULT 'IN_KNITTING',    -- PLANNED, IN_KNITTING, COMPLETED
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE fabric_rolls (
    id BIGSERIAL PRIMARY KEY,
    knitting_program_id BIGINT REFERENCES knitting_programs(id) ON DELETE CASCADE,
    roll_number VARCHAR(100) UNIQUE NOT NULL,    -- Barcode / QR ID (e.g. 'ROLL-9A7B31')
    roll_weight_kg NUMERIC(10, 2) NOT NULL,
    actual_gsm INT NOT NULL,
    quality_grade VARCHAR(10) DEFAULT 'A',       -- 'A', 'B', 'REJECT'
    inspected_by VARCHAR(100) DEFAULT 'QC Team',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 4. DYEING & WET PROCESSING (JOB WORK CHALLANS)
CREATE TABLE dyeing_batches (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT REFERENCES production_orders(id) ON DELETE CASCADE,
    batch_number VARCHAR(100) UNIQUE NOT NULL,   -- e.g., 'DYE-BATCH-101'
    dyeing_unit_name VARCHAR(150) NOT NULL,      -- e.g., 'SIPCOT Modern Processors'
    outward_dc_number VARCHAR(100) NOT NULL,     -- Outward Delivery Challan
    sent_weight_kg NUMERIC(12, 2) NOT NULL,
    received_weight_kg NUMERIC(12, 2),          -- Reconciled inward weight
    pantone_shade VARCHAR(100) NOT NULL,
    finish_type VARCHAR(100) DEFAULT 'Bio-Wash Silicon Finish',
    status VARCHAR(50) DEFAULT 'SENT_TO_DYEING', -- SENT_TO_DYEING, IN_PROCESS, RECEIVED, QA_APPROVED
    sent_date DATE DEFAULT CURRENT_DATE,
    received_date DATE
);

-- 5. CUTTING SECTION & BUNDLE TICKETING
CREATE TABLE cutting_orders (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT REFERENCES production_orders(id) ON DELETE CASCADE,
    lay_number VARCHAR(100) NOT NULL,            -- e.g., 'LAY-01-NAVY'
    total_plies INT NOT NULL,                    -- Fabric layers spread
    total_cut_pieces INT NOT NULL,
    fabric_consumed_kg NUMERIC(12, 2) NOT NULL,
    cutting_wastage_kg NUMERIC(10, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE cut_bundles (
    id BIGSERIAL PRIMARY KEY,
    cutting_order_id BIGINT REFERENCES cutting_orders(id) ON DELETE CASCADE,
    bundle_code VARCHAR(100) UNIQUE NOT NULL,    -- QR Barcode on ticket (e.g. 'BNDL-S-001')
    size VARCHAR(20) NOT NULL,                   -- S, M, L, XL, XXL
    quantity INT NOT NULL,                       -- pieces per bundle (e.g. 20 pcs)
    current_stage VARCHAR(50) DEFAULT 'CUT',     -- CUT, PRINTING, EMBROIDERY, SEWING, FINISHED
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 6. SEWING ASSEMBLY LINES & PRODUCTION LOGS
CREATE TABLE sewing_lines (
    id BIGSERIAL PRIMARY KEY,
    line_name VARCHAR(50) UNIQUE NOT NULL,       -- e.g., 'Line 1 (Overlock & Flatlock)', 'Line 2'
    supervisor_name VARCHAR(100) NOT NULL,
    hourly_target_pieces INT DEFAULT 120
);

CREATE TABLE sewing_production_log (
    id BIGSERIAL PRIMARY KEY,
    line_id BIGINT REFERENCES sewing_lines(id) ON DELETE CASCADE,
    bundle_id BIGINT REFERENCES cut_bundles(id) ON DELETE CASCADE,
    operator_name VARCHAR(100) NOT NULL,
    passed_pieces INT NOT NULL,
    reworked_pieces INT DEFAULT 0,
    rejected_pieces INT DEFAULT 0,
    logged_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 7. CARTON PACKING & DISPATCH
CREATE TABLE carton_packings (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT REFERENCES production_orders(id) ON DELETE CASCADE,
    carton_number VARCHAR(100) UNIQUE NOT NULL,  -- e.g., 'CTN-2026-001'
    packing_type VARCHAR(50) NOT NULL,           -- 'SOLID_SIZE', 'ASSORTED'
    size_breakdown VARCHAR(200) NOT NULL,        -- e.g., 'S:10, M:20, L:20, XL:10'
    total_garments INT NOT NULL,                 -- total pcs in box
    gross_weight_kg NUMERIC(10, 2) NOT NULL,
    cbm NUMERIC(8, 4) NOT NULL,                  -- Cubic Meters for shipping
    is_dispatched BOOLEAN DEFAULT FALSE,
    dispatched_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Performance Indexes for ERP Queries
CREATE INDEX idx_production_orders_status ON production_orders(status);
CREATE INDEX idx_yarn_stock_count ON yarn_stock(yarn_count);
CREATE INDEX idx_fabric_rolls_roll_num ON fabric_rolls(roll_number);
CREATE INDEX idx_cut_bundles_code ON cut_bundles(bundle_code);
CREATE INDEX idx_cut_bundles_stage ON cut_bundles(current_stage);
CREATE INDEX idx_sewing_log_line ON sewing_production_log(line_id);
