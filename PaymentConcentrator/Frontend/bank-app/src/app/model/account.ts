export class Account {
    public id: number;
    public number: String;
    public securityCode: String;
    public name: String;
    public surname: String;
    public expDate: String;
    public funds: number;

    constructor(id: number, number: String, securityCode: String, name: String, surname: String, expDate: String, funds: number) {
        this.id = id;
        this.number = number;
        this.securityCode = securityCode;
        this.name = name;
        this.surname = surname;
        this.expDate = expDate;
        this.funds = funds;
    }
}