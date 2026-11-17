import { calculateKnittingLoss, calculateDyeingWeightChange, calculateDHU, calculateCBM } from './textile-calc.util';

describe('Textile Calculation Utilities', () => {
  it('should calculate knitting loss percentage correctly', () => {
    // 2450 kg allocated yarn, 2405.9 kg fabric produced = ~1.8% loss
    const loss = calculateKnittingLoss(2450, 2405.9);
    expect(loss).toBe(1.8);
  });

  it('should return 0 when yarn allocated is 0 or negative', () => {
    expect(calculateKnittingLoss(0, 100)).toBe(0);
  });

  it('should calculate dyeing process weight difference %', () => {
    // 2400 kg sent, 2330 kg received = 2.92% loss
    const diff = calculateDyeingWeightChange(2400, 2330);
    expect(diff).toBe(2.92);
  });

  it('should calculate sewing line DHU defect percentage', () => {
    // 2 defects out of 100 inspected = 2% DHU
    const dhu = calculateDHU(2, 100);
    expect(dhu).toBe(2.0);
  });

  it('should calculate carton CBM volume accurately', () => {
    // Standard master carton 60cm x 40cm x 30cm = 0.072 CBM
    const cbm = calculateCBM(60, 40, 30, 1);
    expect(cbm).toBe(0.072);
  });
});
