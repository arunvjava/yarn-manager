import { TestBed } from '@angular/core/testing';
import { SidebarService } from './sidebar.service';

describe('SidebarService', () => {
  let service: SidebarService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [SidebarService]
    });
    service = TestBed.inject(SidebarService);
    service.expand(); // start with expanded
  });

  it('should toggle sidebar state', () => {
    expect(service.isCollapsed()).toBeFalse();

    service.toggle();
    expect(service.isCollapsed()).toBeTrue();

    service.toggle();
    expect(service.isCollapsed()).toBeFalse();
  });

  it('should collapse and expand explicitly', () => {
    service.collapse();
    expect(service.isCollapsed()).toBeTrue();

    service.expand();
    expect(service.isCollapsed()).toBeFalse();
  });
});
