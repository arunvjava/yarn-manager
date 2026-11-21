import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MasterDataComponent } from './master-data.component';
import { MasterDataService } from '../../core/services/master-data.service';
import { of } from 'rxjs';

describe('MasterDataComponent', () => {
  let component: MasterDataComponent;
  let fixture: ComponentFixture<MasterDataComponent>;
  let masterServiceSpy: jasmine.SpyObj<MasterDataService>;

  beforeEach(async () => {
    masterServiceSpy = jasmine.createSpyObj('MasterDataService', [
      'getCompanies',
      'getProducts',
      'getCustomers',
      'getUoms',
      'getPricing',
      'getExchangeRates',
      'saveCompany',
      'saveProduct',
      'saveCustomer',
      'saveUom',
      'savePrice',
      'saveExchangeRate'
    ]);

    masterServiceSpy.getCompanies.and.returnValue(of({ success: true, data: [], message: 'OK', timestamp: '' }));
    masterServiceSpy.getProducts.and.returnValue(of({ success: true, data: [], message: 'OK', timestamp: '' }));
    masterServiceSpy.getCustomers.and.returnValue(of({ success: true, data: [], message: 'OK', timestamp: '' }));
    masterServiceSpy.getUoms.and.returnValue(of({ success: true, data: [], message: 'OK', timestamp: '' }));
    masterServiceSpy.getPricing.and.returnValue(of({ success: true, data: [], message: 'OK', timestamp: '' }));
    masterServiceSpy.getExchangeRates.and.returnValue(of({ success: true, data: [], message: 'OK', timestamp: '' }));

    await TestBed.configureTestingModule({
      imports: [MasterDataComponent],
      providers: [
        { provide: MasterDataService, useValue: masterServiceSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(MasterDataComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create master data management component', () => {
    expect(component).toBeTruthy();
  });

  it('should switch active tab when selected', () => {
    component.setTab('product');
    expect(component.activeTab()).toBe('product');

    component.setTab('uom');
    expect(component.activeTab()).toBe('uom');

    component.setTab('pricing');
    expect(component.activeTab()).toBe('pricing');

    component.setTab('exchange');
    expect(component.activeTab()).toBe('exchange');
  });

  it('should validate company form before submitting', () => {
    component.newCompany.companyName = '';
    component.submitCompany();
    expect(component.errorMessage()).toContain('Company Name, Code, and GSTIN are required');
  });

  it('should validate UOM form before submitting', () => {
    component.newUom.uomCode = '';
    component.submitUom();
    expect(component.errorMessage()).toContain('UOM Code and Name are required');
  });

  it('should validate pricing rate form before submitting', () => {
    component.newPricing.priceCode = '';
    component.submitPricing();
    expect(component.errorMessage()).toContain('Rate Code, Specification, and Base Rate (> 0) are required');
  });

  it('should accurately convert currency based on exchange rates', () => {
    component.exchangeRates.set([
      { fromCurrency: 'USD', toCurrency: 'INR', exchangeRate: 85.00, effectiveDate: '2026-10-01', calendarMonth: '2026-10', active: true },
      { fromCurrency: 'USD', toCurrency: 'INR', exchangeRate: 86.50, effectiveDate: '2026-11-01', calendarMonth: '2026-11', active: true }
    ]);

    // October conversion: 170 INR / 85 USD = 2.00 USD
    const convertedOct = component.getConvertedRate(170, 'INR', 'USD', '2026-10-15');
    expect(convertedOct).toBe(2);

    // Same currency: should return original rate
    const sameCurrency = component.getConvertedRate(170, 'INR', 'INR', '2026-10-15');
    expect(sameCurrency).toBe(170);
  });
});
