import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { FormsModule } from '@angular/forms';
import { CartonPackingComponent } from './carton-packing.component';
import { ApiService } from '../../core/services/api.service';

describe('CartonPackingComponent', () => {
  let component: CartonPackingComponent;
  let fixture: ComponentFixture<CartonPackingComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CartonPackingComponent, FormsModule],
      providers: [
        ApiService,
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(CartonPackingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should mark carton as dispatched', () => {
    const carton = component.cartons()[0];
    component.markDispatched(carton);
    expect(component.cartons()[0].isDispatched).toBeTrue();
  });
});
