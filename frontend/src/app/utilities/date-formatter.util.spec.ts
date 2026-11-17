import { formatFactoryDate, getCurrentShift } from './date-formatter.util';

describe('Date Formatter Utilities', () => {
  it('should format date string into Indian standard display format', () => {
    const formatted = formatFactoryDate('2026-11-01');
    expect(formatted).toContain('2026');
  });

  it('should return "-" when date is null or undefined', () => {
    expect(formatFactoryDate(null)).toBe('-');
    expect(formatFactoryDate(undefined)).toBe('-');
  });

  it('should return a valid factory shift string', () => {
    const shift = getCurrentShift();
    expect(['Shift A (Day)', 'Shift B (Evening)', 'Shift C (Night)']).toContain(shift);
  });
});
