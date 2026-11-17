import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../core/services/api.service';
import { SewingLine, SewingProductionLog } from '../../core/models/yarn-manager.models';

@Component({
  selector: 'app-sewing-lines',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './sewing-lines.component.html'
})
export class SewingLinesComponent implements OnInit {
  private api = inject(ApiService);
  operatorName = 'Kavitha R';
  passedPcs = 25;
  reworkPcs = 1;
  rejectPcs = 0;

  lines = signal<SewingLine[]>([
    { id: 1, lineName: 'Line 1 - Overlock & Flatlock Assembly', supervisorName: 'Muruganandam (Supervisor)', hourlyTargetPieces: 120 },
    { id: 2, lineName: 'Line 2 - Polo Collar & Placket Special', supervisorName: 'Velusamy (Supervisor)', hourlyTargetPieces: 90 }
  ]);

  logs = signal<SewingProductionLog[]>([
    {
      id: 1,
      sewingLine: { lineName: 'Line 1', supervisorName: 'Muruganandam', hourlyTargetPieces: 120 },
      bundle: { bundleCode: 'BNDL-PO001-S-01', size: 'S', quantity: 25, currentStage: 'SEWING' },
      operatorName: 'Kavitha R',
      passedPieces: 24,
      reworkedPieces: 1,
      rejectedPieces: 0,
      loggedAt: '10:30 AM'
    },
    {
      id: 2,
      sewingLine: { lineName: 'Line 1', supervisorName: 'Muruganandam', hourlyTargetPieces: 120 },
      bundle: { bundleCode: 'BNDL-PO001-M-01', size: 'M', quantity: 25, currentStage: 'SEWING' },
      operatorName: 'Revathi M',
      passedPieces: 25,
      reworkedPieces: 0,
      rejectedPieces: 0,
      loggedAt: '11:30 AM'
    }
  ]);

  ngOnInit(): void {
    this.api.getSewingLines().subscribe({
      next: (data) => { if (data && data.length > 0) this.lines.set(data); },
      error: () => {}
    });
  }

  submitOutput(lineId: number): void {
    const newEntry: SewingProductionLog = {
      id: Date.now(),
      sewingLine: this.lines().find(l => l.id === lineId) || this.lines()[0],
      bundle: { bundleCode: 'BNDL-PO001-AUTO', size: 'M', quantity: this.passedPcs, currentStage: 'SEWING' },
      operatorName: this.operatorName,
      passedPieces: this.passedPcs,
      reworkedPieces: this.reworkPcs,
      rejectedPieces: this.rejectPcs,
      loggedAt: new Date().toLocaleTimeString()
    };
    this.logs.update(l => [newEntry, ...l]);
  }
}
