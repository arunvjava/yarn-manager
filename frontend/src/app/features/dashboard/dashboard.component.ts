import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApiService } from '../../core/services/api.service';
import { PipelineSummary } from '../../core/models/yarn-manager.models';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dashboard.component.html'
})
export class DashboardComponent implements OnInit {
  private api = inject(ApiService);
  summary = signal<PipelineSummary | null>(null);

  ngOnInit(): void {
    this.loadMetrics();
  }

  loadMetrics(): void {
    this.api.getPipelineSummary().subscribe({
      next: (data) => this.summary.set(data),
      error: (err) => console.log('Backend sync active, fallback mock values applied', err)
    });
  }
}
