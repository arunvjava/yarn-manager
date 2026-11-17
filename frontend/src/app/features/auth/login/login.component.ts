import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { LoginPayload } from '../../../core/models/auth.models';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.component.html'
})
export class LoginComponent {
  private authService = inject(AuthService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);

  credentials: LoginPayload = {
    username: 'admin',
    password: 'admin123'
  };

  isLoading = signal(false);
  errorMessage = signal<string | null>(null);

  onSubmit(): void {
    if (!this.credentials.username || !this.credentials.password) {
      this.errorMessage.set('Please enter both employee ID and password.');
      return;
    }

    this.isLoading.set(true);
    this.errorMessage.set(null);

    this.authService.login(this.credentials).subscribe({
      next: () => {
        this.isLoading.set(false);
        const returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/dashboard';
        this.router.navigateByUrl(returnUrl);
      },
      error: (err) => {
        this.isLoading.set(false);
        this.errorMessage.set(err?.error?.message || 'Login failed. Check your credentials.');
      }
    });
  }

  setDemoRole(role: 'admin' | 'supervisor' | 'qc'): void {
    if (role === 'admin') {
      this.credentials = { username: 'admin', password: 'admin123' };
    } else if (role === 'supervisor') {
      this.credentials = { username: 'supervisor', password: 'super123' };
    } else if (role === 'qc') {
      this.credentials = { username: 'qc_lead', password: 'qc123' };
    }
  }
}
