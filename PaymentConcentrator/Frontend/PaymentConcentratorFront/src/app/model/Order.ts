export interface Order {
  merchantId: string;
  amount: number;
  paymentMethod: string;
  successUrl: string;
  failedUrl: string;
  errorUrl: string;
  paymentUrl: string;
  frequency: string;
  interval: string;
  cycles: number;
  /*String frequency;
	private String interval;
	private Integer cycles; */
}
