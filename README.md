# Yarn Manager (TexFlow Tiruppur ERP & MES)

> **Enterprise Manufacturing Execution System & ERP for the Tiruppur Knitwear & Garment Export Cluster**  
> Complete industrial lifecycle tracking converting raw spun yarn cones into finished packaged clothes, equipped with dynamic Master Data setup, JWT role-based security, collapsible responsive UI, and light/dark theme design system.

---

## 🏭 System Architecture & Process Diagrams

### 1. Detailed Industrial Manufacturing Flow (Yarn-to-Garment)

```mermaid
flowchart TD
    subgraph S1["Stage 1: Yarn Procurement & Inward"]
        Y1["Spinning Mill Inward<br/>(KPR, Ramco, Bannari)"] --> Y2["Yarn QC & Lot Registration<br/>(Counts: 30s, 34s, Lycra, Melange)"]
        Y2 --> Y3[("Yarn Warehouse Stock<br/>Bags & Net Weight kg")]
    end

    subgraph S2["Stage 2: Circular Knitting"]
        Y3 --> K1["Knitting Program Allocation<br/>(Dia 30 inch, 24 GG, Loop 2.85mm)"]
        K1 --> K2["Circular Knitting Machines<br/>(Single Jersey / Rib / Fleece)"]
        K2 --> K3["Grey Fabric Rolls Weighed & Barcoded<br/>(Actual GSM & Grade A/B)"]
        K3 --> K4{"Knitting Loss Check<br/>(Tolerance <= 2%)"}
    end

    subgraph S3["Stage 3: Dyeing & Wet Processing (Job Work)"]
        K4 --> D1["Outward Delivery Challan DC<br/>Sent to SIPCOT / Perundurai"]
        D1 --> D2["Dyeing & Compaction<br/>(Bio-Wash Silicon Finish)"]
        D2 --> D3["Inward DC Receipt & Reconcile<br/>(Net Weight & Shade Approval)"]
        D3 --> D4{"Process Loss Check<br/>(Tolerance <= 3.5%)"}
    end

    subgraph S4["Stage 4: Lay Planning & Cutting Section"]
        D4 --> C1["Fabric Relaxation & Spreading<br/>(Plies Count & Lay Length)"]
        C1 --> C2["Panel Cutting<br/>(Front, Back, Sleeve, Rib)"]
        C2 --> C3["QR Bundle Ticketing<br/>(Size: S, M, L, XL, XXL)"]
    end

    subgraph S5["Stage 5: Value Addition (Optional Job Work)"]
        C3 --> V1["Screen Printing / Embroidery<br/>(Outward & Inward DC)"]
    end

    subgraph S6["Stage 6: Sewing Assembly Lines"]
        V1 --> W1["Line Loading & Bundle Scan<br/>(Line 1 Overlock / Line 2 Flatlock)"]
        W1 --> W2["Assembly Operations<br/>(Collar attach, Sleeve, Hem)"]
        W2 --> W3["Inline & End-line QA<br/>(Passed, Rework, Rejected)"]
        W3 --> W4{"DHU Defect Rate<br/>(Target < 2.5%)"}
    end

    subgraph S7["Stage 7: Finishing, Packing & Dispatch"]
        W4 --> P1["Thread Trimming & Steam Ironing"]
        P1 --> P2["Carton Packing Matrix<br/>(Solid / Assorted Ratio)"]
        P2 --> P3["Carton CBM & Gross Wt<br/>(Export Barcode Labeled)"]
        P3 --> P4["Shipment & Dispatch Invoice<br/>(Buyer PO Completed)"]
    end
```

---

### 2. Configurable Dynamic Workflow Architecture

Rather than hardcoding customer pipeline stages, TexFlow supports configurable workflow state machines:

```mermaid
flowchart LR
    MD["Mermaid / JSON<br/>Workflow Blueprint"] --> PARSER["Workflow Definition Engine"]
    PARSER --> REGISTRY["Pipeline Stage Registry"]
    REGISTRY --> TRANS["Transition Validator & Rules"]
    TRANS --> EXEC["Stage Execution & Dispatch"]

    subgraph WORKFLOW_STAGES["Dynamic Pipeline Stage Transitions"]
        direction LR
        ST_YARN["1. Yarn Inward"] --> ST_KNIT["2. Knitting"]
        ST_KNIT --> ST_DYE["3. Dyeing DC"]
        ST_DYE --> ST_CUT["4. Lay Cutting"]
        ST_CUT -.->|"Optional"| ST_PRINT["Job Work Print"]
        ST_PRINT -.-> ST_SEW["5. Sewing Line"]
        ST_CUT --> ST_SEW
        ST_SEW --> ST_PACK["6. Carton Dispatch"]
    end
    EXEC --> WORKFLOW_STAGES
```

---

### 3. Relational Database Schema (PostgreSQL 18 ERD)

```mermaid
erDiagram
    COMPANIES ||--o{ PRODUCTION_ORDERS : operates
    BUYERS ||--o{ PRODUCTION_ORDERS : places
    PRODUCTS ||--o{ PRODUCTION_ORDERS : specifies
    UOM_MASTER ||--o{ YARN_STOCK : measures
    PRICING_MASTER ||--o{ DYEING_BATCHES : rates
    PRODUCTION_ORDERS ||--o{ KNITTING_PROGRAMS : initiates
    KNITTING_PROGRAMS ||--o{ FABRIC_ROLLS : produces
    PRODUCTION_ORDERS ||--o{ DYEING_BATCHES : routes_to
    PRODUCTION_ORDERS ||--o{ CUTTING_ORDERS : schedules
    CUTTING_ORDERS ||--o{ CUT_BUNDLES : creates
    SEWING_LINES ||--o{ SEWING_PRODUCTION_LOG : executes
    CUT_BUNDLES ||--o{ SEWING_PRODUCTION_LOG : tracked_in
    PRODUCTION_ORDERS ||--o{ CARTON_PACKINGS : packed_into

    COMPANIES {
        bigint id PK
        varchar company_code UK
        varchar name
        varchar plant_location
        varchar gstin
        varchar iec_code
        varchar currency
        boolean active
    }

    PRODUCTS {
        bigint id PK
        varchar style_number UK
        varchar style_name
        varchar category
        varchar fabric_type
        int standard_gsm
        varchar size_range
        boolean active
    }

    UOM_MASTER {
        bigint id PK
        varchar uom_code UK
        varchar uom_name
        varchar uom_category
        numeric conversion_factor_to_base
        boolean active
    }

    PRICING_MASTER {
        bigint id PK
        varchar process_or_item_type
        varchar category
        numeric base_rate
        varchar currency
        boolean active
    }

    BUYERS {
        bigint id PK
        varchar buyer_code UK
        varchar name
        varchar country
        varchar contact_person
        varchar contact_email
        boolean active
        timestamp created_at
    }

    PRODUCTION_ORDERS {
        bigint id PK
        varchar order_number UK
        bigint buyer_id FK
        varchar style_name
        int target_gsm
        varchar color_shade
        int total_quantity
        date delivery_date
        varchar status
        timestamp created_at
    }

    YARN_STOCK {
        bigint id PK
        varchar mill_name
        varchar lot_number
        varchar yarn_count
        varchar yarn_type
        int total_bags
        numeric net_weight_kg
        numeric rate_per_kg
        date received_date
    }

    KNITTING_PROGRAMS {
        bigint id PK
        bigint order_id FK
        varchar program_number UK
        int machine_dia
        int gauge
        int target_gsm
        varchar fabric_type
        numeric loop_length
        numeric allocated_yarn_kg
        varchar status
    }

    FABRIC_ROLLS {
        bigint id PK
        bigint knitting_program_id FK
        varchar roll_number UK
        numeric roll_weight_kg
        int actual_gsm
        varchar quality_grade
        varchar inspected_by
    }

    DYEING_BATCHES {
        bigint id PK
        bigint order_id FK
        varchar batch_number UK
        varchar dyeing_unit_name
        varchar outward_dc_number
        numeric sent_weight_kg
        numeric received_weight_kg
        varchar pantone_shade
        varchar finish_type
        varchar status
        date sent_date
        date received_date
    }

    CUTTING_ORDERS {
        bigint id PK
        bigint order_id FK
        varchar lay_number
        int total_plies
        int total_cut_pieces
        numeric fabric_consumed_kg
        numeric cutting_wastage_kg
    }

    CUT_BUNDLES {
        bigint id PK
        bigint cutting_order_id FK
        varchar bundle_code UK
        varchar size
        int quantity
        varchar current_stage
    }

    SEWING_LINES {
        bigint id PK
        varchar line_name UK
        varchar supervisor_name
        int hourly_target_pieces
    }

    SEWING_PRODUCTION_LOG {
        bigint id PK
        bigint line_id FK
        bigint bundle_id FK
        varchar operator_name
        int passed_pieces
        int reworked_pieces
        int rejected_pieces
        timestamp logged_at
    }

    CARTON_PACKINGS {
        bigint id PK
        bigint order_id FK
        varchar carton_number UK
        varchar packing_type
        varchar size_breakdown
        int total_garments
        numeric gross_weight_kg
        numeric cbm
        boolean is_dispatched
        date dispatched_date
    }
```

---

### 4. Full-Stack System Architecture

```mermaid
flowchart TB
    subgraph UI["Client Tier: Angular 19 (Port 4200)"]
        direction TB
        COMP["Standalone Components<br/>(Dashboard, Yarn, Knitting, Dyeing, Cutting, Sewing, Packing, Master Data)"]
        STYLE["Bootstrap 5.3 + Bootstrap Icons + Angular Material"]
        THEME["ThemeService (Light & Dark Modes)"]
        SIDEBAR["SidebarService (Collapsible 260px / 76px Icon-Only)"]
        SIG["Reactive Signals & State Management"]
        GUARD["Auth Guard & Session Signals"]
        INTER["JWT Bearer HTTP Interceptor"]
        API_SERV["ApiService, AuthService & MasterDataService"]
        
        COMP --> STYLE
        COMP --> THEME
        COMP --> SIDEBAR
        COMP --> SIG
        GUARD --> COMP
        COMP --> API_SERV
        API_SERV --> INTER
    end

    subgraph BACKEND["Application Tier: Spring Boot 3.4.1 (Port 8080)"]
        direction TB
        SEC["Spring Security + Stateless JWT Filter + CORS"]
        SWAG["SpringDoc OpenAPI 3.0 / Swagger UI"]
        
        subgraph MODULES["Domain Services & REST APIs"]
            AUTH_M["Auth Service (/api/v1/auth)"]
            MASTER_M["Master Data Service (/api/v1/master)"]
            ORDER_M["Order Service (/api/v1/orders)"]
            YARN_M["Yarn Service (/api/v1/yarn)"]
            KNIT_M["Knitting Service (/api/v1/knitting)"]
            DYE_M["Dyeing DC Service (/api/v1/dyeing)"]
            CUT_M["Cutting Service (/api/v1/cutting)"]
            SEW_M["Sewing Service (/api/v1/sewing)"]
            PACK_M["Packing Service (/api/v1/packing)"]
            DASH_M["Dashboard Service (/api/v1/dashboard)"]
        end

        JPA["Spring Data JPA & Hibernate 6"]
        POOL["HikariCP Connection Pool (Max: 10, Idle: 2)"]

        SEC --> MODULES
        SWAG --> MODULES
        MODULES --> JPA
        JPA --> POOL
    end

    subgraph DATA["Database Tier: PostgreSQL 18 (Port 5432)"]
        PG[("PostgreSQL 18 Database<br/>yarn_manager_db")]
        ADMIN["pgAdmin 4 (Port 5050)<br/>Web Administration GUI"]
    end

    INTER -->|"REST / JSON (Bearer Token)"| SEC
    POOL -->|"JDBC Driver"| PG
    ADMIN -->|"SQL Admin"| PG
```

---

### 5. Dyeing Job Work Delivery Challan (DC) Reconciliation Flow

```mermaid
sequenceDiagram
    autonumber
    actor Mgr as Production Merchandiser
    participant ERP as Yarn Manager ERP
    actor DYE as External Dyeing Unit (SIPCOT)
    actor QC as Plant QC Inspector

    Mgr->>ERP: Create Outward DC (Sent Wt: 2400 kg, Shade: Navy Blue)
    ERP-->>Mgr: Generate DC-OUT-9921 & Gate Pass Barcode
    Mgr->>DYE: Dispatch Grey Fabric Rolls with Outward DC
    Note over DYE: Scouring, Bio-Polish, Dyeing & Compaction
    DYE->>Mgr: Return Finished Fabric with Delivery Invoice
    Mgr->>ERP: Scan Inward DC & Enter Received Wt (2330 kg)
    ERP->>ERP: Auto-Calculate Process Loss % = ((2400 - 2330) / 2400) * 100 = 2.91%
    alt Loss <= 3.5% (Within Tolerance)
        ERP->>QC: Trigger Shade & Shrinkage Inspection Task
        QC->>ERP: Approve Shade (Delta-E < 0.5) & Grade 'A'
        ERP->>ERP: Status updated to 'RECEIVED' & Available for Lay Cutting
    else Loss > 3.5% (Excessive Wastage)
        ERP-->>Mgr: Alert Flagged: Process Loss Exceeded Tolerance!
        ERP->>DYE: Issue Debit Note / Reconciliation Notice
    end
```

---

## 🌟 Key Features & Capabilities

### 1. Master Data Hub (`/master`)
Centralized management for factory foundation entities, replacing hardcoded constants with dynamic database records:
- **Units of Measure (UOM)**: Standard textile measurement units (`KG`, `BAGS`, `METERS`, `ROLLS`, `PCS`, `DOZENS`, `CARTONS`) with base conversion multipliers.
- **Process Pricing & Rate Cards**: Industrial rate cards per kg/piece for Knitting, Dyeing, Compacting, Lay Cutting, Sewing, and Bio-Wash finishes.
- **Customer / Buyer Directory**: International and domestic apparel brands, export destination country, contact details, payment currencies, and active status.
- **Company Master**: Multi-entity plant profiling (Tiruppur Unit #1, Netaji Apparel Park Unit, etc.), GSTIN, IEC export code, and headquarters details.
- **Product / Garment Styles**: Master product catalog with style codes, fabric specifications (Single Jersey, Rib, Interlock), target GSM, and size curves.

### 2. Collapsible UI Sidebar (Icon-Only Mode)
- **Compact View (76px)**: Collapses text labels to icon-only navigation with hover tooltips (`[title]`) for clean, distraction-free floor terminals.
- **Expanded View (260px)**: Full brand headers, stage sequence numbers, and database status indicators.
- **Dual Toggle Controls**: Quick toggle button in the sidebar header and top navbar.
- **Persistent State**: Automatically stored in `localStorage` via Angular signals.

### 3. Light & Dark Mode Theming
- Built on Bootstrap 5.3 color modes (`[data-bs-theme="dark"]` / `[data-bs-theme="light"]`) with custom CSS custom properties (`--app-bg`, `--app-card-bg`, `--app-sidebar-bg`, `--app-text`).
- Instant theme toggle in the top navbar with persistent user preference in `localStorage`.

### 4. JWT Authentication & Role-Based Access Control (RBAC)
- Stateless authentication with signed HMAC-SHA256 JWT bearer tokens.
- Secure HTTP interceptor (`auth.interceptor.ts`) attaching bearer tokens to API calls.
- Plant roles:
  - `ROLE_ADMIN`: Full system administration and master data configuration.
  - `ROLE_SUPERVISOR`: Floor line execution, knitting programs, and cutting schedules.
  - `ROLE_QC_LEAD`: Roll quality inspection, dyeing shade clearance, and DHU logs.
  - `ROLE_OPERATOR`: Daily bundle tracking and scan inputs.

### 5. Full Manufacturing Lifecycle (Stage 1 to Stage 6)
- **Yarn Inward**: Warehouse inventory tracking by Mill, Lot, Yarn Count, Net Weight, and Bags.
- **Circular Knitting**: Machine dia, gauge, target GSM, loop length, roll barcoding, and knitting loss % verification.
- **Dyeing DC Tracking**: Subcontract job-work challan management with automated process loss % and debit alert calculation.
- **Cutting & Bundles**: Lay planning, plies calculation, wastage tracking, and bundle QR ticketing.
- **Sewing Assembly**: Hourly line balancing and real-time Defects per Hundred Units (DHU %) tracking.
- **Carton Packing & Dispatch**: Assorted/solid packing matrix, volumetric CBM, gross weight, and dispatch labeling.

---

## 🏗️ Technology Stack

| Layer | Technology | Key Libraries / Modules |
|---|---|---|
| **Backend** | Java 21 / 22 (Corretto), Spring Boot 3.4.1 | Spring Data JPA, Spring Security 6, JJWT (`0.12.6`), Hibernate 6, HikariCP, Lombok |
| **Frontend** | Angular 19 (Standalone Components) | Angular Signals, Bootstrap 5.3.3, Bootstrap Icons 1.11.3, Angular Material 19, RxJS 7.8 |
| **Database** | PostgreSQL 18 | Normalized relational schema, indexes, cascade foreign keys, automated DDL defaults |
| **API Docs** | SpringDoc OpenAPI 3.0 | Swagger UI interactive explorer (`/swagger-ui.html`) |
| **DevOps** | Docker & Docker Compose | Containerized PostgreSQL 18 & pgAdmin 4 |

---

## 📁 Project Structure

```text
yarn-manager/
├── docker-compose.yml             # Orchestration for PostgreSQL 18 and pgAdmin
├── README.md                      # Project documentation and architectural diagrams
├── database/
│   ├── 01-schema.sql              # Normalized DDL schema with relational tables & indexes
│   └── 02-seed-data.sql           # Realistic Tiruppur mill & garment production seed data
├── backend/                       # Spring Boot 3.4.1 Application
│   ├── pom.xml
│   ├── mvnw & mvnw.cmd            # Zero-install Maven Wrapper
│   ├── src/main/resources/
│   │   └── application.yml        # PostgreSQL, HikariCP, and JWT settings
│   └── src/main/java/com/tiruppur/yarnmanager/
│       ├── YarnManagerApplication.java
│       ├── config/                # OpenAPI Swagger configuration
│       ├── common/                # Standard API response wrappers & exception advice
│       ├── security/              # Spring Security, JWT utilities, and Auth Filter
│       │   ├── WebSecurityConfig.java
│       │   ├── UserDetailsServiceImpl.java
│       │   └── jwt/               # JwtUtils, AuthTokenFilter, AuthEntryPointJwt
│       └── domain/
│           ├── auth/              # Login, register, and token refresh endpoints
│           ├── master/            # Master Data Hub (UOM, Pricing, Buyers, Companies, Products)
│           │   ├── entity/        # UomMaster, PricingMaster, CompanyMaster, ProductMaster
│           │   ├── repository/    # Spring Data JPA repositories
│           │   ├── service/       # MasterDataService
│           │   └── controller/    # MasterDataController (/api/v1/master)
│           ├── order/             # Buyer styles and PO management
│           ├── yarn/              # Yarn stock and inward receipt
│           ├── knitting/          # Circular knitting programs & roll barcodes
│           ├── dyeing/            # Job work Delivery Challan & reconciliation
│           ├── cutting/           # Lay cutting and QR bundle ticketing
│           ├── sewing/            # Assembly lines, hourly logs & QA
│           ├── packing/           # Carton packaging & CBM calculator
│           └── dashboard/         # Real-time pipeline KPI metrics
└── frontend/                      # Angular 19 Client Application
    ├── package.json
    ├── angular.json
    └── src/
        ├── index.html
        ├── styles.css             # Bootstrap 5, Bootstrap Icons & theme design system
        └── app/
            ├── app.component.ts   # Root layout with reactive theme and auth state
            ├── app.routes.ts      # Lazy-loaded routes with auth guards
            ├── core/
            │   ├── guards/        # auth.guard.ts
            │   ├── interceptors/  # auth.interceptor.ts (Bearer token injector)
            │   ├── models/        # TypeScript interfaces & models
            │   └── services/      # ApiService, AuthService, ThemeService, SidebarService, MasterDataService
            ├── utilities/         # Industrial formulas (knitting loss, dyeing loss, DHU, CBM)
            ├── shared/
            │   ├── navbar/        # Shift status, theme switcher, sidebar toggle, user profile
            │   └── sidebar/       # Collapsible navigation with icon-only mode and tooltips
            └── features/          # Feature components (.ts, .html, .spec.ts)
                ├── auth/login/    # JWT factory floor personnel login
                ├── dashboard/     # Conversion pipeline KPI cards
                ├── master/        # Master Data Hub (UOM, Rate Cards, Buyers, Companies, Products)
                ├── yarn/          # Warehouse stock & inward receipt
                ├── knitting/      # Circular knitting & roll inspector
                ├── dyeing/        # DC outward/inward reconciliation
                ├── cutting/       # Lay cutting & QR bundle tickets
                ├── sewing/        # Line balancing & DHU inspection
                └── packing/       # Carton packaging & CBM calculator
```

---

## 🚀 Quick Start Guide

### 1. Database Setup (PostgreSQL)

Launch PostgreSQL 18 and pgAdmin using Docker Compose:

```bash
docker compose up -d postgres pgadmin
```

* **PostgreSQL Port**: `5432` (`yarn_manager_db` / `postgres` / `postgrespassword`)
* **pgAdmin 4 GUI**: [http://localhost:5050](http://localhost:5050) (`admin@tiruppurtextiles.com` / `adminpassword`)

Alternatively, run locally on native PostgreSQL 18:
```powershell
& "C:\Program Files\PostgreSQL\18\bin\psql.exe" -U postgres -c "CREATE DATABASE yarn_manager_db;"
& "C:\Program Files\PostgreSQL\18\bin\psql.exe" -U postgres -d yarn_manager_db -f .\database\01-schema.sql
& "C:\Program Files\PostgreSQL\18\bin\psql.exe" -U postgres -d yarn_manager_db -f .\database\02-seed-data.sql
```

---

### 2. Backend Setup (Spring Boot)

Run unit tests and start the backend service:

```powershell
cd D:\Projects\Github\yarn-manager\backend

# Run all 14 JUnit 5 tests
.\mvnw.cmd test

# Run application
.\mvnw.cmd spring-boot:run
```

* **API Base URL**: `http://localhost:8080/api/v1`
* **Swagger UI Documentation**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
* **OpenAPI 3.0 Schema**: `http://localhost:8080/v3/api-docs`

---

### 3. Frontend Setup (Angular 19)

Install packages, run unit tests, and start the development server:

```powershell
cd D:\Projects\Github\yarn-manager\frontend

# Install dependencies
npm install

# Run all 48 Angular unit tests in headless Chrome
npm test -- --watch=false --browsers=ChromeHeadless

# Start Angular development server
npm start
```

Open [http://localhost:4200](http://localhost:4200) in your browser.

#### Demo Credentials:
| Username | Password | Role | Access Level |
|---|---|---|---|
| `admin` | `admin123` | `ROLE_ADMIN` | Full access to all stages and Master Data Hub |
| `supervisor` | `super123` | `ROLE_SUPERVISOR` | Floor operations, knitting programs, and sewing lines |
| `qc_lead` | `qc123` | `ROLE_QC_LEAD` | Roll inspections, dyeing DC approvals, and defect logs |

---

## 🧪 Testing & Verification Summary

- **Backend**: 14/14 automated JUnit 5 tests passing across Authentication, JWT, and Master Data domain services.
- **Frontend**: 48/48 Jasmine & Karma unit tests passing across all components, guards, interceptors, and services.
- **Production Build**: Successfully validated via `npm run build` with zero compiler errors.
