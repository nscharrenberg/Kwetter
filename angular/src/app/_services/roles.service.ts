import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { User } from '../_models';
import {KWETTER_V1_API} from "../_helpers/api-constants";
import {Role} from "../_models/role";

@Injectable()
export class RoleService {
    constructor(private http: HttpClient) { }

    /**
     * Get All Roles
     * @returns {Observable<Role[]>}
     */
    getAll() {
        console.log(KWETTER_V1_API("users"));
        return this.http.get<Role[]>(KWETTER_V1_API("roles"));
    }

    /**
     * Get a Role by ID
     * @param {number} id
     * @returns {Observable<Role>}
     */
    getById(id: number) {
        return this.http.get<User>(KWETTER_V1_API("roles/" + id));
    }

    /**
     * Get a Role by Name
     * @param {string} name
     * @returns {Observable<Role>}
     */
    getByName(name: string) {
        return this.http.get<Role>(KWETTER_V1_API("roles/name/" + name))
    }
}
