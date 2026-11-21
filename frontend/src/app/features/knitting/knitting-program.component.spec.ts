import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { FormsModule } from '@angular/forms';
import { of } from 'rxjs';
import { KnittingProgramComponent } from './knitting-program.component';
import { ApiService } from '../../core/services/api.service';
import { FabricRoll } from '../../core/models/yarn-manager.models';

describe('KnittingProgramComponent', () => {
  let component: KnittingProgramComponent;
  let fixture: ComponentFixture<KnittingProgramComponent>;
  let apiService: ApiService;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [KnittingProgramComponent, FormsModule],
      providers: [
        ApiService,
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(KnittingProgramComponent);
    component = fixture.componentInstance;
    apiService = TestBed.inject(ApiService);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should add a roll when addRoll is called', () => {
    const mockRoll: FabricRoll = {
      id: 5,
      rollNumber: 'ROLL-TEST-005',
      rollWeightKg: 25.5,
      actualGsm: 180,
      qualityGrade: 'A',
      inspectedBy: 'Operator (Floor QC)'
    };
    spyOn(apiService, 'registerFabricRoll').and.returnValue(of(mockRoll));

    const initialCount = component.rolls().length;
    component.newRollWeight = 25.5;
    component.newRollGsm = 180;
    component.addRoll(1);
    expect(component.rolls().length).toBe(initialCount + 1);
  });
});
