export interface UomMaster {
  id?: number;
  uomCode: string; // 'KG' | 'BAG' | 'PCS' | 'MTR' | 'ROLL' | 'CONE' | 'CTN' | 'DOZ' | 'LBS' | 'YDS'
  uomName: string;
  category: 'WEIGHT' | 'COUNT' | 'LENGTH' | 'PACKAGING' | 'VOLUME';
  baseUom: boolean;
  conversionFactor: number;
  decimalPlaces: number;
  active: boolean;
}

export interface CustomerMaster {
  id?: number;
  name: string;
  buyerCode: string;
  brandNames?: string;
  country: string;
  currency: string;
  paymentTerms?: string;
  taxIdGstin?: string;
  contactPerson?: string;
  contactEmail?: string;
  contactPhone?: string;
  billingAddress?: string;
  shippingPort?: string;
  tolerancePercentage?: number;
  active: boolean;
}

export interface PricingMaster {
  id?: number;
  priceCode: string;
  processType: string; // 'KNITTING_JOBWORK' | 'DYEING_JOBWORK' | 'SEWING_CM' | 'YARN_PURCHASE' | 'FINISHING'
  categoryOrSpec: string;
  uomCode: string;
  baseRate: number;
  currency: string;
  buyerId?: number;
  effectiveFrom: string;
  effectiveTo?: string;
  notes?: string;
  active: boolean;
}
