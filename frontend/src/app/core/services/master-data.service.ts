import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApiResponse } from '../models/yarn-manager.models';
import {
  CompanyMaster,
  ProductMaster,
  UomMaster,
  CustomerMaster,
  PricingMaster,
  CurrencyExchangeRate,
  ResolvedRateDto
} from '../models/master-data.models';

@Injectable({
  providedIn: 'root'
})
export class MasterDataService {
  private http = inject(HttpClient);
  private baseUrl = 'http://localhost:8080/api/v1/master';

  // 1. Company Master
  getCompanies(activeOnly = false): Observable<ApiResponse<CompanyMaster[]>> {
    const params = new HttpParams().set('activeOnly', activeOnly.toString());
    return this.http.get<ApiResponse<CompanyMaster[]>>(`${this.baseUrl}/company`, { params });
  }

  getCompanyById(id: number): Observable<ApiResponse<CompanyMaster>> {
    return this.http.get<ApiResponse<CompanyMaster>>(`${this.baseUrl}/company/${id}`);
  }

  saveCompany(company: CompanyMaster): Observable<ApiResponse<CompanyMaster>> {
    return this.http.post<ApiResponse<CompanyMaster>>(`${this.baseUrl}/company`, company);
  }

  updateCompany(id: number, company: CompanyMaster): Observable<ApiResponse<CompanyMaster>> {
    return this.http.put<ApiResponse<CompanyMaster>>(`${this.baseUrl}/company/${id}`, company);
  }

  // 2. Product Master (Garment Styles)
  getProducts(activeOnly = false, category?: string): Observable<ApiResponse<ProductMaster[]>> {
    let params = new HttpParams().set('activeOnly', activeOnly.toString());
    if (category) {
      params = params.set('category', category);
    }
    return this.http.get<ApiResponse<ProductMaster[]>>(`${this.baseUrl}/products`, { params });
  }

  getProductById(id: number): Observable<ApiResponse<ProductMaster>> {
    return this.http.get<ApiResponse<ProductMaster>>(`${this.baseUrl}/products/${id}`);
  }

  saveProduct(product: ProductMaster): Observable<ApiResponse<ProductMaster>> {
    return this.http.post<ApiResponse<ProductMaster>>(`${this.baseUrl}/products`, product);
  }

  updateProduct(id: number, product: ProductMaster): Observable<ApiResponse<ProductMaster>> {
    return this.http.put<ApiResponse<ProductMaster>>(`${this.baseUrl}/products/${id}`, product);
  }

  // 3. UOM
  getUoms(activeOnly = false): Observable<ApiResponse<UomMaster[]>> {
    const params = new HttpParams().set('activeOnly', activeOnly.toString());
    return this.http.get<ApiResponse<UomMaster[]>>(`${this.baseUrl}/uom`, { params });
  }

  getUomByCode(code: string): Observable<ApiResponse<UomMaster>> {
    return this.http.get<ApiResponse<UomMaster>>(`${this.baseUrl}/uom/${code}`);
  }

  saveUom(uom: UomMaster): Observable<ApiResponse<UomMaster>> {
    return this.http.post<ApiResponse<UomMaster>>(`${this.baseUrl}/uom`, uom);
  }

  convertUom(quantity: number, from: string, to: string): Observable<ApiResponse<number>> {
    const params = new HttpParams()
      .set('quantity', quantity.toString())
      .set('from', from)
      .set('to', to);
    return this.http.get<ApiResponse<number>>(`${this.baseUrl}/uom/convert`, { params });
  }

  // 4. Customers
  getCustomers(activeOnly = false): Observable<ApiResponse<CustomerMaster[]>> {
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

  // 5. Pricing (Calendar & Multi-Period)
  getPricing(activeOnly = false, processType?: string, effectiveDate?: string): Observable<ApiResponse<PricingMaster[]>> {
    let params = new HttpParams().set('activeOnly', activeOnly.toString());
    if (processType) {
      params = params.set('processType', processType);
    }
    if (effectiveDate) {
      params = params.set('effectiveDate', effectiveDate);
    }
    return this.http.get<ApiResponse<PricingMaster[]>>(`${this.baseUrl}/pricing`, { params });
  }

  savePrice(price: PricingMaster): Observable<ApiResponse<PricingMaster>> {
    return this.http.post<ApiResponse<PricingMaster>>(`${this.baseUrl}/pricing`, price);
  }

  resolveRate(
    processType: string,
    spec: string,
    buyerId?: number,
    orderDate?: string,
    targetCurrency?: string
  ): Observable<ApiResponse<ResolvedRateDto>> {
    let params = new HttpParams()
      .set('processType', processType)
      .set('spec', spec);
    if (buyerId) {
      params = params.set('buyerId', buyerId.toString());
    }
    if (orderDate) {
      params = params.set('orderDate', orderDate);
    }
    if (targetCurrency) {
      params = params.set('targetCurrency', targetCurrency);
    }
    return this.http.get<ApiResponse<ResolvedRateDto>>(`${this.baseUrl}/pricing/resolve`, { params });
  }

  // 6. Currency Exchange Rates
  getExchangeRates(calendarMonth?: string): Observable<ApiResponse<CurrencyExchangeRate[]>> {
    let params = new HttpParams();
    if (calendarMonth) {
      params = params.set('calendarMonth', calendarMonth);
    }
    return this.http.get<ApiResponse<CurrencyExchangeRate[]>>(`${this.baseUrl}/exchange-rates`, { params });
  }

  saveExchangeRate(rate: CurrencyExchangeRate): Observable<ApiResponse<CurrencyExchangeRate>> {
    return this.http.post<ApiResponse<CurrencyExchangeRate>>(`${this.baseUrl}/exchange-rates`, rate);
  }

  convertCurrency(amount: number, from: string, to: string, date?: string): Observable<ApiResponse<number>> {
    let params = new HttpParams()
      .set('amount', amount.toString())
      .set('from', from)
      .set('to', to);
    if (date) {
      params = params.set('date', date);
    }
    return this.http.get<ApiResponse<number>>(`${this.baseUrl}/exchange-rates/convert`, { params });
  }
}
