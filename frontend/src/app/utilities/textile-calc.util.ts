/**
 * Tiruppur Textile & Garment Manufacturing Calculation Utilities
 */

/**
 * Calculates percentage weight loss during circular knitting
 * @param yarnAllocatedKg Allocated raw yarn cones weight (kg)
 * @param fabricProducedKg Actual produced grey rolls weight (kg)
 * @returns percentage loss rounded to 2 decimal places
 */
export function calculateKnittingLoss(yarnAllocatedKg: number, fabricProducedKg: number): number {
  if (!yarnAllocatedKg || yarnAllocatedKg <= 0) return 0;
  const loss = Math.max(0, yarnAllocatedKg - fabricProducedKg);
  return Number(((loss / yarnAllocatedKg) * 100).toFixed(2));
}

/**
 * Calculates Dyeing & Wet Processing weight difference %
 * @param sentWeightKg Grey fabric sent under Outward DC (kg)
 * @param receivedWeightKg Dyed & compacted fabric received under Inward DC (kg)
 */
export function calculateDyeingWeightChange(sentWeightKg: number, receivedWeightKg: number): number {
  if (!sentWeightKg || sentWeightKg <= 0) return 0;
  const diff = sentWeightKg - receivedWeightKg;
  return Number(((diff / sentWeightKg) * 100).toFixed(2));
}

/**
 * Calculates DHU (Defects per Hundred Units) for sewing assembly lines
 * @param totalDefects Total defective or rejected garments
 * @param totalInspected Total units checked by quality line inspector
 */
export function calculateDHU(totalDefects: number, totalInspected: number): number {
  if (!totalInspected || totalInspected <= 0) return 0;
  return Number(((totalDefects / totalInspected) * 100).toFixed(2));
}

/**
 * Calculates total carton shipping volume in Cubic Meters (CBM)
 * @param lengthCm Length in centimeters
 * @param widthCm Width in centimeters
 * @param heightCm Height in centimeters
 * @param cartonCount Total number of boxes
 */
export function calculateCBM(lengthCm: number, widthCm: number, heightCm: number, cartonCount: number = 1): number {
  const cbmPerBox = (lengthCm * widthCm * heightCm) / 1_000_000;
  return Number((cbmPerBox * cartonCount).toFixed(4));
}
