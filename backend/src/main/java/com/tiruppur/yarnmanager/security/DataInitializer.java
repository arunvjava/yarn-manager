package com.tiruppur.yarnmanager.security;

import com.tiruppur.yarnmanager.domain.auth.entity.Role;
import com.tiruppur.yarnmanager.domain.auth.entity.User;
import com.tiruppur.yarnmanager.domain.auth.repository.UserRepository;
import com.tiruppur.yarnmanager.domain.master.entity.CompanyMaster;
import com.tiruppur.yarnmanager.domain.master.entity.CurrencyExchangeRate;
import com.tiruppur.yarnmanager.domain.master.entity.PricingMaster;
import com.tiruppur.yarnmanager.domain.master.entity.ProductMaster;
import com.tiruppur.yarnmanager.domain.master.entity.UomMaster;
import com.tiruppur.yarnmanager.domain.master.repository.CompanyMasterRepository;
import com.tiruppur.yarnmanager.domain.master.repository.CurrencyExchangeRateRepository;
import com.tiruppur.yarnmanager.domain.master.repository.PricingMasterRepository;
import com.tiruppur.yarnmanager.domain.master.repository.ProductMasterRepository;
import com.tiruppur.yarnmanager.domain.master.repository.UomMasterRepository;
import com.tiruppur.yarnmanager.domain.order.entity.Buyer;
import com.tiruppur.yarnmanager.domain.order.repository.BuyerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UomMasterRepository uomRepository;
    private final BuyerRepository buyerRepository;
    private final PricingMasterRepository pricingRepository;
    private final CompanyMasterRepository companyRepository;
    private final ProductMasterRepository productRepository;
    private final CurrencyExchangeRateRepository exchangeRateRepository;

    public DataInitializer(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           UomMasterRepository uomRepository,
                           BuyerRepository buyerRepository,
                           PricingMasterRepository pricingRepository,
                           CompanyMasterRepository companyRepository,
                           ProductMasterRepository productRepository,
                           CurrencyExchangeRateRepository exchangeRateRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.uomRepository = uomRepository;
        this.buyerRepository = buyerRepository;
        this.pricingRepository = pricingRepository;
        this.companyRepository = companyRepository;
        this.productRepository = productRepository;
        this.exchangeRateRepository = exchangeRateRepository;
    }

    @Override
    public void run(String... args) {
        initUsers();
        initCompany();
        initProducts();
        initUoms();
        initCustomers();
        initPricing();
        initExchangeRates();
    }

    private void initUsers() {
        if (userRepository.count() == 0) {
            logger.info("Initializing plant personnel accounts...");
            List<User> defaultUsers = List.of(
                User.builder().username("admin").password(passwordEncoder.encode("admin123"))
                    .fullName("Ramasamy (Managing Director)").role(Role.ROLE_ADMIN).active(true).build(),
                User.builder().username("supervisor").password(passwordEncoder.encode("super123"))
                    .fullName("Muruganandam (Knitting & Sewing Head)").role(Role.ROLE_KNITTING_SUPERVISOR).active(true).build(),
                User.builder().username("qc_lead").password(passwordEncoder.encode("qc123"))
                    .fullName("Senthil Kumar (Quality Head)").role(Role.ROLE_QC_INSPECTOR).active(true).build(),
                User.builder().username("yarn_mgr").password(passwordEncoder.encode("yarn123"))
                    .fullName("Palanisamy (Yarn Procurement Head)").role(Role.ROLE_YARN_MANAGER).active(true).build(),
                User.builder().username("dispatch_officer").password(passwordEncoder.encode("dispatch123"))
                    .fullName("Karthik (Logistics & Shipping Officer)").role(Role.ROLE_DISPATCH_OFFICER).active(true).build()
            );
            userRepository.saveAll(defaultUsers);
            logger.info("Saved {} default users.", defaultUsers.size());
        }
    }

    private void initCompany() {
        if (companyRepository.count() == 0) {
            logger.info("Initializing Company Master profiles...");
            List<CompanyMaster> companies = List.of(
                CompanyMaster.builder()
                    .companyName("Tiruppur TexFlow Garments Pvt Ltd")
                    .companyCode("TTG-CORP-01")
                    .unitOrDivision("Unit 1 - Headquarters & Integrated Garment Plant")
                    .gstin("33AAACT1234F1Z5")
                    .pan("AAACT1234F")
                    .iecCode("0498765432")
                    .registeredAddress("SF No. 124/2, Kangeyam Main Road, Nallur, Tiruppur, Tamil Nadu 641604")
                    .contactPerson("Ramasamy")
                    .contactEmail("contact@tiruppurtextiles.com")
                    .contactPhone("+91 421 223 4567")
                    .bankName("State Bank of India, Overseas Branch, Tiruppur")
                    .bankAccountNumber("30123456789")
                    .bankIfsc("SBIN0001234")
                    .isPrimary(true)
                    .active(true)
                    .build(),

                CompanyMaster.builder()
                    .companyName("TexFlow Processing & Dyeing Division")
                    .companyCode("TTG-DYE-02")
                    .unitOrDivision("Unit 2 - CETP Zero-Liquid Discharge Dyeing House")
                    .gstin("33AAACT1234F2Z4")
                    .pan("AAACT1234F")
                    .iecCode("0498765432")
                    .registeredAddress("SIPCOT Industrial Park, Perundurai, Erode / Tiruppur Border")
                    .contactPerson("K. Palanisamy")
                    .contactEmail("dyeing@tiruppurtextiles.com")
                    .contactPhone("+91 421 245 8900")
                    .bankName("Canara Bank, SME Branch, Tiruppur")
                    .bankAccountNumber("12345678901234")
                    .bankIfsc("CNRB0005678")
                    .isPrimary(false)
                    .active(true)
                    .build()
            );
            companyRepository.saveAll(companies);
            logger.info("Saved {} company master profiles.", companies.size());
        }
    }

    private void initProducts() {
        if (productRepository.count() == 0) {
            logger.info("Initializing Product Master (Garment Styles)...");
            List<ProductMaster> products = List.of(
                ProductMaster.builder()
                    .productCode("STY-CREW-180")
                    .styleName("Men Bio-Wash Crew Neck T-Shirt")
                    .category("MEN_ROUND_NECK")
                    .fabricType("Single Jersey")
                    .yarnCount("30s Combed Cotton")
                    .targetGsm(180)
                    .neckType("Round Neck (1x1 Cotton Rib)")
                    .sleeveType("Half Sleeve")
                    .standardSmv(new BigDecimal("12.50"))
                    .availableSizes("S, M, L, XL, 2XL")
                    .hsnCode("61091000")
                    .active(true)
                    .build(),

                ProductMaster.builder()
                    .productCode("STY-POLO-220")
                    .styleName("Men Pique Polo Shirt with Flat Knit Collar")
                    .category("MEN_POLO")
                    .fabricType("Pique / Honeycomb")
                    .yarnCount("30s Combed Cotton")
                    .targetGsm(220)
                    .neckType("Flat Knit Rib Collar with 2-Button Box Placket")
                    .sleeveType("Half Sleeve with Rib Cuff")
                    .standardSmv(new BigDecimal("22.00"))
                    .availableSizes("M, L, XL, 2XL, 3XL")
                    .hsnCode("61051000")
                    .active(true)
                    .build(),

                ProductMaster.builder()
                    .productCode("STY-HOOD-320")
                    .styleName("Unisex Heavyweight Kangaroo Pocket Hoodie")
                    .category("HOODIE")
                    .fabricType("3-Thread Fleece")
                    .yarnCount("24s Melange Cotton")
                    .targetGsm(320)
                    .neckType("Drawstring Hood with Metal Eyelets")
                    .sleeveType("Full Sleeve with 2x2 Spun Rib")
                    .standardSmv(new BigDecimal("34.00"))
                    .availableSizes("XS, S, M, L, XL, 2XL")
                    .hsnCode("61102000")
                    .active(true)
                    .build(),

                ProductMaster.builder()
                    .productCode("STY-JOG-260")
                    .styleName("Athletic Slim Fit French Terry Joggers")
                    .category("JOGGERS")
                    .fabricType("French Terry")
                    .yarnCount("30s Combed Cotton")
                    .targetGsm(260)
                    .neckType("Elastic Rib Waistband with Drawcord")
                    .sleeveType("Ribbed Ankle Hem")
                    .standardSmv(new BigDecimal("24.50"))
                    .availableSizes("S, M, L, XL, 2XL")
                    .hsnCode("61034200")
                    .active(true)
                    .build()
            );
            productRepository.saveAll(products);
            logger.info("Saved {} product / garment styles.", products.size());
        }
    }

    private void initUoms() {
        if (uomRepository.count() == 0) {
            logger.info("Initializing Unit of Measure (UOM) master data...");
            List<UomMaster> uoms = List.of(
                // Weight Units (Base: KG)
                UomMaster.builder().uomCode("KG").uomName("Kilogram").category("WEIGHT").baseUom(true).conversionFactor(new BigDecimal("1.000000")).decimalPlaces(2).active(true).build(),
                UomMaster.builder().uomCode("BAG").uomName("Yarn Bag (100 lbs)").category("WEIGHT").baseUom(false).conversionFactor(new BigDecimal("45.360000")).decimalPlaces(2).active(true).build(),
                UomMaster.builder().uomCode("BAG_50").uomName("Yarn Bag (50 kg)").category("WEIGHT").baseUom(false).conversionFactor(new BigDecimal("50.000000")).decimalPlaces(2).active(true).build(),
                UomMaster.builder().uomCode("LBS").uomName("Pound (lb)").category("WEIGHT").baseUom(false).conversionFactor(new BigDecimal("0.453592")).decimalPlaces(3).active(true).build(),

                // Count Units (Base: PCS)
                UomMaster.builder().uomCode("PCS").uomName("Pieces").category("COUNT").baseUom(true).conversionFactor(new BigDecimal("1.000000")).decimalPlaces(0).active(true).build(),
                UomMaster.builder().uomCode("DOZ").uomName("Dozen (12 pcs)").category("COUNT").baseUom(false).conversionFactor(new BigDecimal("12.000000")).decimalPlaces(1).active(true).build(),

                // Packaging Units
                UomMaster.builder().uomCode("ROLL").uomName("Fabric Roll (~25 kg)").category("PACKAGING").baseUom(false).conversionFactor(new BigDecimal("25.000000")).decimalPlaces(2).active(true).build(),
                UomMaster.builder().uomCode("CONE").uomName("Yarn Cone (~1.89 kg)").category("PACKAGING").baseUom(false).conversionFactor(new BigDecimal("1.890000")).decimalPlaces(2).active(true).build(),
                UomMaster.builder().uomCode("CTN").uomName("Carton Box").category("PACKAGING").baseUom(true).conversionFactor(new BigDecimal("1.000000")).decimalPlaces(0).active(true).build(),

                // Length Units (Base: MTR)
                UomMaster.builder().uomCode("MTR").uomName("Meter").category("LENGTH").baseUom(true).conversionFactor(new BigDecimal("1.000000")).decimalPlaces(2).active(true).build(),
                UomMaster.builder().uomCode("YDS").uomName("Yard").category("LENGTH").baseUom(false).conversionFactor(new BigDecimal("0.914400")).decimalPlaces(2).active(true).build()
            );
            uomRepository.saveAll(uoms);
            logger.info("Saved {} UOM master records.", uoms.size());
        }
    }

    private void initCustomers() {
        if (buyerRepository.count() == 0) {
            logger.info("Initializing Customer Master records...");
            List<Buyer> buyers = List.of(
                Buyer.builder()
                    .name("Zara Sourcing Europe (Inditex Group)")
                    .buyerCode("BUY-ZARA-01")
                    .brandNames("Zara Man, Zara Woman, TRF")
                    .country("Spain")
                    .currency("EUR")
                    .paymentTerms("LC at 60 Days from BL Date")
                    .taxIdGstin("ESB15075062")
                    .contactPerson("Carlos Mendez")
                    .contactEmail("carlos@inditex.com")
                    .contactPhone("+34 981 185 400")
                    .billingAddress("Avenida de la Diputacion, Edificio Inditex, 15142 Arteixo, A Coruna, Spain")
                    .shippingPort("Tuticorin Port (VOCPT) -> Port of Valencia")
                    .tolerancePercentage(new BigDecimal("3.00"))
                    .active(true)
                    .build(),

                Buyer.builder()
                    .name("Decathlon Sports India Pvt Ltd")
                    .buyerCode("BUY-DECATH-02")
                    .brandNames("Domyos, Kalenji, Quechua")
                    .country("France")
                    .currency("INR")
                    .paymentTerms("Net 30 Days from Invoice")
                    .taxIdGstin("29AABCD1234F1Z5")
                    .contactPerson("Anand Kumar")
                    .contactEmail("anand.k@decathlon.in")
                    .contactPhone("+91 80 4646 7000")
                    .billingAddress("Survey No 78/10, Anekal Taluk, Bengaluru, Karnataka 562106")
                    .shippingPort("Chennai Port / Domestic Hub")
                    .tolerancePercentage(new BigDecimal("5.00"))
                    .active(true)
                    .build(),

                Buyer.builder()
                    .name("Myntra Designs Private Limited")
                    .buyerCode("BUY-MYNTRA-03")
                    .brandNames("Roadster, HRX, Mast & Harbour")
                    .country("India")
                    .currency("INR")
                    .paymentTerms("Net 45 Days")
                    .taxIdGstin("29AAACM3210G1ZU")
                    .contactPerson("Priya Sharma")
                    .contactEmail("priya.s@myntra.com")
                    .contactPhone("+91 80 4354 0000")
                    .billingAddress("Buildings Alyssa, Begonia & Clover, Embassy Tech Village, Outer Ring Road, Bengaluru 560103")
                    .shippingPort("Tiruppur Hub Dispatch")
                    .tolerancePercentage(new BigDecimal("5.00"))
                    .active(true)
                    .build()
            );
            buyerRepository.saveAll(buyers);
            logger.info("Saved {} customer master accounts.", buyers.size());
        }
    }

    private void initPricing() {
        LocalDate currentMonthStart = LocalDate.of(2026, 11, 1);
        LocalDate previousMonthStart = LocalDate.of(2026, 10, 1);
        LocalDate previousMonthEnd = LocalDate.of(2026, 10, 31);

        // Previous Month (October 2026) Rates
        seedRateIfNotExists("RATE-KNIT-SJ-24-OCT26", "KNITTING_JOBWORK", "Single Jersey 24GG", "KG",
                new BigDecimal("17.00"), "INR", previousMonthStart, previousMonthEnd, "October 2026 Rate");
        seedRateIfNotExists("RATE-DYE-DARK-OCT26", "DYEING_JOBWORK", "Dark Shade (Navy / Olive / Maroon)", "KG",
                new BigDecimal("90.00"), "INR", previousMonthStart, previousMonthEnd, "October 2026 Dyeing Rate");
        seedRateIfNotExists("RATE-SEW-CREW-OCT26", "SEWING_CM", "Basic Crew Neck T-Shirt", "PCS",
                new BigDecimal("17.50"), "INR", previousMonthStart, previousMonthEnd, "October 2026 Sewing CM");
        seedRateIfNotExists("RATE-YARN-30SC-OCT26", "YARN_PURCHASE", "30s Combed Cotton Cone Yarn", "KG",
                new BigDecimal("275.00"), "INR", previousMonthStart, previousMonthEnd, "October 2026 Spot Rate");

        // Current Month (November 2026 onwards) Rates
        seedRateIfNotExists("RATE-KNIT-SJ-24", "KNITTING_JOBWORK", "Single Jersey 24GG", "KG",
                new BigDecimal("18.00"), "INR", currentMonthStart, null, "Standard 180 GSM circular knitting");
        seedRateIfNotExists("RATE-KNIT-RIB-18", "KNITTING_JOBWORK", "1x1 Rib 18GG", "KG",
                new BigDecimal("22.00"), "INR", currentMonthStart, null, "Neck rib & cuff knit");
        seedRateIfNotExists("RATE-KNIT-INT-28", "KNITTING_JOBWORK", "Interlock 28GG", "KG",
                new BigDecimal("25.00"), "INR", currentMonthStart, null, "Heavy interlock double jersey");

        seedRateIfNotExists("RATE-DYE-LIGHT", "DYEING_JOBWORK", "Light Shade (Pastel / Off-White)", "KG",
                new BigDecimal("55.00"), "INR", currentMonthStart, null, "Low dye concentration");
        seedRateIfNotExists("RATE-DYE-MED", "DYEING_JOBWORK", "Medium Shade (Sky Blue / Coral)", "KG",
                new BigDecimal("75.00"), "INR", currentMonthStart, null, "Medium depth shade");
        seedRateIfNotExists("RATE-DYE-DARK", "DYEING_JOBWORK", "Dark Shade (Navy / Olive / Maroon)", "KG",
                new BigDecimal("95.00"), "INR", currentMonthStart, null, "Standard dark shade reactive dyeing");
        seedRateIfNotExists("RATE-DYE-BLACK", "DYEING_JOBWORK", "Jet Black (Reactive)", "KG",
                new BigDecimal("110.00"), "INR", currentMonthStart, null, "Extra depth reactive black with wash-off");

        seedRateIfNotExists("RATE-BIO-POLISH", "FINISHING", "Enzyme Bio-Polishing", "KG",
                new BigDecimal("16.00"), "INR", currentMonthStart, null, "Cellulase enzyme treatment for zero fuzz");

        seedRateIfNotExists("RATE-SEW-CREW", "SEWING_CM", "Basic Crew Neck T-Shirt", "PCS",
                new BigDecimal("18.50"), "INR", currentMonthStart, null, "Standard 4-thread overlock + flatlock hem");
        seedRateIfNotExists("RATE-SEW-POLO", "SEWING_CM", "Pique Polo Tee with Placket", "PCS",
                new BigDecimal("34.00"), "INR", currentMonthStart, null, "Rib collar, box placket, 2-button attachment");

        seedRateIfNotExists("RATE-YARN-30SC", "YARN_PURCHASE", "30s Combed Cotton Cone Yarn", "KG",
                new BigDecimal("285.00"), "INR", currentMonthStart, null, "KPR / Ramco Mill standard 30s combed");
        seedRateIfNotExists("RATE-YARN-34SL", "YARN_PURCHASE", "34s Lycra Spun Yarn (95/5)", "KG",
                new BigDecimal("340.00"), "INR", currentMonthStart, null, "Core spun Lycra for stretch tees");
    }

    private void seedRateIfNotExists(String code, String proc, String spec, String uom, BigDecimal rate,
                                     String curr, LocalDate from, LocalDate to, String notes) {
        if (pricingRepository.findByPriceCode(code).isEmpty()) {
            pricingRepository.save(PricingMaster.builder()
                    .priceCode(code)
                    .processType(proc)
                    .categoryOrSpec(spec)
                    .uomCode(uom)
                    .baseRate(rate)
                    .currency(curr)
                    .effectiveFrom(from)
                    .effectiveTo(to)
                    .notes(notes)
                    .active(true)
                    .build());
        }
    }

    private void initExchangeRates() {
        if (exchangeRateRepository.count() == 0) {
            logger.info("Initializing Currency Exchange Rates (USD, EUR, GBP to INR)...");

            List<CurrencyExchangeRate> rates = List.of(
                // October 2026 (Previous Month Calendar Rate)
                CurrencyExchangeRate.builder().fromCurrency("USD").toCurrency("INR").exchangeRate(new BigDecimal("85.800000"))
                    .effectiveDate(LocalDate.of(2026, 10, 1)).calendarMonth("2026-10").source("RBI Monthly Reference (Oct 2026)").active(true).build(),
                CurrencyExchangeRate.builder().fromCurrency("EUR").toCurrency("INR").exchangeRate(new BigDecimal("90.500000"))
                    .effectiveDate(LocalDate.of(2026, 10, 1)).calendarMonth("2026-10").source("RBI Monthly Reference (Oct 2026)").active(true).build(),
                CurrencyExchangeRate.builder().fromCurrency("GBP").toCurrency("INR").exchangeRate(new BigDecimal("108.500000"))
                    .effectiveDate(LocalDate.of(2026, 10, 1)).calendarMonth("2026-10").source("RBI Monthly Reference (Oct 2026)").active(true).build(),

                // November 2026 (Current Month Calendar Rate)
                CurrencyExchangeRate.builder().fromCurrency("USD").toCurrency("INR").exchangeRate(new BigDecimal("86.500000"))
                    .effectiveDate(LocalDate.of(2026, 11, 1)).calendarMonth("2026-11").source("RBI Monthly Reference (Nov 2026)").active(true).build(),
                CurrencyExchangeRate.builder().fromCurrency("EUR").toCurrency("INR").exchangeRate(new BigDecimal("91.200000"))
                    .effectiveDate(LocalDate.of(2026, 11, 1)).calendarMonth("2026-11").source("RBI Monthly Reference (Nov 2026)").active(true).build(),
                CurrencyExchangeRate.builder().fromCurrency("GBP").toCurrency("INR").exchangeRate(new BigDecimal("109.800000"))
                    .effectiveDate(LocalDate.of(2026, 11, 1)).calendarMonth("2026-11").source("RBI Monthly Reference (Nov 2026)").active(true).build()
            );
            exchangeRateRepository.saveAll(rates);
            logger.info("Saved {} currency exchange rate records.", rates.size());
        }
    }
}
