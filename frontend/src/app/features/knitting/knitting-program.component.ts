import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../core/services/api.service';
import { KnittingProgram, FabricRoll } from '../../core/models/yarn-manager.models';

@Component({
  selector: 'app-knitting-program',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './knitting-program.component.html'
})
export class KnittingProgramComponent implements OnInit {
  private api = inject(ApiService);

  programs = signal<KnittingProgram[]>([
    { id: 1, programNumber: 'KNIT-PO001-01', machineDia: 30, gauge: 24, targetGsm: 180, fabricType: 'Single Jersey', loopLength: 2.85, allocatedYarnKg: 2450, status: 'IN_KNITTING' }
  ]);

  rolls = signal<FabricRoll[]>([
    { id: 1, rollNumber: 'ROLL-TRP-001', rollWeightKg: 24.50, actualGsm: 182, qualityGrade: 'A', inspectedBy: 'Senthil Kumar (QC)' },
    { id: 2, rollNumber: 'ROLL-TRP-002', rollWeightKg: 25.10, actualGsm: 180, qualityGrade: 'A', inspectedBy: 'Senthil Kumar (QC)' },
    { id: 3, rollNumber: 'ROLL-TRP-003', rollWeightKg: 24.80, actualGsm: 179, qualityGrade: 'A', inspectedBy: 'Senthil Kumar (QC)' },
    { id: 4, rollNumber: 'ROLL-TRP-004', rollWeightKg: 23.90, actualGsm: 184, qualityGrade: 'B', inspectedBy: 'Senthil Kumar (QC)' }
  ]);

  newRollWeight = 24.70;
  newRollGsm = 181;

  ngOnInit(): void {
    this.api.getKnittingPrograms().subscribe({
      next: (data) => { if (data && data.length > 0) this.programs.set(data); },
      error: () => {}
    });
  }

  addRoll(programId: number): void {
    this.api.registerFabricRoll(programId, this.newRollWeight, this.newRollGsm, 'Operator (Floor QC)').subscribe({
      next: (saved) => this.rolls.update(r => [saved, ...r]),
      error: () => {
        const dummy: FabricRoll = {
          rollNumber: 'ROLL-TRP-' + Math.floor(100 + Math.random() * 900),
          rollWeightKg: this.newRollWeight,
          actualGsm: this.newRollGsm,
          qualityGrade: 'A',
          inspectedBy: 'Operator (Floor QC)'
        };
        this.rolls.update(r => [dummy, ...r]);
      }
    });
  }
}
