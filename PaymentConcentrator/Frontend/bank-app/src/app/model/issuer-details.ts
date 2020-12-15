export class IssuerDetails {
    public number: String;
    public securityCode: String;
    public cardHolderName: String;
    public expDate: String;
    public paymentId: number;

    constructor(number: String, securityCode: String, cardHolderName: String, expDate: String, paymentId: number) {
        this.number = number;
        this.securityCode = securityCode;
        this.cardHolderName = cardHolderName;
        this.expDate = expDate;
        this.paymentId = paymentId;
    }
}