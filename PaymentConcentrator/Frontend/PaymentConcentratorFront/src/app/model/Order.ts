export interface Order {
  merchantId: string;
  amount: number;
  paymentMethod: string;
  successUrl: string;
  failedUrl: string;
  errorUrl: string;
  paymentUrl: string;
}
