import { Injectable, signal } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class SidebarService {
  private readonly STORAGE_KEY = 'texflow_sidebar_collapsed';

  isCollapsed = signal<boolean>(this.getStoredState());

  toggle(): void {
    const nextState = !this.isCollapsed();
    this.isCollapsed.set(nextState);
    if (typeof localStorage !== 'undefined') {
      localStorage.setItem(this.STORAGE_KEY, String(nextState));
    }
  }

  collapse(): void {
    this.isCollapsed.set(true);
    if (typeof localStorage !== 'undefined') {
      localStorage.setItem(this.STORAGE_KEY, 'true');
    }
  }

  expand(): void {
    this.isCollapsed.set(false);
    if (typeof localStorage !== 'undefined') {
      localStorage.setItem(this.STORAGE_KEY, 'false');
    }
  }

  private getStoredState(): boolean {
    if (typeof localStorage === 'undefined') return false;
    return localStorage.getItem(this.STORAGE_KEY) === 'true';
  }
}
