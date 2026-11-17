import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../core/services/api.service';
import { YarnStock } from '../../core/models/yarn-manager.models';

@Component({
  selector: 'app-yarn-stock',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './yarn-stock.component.html'
})
export class YarnStockComponent implements OnInit {
  private api = inject(ApiService);
  stockList = signal<YarnStock[]>([
    { id: 1, millName: 'KPR Mill Ltd, Coimbatore', lotNumber: 'LOT-KPR-30SC-892', yarnCount: '30s Combed Cotton', yarnType: '100% Cotton', totalBags: 50, netWeightKg: 2500, ratePerKg: 285, receivedDate: '2026-11-01' },
    { id: 2, millName: 'Ramco Industries, Rajapalayam', lotNumber: 'LOT-RMC-34SL-114', yarnCount: '34s Lycra Spun', yarnType: '95% Cotton 5% Spandex', totalBags: 30, netWeightKg: 1500, ratePerKg: 340, receivedDate: '2026-11-03' },
    { id: 3, millName: 'Bannari Amman Spinning Mills', lotNumber: 'LOT-BASM-24SM-042', yarnCount: '24s Melange Cotton', yarnType: 'Melange', totalBags: 40, netWeightKg: 2000, ratePerKg: 310, receivedDate: '2026-11-05' }
  ]);

  showModal = signal(false);
  newStock: YarnStock = {
    millName: '',
    lotNumber: '',
    yarnCount: '30s Combed Cotton',
    yarnType: '100% Cotton',
    totalBags: 20,
    netWeightKg: 1000,
    ratePerKg: 290,
    receivedDate: new Date().toISOString().substring(0, 10)
  };

  ngOnInit(): void {
    this.api.getYarnStock().subscribe({
      next: (data) => {
        if (data && data.length > 0) this.stockList.set(data);
      },
      error: () => {}
    });
  }

  toggleInwardModal(): void {
    this.showModal.update(v => !v);
  }

  saveInward(): void {
    this.api.recordYarnInward(this.newStock).subscribe({
      next: (saved) => {
        this.stockList.update(list => [saved, ...list]);
        this.toggleInwardModal();
      },
      error: () => {
        this.stockList.update(list => [{ ...this.newStock, id: Date.now() }, ...list]);
        this.toggleInwardModal();
      }
    });
  }
}
