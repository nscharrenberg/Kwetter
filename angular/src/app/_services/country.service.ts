import { HttpClient, HttpBackend } from '@angular/common/http';
import { Injectable } from '@angular/core';
import {COUNTRY_API} from "../_helpers/api-constants";

@Injectable()
export class CountryService {
    private http: HttpClient
    constructor(private handler: HttpBackend) {
        this.http = new HttpClient(handler);
    }

    getAll() {
        return this.http.get(COUNTRY_API("all"));
    }
}
