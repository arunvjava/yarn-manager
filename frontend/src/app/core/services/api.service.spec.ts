import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { ApiService } from './api.service';

describe('ApiService', () => {
  let service: ApiService;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        ApiService,
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    });
    service = TestBed.inject(ApiService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should retrieve pipeline summary', () => {
    const mockSummary = {
      success: true,
      message: 'OK',
      timestamp: '2026-11-17T00:00:00',
      data: {
        totalYarnInwardKg: 6000,
        totalKnittedFabricKg: 2450,
        totalDyedFabricKg: 2330,
        totalCutPieces: 10000,
        totalSewnGarments: 49,
        totalRejectedGarments: 0,
        totalPackedGarments: 120,
        overallWastagePercentage: 1.8
      }
    };

    service.getPipelineSummary().subscribe(data => {
      expect(data.totalYarnInwardKg).toBe(6000);
    });

    const req = httpTestingController.expectOne('http://localhost:8080/api/v1/dashboard/summary');
    expect(req.request.method).toEqual('GET');
    req.flush(mockSummary);
  });
});
