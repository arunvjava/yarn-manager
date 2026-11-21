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
      'saveCompany',
      'saveProduct',
      'saveCustomer'
    ]);

    masterServiceSpy.getCompanies.and.returnValue(of({ success: true, data: [], message: 'OK', timestamp: '' }));
    masterServiceSpy.getProducts.and.returnValue(of({ success: true, data: [], message: 'OK', timestamp: '' }));
    masterServiceSpy.getCustomers.and.returnValue(of({ success: true, data: [], message: 'OK', timestamp: '' }));
    masterServiceSpy.getUoms.and.returnValue(of({ success: true, data: [], message: 'OK', timestamp: '' }));
    masterServiceSpy.getPricing.and.returnValue(of({ success: true, data: [], message: 'OK', timestamp: '' }));

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

    component.setTab('customer');
    expect(component.activeTab()).toBe('customer');
  });

  it('should validate company form before submitting', () => {
    component.newCompany.companyName = '';
    component.submitCompany();
    expect(component.errorMessage()).toContain('Company Name, Code, and GSTIN are required');
  });
});
