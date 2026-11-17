import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import {
  ApiResponse,
  Buyer,
  ProductionOrder,
  YarnStock,
  KnittingProgram,
  FabricRoll,
  DyeingBatch,
  CuttingOrder,
  CutBundle,
  SewingLine,
  SewingProductionLog,
  CartonPacking,
  PipelineSummary
} from '../models/yarn-manager.models';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private http = inject(HttpClient);
  private baseUrl = 'http://localhost:8080/api/v1';

  // Dashboard
  getPipelineSummary(): Observable<PipelineSummary> {
    return this.http.get<ApiResponse<PipelineSummary>>(`${this.baseUrl}/dashboard/summary`)
      .pipe(map(res => res.data));
  }

  // Orders
  getOrders(): Observable<ProductionOrder[]> {
    return this.http.get<ApiResponse<ProductionOrder[]>>(`${this.baseUrl}/orders`)
      .pipe(map(res => res.data));
  }

  createOrder(order: ProductionOrder): Observable<ProductionOrder> {
    return this.http.post<ApiResponse<ProductionOrder>>(`${this.baseUrl}/orders`, order)
      .pipe(map(res => res.data));
  }

  // Yarn Stock
  getYarnStock(): Observable<YarnStock[]> {
    return this.http.get<ApiResponse<YarnStock[]>>(`${this.baseUrl}/yarn`)
      .pipe(map(res => res.data));
  }

  recordYarnInward(stock: YarnStock): Observable<YarnStock> {
    return this.http.post<ApiResponse<YarnStock>>(`${this.baseUrl}/yarn/inward`, stock)
      .pipe(map(res => res.data));
  }

  // Knitting
  getKnittingPrograms(): Observable<KnittingProgram[]> {
    return this.http.get<ApiResponse<KnittingProgram[]>>(`${this.baseUrl}/knitting/programs`)
      .pipe(map(res => res.data));
  }

  createKnittingProgram(orderId: number, program: Partial<KnittingProgram>): Observable<KnittingProgram> {
    return this.http.post<ApiResponse<KnittingProgram>>(`${this.baseUrl}/knitting/programs?orderId=${orderId}`, program)
      .pipe(map(res => res.data));
  }

  registerFabricRoll(programId: number, weightKg: number, actualGsm: number, inspectedBy: string): Observable<FabricRoll> {
    return this.http.post<ApiResponse<FabricRoll>>(
      `${this.baseUrl}/knitting/programs/${programId}/rolls?weightKg=${weightKg}&actualGsm=${actualGsm}&inspectedBy=${encodeURIComponent(inspectedBy)}`,
      {}
    ).pipe(map(res => res.data));
  }

  // Dyeing
  getDyeingBatches(): Observable<DyeingBatch[]> {
    return this.http.get<ApiResponse<DyeingBatch[]>>(`${this.baseUrl}/dyeing/batches`)
      .pipe(map(res => res.data));
  }

  createOutwardDc(orderId: number, dyeingUnitName: string, sentWeightKg: number, pantoneShade: string, finishType: string): Observable<DyeingBatch> {
    const params = `orderId=${orderId}&dyeingUnitName=${encodeURIComponent(dyeingUnitName)}&sentWeightKg=${sentWeightKg}&pantoneShade=${encodeURIComponent(pantoneShade)}&finishType=${encodeURIComponent(finishType)}`;
    return this.http.post<ApiResponse<DyeingBatch>>(`${this.baseUrl}/dyeing/outward-dc?${params}`, {})
      .pipe(map(res => res.data));
  }

  recordInwardDc(batchId: number, receivedWeightKg: number): Observable<DyeingBatch> {
    return this.http.post<ApiResponse<DyeingBatch>>(`${this.baseUrl}/dyeing/batches/${batchId}/inward-dc?receivedWeightKg=${receivedWeightKg}`, {})
      .pipe(map(res => res.data));
  }

  // Cutting & Bundles
  getCuttingOrders(): Observable<CuttingOrder[]> {
    return this.http.get<ApiResponse<CuttingOrder[]>>(`${this.baseUrl}/cutting/orders`)
      .pipe(map(res => res.data));
  }

  updateBundleStage(bundleCode: string, newStage: string): Observable<CutBundle> {
    return this.http.put<ApiResponse<CutBundle>>(`${this.baseUrl}/cutting/bundles/stage?bundleCode=${encodeURIComponent(bundleCode)}&newStage=${encodeURIComponent(newStage)}`, {})
      .pipe(map(res => res.data));
  }

  // Sewing Lines
  getSewingLines(): Observable<SewingLine[]> {
    return this.http.get<ApiResponse<SewingLine[]>>(`${this.baseUrl}/sewing/lines`)
      .pipe(map(res => res.data));
  }

  logSewingOutput(lineId: number, bundleCode: string, operatorName: string, passed: number, reworked: number, rejected: number): Observable<SewingProductionLog> {
    const params = `lineId=${lineId}&bundleCode=${encodeURIComponent(bundleCode)}&operatorName=${encodeURIComponent(operatorName)}&passed=${passed}&reworked=${reworked}&rejected=${rejected}`;
    return this.http.post<ApiResponse<SewingProductionLog>>(`${this.baseUrl}/sewing/logs?${params}`, {})
      .pipe(map(res => res.data));
  }

  // Packing
  getCartons(): Observable<CartonPacking[]> {
    return this.http.get<ApiResponse<CartonPacking[]>>(`${this.baseUrl}/packing/cartons`)
      .pipe(map(res => res.data));
  }
}
