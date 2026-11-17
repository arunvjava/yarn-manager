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
}
