package com.tiruppur.yarnmanager.domain.master;

import com.tiruppur.yarnmanager.domain.master.entity.PricingMaster;
import com.tiruppur.yarnmanager.domain.master.entity.UomMaster;
import com.tiruppur.yarnmanager.domain.master.repository.PricingMasterRepository;
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

    private MasterDataService masterDataService;

    @BeforeEach
    void setUp() {
        masterDataService = new MasterDataService(uomRepository, buyerRepository, pricingRepository);
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
                .processOrItemType("DYEING_JOBWORK")
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
