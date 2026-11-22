import { Injectable, inject, signal, computed } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, tap, map, catchError, of } from 'rxjs';
import { UserSession, LoginPayload } from '../models/auth.models';
import { ApiResponse } from '../models/yarn-manager.models';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private http = inject(HttpClient);
  private router = inject(Router);
  private baseUrl = 'http://localhost:8080/api/v1/auth';

  private readonly STORAGE_KEY = 'texflow_session';

  // Signals for state management
  private currentUserSignal = signal<UserSession | null>(this.getStoredSession());
  sessionExpiredMessage = signal<string | null>(null);

  readonly currentUser = this.currentUserSignal.asReadonly();
  readonly isAuthenticated = computed(() => !!this.currentUserSignal());
  readonly userRole = computed(() => this.currentUserSignal()?.role ?? null);

  login(payload: LoginPayload): Observable<UserSession> {
    return this.http.post<ApiResponse<UserSession>>(`${this.baseUrl}/login`, payload)
      .pipe(
        map(res => res.data),
        tap(user => {
          this.setSession(user);
          this.clearSessionExpiredMessage();
        }),
        catchError(err => {
          // Fallback mock authentication if backend server is offline during dev/demo
          if (payload.username === 'admin' && payload.password === 'admin123') {
            const mockAdmin: UserSession = {
              token: 'mock-jwt-admin-token',
              username: 'admin',
              fullName: 'Ramasamy (Managing Director)',
              role: 'ADMIN',
              permissions: ['READ_ALL', 'WRITE_ALL', 'APPROVE_DC', 'DISPATCH']
            };
            this.setSession(mockAdmin);
            this.clearSessionExpiredMessage();
            return of(mockAdmin);
          }
          throw err;
        })
      );
  }

  logout(): void {
    if (typeof localStorage !== 'undefined') {
      localStorage.removeItem(this.STORAGE_KEY);
    }
    this.currentUserSignal.set(null);
    this.sessionExpiredMessage.set(null);
    this.router.navigate(['/login']);
  }

  handleSessionExpired(message = 'Your session has expired or you are unauthorized. Please sign in again to continue.'): void {
    if (typeof localStorage !== 'undefined') {
      localStorage.removeItem(this.STORAGE_KEY);
    }
    this.currentUserSignal.set(null);
    this.sessionExpiredMessage.set(message);

    const currentUrl = this.router.url;
    const queryParams: Record<string, string> = { sessionExpired: 'true' };
    if (currentUrl && currentUrl !== '/login') {
      queryParams['returnUrl'] = currentUrl;
    }

    this.router.navigate(['/login'], { queryParams });
  }

  clearSessionExpiredMessage(): void {
    this.sessionExpiredMessage.set(null);
  }

  getToken(): string | null {
    return this.currentUserSignal()?.token ?? null;
  }

  private setSession(user: UserSession): void {
    if (typeof localStorage !== 'undefined') {
      localStorage.setItem(this.STORAGE_KEY, JSON.stringify(user));
    }
    this.currentUserSignal.set(user);
  }

  private getStoredSession(): UserSession | null {
    if (typeof localStorage === 'undefined') return null;
    const data = localStorage.getItem(this.STORAGE_KEY);
    return data ? JSON.parse(data) : null;
  }
}
