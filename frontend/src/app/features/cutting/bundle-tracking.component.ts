import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../core/services/api.service';
import { CutBundle } from '../../core/models/yarn-manager.models';

@Component({
  selector: 'app-bundle-tracking',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './bundle-tracking.component.html'
})
export class BundleTrackingComponent implements OnInit {
  private api = inject(ApiService);
  scanCode = '';
  selectedStage = 'SEWING';

  bundles = signal<CutBundle[]>([
    { id: 1, bundleCode: 'BNDL-PO001-S-01', size: 'S', quantity: 25, currentStage: 'SEWING' },
    { id: 2, bundleCode: 'BNDL-PO001-M-01', size: 'M', quantity: 25, currentStage: 'SEWING' },
    { id: 3, bundleCode: 'BNDL-PO001-L-01', size: 'L', quantity: 25, currentStage: 'PRINTING' },
    { id: 4, bundleCode: 'BNDL-PO001-XL-01', size: 'XL', quantity: 25, currentStage: 'CUT' }
  ]);

  ngOnInit(): void {}

  advanceStage(): void {
    if (!this.scanCode) return;
    this.bundles.update(list => list.map(b => b.bundleCode === this.scanCode ? { ...b, currentStage: this.selectedStage } : b));
    this.scanCode = '';
  }

  quickMove(b: CutBundle, stage: string): void {
    this.bundles.update(list => list.map(item => item.bundleCode === b.bundleCode ? { ...item, currentStage: stage } : item));
  }

  getStageBadgeClass(stage: string): string {
    switch (stage) {
      case 'CUT': return 'badge-stage-cut';
      case 'PRINTING': return 'badge-stage-printing';
      case 'EMBROIDERY': return 'badge-stage-embroidery';
      case 'SEWING': return 'badge-stage-sewing';
      case 'FINISHED': return 'badge-stage-finished';
      default: return 'badge-stage-cut';
    }
  }

  getStageIcon(stage: string): string {
    switch (stage) {
      case 'CUT': return 'bi-scissors';
      case 'PRINTING': return 'bi-palette';
      case 'EMBROIDERY': return 'bi-stars';
      case 'SEWING': return 'bi-activity';
      case 'FINISHED': return 'bi-check2-circle';
      default: return 'bi-tag';
    }
  }
}
