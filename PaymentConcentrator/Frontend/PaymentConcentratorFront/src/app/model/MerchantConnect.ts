export class MerchantConnect {
    public username: String;
    public password: String;
    public name:string;
    public url:string;

    constructor(username: String, password: String, name: string, url: string) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.url = url;
    }
}