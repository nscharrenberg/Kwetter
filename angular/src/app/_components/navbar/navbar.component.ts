/// <reference types="@types/googlemaps" />
import {
    Component,
    NgZone,
    OnInit,
} from "@angular/core";
import {AuthenticationService} from "../../_services";
import {User} from "../../_models";

@Component({
    selector: 'app-navbar',
    styleUrls: ['./navbar.component.scss'],
    templateUrl: 'navbar.component.html'
})
export class NavbarComponent  implements OnInit {
    private user: User;

    constructor(private authenticationService: AuthenticationService) {
    }

    ngOnInit() {
        this.user = this.authenticationService.getLoggedInUser();
    }
}