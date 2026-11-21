import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { SidebarComponent } from './sidebar.component';
import { SidebarService } from '../../../core/services/sidebar.service';

describe('SidebarComponent', () => {
  let component: SidebarComponent;
  let fixture: ComponentFixture<SidebarComponent>;
  let sidebarService: SidebarService;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SidebarComponent],
      providers: [provideRouter([]), SidebarService]
    }).compileComponents();

    fixture = TestBed.createComponent(SidebarComponent);
    component = fixture.componentInstance;
    sidebarService = TestBed.inject(SidebarService);
    fixture.detectChanges();
  });

  it('should create sidebar', () => {
    expect(component).toBeTruthy();
  });

  it('should toggle collapse state via service', () => {
    sidebarService.expand();
    expect(sidebarService.isCollapsed()).toBeFalse();

    sidebarService.toggle();
    expect(sidebarService.isCollapsed()).toBeTrue();
  });
});
