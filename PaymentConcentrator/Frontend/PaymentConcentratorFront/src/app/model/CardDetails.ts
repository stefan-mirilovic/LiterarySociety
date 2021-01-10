export class CardDetails {
    public number: String;
    public securityCode: String;
    public cardHolderName: String;
    public expDate: String;
    public name:string;
    public url:string;

    constructor(number: String, securityCode: String, cardHolderName: String, expDate: String, name: string, url: string) {
        this.number = number;
        this.securityCode = securityCode;
        this.cardHolderName = cardHolderName;
        this.expDate = expDate;
        this.name = name;
        this.url = url;
    }
}