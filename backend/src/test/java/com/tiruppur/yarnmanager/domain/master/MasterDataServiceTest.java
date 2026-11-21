package com.tiruppur.yarnmanager.domain.master;

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
import com.tiruppur.yarnmanager.domain.master.service.MasterDataService;
import com.tiruppur.yarnmanager.domain.order.entity.Buyer;
import com.tiruppur.yarnmanager.domain.order.repository.BuyerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("MasterDataService Unit Tests")
class MasterDataServiceTest {

    @Mock
    private UomMasterRepository uomRepository;

    @Mock
    private BuyerRepository buyerRepository;

    @Mock
    private PricingMasterRepository pricingRepository;

    @Mock
    private CompanyMasterRepository companyRepository;

    @Mock
    private ProductMasterRepository productRepository;

    @Mock
    private CurrencyExchangeRateRepository exchangeRateRepository;

    private MasterDataService masterDataService;

    @BeforeEach
    void setUp() {
        masterDataService = new MasterDataService(
                uomRepository, buyerRepository, pricingRepository, companyRepository, productRepository, exchangeRateRepository);
    }

    @Test
    @DisplayName("Should save and retrieve company profile")
    void shouldSaveCompanyProfile() {
        CompanyMaster company = CompanyMaster.builder()
                .companyCode("TTG-CORP-01")
                .companyName("Tiruppur TexFlow Garments")
                .gstin("33AAACT1234F1Z5")
                .registeredAddress("SF No 124, Kangeyam Road, Tiruppur")
                .isPrimary(true)
                .active(true)
                .build();

        when(companyRepository.save(any(CompanyMaster.class))).thenReturn(company);

        CompanyMaster saved = masterDataService.saveCompany(company);

        assertNotNull(saved);
        assertEquals("TTG-CORP-01", saved.getCompanyCode());
        assertEquals("33AAACT1234F1Z5", saved.getGstin());
    }

    @Test
    @DisplayName("Should save and retrieve garment product style")
    void shouldSaveProductStyle() {
        ProductMaster product = ProductMaster.builder()
                .productCode("STY-CREW-180")
                .styleName("Men Bio-Wash Crew Neck T-Shirt")
                .category("MEN_ROUND_NECK")
                .fabricType("Single Jersey")
                .yarnCount("30s Combed Cotton")
                .targetGsm(180)
                .standardSmv(new BigDecimal("12.50"))
                .active(true)
                .build();

        when(productRepository.save(any(ProductMaster.class))).thenReturn(product);

        ProductMaster saved = masterDataService.saveProduct(product);

        assertNotNull(saved);
        assertEquals("STY-CREW-180", saved.getProductCode());
        assertEquals(180, saved.getTargetGsm());
    }

    @Test
    @DisplayName("Should convert 50 Bags to accurate Kilograms using UOM conversion factors")
    void shouldConvertBagsToKilograms() {
        UomMaster bag = UomMaster.builder()
                .uomCode("BAG")
                .category("WEIGHT")
                .conversionFactor(new BigDecimal("45.360000"))
                .decimalPlaces(2)
                .build();

        UomMaster kg = UomMaster.builder()
                .uomCode("KG")
                .category("WEIGHT")
                .conversionFactor(new BigDecimal("1.000000"))
                .decimalPlaces(2)
                .build();

        when(uomRepository.findByUomCode("BAG")).thenReturn(Optional.of(bag));
        when(uomRepository.findByUomCode("KG")).thenReturn(Optional.of(kg));

        BigDecimal converted = masterDataService.convertUom(new BigDecimal("50"), "BAG", "KG");

        // 50 * 45.36 = 2268.00
        assertEquals(new BigDecimal("2268.00"), converted);
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when converting incompatible categories")
    void shouldRejectIncompatibleUomConversion() {
        UomMaster kg = UomMaster.builder()
                .uomCode("KG")
                .category("WEIGHT")
                .conversionFactor(BigDecimal.ONE)
                .build();

        UomMaster mtr = UomMaster.builder()
                .uomCode("MTR")
                .category("LENGTH")
                .conversionFactor(BigDecimal.ONE)
                .build();

        when(uomRepository.findByUomCode("KG")).thenReturn(Optional.of(kg));
        when(uomRepository.findByUomCode("MTR")).thenReturn(Optional.of(mtr));

        assertThrows(IllegalArgumentException.class, () ->
                masterDataService.convertUom(BigDecimal.TEN, "KG", "MTR"));
    }

    @Test
    @DisplayName("Should resolve effective pricing rate for dyeing process")
    void shouldResolveEffectivePricingRate() {
        PricingMaster darkShadeRate = PricingMaster.builder()
                .priceCode("RATE-DYE-DARK")
                .processType("DYEING_JOBWORK")
                .categoryOrSpec("Dark Shade")
                .uomCode("KG")
                .baseRate(new BigDecimal("95.00"))
                .currency("INR")
                .effectiveFrom(LocalDate.now().minusDays(10))
                .active(true)
                .build();

        when(pricingRepository.findMatchingRates(eq("DYEING_JOBWORK"), eq("Dark Shade"), any(LocalDate.class), eq(1L)))
                .thenReturn(List.of(darkShadeRate));

        PricingMaster resolved = masterDataService.resolveEffectiveRate("DYEING_JOBWORK", "Dark Shade", 1L);

        assertNotNull(resolved);
        assertEquals("RATE-DYE-DARK", resolved.getPriceCode());
        assertEquals(new BigDecimal("95.00"), resolved.getBaseRate());
    }

    @Test
    @DisplayName("Should resolve rate and convert currency for calendar order date")
    void shouldResolveRateWithCurrencyConversion() {
        LocalDate orderDate = LocalDate.of(2026, 10, 15);
        PricingMaster octRate = PricingMaster.builder()
                .priceCode("RATE-KNIT-SJ-24-OCT26")
                .processType("KNITTING_JOBWORK")
                .categoryOrSpec("Single Jersey 24GG")
                .uomCode("KG")
                .baseRate(new BigDecimal("170.00"))
                .currency("INR")
                .effectiveFrom(LocalDate.of(2026, 10, 1))
                .effectiveTo(LocalDate.of(2026, 10, 31))
                .active(true)
                .build();

        CurrencyExchangeRate usdRate = CurrencyExchangeRate.builder()
                .fromCurrency("USD")
                .toCurrency("INR")
                .exchangeRate(new BigDecimal("85.000000"))
                .effectiveDate(LocalDate.of(2026, 10, 1))
                .active(true)
                .build();

        when(pricingRepository.findMatchingRates(eq("KNITTING_JOBWORK"), eq("Single Jersey 24GG"), eq(orderDate), isNull()))
                .thenReturn(List.of(octRate));
        when(exchangeRateRepository.findEffectiveRates(eq("INR"), eq("USD"), eq(orderDate)))
                .thenReturn(List.of());
        when(exchangeRateRepository.findEffectiveRates(eq("USD"), eq("INR"), eq(orderDate)))
                .thenReturn(List.of(usdRate));

        ResolvedRateDto resolved = masterDataService.resolveEffectiveRateWithConversion(
                "KNITTING_JOBWORK", "Single Jersey 24GG", null, orderDate, "USD");

        assertNotNull(resolved);
        assertEquals("INR", resolved.getBaseCurrency());
        assertEquals("USD", resolved.getTargetCurrency());
        assertEquals(new BigDecimal("170.00"), resolved.getBaseRate());
        // 170 / 85 = 2.0000 USD
        assertEquals(new BigDecimal("2.0000"), resolved.getConvertedRate());
    }

    @Test
    @DisplayName("Should update customer profile details")
    void shouldUpdateCustomerProfile() {
        Buyer existing = Buyer.builder()
                .id(1L)
                .name("Old Name")
                .buyerCode("BUY-01")
                .country("India")
                .build();

        Buyer updated = Buyer.builder()
                .name("Zara Sourcing Europe")
                .buyerCode("BUY-ZARA-01")
                .country("Spain")
                .currency("EUR")
                .paymentTerms("LC at 60 Days")
                .active(true)
                .build();

        when(buyerRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(buyerRepository.save(any(Buyer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Buyer result = masterDataService.updateCustomer(1L, updated);

        assertEquals("Zara Sourcing Europe", result.getName());
        assertEquals("Spain", result.getCountry());
        assertEquals("EUR", result.getCurrency());
    }
}
