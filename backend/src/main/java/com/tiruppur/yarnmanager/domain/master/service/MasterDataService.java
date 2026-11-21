package com.tiruppur.yarnmanager.domain.master.service;

import com.tiruppur.yarnmanager.domain.master.dto.ResolvedRateDto;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class MasterDataService {

    private final UomMasterRepository uomRepository;
    private final BuyerRepository buyerRepository;
    private final PricingMasterRepository pricingRepository;
    private final CompanyMasterRepository companyRepository;
    private final ProductMasterRepository productRepository;
    private final CurrencyExchangeRateRepository exchangeRateRepository;

    public MasterDataService(UomMasterRepository uomRepository,
                             BuyerRepository buyerRepository,
                             PricingMasterRepository pricingRepository,
                             CompanyMasterRepository companyRepository,
                             ProductMasterRepository productRepository,
                             CurrencyExchangeRateRepository exchangeRateRepository) {
        this.uomRepository = uomRepository;
        this.buyerRepository = buyerRepository;
        this.pricingRepository = pricingRepository;
        this.companyRepository = companyRepository;
        this.productRepository = productRepository;
        this.exchangeRateRepository = exchangeRateRepository;
    }

    // ==========================================
    // 1. Company Master Operations
    // ==========================================
    @Transactional(readOnly = true)
    public List<CompanyMaster> getAllCompanies(Boolean activeOnly) {
        if (Boolean.TRUE.equals(activeOnly)) {
            return companyRepository.findByActiveTrue();
        }
        return companyRepository.findAll();
    }

    @Transactional(readOnly = true)
    public CompanyMaster getCompanyById(Long id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Company not found with ID: " + id));
    }

    public CompanyMaster saveCompany(CompanyMaster company) {
        company.setCompanyCode(company.getCompanyCode().toUpperCase().trim());
        return companyRepository.save(company);
    }

    public CompanyMaster updateCompany(Long id, CompanyMaster updated) {
        CompanyMaster existing = getCompanyById(id);
        existing.setCompanyName(updated.getCompanyName());
        existing.setCompanyCode(updated.getCompanyCode().toUpperCase().trim());
        existing.setUnitOrDivision(updated.getUnitOrDivision());
        existing.setGstin(updated.getGstin());
        existing.setPan(updated.getPan());
        existing.setIecCode(updated.getIecCode());
        existing.setRegisteredAddress(updated.getRegisteredAddress());
        existing.setContactPerson(updated.getContactPerson());
        existing.setContactEmail(updated.getContactEmail());
        existing.setContactPhone(updated.getContactPhone());
        existing.setBankName(updated.getBankName());
        existing.setBankAccountNumber(updated.getBankAccountNumber());
        existing.setBankIfsc(updated.getBankIfsc());
        existing.setPrimary(updated.isPrimary());
        existing.setActive(updated.isActive());
        return companyRepository.save(existing);
    }

    // ==========================================
    // 2. Product Master (Garment Styles) Operations
    // ==========================================
    @Transactional(readOnly = true)
    public List<ProductMaster> getAllProducts(Boolean activeOnly, String category) {
        if (category != null && !category.isBlank()) {
            return productRepository.findByCategoryAndActiveTrue(category.toUpperCase().trim());
        }
        if (Boolean.TRUE.equals(activeOnly)) {
            return productRepository.findByActiveTrue();
        }
        return productRepository.findAll();
    }

    @Transactional(readOnly = true)
    public ProductMaster getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product / Style not found with ID: " + id));
    }

    @Transactional(readOnly = true)
    public ProductMaster getProductByCode(String code) {
        return productRepository.findByProductCode(code.toUpperCase().trim())
                .orElseThrow(() -> new IllegalArgumentException("Product not found for code: " + code));
    }

    public ProductMaster saveProduct(ProductMaster product) {
        product.setProductCode(product.getProductCode().toUpperCase().trim());
        return productRepository.save(product);
    }

    public ProductMaster updateProduct(Long id, ProductMaster updated) {
        ProductMaster existing = getProductById(id);
        existing.setProductCode(updated.getProductCode().toUpperCase().trim());
        existing.setStyleName(updated.getStyleName());
        existing.setCategory(updated.getCategory());
        existing.setFabricType(updated.getFabricType());
        existing.setYarnCount(updated.getYarnCount());
        existing.setTargetGsm(updated.getTargetGsm());
        existing.setNeckType(updated.getNeckType());
        existing.setSleeveType(updated.getSleeveType());
        existing.setStandardSmv(updated.getStandardSmv());
        existing.setAvailableSizes(updated.getAvailableSizes());
        existing.setHsnCode(updated.getHsnCode());
        existing.setActive(updated.isActive());
        return productRepository.save(existing);
    }

    // ==========================================
    // 3. UOM (Unit of Measure) Operations
    // ==========================================
    @Transactional(readOnly = true)
    public List<UomMaster> getAllUoms(Boolean activeOnly) {
        if (Boolean.TRUE.equals(activeOnly)) {
            return uomRepository.findByActiveTrue();
        }
        return uomRepository.findAll();
    }

    @Transactional(readOnly = true)
    public UomMaster getUomByCode(String uomCode) {
        return uomRepository.findByUomCode(uomCode.toUpperCase().trim())
                .orElseThrow(() -> new IllegalArgumentException("UOM not found for code: " + uomCode));
    }

    public UomMaster saveUom(UomMaster uom) {
        uom.setUomCode(uom.getUomCode().toUpperCase().trim());
        return uomRepository.save(uom);
    }

    @Transactional(readOnly = true)
    public BigDecimal convertUom(BigDecimal quantity, String fromUomCode, String toUomCode) {
        if (fromUomCode.equalsIgnoreCase(toUomCode)) {
            return quantity;
        }

        UomMaster from = getUomByCode(fromUomCode);
        UomMaster to = getUomByCode(toUomCode);

        if (!from.getCategory().equalsIgnoreCase(to.getCategory())) {
            throw new IllegalArgumentException(
                    "Cannot convert between incompatible UOM categories: " + from.getCategory() + " and " + to.getCategory());
        }

        // Convert from Source -> Base unit -> Target unit
        BigDecimal baseValue = quantity.multiply(from.getConversionFactor());
        return baseValue.divide(to.getConversionFactor(), to.getDecimalPlaces(), RoundingMode.HALF_UP);
    }

    // ==========================================
    // 4. Customer / Buyer Master Operations
    // ==========================================
    @Transactional(readOnly = true)
    public List<Buyer> getAllCustomers(Boolean activeOnly) {
        if (Boolean.TRUE.equals(activeOnly)) {
            return buyerRepository.findByActiveTrue();
        }
        return buyerRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Buyer getCustomerById(Long id) {
        return buyerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Customer / Buyer not found with ID: " + id));
    }

    public Buyer saveCustomer(Buyer customer) {
        return buyerRepository.save(customer);
    }

    public Buyer updateCustomer(Long id, Buyer updated) {
        Buyer existing = getCustomerById(id);
        existing.setName(updated.getName());
        existing.setBuyerCode(updated.getBuyerCode());
        existing.setBrandNames(updated.getBrandNames());
        existing.setCountry(updated.getCountry());
        existing.setCurrency(updated.getCurrency());
        existing.setPaymentTerms(updated.getPaymentTerms());
        existing.setTaxIdGstin(updated.getTaxIdGstin());
        existing.setContactPerson(updated.getContactPerson());
        existing.setContactEmail(updated.getContactEmail());
        existing.setContactPhone(updated.getContactPhone());
        existing.setBillingAddress(updated.getBillingAddress());
        existing.setShippingPort(updated.getShippingPort());
        existing.setTolerancePercentage(updated.getTolerancePercentage());
        existing.setActive(updated.isActive());
        return buyerRepository.save(existing);
    }

    // ==========================================
    // 5. Pricing Master Operations
    // ==========================================
    @Transactional(readOnly = true)
    public List<PricingMaster> getAllPrices(Boolean activeOnly, String processType) {
        return getAllPrices(activeOnly, processType, null);
    }

    @Transactional(readOnly = true)
    public List<PricingMaster> getAllPrices(Boolean activeOnly, String processType, LocalDate effectiveDate) {
        if (effectiveDate != null) {
            String proc = (processType != null && !processType.isBlank()) ? processType.toUpperCase().trim() : null;
            return pricingRepository.findByEffectiveDateAndProcessType(effectiveDate, proc);
        }
        if (processType != null && !processType.isBlank()) {
            return pricingRepository.findByProcessTypeAndActiveTrue(processType.toUpperCase().trim());
        }
        if (Boolean.TRUE.equals(activeOnly)) {
            return pricingRepository.findByActiveTrue();
        }
        return pricingRepository.findAll();
    }

    @Transactional(readOnly = true)
    public PricingMaster getPriceByCode(String priceCode) {
        return pricingRepository.findByPriceCode(priceCode.toUpperCase().trim())
                .orElseThrow(() -> new IllegalArgumentException("Price rule not found for code: " + priceCode));
    }

    public PricingMaster savePrice(PricingMaster price) {
        price.setPriceCode(price.getPriceCode().toUpperCase().trim());
        if (price.getBuyer() != null && price.getBuyer().getId() != null) {
            price.setBuyer(getCustomerById(price.getBuyer().getId()));
        }
        return pricingRepository.save(price);
    }

    @Transactional(readOnly = true)
    public PricingMaster resolveEffectiveRate(String processType, String category, Long buyerId) {
        return resolveEffectiveRate(processType, category, buyerId, LocalDate.now());
    }

    @Transactional(readOnly = true)
    public PricingMaster resolveEffectiveRate(String processType, String category, Long buyerId, LocalDate date) {
        LocalDate refDate = (date != null) ? date : LocalDate.now();
        List<PricingMaster> matching = pricingRepository.findMatchingRates(
                processType.toUpperCase().trim(),
                category.trim(),
                refDate,
                buyerId
        );

        if (matching.isEmpty()) {
            throw new IllegalArgumentException(
                    "No active pricing rate found for process: " + processType + ", spec: " + category + " as of " + refDate);
        }

        return matching.get(0);
    }

    @Transactional(readOnly = true)
    public ResolvedRateDto resolveEffectiveRateWithConversion(String processType, String category, Long buyerId,
                                                              LocalDate date, String targetCurrency) {
        LocalDate refDate = (date != null) ? date : LocalDate.now();
        PricingMaster baseRate = resolveEffectiveRate(processType, category, buyerId, refDate);

        String baseCurr = (baseRate.getCurrency() != null && !baseRate.getCurrency().isBlank()) ? baseRate.getCurrency() : "INR";
        String targetCurr = (targetCurrency != null && !targetCurrency.isBlank()) ? targetCurrency.toUpperCase().trim() : baseCurr;

        BigDecimal converted = baseRate.getBaseRate();
        BigDecimal exchangeRate = BigDecimal.ONE;
        String note = "Rate in base currency (" + baseCurr + ")";

        if (!baseCurr.equalsIgnoreCase(targetCurr)) {
            converted = convertCurrency(baseRate.getBaseRate(), baseCurr, targetCurr, refDate);
            if (baseRate.getBaseRate().compareTo(BigDecimal.ZERO) > 0) {
                exchangeRate = converted.divide(baseRate.getBaseRate(), 6, RoundingMode.HALF_UP);
            }
            note = "Converted from " + baseCurr + " to " + targetCurr + " as of " + refDate;
        }

        return ResolvedRateDto.builder()
                .pricingMaster(baseRate)
                .effectiveDate(refDate)
                .baseRate(baseRate.getBaseRate())
                .baseCurrency(baseCurr)
                .targetCurrency(targetCurr)
                .convertedRate(converted)
                .exchangeRateUsed(exchangeRate)
                .note(note)
                .build();
    }

    // ==========================================
    // 6. Currency Exchange Rate Operations
    // ==========================================
    @Transactional(readOnly = true)
    public List<CurrencyExchangeRate> getAllExchangeRates(String calendarMonth) {
        if (calendarMonth != null && !calendarMonth.isBlank()) {
            return exchangeRateRepository.findByCalendarMonthAndActiveTrue(calendarMonth.trim());
        }
        return exchangeRateRepository.findByActiveTrueOrderByEffectiveDateDesc();
    }

    public CurrencyExchangeRate saveExchangeRate(CurrencyExchangeRate exchangeRate) {
        exchangeRate.setFromCurrency(exchangeRate.getFromCurrency().toUpperCase().trim());
        exchangeRate.setToCurrency(exchangeRate.getToCurrency().toUpperCase().trim());
        if (exchangeRate.getCalendarMonth() == null && exchangeRate.getEffectiveDate() != null) {
            exchangeRate.setCalendarMonth(exchangeRate.getEffectiveDate().getYear() + "-" +
                    String.format("%02d", exchangeRate.getEffectiveDate().getMonthValue()));
        }
        return exchangeRateRepository.save(exchangeRate);
    }

    @Transactional(readOnly = true)
    public BigDecimal convertCurrency(BigDecimal amount, String fromCurrency, String toCurrency, LocalDate date) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        String from = fromCurrency.toUpperCase().trim();
        String to = toCurrency.toUpperCase().trim();

        if (from.equals(to)) {
            return amount;
        }

        LocalDate refDate = (date != null) ? date : LocalDate.now();

        // 1. Check direct rate: from -> to
        List<CurrencyExchangeRate> direct = exchangeRateRepository.findEffectiveRates(from, to, refDate);
        if (!direct.isEmpty()) {
            return amount.multiply(direct.get(0).getExchangeRate()).setScale(4, RoundingMode.HALF_UP);
        }

        // 2. Check inverse rate: to -> from
        List<CurrencyExchangeRate> inverse = exchangeRateRepository.findEffectiveRates(to, from, refDate);
        if (!inverse.isEmpty()) {
            return amount.divide(inverse.get(0).getExchangeRate(), 4, RoundingMode.HALF_UP);
        }

        // 3. Fallback: Convert via INR base
        if (!from.equals("INR") && !to.equals("INR")) {
            BigDecimal inrAmount = convertCurrency(amount, from, "INR", refDate);
            return convertCurrency(inrAmount, "INR", to, refDate);
        }

        // 4. Default Static Fallback Rates if not present in DB
        BigDecimal fallbackRate = getFallbackRate(from, to);
        return amount.multiply(fallbackRate).setScale(4, RoundingMode.HALF_UP);
    }

    private BigDecimal getFallbackRate(String from, String to) {
        if (from.equals("USD") && to.equals("INR")) return new BigDecimal("86.5000");
        if (from.equals("INR") && to.equals("USD")) return new BigDecimal("0.01156");
        if (from.equals("EUR") && to.equals("INR")) return new BigDecimal("91.2000");
        if (from.equals("INR") && to.equals("EUR")) return new BigDecimal("0.01096");
        if (from.equals("GBP") && to.equals("INR")) return new BigDecimal("109.8000");
        if (from.equals("INR") && to.equals("GBP")) return new BigDecimal("0.00910");
        return BigDecimal.ONE;
    }
}
