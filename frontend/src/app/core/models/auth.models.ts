export interface UserSession {
  token: string;
  username: string;
  fullName: string;
  role: 'ADMIN' | 'YARN_MANAGER' | 'KNITTING_SUPERVISOR' | 'QC_INSPECTOR' | 'DISPATCH_OFFICER';
  permissions: string[];
}

export interface LoginPayload {
  username: string;
  password: string;
}
