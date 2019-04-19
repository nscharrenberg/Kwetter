import {Component, EventEmitter, Input, NgZone, OnInit, Output, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AlertService, AuthenticationService, TweetService, UserService} from "../../_services";
import {Router} from "@angular/router";
import {first} from "rxjs/operators";
import {User} from "../../_models";
import {Observable} from "rxjs/internal/Observable";
import {Observer} from "rxjs/internal/types";
import LatLng = google.maps.LatLng;
import {Tweet} from "../../_models/tweet";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
    @Input() adressType: string;
    @Output() setAddress: EventEmitter<any> = new EventEmitter();
    @ViewChild('googleAddress') addresstext: any;

    registerForm: FormGroup;
    loading = false;
    submitted = false;

    loggedIn: User;
    tweets: Observable<Tweet[]>;
    user: Observable<User>;
    geocoder: google.maps.Geocoder;
    address: any;
    public latitude: number;
    public longitude: number;

    constructor(
        private formBuilder: FormBuilder,
        private router: Router,
        private zone: NgZone,
        private tweetService: TweetService,
        private userService: UserService,
        private alertService: AlertService,
        private authenticationService: AuthenticationService) {
        this.geocoder = new google.maps.Geocoder();
    }

    ngOnInit() {
        this.loggedIn = this.authenticationService.getLoggedInUser();
        this.tweets = this.tweetService.getMyTimeLine(this.loggedIn.id, 1, 10);
        this.user = this.userService.getById(this.loggedIn.id);

        this.latitude = this.loggedIn.latitude;
        this.longitude = this.loggedIn.longitude;

        this.executeGeocode();


        this.getPlaceAutocomplete();
        this.registerForm = this.formBuilder.group({
            id: [this.loggedIn.id, [Validators.required]],
            email: [this.loggedIn.email, Validators.required],
            username: [this.loggedIn.username, Validators.required],
            password: ['', []],
            biography: [this.loggedIn.biography, Validators.required],
            website: [this.loggedIn.website, Validators.required],
            longitude: [this.longitude, [Validators.required]],
            latitude: [this.latitude, [Validators.required]],
            firstname: [this.loggedIn.firstname, Validators.required],
            lastname: [this.loggedIn.lastname, Validators.required],
            avatar: [this.loggedIn.avatar, Validators.required],
            address: [this.address, [Validators.required]],
        });

        this.user.forEach(data => {
            this.registerForm.controls["email"].setValue(data.email);
            this.registerForm.controls["username"].setValue(data.username);
            this.registerForm.controls["firstname"].setValue(data.firstname);
            this.registerForm.controls["lastname"].setValue(data.lastname);
            this.registerForm.controls["biography"].setValue(data.biography);
            this.registerForm.controls["website"].setValue(data.website);
            this.registerForm.controls["avatar"].setValue(data.avatar);
            this.longitude = data.longitude;
            this.latitude = data.latitude;

            this.executeGeocode();

        }).catch(error => {
            this.alertService.error(error.error.toString());
        });
    }

    // convenience getter for easy access to form fields
    get f() { return this.registerForm.controls; }

    onSubmit() {
        this.submitted = true;

        // stop here if form is invalid
        if (this.registerForm.invalid) {
            return;
        }

        this.loading = true;
        this.userService.update(this.registerForm.value)
            .pipe(first())
            .subscribe(
                data => {
                    this.alertService.success('Profile Updated', true);
                },
                error => {
                    this.alertService.error(error.error);
                    this.loading = false;
                });
    }

    private getPlaceAutocomplete() {
        const autocomplete = new google.maps.places.Autocomplete(this.addresstext.nativeElement,
            {
                types: ["address"]  // 'establishment' / 'address' / 'geocode'
            });
        google.maps.event.addListener(autocomplete, 'place_changed', () => {
            this.zone.run(() => {
                //get the place result
                let place: google.maps.places.PlaceResult = autocomplete.getPlace();

                //verify result
                if (place.geometry === undefined || place.geometry === null) {
                    return;
                }

                //set latitude, longitude and zoom
                this.registerForm.controls["longitude"].setValue(place.geometry.location.lng());
                this.registerForm.controls["latitude"].setValue(place.geometry.location.lat());
                this.latitude = place.geometry.location.lat();
                this.longitude = place.geometry.location.lng();
            });
        });
    }

    executeGeocode() {
        this.geocode().forEach(
            (results: google.maps.GeocoderResult[]) => {
                this.address = results[0].formatted_address;

                this.registerForm.controls["address"].setValue(this.address);
            })
            .then(() => console.log('Geocoding service: completed.'))
            .catch((error: google.maps.GeocoderStatus) => {
                if (error === google.maps.GeocoderStatus.ZERO_RESULTS) {
                    console.log('Geocoding service: geocoder failed due to: ' + error);
                }
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
