import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MasterDataService } from '../../core/services/master-data.service';
import { CompanyMaster, ProductMaster, CustomerMaster, UomMaster, PricingMaster } from '../../core/models/master-data.models';

type MasterTab = 'company' | 'product' | 'customer' | 'uom' | 'pricing';

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

  // Form toggles
  showCompanyForm = signal<boolean>(false);
  showProductForm = signal<boolean>(false);
  showCustomerForm = signal<boolean>(false);

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
      next: (res) => {
        this.pricingRules.set(res.data);
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
}
