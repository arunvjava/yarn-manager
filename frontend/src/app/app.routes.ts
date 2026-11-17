import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  {
    path: 'login',
    loadComponent: () => import('./features/auth/login/login.component').then(m => m.LoginComponent)
  },
  {
    path: '',
    redirectTo: 'dashboard',
    pathMatch: 'full'
  },
  {
    path: 'dashboard',
    canActivate: [authGuard],
    loadComponent: () => import('./features/dashboard/dashboard.component').then(m => m.DashboardComponent)
  },
  {
    path: 'yarn',
    canActivate: [authGuard],
    loadComponent: () => import('./features/yarn/yarn-stock.component').then(m => m.YarnStockComponent)
  },
  {
    path: 'knitting',
    canActivate: [authGuard],
    loadComponent: () => import('./features/knitting/knitting-program.component').then(m => m.KnittingProgramComponent)
  },
  {
    path: 'dyeing',
    canActivate: [authGuard],
    loadComponent: () => import('./features/dyeing/dyeing-challan.component').then(m => m.DyeingChallanComponent)
  },
  {
    path: 'cutting',
    canActivate: [authGuard],
    loadComponent: () => import('./features/cutting/bundle-tracking.component').then(m => m.BundleTrackingComponent)
  },
  {
    path: 'sewing',
    canActivate: [authGuard],
    loadComponent: () => import('./features/sewing/sewing-lines.component').then(m => m.SewingLinesComponent)
  },
  {
    path: 'packing',
    canActivate: [authGuard],
    loadComponent: () => import('./features/packing/carton-packing.component').then(m => m.CartonPackingComponent)
  },
  {
    path: '**',
    redirectTo: 'dashboard'
  }
];
