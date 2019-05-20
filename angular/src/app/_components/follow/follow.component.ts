/// <reference types="@types/googlemaps" />
import {
    Component, Input,
    NgZone,
    OnInit,
} from "@angular/core";
import {AlertService, AuthenticationService, UserService} from "../../_services";
import {User} from "../../_models";
import {Tweet} from "../../_models/tweet";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
    selector: 'app-follow',
    styleUrls: ['./follow.component.scss'],
    template: `<button (click)="follow()" class="btn btn-block btn-follow btn-lg"> <i class="fa fa-user-plus"></i> Follow</button>`
})
export class FollowComponent {
    @Input("toFollow") toFollow: number;
    @Input("me") me: number;

    constructor(
        private alertService: AlertService,
        private userService: UserService
    ) {
    }

    follow() {
        this.userService.follow(this.me, this.toFollow).forEach(data => {
            this.alertService.success("You are now following " + data.username);
            location.reload();
        }).catch(error => {
            this.alertService.error(error.error.toString());
        });
    }
}