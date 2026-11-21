package com.tiruppur.yarnmanager.security;

import com.tiruppur.yarnmanager.domain.auth.entity.Role;
import com.tiruppur.yarnmanager.domain.auth.entity.User;
import com.tiruppur.yarnmanager.domain.auth.repository.UserRepository;
import com.tiruppur.yarnmanager.domain.master.entity.PricingMaster;
import com.tiruppur.yarnmanager.domain.master.entity.UomMaster;
import com.tiruppur.yarnmanager.domain.master.repository.PricingMasterRepository;
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

    public DataInitializer(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           UomMasterRepository uomRepository,
                           BuyerRepository buyerRepository,
                           PricingMasterRepository pricingRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.uomRepository = uomRepository;
        this.buyerRepository = buyerRepository;
        this.pricingRepository = pricingRepository;
    }

    @Override
    public void run(String... args) {
        initUsers();
        initUoms();
        initCustomers();
        initPricing();
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
        if (pricingRepository.count() == 0) {
            logger.info("Initializing Pricing Master rate cards...");
            LocalDate now = LocalDate.now();
            List<PricingMaster> rateCards = List.of(
                // Knitting Job-Work Rates
                PricingMaster.builder().priceCode("RATE-KNIT-SJ-24").processOrItemType("KNITTING_JOBWORK")
                    .categoryOrSpec("Single Jersey 24GG").uomCode("KG").baseRate(new BigDecimal("18.00"))
                    .currency("INR").effectiveFrom(now).notes("Standard 180 GSM circular knitting").active(true).build(),

                PricingMaster.builder().priceCode("RATE-KNIT-RIB-18").processOrItemType("KNITTING_JOBWORK")
                    .categoryOrSpec("1x1 Rib 18GG").uomCode("KG").baseRate(new BigDecimal("22.00"))
                    .currency("INR").effectiveFrom(now).notes("Neck rib & cuff knit").active(true).build(),

                PricingMaster.builder().priceCode("RATE-KNIT-INT-28").processOrItemType("KNITTING_JOBWORK")
                    .categoryOrSpec("Interlock 28GG").uomCode("KG").baseRate(new BigDecimal("25.00"))
                    .currency("INR").effectiveFrom(now).notes("Heavy interlock double jersey").active(true).build(),

                // Dyeing Job-Work Rates (by shade depth)
                PricingMaster.builder().priceCode("RATE-DYE-LIGHT").processOrItemType("DYEING_JOBWORK")
                    .categoryOrSpec("Light Shade (Pastel / Off-White)").uomCode("KG").baseRate(new BigDecimal("55.00"))
                    .currency("INR").effectiveFrom(now).notes("Low dye concentration").active(true).build(),

                PricingMaster.builder().priceCode("RATE-DYE-MED").processOrItemType("DYEING_JOBWORK")
                    .categoryOrSpec("Medium Shade (Sky Blue / Coral)").uomCode("KG").baseRate(new BigDecimal("75.00"))
                    .currency("INR").effectiveFrom(now).notes("Medium depth shade").active(true).build(),

                PricingMaster.builder().priceCode("RATE-DYE-DARK").processOrItemType("DYEING_JOBWORK")
                    .categoryOrSpec("Dark Shade (Navy / Olive / Maroon)").uomCode("KG").baseRate(new BigDecimal("95.00"))
                    .currency("INR").effectiveFrom(now).notes("Standard dark shade reactive dyeing").active(true).build(),

                PricingMaster.builder().priceCode("RATE-DYE-BLACK").processOrItemType("DYEING_JOBWORK")
                    .categoryOrSpec("Jet Black (Reactive)").uomCode("KG").baseRate(new BigDecimal("110.00"))
                    .currency("INR").effectiveFrom(now).notes("Extra depth reactive black with wash-off").active(true).build(),

                // Finishing & Bio-Polish
                PricingMaster.builder().priceCode("RATE-BIO-POLISH").processOrItemType("FINISHING")
                    .categoryOrSpec("Enzyme Bio-Polishing").uomCode("KG").baseRate(new BigDecimal("16.00"))
                    .currency("INR").effectiveFrom(now).notes("Cellulase enzyme treatment for zero fuzz").active(true).build(),

                // Sewing Cut-to-Make (CM) Rates
                PricingMaster.builder().priceCode("RATE-SEW-CREW").processOrItemType("SEWING_CM")
                    .categoryOrSpec("Basic Crew Neck T-Shirt").uomCode("PCS").baseRate(new BigDecimal("18.50"))
                    .currency("INR").effectiveFrom(now).notes("Standard 4-thread overlock + flatlock hem").active(true).build(),

                PricingMaster.builder().priceCode("RATE-SEW-POLO").processOrItemType("SEWING_CM")
                    .categoryOrSpec("Pique Polo Tee with Placket").uomCode("PCS").baseRate(new BigDecimal("34.00"))
                    .currency("INR").effectiveFrom(now).notes("Rib collar, box placket, 2-button attachment").active(true).build(),

                // Raw Material Yarn Purchase Rates
                PricingMaster.builder().priceCode("RATE-YARN-30SC").processOrItemType("YARN_PURCHASE")
                    .categoryOrSpec("30s Combed Cotton Cone Yarn").uomCode("KG").baseRate(new BigDecimal("285.00"))
                    .currency("INR").effectiveFrom(now).notes("KPR / Ramco Mill standard 30s combed").active(true).build(),

                PricingMaster.builder().priceCode("RATE-YARN-34SL").processOrItemType("YARN_PURCHASE")
                    .categoryOrSpec("34s Lycra Spun Yarn (95/5)").uomCode("KG").baseRate(new BigDecimal("340.00"))
                    .currency("INR").effectiveFrom(now).notes("Core spun Lycra for stretch tees").active(true).build()
            );
            pricingRepository.saveAll(rateCards);
            logger.info("Saved {} pricing rate cards.", rateCards.size());
        }
    }
}
