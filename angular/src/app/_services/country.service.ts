import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import {COUNTRY_API} from "../_helpers/api-constants";

@Injectable()
export class CountryService {
    constructor(private http: HttpClient) {
    }

    getAll() {
        return this.http.get(COUNTRY_API("all"));
    }
}
