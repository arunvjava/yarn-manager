import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { FormsModule } from '@angular/forms';
import { SewingLinesComponent } from './sewing-lines.component';
import { ApiService } from '../../core/services/api.service';

describe('SewingLinesComponent', () => {
  let component: SewingLinesComponent;
  let fixture: ComponentFixture<SewingLinesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SewingLinesComponent, FormsModule],
      providers: [
        ApiService,
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(SewingLinesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should add output log when submitOutput is called', () => {
    const initialLogs = component.logs().length;
    component.passedPcs = 30;
    component.submitOutput(1);
    expect(component.logs().length).toBe(initialLogs + 1);
  });
});
