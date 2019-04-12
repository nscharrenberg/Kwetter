import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { User } from '../_models';
import {KWETTER_V1_API} from "../_helpers/api-constants";
import {Role} from "../_models/role";
import {Tweet} from "../_models/tweet";

@Injectable()
export class TweetService {
    constructor(private http: HttpClient) { }

    search() {
        return this.http.get<any>(KWETTER_V1_API("tweets"));
    }
}
