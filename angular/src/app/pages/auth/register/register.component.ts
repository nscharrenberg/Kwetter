import {Component, OnInit, NgZone, EventEmitter, Input, Output, ViewChild} from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { first } from 'rxjs/operators';
import {AlertService, UserService} from "../../../_services";

@Component({
    styleUrls: ['./register.component.scss'],
    templateUrl: 'register.component.html'
})
export class RegisterComponent implements OnInit {
    @Input() adressType: string;
    @Output() setAddress: EventEmitter<any> = new EventEmitter();
    @ViewChild('googleAddress') addresstext: any;

    registerForm: FormGroup;
    loading = false;
    submitted = false;

    // Google Maps Specifics
    public latitude: number;
    public longitude: number;


    constructor(
        private formBuilder: FormBuilder,
        private router: Router,
        private zone: NgZone,
        private userService: UserService,
        private alertService: AlertService) { }

    ngOnInit() {
        this.getPlaceAutocomplete();
        this.registerForm = this.formBuilder.group({
            email: ['', Validators.required],
            username: ['', Validators.required],
            password: ['', [Validators.required, Validators.minLength(6)]],
            biography: ['', Validators.required],
            website: ['', Validators.required],
            longitude: [this.longitude, [Validators.required]],
            latitude: [this.latitude, [Validators.required]],
            firstname: ['', Validators.required],
            lastname: ['', Validators.required],
            avatar: ['', Validators.required],
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
        this.userService.register(this.registerForm.value)
            .pipe(first())
            .subscribe(
                data => {
                    this.alertService.success('Registration successful', true);
                    this.router.navigate(['/auth/login']);
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
                this.latitude = place.geometry.location.lat();
                this.longitude = place.geometry.location.lng();
            });
        });
    }
}
