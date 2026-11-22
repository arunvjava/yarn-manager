import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter, Router } from '@angular/router';
import { AuthService } from './auth.service';

describe('AuthService', () => {
  let service: AuthService;
  let router: Router;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        AuthService,
        provideHttpClient(),
        provideHttpClientTesting(),
        provideRouter([])
      ]
    });
    service = TestBed.inject(AuthService);
    router = TestBed.inject(Router);
    spyOn(router, 'navigate');
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should default to not authenticated when storage is empty', () => {
    expect(service.isAuthenticated()).toBeFalse();
    expect(service.getToken()).toBeNull();
  });

  it('should handle session expired by clearing state, setting message, and navigating to login', () => {
    service.handleSessionExpired('Session expired notice');

    expect(service.isAuthenticated()).toBeFalse();
    expect(service.sessionExpiredMessage()).toBe('Session expired notice');
    expect(router.navigate).toHaveBeenCalledWith(['/login'], jasmine.objectContaining({
      queryParams: jasmine.objectContaining({ sessionExpired: 'true' })
    }));
  });

  it('should clear session expired message', () => {
    service.sessionExpiredMessage.set('Temporary message');
    expect(service.sessionExpiredMessage()).toBe('Temporary message');

    service.clearSessionExpiredMessage();
    expect(service.sessionExpiredMessage()).toBeNull();
  });
});
