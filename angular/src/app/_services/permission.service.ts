import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { User } from '../_models';
import {KWETTER_V1_API} from "../_helpers/api-constants";
import {Role} from "../_models/role";
import {Permission} from "../_models/permission";

@Injectable()
export class PermissionService {
    constructor(private http: HttpClient) { }

    /**
     * Get Permission by Id
     * @param {number} id
     * @returns {Observable<Permission>}
     */
    getById(id: number) {
        return this.http.get<Permission>(KWETTER_V1_API("permissions/" + id));
    }

    /**
     * Get Permission By Name
     * @param {string} name
     * @returns {Observable<Permission>}
     */
    getByName(name: string) {
        return this.http.get<Permission>(KWETTER_V1_API("permissions/name/" + name))
    }
}
