import {Component, NgZone, OnInit} from '@angular/core';
import {Tweet} from "../../_models/tweet";
import {AuthenticationService, TweetService, UserService} from "../../_services";
import {User} from "../../_models";
import {Observable} from "rxjs/internal/Observable";
import LatLng = google.maps.LatLng;
import GeocoderStatus = google.maps.GeocoderStatus;
import {Observer} from "rxjs/internal/types";
import {Router} from "@angular/router";


@Component({
    styleUrls: ['./home.component.scss'],
    templateUrl: 'home.component.html'
})
export class HomeComponent implements OnInit {
    loggedIn: User;
    tweets: Observable<Tweet[]>;
    user: Observable<User>;
    followers: User[];
    geocoder: google.maps.Geocoder;
    address: any;

    constructor(
        private router: Router,
        private zone: NgZone,
        private tweetService: TweetService,
        private authenticationService: AuthenticationService,
        private userService: UserService) {
        this.geocoder = new google.maps.Geocoder();
    }

    ngOnInit() {
        this.loggedIn = this.authenticationService.getLoggedInUser();
        this.tweets = this.tweetService.getMyTimeLine(this.loggedIn.id, 1, 10);
        this.user = this.userService.getById(this.loggedIn.id);

        this.geocode().forEach(
            (results: google.maps.GeocoderResult[]) => {
                this.address = results[0].formatted_address;
            })
            .then(() => console.log('Geocoding service: completed.'))
            .catch((error: google.maps.GeocoderStatus) => {
                if (error === google.maps.GeocoderStatus.ZERO_RESULTS) {
                    console.log('Geocoding service: geocoder failed due to: ' + error);
                }
            });

        this.getFollowers();
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
                        console.log('Geocoding service: geocoder failed due to: ' + status);
                        observer.error(status);
                    }
                })
            );
        });
    }
}