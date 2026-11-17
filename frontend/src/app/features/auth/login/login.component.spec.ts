import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter, Router } from '@angular/router';
import { of } from 'rxjs';
import { LoginComponent } from './login.component';
import { AuthService } from '../../../core/services/auth.service';
import { UserSession } from '../../../core/models/auth.models';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authServiceSpy: jasmine.SpyObj<AuthService>;
  let router: Router;

  beforeEach(async () => {
    authServiceSpy = jasmine.createSpyObj('AuthService', ['login']);

    await TestBed.configureTestingModule({
      imports: [LoginComponent],
      providers: [
        { provide: AuthService, useValue: authServiceSpy },
        provideHttpClient(),
        provideHttpClientTesting(),
        provideRouter([])
      ]
    }).compileComponents();

    router = TestBed.inject(Router);
    spyOn(router, 'navigateByUrl');
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should populate credentials when demo role is selected', () => {
    component.setDemoRole('supervisor');
    expect(component.credentials.username).toBe('supervisor');
    expect(component.credentials.password).toBe('super123');
  });

  it('should call authService.login on submit and navigate', () => {
    const mockUser: UserSession = {
      token: 'jwt-123',
      username: 'admin',
      fullName: 'Ramasamy',
      role: 'ADMIN',
      permissions: ['ALL']
    };
    authServiceSpy.login.and.returnValue(of(mockUser));

    component.credentials = { username: 'admin', password: 'admin123' };
    component.onSubmit();

    expect(authServiceSpy.login).toHaveBeenCalledWith({ username: 'admin', password: 'admin123' });
    expect(router.navigateByUrl).toHaveBeenCalledWith('/dashboard');
  });
});
