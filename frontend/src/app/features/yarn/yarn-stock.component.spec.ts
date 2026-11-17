import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { FormsModule } from '@angular/forms';
import { YarnStockComponent } from './yarn-stock.component';
import { ApiService } from '../../core/services/api.service';

describe('YarnStockComponent', () => {
  let component: YarnStockComponent;
  let fixture: ComponentFixture<YarnStockComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [YarnStockComponent, FormsModule],
      providers: [
        ApiService,
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(YarnStockComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should toggle inward modal state', () => {
    expect(component.showModal()).toBeFalse();
    component.toggleInwardModal();
    expect(component.showModal()).toBeTrue();
  });
});
