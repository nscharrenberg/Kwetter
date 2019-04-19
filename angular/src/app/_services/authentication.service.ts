import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';
import {KWETTER_V1_API} from "../_helpers/api-constants";
import {User} from "../_models";
import {Role} from "../_models/role";

@Injectable()
export class AuthenticationService {
    user: User;

    constructor(private http: HttpClient) { }

    login(username: string, password: string) {
        return this.http.post<any>(KWETTER_V1_API("auth/login"), { username: username, password: password })
            .pipe(map(user => {
                // login successful if there's a jwt token in the response
                if (user && user.token && user.user) {
                    // store user details and jwt token in local storage to keep user logged in between page refreshes
                    localStorage.setItem('currentUser', JSON.stringify(user));
                }

                return user;
            }));
    }

    logout() {
        // remove user from local storage to log user out
        localStorage.removeItem('currentUser');
    }

    getLoggedInUser() {
        let currentUser = JSON.parse(localStorage.getItem('currentUser'));

        if (currentUser && currentUser.token && currentUser.user) {
            this.user = new User();
            this.user.id = currentUser.user.id;
            this.user.username = currentUser.user.username;
            this.user.email = currentUser.user.email;
            this.user.biography = currentUser.user.biography;
            this.user.website = currentUser.user.website;
            this.user.longitude = currentUser.user.longitude;
            this.user.latitude = currentUser.user.latitude;
            this.user.firstname = currentUser.user.firstname;
            this.user.lastname = currentUser.user.lastname;
            this.user.avatar = currentUser.user.avatar;
            if(currentUser.user.role != undefined) {
                this.user.role = new Role();
                this.user.role.id = currentUser.user.role.id;
                this.user.role.name = currentUser.user.role.name;
            }


            return this.user;
        } else {
            this.logout();
        }
    }
}