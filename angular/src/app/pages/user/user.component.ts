import {Component, NgZone, OnInit} from '@angular/core';
import {Tweet} from "../../_models/tweet";
import {AlertService, AuthenticationService, TweetService, UserService} from "../../_services";
import {User} from "../../_models";
import {Observable} from "rxjs/internal/Observable";
import LatLng = google.maps.LatLng;
import {Observer} from "rxjs/internal/types";
import {ActivatedRoute, Router} from "@angular/router";
import {catchError, takeUntil} from "rxjs/operators";
import {of} from "rxjs/internal/observable/of";


@Component({
    styleUrls: ['./user.component.scss'],
    templateUrl: 'user.component.html'
})
export class UserComponent implements OnInit {
    private paramId: number = null;
    loggedIn: User = null;
    tweets: Observable<Tweet[]> = null;
    user: Observable<User> = null;
    followers: User[] = null;
    geocoder: google.maps.Geocoder = null;
    address: any = null;

    constructor(
        private router: Router,
        private activatedRoute: ActivatedRoute,
        private zone: NgZone,
        private alertService: AlertService,
        private tweetService: TweetService,
        private authenticationService: AuthenticationService,
        private userService: UserService) {
        this.geocoder = new google.maps.Geocoder();
    }

    ngOnInit() {
        this.activatedRoute.paramMap.forEach(params => {
            this.paramId = parseInt(params.get("id"));
        });


        this.loggedIn = this.authenticationService.getLoggedInUser();
        this.tweets = this.tweetService.getMyTimeLine(this.paramId, 1, 10);
        this.user = this.userService.getById(this.paramId);

        this.user.forEach(r => {
            this.geocode(r.longitude, r.latitude).forEach(
                (results: google.maps.GeocoderResult[]) => {
                    this.address = results[0].formatted_address;
                })
                .then(() => console.log('Geocoding service: completed.'))
                .catch((error: google.maps.GeocoderStatus) => {
                    if (error === google.maps.GeocoderStatus.ZERO_RESULTS) {
                        console.log('Geocoding service: geocoder failed due to: ' + error);
                    }
                });
        });

        this.getFollowers();
    }

    getFollowers() {
        this.user.forEach(u => {
            this.followers = u.followers.sort(() => Math.random() * u.followers.length);
            return;
        });
    }

    geocode(long: number, lat: number): Observable<google.maps.GeocoderResult[]> {
        return Observable.create((observer: Observer<google.maps.GeocoderResult[]>) => {
            // Invokes geocode method of Google Maps API geocoding.
            this.geocoder.geocode({ location: new LatLng(lat, long)}, (
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

    follow() {
        this.user.forEach(u => {
            this.userService.follow(this.loggedIn.id, u.id).forEach(data => {
                this.alertService.success("You are now following " + data.username);
            }).catch(error => {
                this.alertService.error(error.error.toString());
            });
        });
    }

    unfollow() {
        this.user.forEach(u => {
            this.userService.unfollow(this.loggedIn.id, u.id).forEach(data => {
                this.alertService.success("You are not following " + data.username + " anymore");
            }).catch(error => {
                this.alertService.error(error.error.toString());
            });
        });
    }
}