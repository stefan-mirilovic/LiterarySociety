export class AccountCreate {
    public name: string;
    public surname: string;

    constructor(name: string, surname: string) {
        this.name = name;
        this.surname = surname;
    }
}