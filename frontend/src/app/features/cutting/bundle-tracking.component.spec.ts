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

  it('should return correct badge classes for all workflow stages', () => {
    expect(component.getStageBadgeClass('CUT')).toBe('badge-stage-cut');
    expect(component.getStageBadgeClass('PRINTING')).toBe('badge-stage-printing');
    expect(component.getStageBadgeClass('EMBROIDERY')).toBe('badge-stage-embroidery');
    expect(component.getStageBadgeClass('SEWING')).toBe('badge-stage-sewing');
    expect(component.getStageBadgeClass('FINISHED')).toBe('badge-stage-finished');
    expect(component.getStageBadgeClass('UNKNOWN')).toBe('badge-stage-cut');
  });

  it('should return correct icon classes for each stage', () => {
    expect(component.getStageIcon('CUT')).toBe('bi-scissors');
    expect(component.getStageIcon('PRINTING')).toBe('bi-palette');
    expect(component.getStageIcon('EMBROIDERY')).toBe('bi-stars');
    expect(component.getStageIcon('SEWING')).toBe('bi-activity');
    expect(component.getStageIcon('FINISHED')).toBe('bi-check2-circle');
    expect(component.getStageIcon('UNKNOWN')).toBe('bi-tag');
  });

  it('should render visible stage text inside the table badge elements', () => {
    fixture.detectChanges();
    const compiled = fixture.nativeElement as HTMLElement;
    const badges = compiled.querySelectorAll('.badge-stage');
    expect(badges.length).toBeGreaterThan(0);
    const badgeTexts = Array.from(badges).map(b => b.textContent?.trim());
    expect(badgeTexts).toContain('SEWING');
    expect(badgeTexts).toContain('PRINTING');
    expect(badgeTexts).toContain('CUT');
  });

  it('should advance stage using scan code', () => {
    component.scanCode = 'BNDL-PO001-XL-01';
    component.selectedStage = 'FINISHED';
    component.advanceStage();
    expect(component.bundles().find(b => b.bundleCode === 'BNDL-PO001-XL-01')?.currentStage).toBe('FINISHED');
    expect(component.scanCode).toBe('');
  });
});
