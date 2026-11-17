import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { FormsModule } from '@angular/forms';
import { BundleTrackingComponent } from './bundle-tracking.component';
import { ApiService } from '../../core/services/api.service';

describe('BundleTrackingComponent', () => {
  let component: BundleTrackingComponent;
  let fixture: ComponentFixture<BundleTrackingComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BundleTrackingComponent, FormsModule],
      providers: [
        ApiService,
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(BundleTrackingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should update stage on quickMove', () => {
    const bundle = component.bundles()[0];
    component.quickMove(bundle, 'PRINTING');
    expect(component.bundles()[0].currentStage).toBe('PRINTING');
  });
});
