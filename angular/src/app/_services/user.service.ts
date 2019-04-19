import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { User } from '../_models';
import {KWETTER_V1_API} from "../_helpers/api-constants";
import {Tweet} from "../_models/tweet";

@Injectable()
export class UserService {
    constructor(private http: HttpClient) { }

    /**
     * Get All Users
     * @returns {Observable<User[]>}
     */
    getAll() {
        console.log(KWETTER_V1_API("users"));
        return this.http.get<User[]>(KWETTER_V1_API("users"));
    }

    /**
     * Get a User by ID
     * @param {number} id
     * @returns {Observable<User>}
     */
    getById(id: number) {
        return this.http.get<User>(KWETTER_V1_API("users/" + id));
    }

    /**
     * Get a User by Username
     * @param {string} username
     * @returns {Observable<User>}
     */
    getByUsername(username: string) {
        return this.http.get<User>(KWETTER_V1_API("users/username/" + username));
    }

    /**
     * Get a User By Email
     * @param {string} email
     * @returns {Observable<User>}
     */
    getByEmail(email: string) {
        return this.http.get<User>(KWETTER_V1_API("users/email/" + email));
    }

    /**
     * Creat a new User
     * @param {User} user
     * @returns {Observable<Object>}
     */
    register(user: User) {
        return this.http.post<User>(KWETTER_V1_API("auth/register"), user);
    }

    /**
     * Update user information
     * @param {User} user
     * @returns {Observable<Object>}
     */
    update(user: User) {
        console.log(JSON.stringify(user));

        return this.http.patch<User>(KWETTER_V1_API("users/" + user.id), user);
    }

    /**
     *
     * @param {number} me
     * @param {number} toFollow
     * @returns {Observable<Object>}
     */
    follow(me: number, toFollow: number) {
        return this.http.post<User>(KWETTER_V1_API("users/" + toFollow + "/follow"), {
            "userId": me,
            "toFollowId": toFollow
        });
    }

    unfollow(me: number, toUnfollow: number) {
        return this.http.post<User>(KWETTER_V1_API("users/" + toUnfollow + "/unfollow"), {
            "userId": me,
            "toFollowId": toUnfollow
        });
    }

    getRandomUsers(limitTo: number) {
        return this.http.get<User[]>(KWETTER_V1_API("users/randomUsers?limitTo=" + limitTo));
    }
}
