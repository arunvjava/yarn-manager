import { Component, OnInit, inject, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MasterDataService } from '../../core/services/master-data.service';
import {
  CompanyMaster,
  ProductMaster,
  CustomerMaster,
  UomMaster,
  PricingMaster,
  CurrencyExchangeRate
} from '../../core/models/master-data.models';

type MasterTab = 'company' | 'product' | 'customer' | 'uom' | 'pricing' | 'exchange';

@Component({
  selector: 'app-master-data',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './master-data.component.html'
})
export class MasterDataComponent implements OnInit {
  private masterService = inject(MasterDataService);

  activeTab = signal<MasterTab>('company');
  isLoading = signal<boolean>(false);
  successMessage = signal<string | null>(null);
  errorMessage = signal<string | null>(null);

  // Data lists
  companies = signal<CompanyMaster[]>([]);
  products = signal<ProductMaster[]>([]);
  customers = signal<CustomerMaster[]>([]);
  uoms = signal<UomMaster[]>([]);
  pricingRules = signal<PricingMaster[]>([]);
  exchangeRates = signal<CurrencyExchangeRate[]>([]);

  // Form toggles
  showCompanyForm = signal<boolean>(false);
  showProductForm = signal<boolean>(false);
  showCustomerForm = signal<boolean>(false);
  showUomForm = signal<boolean>(false);
  showPricingForm = signal<boolean>(false);
  showExchangeRateForm = signal<boolean>(false);

  // Calendar & Currency Controls for Pricing
  selectedCalendarPeriod = signal<string>('ALL'); // 'ALL', '2026-11', '2026-10'
  selectedDisplayCurrency = signal<string>('INR'); // 'INR', 'USD', 'EUR', 'GBP'
  filterProcessType = signal<string>('ALL');

  // New Form models
  newCompany: CompanyMaster = {
    companyName: '',
    companyCode: '',
    unitOrDivision: '',
    gstin: '',
    pan: '',
    iecCode: '',
    registeredAddress: '',
    contactPerson: '',
    contactEmail: '',
    contactPhone: '',
    bankName: '',
    bankAccountNumber: '',
    bankIfsc: '',
    primary: false,
    active: true
  };

  newProduct: ProductMaster = {
    productCode: '',
    styleName: '',
    category: 'MEN_ROUND_NECK',
    fabricType: 'Single Jersey',
    yarnCount: '30s Combed Cotton',
    targetGsm: 180,
    neckType: 'Round Neck (1x1 Rib)',
    sleeveType: 'Half Sleeve',
    standardSmv: 12.50,
    availableSizes: 'S, M, L, XL, 2XL',
    hsnCode: '61091000',
    active: true
  };

  newCustomer: CustomerMaster = {
    name: '',
    buyerCode: '',
    brandNames: '',
    country: 'India',
    currency: 'USD',
    paymentTerms: 'Net 30 Days',
    taxIdGstin: '',
    contactPerson: '',
    contactEmail: '',
    contactPhone: '',
    billingAddress: '',
    shippingPort: '',
    tolerancePercentage: 5.0,
    active: true
  };

  newUom: UomMaster = {
    uomCode: '',
    uomName: '',
    category: 'WEIGHT',
    baseUom: false,
    conversionFactor: 1.0,
    decimalPlaces: 2,
    active: true
  };

  newPricing: PricingMaster = {
    priceCode: '',
    processType: 'KNITTING_JOBWORK',
    categoryOrSpec: '',
    uomCode: 'KG',
    baseRate: 0,
    currency: 'INR',
    effectiveFrom: new Date().toISOString().substring(0, 10),
    effectiveTo: '',
    notes: '',
    active: true
  };

  newExchangeRate: CurrencyExchangeRate = {
    fromCurrency: 'USD',
    toCurrency: 'INR',
    exchangeRate: 86.50,
    effectiveDate: new Date().toISOString().substring(0, 10),
    calendarMonth: new Date().toISOString().substring(0, 7),
    source: 'RBI Reference Rate',
    active: true
  };

  // Filtered pricing rules based on selected calendar month & process
  filteredPricingRules = computed(() => {
    let list = this.pricingRules();
    const period = this.selectedCalendarPeriod();
    const process = this.filterProcessType();

    if (process !== 'ALL') {
      list = list.filter(r => r.processType === process);
    }

    if (period !== 'ALL') {
      list = list.filter(r => {
        const fromMonth = r.effectiveFrom ? r.effectiveFrom.substring(0, 7) : '';
        const toMonth = r.effectiveTo ? r.effectiveTo.substring(0, 7) : '';
        if (period === '2026-11') {
          // Current month or ongoing
          return fromMonth === '2026-11' || (!toMonth && fromMonth <= '2026-11');
        } else if (period === '2026-10') {
          // Previous month
          return fromMonth === '2026-10' || toMonth === '2026-10';
        }
        return fromMonth === period || toMonth === period;
      });
    }

    return list;
  });

  ngOnInit(): void {
    this.loadAllMasters();
  }

  setTab(tab: MasterTab): void {
    this.activeTab.set(tab);
    this.clearMessages();
  }

  loadAllMasters(): void {
    this.isLoading.set(true);
    this.masterService.getCompanies().subscribe({
      next: (res) => this.companies.set(res.data),
      error: () => {}
    });

    this.masterService.getProducts().subscribe({
      next: (res) => this.products.set(res.data),
      error: () => {}
    });

    this.masterService.getCustomers().subscribe({
      next: (res) => this.customers.set(res.data),
      error: () => {}
    });

    this.masterService.getUoms().subscribe({
      next: (res) => this.uoms.set(res.data),
      error: () => {}
    });

    this.masterService.getPricing().subscribe({
      next: (res) => this.pricingRules.set(res.data),
      error: () => {}
    });

    this.masterService.getExchangeRates().subscribe({
      next: (res) => {
        this.exchangeRates.set(res.data);
        this.isLoading.set(false);
      },
      error: () => this.isLoading.set(false)
    });
  }

  // Company Actions
  submitCompany(): void {
    if (!this.newCompany.companyName || !this.newCompany.companyCode || !this.newCompany.gstin) {
      this.errorMessage.set('Company Name, Code, and GSTIN are required.');
      return;
    }
    this.isLoading.set(true);
    this.masterService.saveCompany(this.newCompany).subscribe({
      next: (res) => {
        this.companies.update(list => [...list, res.data]);
        this.showCompanyForm.set(false);
        this.isLoading.set(false);
        this.successMessage.set(`Company "${res.data.companyName}" registered successfully.`);
        this.resetCompanyForm();
      },
      error: (err) => {
        this.isLoading.set(false);
        this.errorMessage.set(err?.error?.message || 'Failed to create company.');
      }
    });
  }

  // Product Actions
  submitProduct(): void {
    if (!this.newProduct.productCode || !this.newProduct.styleName || !this.newProduct.targetGsm) {
      this.errorMessage.set('Product Code, Style Name, and GSM are required.');
      return;
    }
    this.isLoading.set(true);
    this.masterService.saveProduct(this.newProduct).subscribe({
      next: (res) => {
        this.products.update(list => [...list, res.data]);
        this.showProductForm.set(false);
        this.isLoading.set(false);
        this.successMessage.set(`Product "${res.data.styleName}" created successfully.`);
        this.resetProductForm();
      },
      error: (err) => {
        this.isLoading.set(false);
        this.errorMessage.set(err?.error?.message || 'Failed to create product.');
      }
    });
  }

  // Customer Actions
  submitCustomer(): void {
    if (!this.newCustomer.name || !this.newCustomer.buyerCode) {
      this.errorMessage.set('Buyer Legal Name and Code are required.');
      return;
    }
    this.isLoading.set(true);
    this.masterService.saveCustomer(this.newCustomer).subscribe({
      next: (res) => {
        this.customers.update(list => [...list, res.data]);
        this.showCustomerForm.set(false);
        this.isLoading.set(false);
        this.successMessage.set(`Customer "${res.data.name}" added successfully.`);
        this.resetCustomerForm();
      },
      error: (err) => {
        this.isLoading.set(false);
        this.errorMessage.set(err?.error?.message || 'Failed to add customer.');
      }
    });
  }

  // UOM Actions
  submitUom(): void {
    if (!this.newUom.uomCode || !this.newUom.uomName) {
      this.errorMessage.set('UOM Code and Name are required.');
      return;
    }
    this.isLoading.set(true);
    this.masterService.saveUom(this.newUom).subscribe({
      next: (res) => {
        this.uoms.update(list => [...list, res.data]);
        this.showUomForm.set(false);
        this.isLoading.set(false);
        this.successMessage.set(`Unit of Measure "${res.data.uomCode} - ${res.data.uomName}" added successfully.`);
        this.resetUomForm();
      },
      error: (err) => {
        this.isLoading.set(false);
        this.errorMessage.set(err?.error?.message || 'Failed to add Unit of Measure.');
      }
    });
  }

  // Pricing Actions
  submitPricing(): void {
    if (!this.newPricing.priceCode || !this.newPricing.categoryOrSpec || this.newPricing.baseRate <= 0) {
      this.errorMessage.set('Rate Code, Specification, and Base Rate (> 0) are required.');
      return;
    }
    this.isLoading.set(true);
    this.masterService.savePrice(this.newPricing).subscribe({
      next: (res) => {
        this.pricingRules.update(list => [res.data, ...list]);
        this.showPricingForm.set(false);
        this.isLoading.set(false);
        this.successMessage.set(`Rate Card "${res.data.priceCode}" saved successfully.`);
        this.resetPricingForm();
      },
      error: (err) => {
        this.isLoading.set(false);
        this.errorMessage.set(err?.error?.message || 'Failed to save rate card.');
      }
    });
  }

  // Currency Exchange Rate Actions
  submitExchangeRate(): void {
    if (!this.newExchangeRate.fromCurrency || !this.newExchangeRate.toCurrency || this.newExchangeRate.exchangeRate <= 0) {
      this.errorMessage.set('From Currency, To Currency, and Exchange Rate are required.');
      return;
    }
    this.isLoading.set(true);
    this.masterService.saveExchangeRate(this.newExchangeRate).subscribe({
      next: (res) => {
        this.exchangeRates.update(list => [res.data, ...list]);
        this.showExchangeRateForm.set(false);
        this.isLoading.set(false);
        this.successMessage.set(`Exchange rate 1 ${res.data.fromCurrency} = ${res.data.exchangeRate} ${res.data.toCurrency} recorded.`);
        this.resetExchangeRateForm();
      },
      error: (err) => {
        this.isLoading.set(false);
        this.errorMessage.set(err?.error?.message || 'Failed to save exchange rate.');
      }
    });
  }

  // Currency Conversion Calculation Helper for Table Rows
  getConvertedRate(baseRate: number, fromCurrency: string, targetCurrency: string, effectiveDate: string): number {
    const from = (fromCurrency || 'INR').toUpperCase();
    const to = (targetCurrency || 'INR').toUpperCase();

    if (from === to) return baseRate;

    const month = effectiveDate ? effectiveDate.substring(0, 7) : '';
    // Look up exchange rate matching from->to and matching month or nearest
    const rates = this.exchangeRates();

    // 1. Direct match
    let match = rates.find(r => r.fromCurrency === from && r.toCurrency === to && (!month || r.calendarMonth === month));
    if (!match) match = rates.find(r => r.fromCurrency === from && r.toCurrency === to);
    if (match) return baseRate * match.exchangeRate;

    // 2. Inverse match (e.g., from INR to USD, rate is USD->INR 86.50 => divide)
    let invMatch = rates.find(r => r.fromCurrency === to && r.toCurrency === from && (!month || r.calendarMonth === month));
    if (!invMatch) invMatch = rates.find(r => r.fromCurrency === to && r.toCurrency === from);
    if (invMatch && invMatch.exchangeRate > 0) return baseRate / invMatch.exchangeRate;

    // 3. Fallback static rate
    if (from === 'INR' && to === 'USD') return baseRate / 86.50;
    if (from === 'USD' && to === 'INR') return baseRate * 86.50;
    if (from === 'INR' && to === 'EUR') return baseRate / 91.20;
    if (from === 'EUR' && to === 'INR') return baseRate * 91.20;
    if (from === 'INR' && to === 'GBP') return baseRate / 109.80;
    if (from === 'GBP' && to === 'INR') return baseRate * 109.80;

    return baseRate;
  }

  getCurrencySymbol(curr: string): string {
    switch (curr) {
      case 'USD': return '$';
      case 'EUR': return '€';
      case 'GBP': return '£';
      default: return '₹';
    }
  }

  clearMessages(): void {
    this.successMessage.set(null);
    this.errorMessage.set(null);
  }

  private resetCompanyForm(): void {
    this.newCompany = {
      companyName: '',
      companyCode: '',
      unitOrDivision: '',
      gstin: '',
      pan: '',
      iecCode: '',
      registeredAddress: '',
      contactPerson: '',
      contactEmail: '',
      contactPhone: '',
      bankName: '',
      bankAccountNumber: '',
      bankIfsc: '',
      primary: false,
      active: true
    };
  }

  private resetProductForm(): void {
    this.newProduct = {
      productCode: '',
      styleName: '',
      category: 'MEN_ROUND_NECK',
      fabricType: 'Single Jersey',
      yarnCount: '30s Combed Cotton',
      targetGsm: 180,
      neckType: 'Round Neck (1x1 Rib)',
      sleeveType: 'Half Sleeve',
      standardSmv: 12.50,
      availableSizes: 'S, M, L, XL, 2XL',
      hsnCode: '61091000',
      active: true
    };
  }

  private resetCustomerForm(): void {
    this.newCustomer = {
      name: '',
      buyerCode: '',
      brandNames: '',
      country: 'India',
      currency: 'USD',
      paymentTerms: 'Net 30 Days',
      taxIdGstin: '',
      contactPerson: '',
      contactEmail: '',
      contactPhone: '',
      billingAddress: '',
      shippingPort: '',
      tolerancePercentage: 5.0,
      active: true
    };
  }

  private resetUomForm(): void {
    this.newUom = {
      uomCode: '',
      uomName: '',
      category: 'WEIGHT',
      baseUom: false,
      conversionFactor: 1.0,
      decimalPlaces: 2,
      active: true
    };
  }

  private resetPricingForm(): void {
    this.newPricing = {
      priceCode: '',
      processType: 'KNITTING_JOBWORK',
      categoryOrSpec: '',
      uomCode: 'KG',
      baseRate: 0,
      currency: 'INR',
      effectiveFrom: new Date().toISOString().substring(0, 10),
      effectiveTo: '',
      notes: '',
      active: true
    };
  }

  private resetExchangeRateForm(): void {
    this.newExchangeRate = {
      fromCurrency: 'USD',
      toCurrency: 'INR',
      exchangeRate: 86.50,
      effectiveDate: new Date().toISOString().substring(0, 10),
      calendarMonth: new Date().toISOString().substring(0, 7),
      source: 'RBI Reference Rate',
      active: true
    };
  }
}
