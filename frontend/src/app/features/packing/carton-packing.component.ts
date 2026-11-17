import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../core/services/api.service';
import { CartonPacking } from '../../core/models/yarn-manager.models';

@Component({
  selector: 'app-carton-packing',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './carton-packing.component.html'
})
export class CartonPackingComponent implements OnInit {
  private api = inject(ApiService);

  cartons = signal<CartonPacking[]>([
    {
      id: 1,
      order: { orderNumber: 'PO-2026-TRP-001', styleName: 'Crew Neck', targetGsm: 180, colorShade: 'Navy', totalQuantity: 10000, deliveryDate: '2026-12-15', status: 'IN_PROGRESS' },
      cartonNumber: 'CTN-PO001-001',
      packingType: 'ASSORTED',
      sizeBreakdown: 'S:10, M:20, L:20, XL:10',
      totalGarments: 60,
      grossWeightKg: 14.50,
      cbm: 0.0850,
      isDispatched: false
    },
    {
      id: 2,
      order: { orderNumber: 'PO-2026-TRP-001', styleName: 'Crew Neck', targetGsm: 180, colorShade: 'Navy', totalQuantity: 10000, deliveryDate: '2026-12-15', status: 'IN_PROGRESS' },
      cartonNumber: 'CTN-PO001-002',
      packingType: 'ASSORTED',
      sizeBreakdown: 'S:10, M:20, L:20, XL:10',
      totalGarments: 60,
      grossWeightKg: 14.60,
      cbm: 0.0850,
      isDispatched: true
    }
  ]);

  ngOnInit(): void {
    this.api.getCartons().subscribe({
      next: (data) => { if (data && data.length > 0) this.cartons.set(data); },
      error: () => {}
    });
  }

  markDispatched(carton: CartonPacking): void {
    this.cartons.update(list => list.map(c => c.cartonNumber === carton.cartonNumber ? { ...c, isDispatched: true } : c));
  }
}
