import { TestBed } from '@angular/core/testing';
import { HttpRequest, HttpHandlerFn, HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { of, throwError } from 'rxjs';
import { authInterceptor } from './auth.interceptor';
import { AuthService } from '../services/auth.service';

describe('authInterceptor', () => {
  let authServiceSpy: jasmine.SpyObj<AuthService>;

  beforeEach(() => {
    authServiceSpy = jasmine.createSpyObj('AuthService', ['getToken', 'handleSessionExpired']);

    TestBed.configureTestingModule({
      providers: [
        { provide: AuthService, useValue: authServiceSpy }
      ]
    });
  });

  it('should append Authorization header when token exists', (done) => {
    authServiceSpy.getToken.and.returnValue('sample-token-123');

    const req = new HttpRequest('GET', '/api/v1/orders');
    const next: HttpHandlerFn = (request) => {
      expect(request.headers.get('Authorization')).toBe('Bearer sample-token-123');
      return of(new HttpResponse({ status: 200 }));
    };

    TestBed.runInInjectionContext(() => {
      authInterceptor(req, next).subscribe(() => done());
    });
  });

  it('should not modify headers when no token exists', (done) => {
    authServiceSpy.getToken.and.returnValue(null);

    const req = new HttpRequest('GET', '/api/v1/orders');
    const next: HttpHandlerFn = (request) => {
      expect(request.headers.has('Authorization')).toBeFalse();
      return of(new HttpResponse({ status: 200 }));
    };

    TestBed.runInInjectionContext(() => {
      authInterceptor(req, next).subscribe(() => done());
    });
  });

  it('should notify user to log in when 401 Unauthorized occurs on API', (done) => {
    authServiceSpy.getToken.and.returnValue('expired-token');

    const req = new HttpRequest('GET', '/api/v1/yarn');
    const next: HttpHandlerFn = () => {
      return throwError(() => new HttpErrorResponse({ status: 401, statusText: 'Unauthorized' }));
    };

    TestBed.runInInjectionContext(() => {
      authInterceptor(req, next).subscribe({
        error: (err) => {
          expect(err.status).toBe(401);
          expect(authServiceSpy.handleSessionExpired).toHaveBeenCalled();
          done();
        }
      });
    });
  });

  it('should not trigger session expiration when 401 is on login endpoint', (done) => {
    authServiceSpy.getToken.and.returnValue(null);

    const req = new HttpRequest('POST', '/api/v1/auth/login', {});
    const next: HttpHandlerFn = () => {
      return throwError(() => new HttpErrorResponse({ status: 401, statusText: 'Unauthorized' }));
    };

    TestBed.runInInjectionContext(() => {
      authInterceptor(req, next).subscribe({
        error: (err) => {
          expect(err.status).toBe(401);
          expect(authServiceSpy.handleSessionExpired).not.toHaveBeenCalled();
          done();
        }
      });
    });
  });
});
