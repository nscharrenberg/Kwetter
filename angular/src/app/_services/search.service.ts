import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import {KWETTER_V1_API} from "../_helpers/api-constants";

@Injectable()
export class SearchService {
    constructor(private http: HttpClient) { }

    search() {
        return this.http.get<any>(KWETTER_V1_API("tweets"));
    }
}
