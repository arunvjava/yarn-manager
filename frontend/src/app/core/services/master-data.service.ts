import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApiResponse } from '../models/yarn-manager.models';
import { UomMaster, CustomerMaster, PricingMaster } from '../models/master-data.models';

@Injectable({
  providedIn: 'root'
})
export class MasterDataService {
  private http = inject(HttpClient);
  private baseUrl = 'http://localhost:8080/api/v1/master';

  // UOM
  getUoms(activeOnly = true): Observable<ApiResponse<UomMaster[]>> {
    const params = new HttpParams().set('activeOnly', activeOnly.toString());
    return this.http.get<ApiResponse<UomMaster[]>>(`${this.baseUrl}/uom`, { params });
  }

  getUomByCode(code: string): Observable<ApiResponse<UomMaster>> {
    return this.http.get<ApiResponse<UomMaster>>(`${this.baseUrl}/uom/${code}`);
  }

  convertUom(quantity: number, from: string, to: string): Observable<ApiResponse<number>> {
    const params = new HttpParams()
      .set('quantity', quantity.toString())
      .set('from', from)
      .set('to', to);
    return this.http.get<ApiResponse<number>>(`${this.baseUrl}/uom/convert`, { params });
  }

  // Customers
  getCustomers(activeOnly = true): Observable<ApiResponse<CustomerMaster[]>> {
    const params = new HttpParams().set('activeOnly', activeOnly.toString());
    return this.http.get<ApiResponse<CustomerMaster[]>>(`${this.baseUrl}/customers`, { params });
  }

  getCustomerById(id: number): Observable<ApiResponse<CustomerMaster>> {
    return this.http.get<ApiResponse<CustomerMaster>>(`${this.baseUrl}/customers/${id}`);
  }

  saveCustomer(customer: CustomerMaster): Observable<ApiResponse<CustomerMaster>> {
    return this.http.post<ApiResponse<CustomerMaster>>(`${this.baseUrl}/customers`, customer);
  }

  updateCustomer(id: number, customer: CustomerMaster): Observable<ApiResponse<CustomerMaster>> {
    return this.http.put<ApiResponse<CustomerMaster>>(`${this.baseUrl}/customers/${id}`, customer);
  }

  // Pricing
  getPricing(activeOnly = true, processType?: string): Observable<ApiResponse<PricingMaster[]>> {
    let params = new HttpParams().set('activeOnly', activeOnly.toString());
    if (processType) {
      params = params.set('processType', processType);
    }
    return this.http.get<ApiResponse<PricingMaster[]>>(`${this.baseUrl}/pricing`, { params });
  }

  resolveRate(processType: string, spec: string, buyerId?: number): Observable<ApiResponse<PricingMaster>> {
    let params = new HttpParams()
      .set('processType', processType)
      .set('spec', spec);
    if (buyerId) {
      params = params.set('buyerId', buyerId.toString());
    }
    return this.http.get<ApiResponse<PricingMaster>>(`${this.baseUrl}/pricing/resolve`, { params });
  }
}
