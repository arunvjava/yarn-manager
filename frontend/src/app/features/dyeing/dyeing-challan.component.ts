import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../core/services/api.service';
import { DyeingBatch } from '../../core/models/yarn-manager.models';

@Component({
  selector: 'app-dyeing-challan',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './dyeing-challan.component.html'
})
export class DyeingChallanComponent implements OnInit {
  private api = inject(ApiService);
  reconciledWeight = 2330;

  batches = signal<DyeingBatch[]>([
    {
      id: 1,
      batchNumber: 'DYE-BATCH-2026-88',
      dyeingUnitName: 'SIPCOT Eco-Friendly Processing, Perundurai',
      outwardDcNumber: 'DC-OUT-9921',
      sentWeightKg: 2400.00,
      receivedWeightKg: 2330.00,
      pantoneShade: 'Navy Blue (19-4024 TPX)',
      finishType: 'Bio-Polish & Silicon Softener',
      status: 'RECEIVED',
      sentDate: '2026-11-08',
      receivedDate: '2026-11-12'
    },
    {
      id: 2,
      batchNumber: 'DYE-BATCH-2026-89',
      dyeingUnitName: 'Perundurai Colors & Washers',
      outwardDcNumber: 'DC-OUT-9934',
      sentWeightKg: 1500.00,
      pantoneShade: 'Anthracite Grey (19-3908 TPX)',
      finishType: 'Quick-Dry Anti-Odor Softener',
      status: 'SENT_TO_DYEING',
      sentDate: '2026-11-14'
    }
  ]);

  ngOnInit(): void {
    this.api.getDyeingBatches().subscribe({
      next: (data) => { if (data && data.length > 0) this.batches.set(data); },
      error: () => {}
    });
  }

  receiveInward(batchId: number): void {
    this.api.recordInwardDc(batchId, this.reconciledWeight).subscribe({
      next: (updated) => {
        this.batches.update(list => list.map(b => b.id === batchId ? updated : b));
      },
      error: () => {
        this.batches.update(list => list.map(b => b.id === batchId ? { ...b, receivedWeightKg: this.reconciledWeight, status: 'RECEIVED' } : b));
      }
    });
  }
}
