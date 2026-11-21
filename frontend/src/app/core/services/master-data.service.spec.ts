import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { MasterDataService } from './master-data.service';
import { UomMaster, CustomerMaster, PricingMaster } from '../models/master-data.models';

describe('MasterDataService', () => {
  let service: MasterDataService;
  let httpTesting: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        MasterDataService,
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    });
    service = TestBed.inject(MasterDataService);
    httpTesting = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTesting.verify();
  });

  it('should fetch UOMs with activeOnly parameter', () => {
    const mockUoms: UomMaster[] = [
      { id: 1, uomCode: 'KG', uomName: 'Kilogram', category: 'WEIGHT', baseUom: true, conversionFactor: 1, decimalPlaces: 2, active: true }
    ];

    service.getUoms(true).subscribe(res => {
      expect(res.data.length).toBe(1);
      expect(res.data[0].uomCode).toBe('KG');
    });

    const req = httpTesting.expectOne('http://localhost:8080/api/v1/master/uom?activeOnly=true');
    expect(req.request.method).toBe('GET');
    req.flush({ success: true, data: mockUoms, message: 'OK', timestamp: new Date().toISOString() });
  });

  it('should convert UOM quantity correctly', () => {
    service.convertUom(50, 'BAG', 'KG').subscribe(res => {
      expect(res.data).toBe(2268);
    });

    const req = httpTesting.expectOne('http://localhost:8080/api/v1/master/uom/convert?quantity=50&from=BAG&to=KG');
    expect(req.request.method).toBe('GET');
    req.flush({ success: true, data: 2268, message: 'OK', timestamp: new Date().toISOString() });
  });

  it('should resolve effective rate for dyeing', () => {
    const mockRate: PricingMaster = {
      priceCode: 'RATE-DYE-DARK',
      processOrItemType: 'DYEING_JOBWORK',
      categoryOrSpec: 'Dark Shade',
      uomCode: 'KG',
      baseRate: 95,
      currency: 'INR',
      effectiveFrom: '2026-01-01',
      active: true
    };

    service.resolveRate('DYEING_JOBWORK', 'Dark Shade', 1).subscribe(res => {
      expect(res.data.baseRate).toBe(95);
      expect(res.data.priceCode).toBe('RATE-DYE-DARK');
    });

    const req = httpTesting.expectOne('http://localhost:8080/api/v1/master/pricing/resolve?processType=DYEING_JOBWORK&spec=Dark%20Shade&buyerId=1');
    expect(req.request.method).toBe('GET');
    req.flush({ success: true, data: mockRate, message: 'OK', timestamp: new Date().toISOString() });
  });
});
