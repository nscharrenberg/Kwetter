/// <reference types="@types/googlemaps" />
import {
    Component,
    NgZone,
    OnInit,
} from "@angular/core";
import {AlertService, AuthenticationService, UserService} from "../../_services";
import {User} from "../../_models";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {first} from "rxjs/operators";
import {Router} from "@angular/router";

@Component({
    selector: 'app-navbar',
    styleUrls: ['./navbar.component.scss'],
    templateUrl: 'navbar.component.html'
})
export class NavbarComponent  implements OnInit {
    private searchForm: FormGroup;
    private user: User;
    private searchValue: string;

    constructor(
        private formBuilder: FormBuilder,
        private authenticationService: AuthenticationService,
        private userService: UserService,
        private router: Router,
        private alertService: AlertService) {
    }

    ngOnInit() {
        this.user = this.authenticationService.getLoggedInUser();
        this.searchForm = this.formBuilder.group({
            searchValue: ['', []],
        });
    }

    // convenience getter for easy access to form fields
    get getSearchFrom() { return this.searchForm.controls; }

    search() {
        console.log("SER: " + JSON.stringify(this.getSearchFrom.searchValue.value));
        this.userService.getByUsername(this.getSearchFrom.searchValue.value).forEach(u => {
            this.router.routeReuseStrategy.shouldReuseRoute = () => false;
            this.router.navigate(["/user", u.username]);
        }).catch(error => {
            console.log("SER: " + JSON.stringify(error));
            this.alertService.error(error.error);
        });
    }
}