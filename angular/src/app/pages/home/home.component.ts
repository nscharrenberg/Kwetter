import {Component, NgZone, OnInit} from '@angular/core';
import {Tweet} from "../../_models/tweet";
import {AuthenticationService, TweetService, UserService} from "../../_services";
import {User} from "../../_models";
import {Observable} from "rxjs/internal/Observable";
import LatLng = google.maps.LatLng;
import {Observer} from "rxjs/internal/types";
import {Router} from "@angular/router";
import {WebsocketService} from "../../_services/websocket.service";
import {Subject} from "rxjs/internal/Subject";
import {KWETTER_WS} from "../../_helpers/api-constants";
import {WsTweetService} from "../../_services/wsTweetService";


@Component({
    styleUrls: ['./home.component.scss'],
    templateUrl: 'home.component.html'
})
export class HomeComponent implements OnInit {
    loggedIn: User;
    tweets: Tweet[];
    user: Observable<User>;
    followers: User[];
    geocoder: google.maps.Geocoder;
    address: any;

    constructor(
        private router: Router,
        private zone: NgZone,
        private tweetService: TweetService,
        private authenticationService: AuthenticationService,
        private userService: UserService,
        private wsTweetService: WsTweetService) {
        this.geocoder = new google.maps.Geocoder();
    }

    ngOnInit() {
        this.loggedIn = this.authenticationService.getLoggedInUser();
        this.tweetService.getMyTimeLine(this.loggedIn.id, 1, 10).subscribe((data: Tweet[]) => {
            this.tweets = data;
        }, (error) => {
            console.log("Error: " + JSON.stringify(error));
        });
        this.user = this.userService.getById(this.loggedIn.id);

        this.geocode().forEach(
            (results: google.maps.GeocoderResult[]) => {
                this.address = results[0].formatted_address;
            })
            .then(() => console.log('Geocoding com.nscharrenberg.kwetter.service: completed.'))
            .catch((error: google.maps.GeocoderStatus) => {
                if (error === google.maps.GeocoderStatus.ZERO_RESULTS) {
                    console.log('Geocoding com.nscharrenberg.kwetter.service: geocoder failed due to: ' + error);
                }
                console.log("FAIL");
            });

        this.getFollowers();
        this.wsTweetService.messages.subscribe(msg => {
            console.log("Response from websocket: " + msg);
            this.tweets.unshift(msg);
        });
    }

    getFollowers() {
        this.user.forEach(u => {
            if(u.followers == undefined || u.followers.length <= 0) {
                this.userService.getRandomUsers(5).forEach(u => {
                    this.followers = u;
                });
            }

            this.followers = u.followers.sort(() => Math.random() * u.followers.length);

            return;
        });
    }

    geocode(): Observable<google.maps.GeocoderResult[]> {
        return Observable.create((observer: Observer<google.maps.GeocoderResult[]>) => {
            // Invokes geocode method of Google Maps API geocoding.
            this.geocoder.geocode({ location: new LatLng(this.loggedIn.latitude, this.loggedIn.longitude)}, (
                (results: google.maps.GeocoderResult[], status: google.maps.GeocoderStatus) => {
                    if (status === google.maps.GeocoderStatus.OK) {
                        observer.next(results);
                        observer.complete();
                    } else {
                        observer.error(status);
                    }
                })
            );
        });
    }
}