export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
  timestamp: string;
}

export interface Buyer {
  id?: number;
  name: string;
  buyerCode: string;
  country: string;
  contactPerson?: string;
  contactEmail?: string;
}

export interface ProductionOrder {
  id?: number;
  orderNumber: string;
  buyer?: Buyer;
  styleName: string;
  targetGsm: number;
  colorShade: string;
  totalQuantity: number;
  deliveryDate: string;
  status: 'PLANNED' | 'IN_PROGRESS' | 'COMPLETED' | 'CANCELLED';
}

export interface YarnStock {
  id?: number;
  millName: string;
  lotNumber: string;
  yarnCount: string;
  yarnType: string;
  totalBags: number;
  netWeightKg: number;
  ratePerKg: number;
  receivedDate: string;
}

export interface FabricRoll {
  id?: number;
  rollNumber: string;
  rollWeightKg: number;
  actualGsm: number;
  qualityGrade: string;
  inspectedBy: string;
}

export interface KnittingProgram {
  id?: number;
  order?: ProductionOrder;
  programNumber: string;
  machineDia: number;
  gauge: number;
  targetGsm: number;
  fabricType: string;
  loopLength: number;
  allocatedYarnKg: number;
  status: string;
  rolls?: FabricRoll[];
}

export interface DyeingBatch {
  id?: number;
  order?: ProductionOrder;
  batchNumber: string;
  dyeingUnitName: string;
  outwardDcNumber: string;
  sentWeightKg: number;
  receivedWeightKg?: number;
  pantoneShade: string;
  finishType: string;
  status: string;
  sentDate: string;
  receivedDate?: string;
}

export interface CutBundle {
  id?: number;
  bundleCode: string;
  size: string;
  quantity: number;
  currentStage: string;
}

export interface CuttingOrder {
  id?: number;
  order?: ProductionOrder;
  layNumber: string;
  totalPlies: number;
  totalCutPieces: number;
  fabricConsumedKg: number;
  cuttingWastageKg: number;
  bundles?: CutBundle[];
}

export interface SewingLine {
  id?: number;
  lineName: string;
  supervisorName: string;
  hourlyTargetPieces: number;
}

export interface SewingProductionLog {
  id?: number;
  sewingLine: SewingLine;
  bundle: CutBundle;
  operatorName: string;
  passedPieces: number;
  reworkedPieces: number;
  rejectedPieces: number;
  loggedAt: string;
}

export interface CartonPacking {
  id?: number;
  order: ProductionOrder;
  cartonNumber: string;
  packingType: string;
  sizeBreakdown: string;
  totalGarments: number;
  grossWeightKg: number;
  cbm: number;
  isDispatched: boolean;
  dispatchedDate?: string;
}

export interface PipelineSummary {
  totalYarnInwardKg: number;
  totalKnittedFabricKg: number;
  totalDyedFabricKg: number;
  totalCutPieces: number;
  totalSewnGarments: number;
  totalRejectedGarments: number;
  totalPackedGarments: number;
  overallWastagePercentage: number;
}
