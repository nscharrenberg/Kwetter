import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { User } from '../_models';
import {KWETTER_V1_API} from "../_helpers/api-constants";
import {Role} from "../_models/role";
import {Tweet} from "../_models/tweet";

@Injectable()
export class TweetService {
    constructor(private http: HttpClient) { }

    /**
     * Get All Tweets
     * @returns {Observable<tweet[]>}
     */
    getAll() {
        return this.http.get<Tweet[]>(KWETTER_V1_API("tweets"));
    }


    /**
     * Get my Tweets
     * @param {number} id
     * @returns {Observable<Tweet[]>}
     */
    getMyTweets(id: number) {
        return this.http.get<Tweet[]>(KWETTER_V1_API("tweets/my/" + id));
    }

    /**
     * Get My Timeline
     * @param {number} id
     * @returns {Observable<Tweet[]>}
     */
    getMyTimeLine(id: number) {
        return this.http.get<Tweet[]>(KWETTER_V1_API("timeline" + id));
    }

    /**
     * Get Tweet By Id
     * @param {number} id
     * @returns {Observable<Tweet>}
     */
    getById(id: number) {
        return this.http.get<Tweet>(KWETTER_V1_API("tweets/" + id));
    }

    /**
     * Get Tweet by Author username
     * @param {string} username
     * @returns {Observable<Tweet>}
     */
    getByAuthorUsername(username: string) {
        return this.http.get<Tweet>(KWETTER_V1_API("tweets/author/name/" + username));
    }

    /**
     * Get Tweet by Author ID
     * @param {number} id
     * @returns {Observable<Tweet>}
     */
    getByAuthorId(id: number) {
        return this.http.get<Tweet>(KWETTER_V1_API("tweets/author/" + id));
    }

    /**
     * Update a Tweet
     * @param {Tweet} tweet
     * @returns {Observable<Object>}
     */
    update(tweet: Tweet) {
        return this.http.patch(KWETTER_V1_API("tweets/" + tweet.id), tweet);
    }

    /**
     * Delete a Tweet
     * @param {Tweet} tweet
     * @returns {Observable<Object>}
     */
    delete(tweet: Tweet) {
        return this.http.delete(KWETTER_V1_API("tweets/" + tweet.id));
    }

    /**
     * Like a Tweet
     * @param {number} tweet
     * @param {number} user
     * @returns {Observable<Object>}
     */
    like(tweet: number, user: number) {
        return this.http.post(KWETTER_V1_API("tweets/" + tweet + "/like"), {
            "tweetId": tweet,
            "userId": user
        });
    }

    /**
     * Unlike a Tweet
     * @param {number} tweet
     * @param {number} user
     * @returns {Observable<Object>}
     */
    unlike(tweet: number, user: number) {
        return this.http.post(KWETTER_V1_API("tweets/" + tweet + "/unlike"), {
            "tweetId": tweet,
            "userId": user
        });
    }
}
