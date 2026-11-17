/**
 * Date and Shift formatting utilities for factory operations
 */

export function formatFactoryDate(dateStr?: string | Date | null): string {
  if (!dateStr) return '-';
  const d = new Date(dateStr);
  if (isNaN(d.getTime())) return String(dateStr);
  return d.toLocaleDateString('en-IN', {
    day: '2-digit',
    month: 'short',
    year: 'numeric'
  });
}

export function getCurrentShift(): 'Shift A (Day)' | 'Shift B (Evening)' | 'Shift C (Night)' {
  const hour = new Date().getHours();
  if (hour >= 6 && hour < 14) return 'Shift A (Day)';
  if (hour >= 14 && hour < 22) return 'Shift B (Evening)';
  return 'Shift C (Night)';
}
