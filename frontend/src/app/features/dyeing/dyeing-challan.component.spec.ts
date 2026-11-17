import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { FormsModule } from '@angular/forms';
import { DyeingChallanComponent } from './dyeing-challan.component';
import { ApiService } from '../../core/services/api.service';

describe('DyeingChallanComponent', () => {
  let component: DyeingChallanComponent;
  let fixture: ComponentFixture<DyeingChallanComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DyeingChallanComponent, FormsModule],
      providers: [
        ApiService,
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(DyeingChallanComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
