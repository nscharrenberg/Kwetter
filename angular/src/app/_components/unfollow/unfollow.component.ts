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
    selector: 'app-unfollow',
    styleUrls: ['./unfollow.component.scss'],
    template: `<button (click)="unfollow()" class="btn btn-block btn-unfollow btn-lg"> <i class="fa fa-frown-o"></i> Unfollow</button>`
})
export class UnfollowComponent {
    @Input("toUnFollow") toUnFollow: number;
    @Input("me") me: number;

    constructor(
        private alertService: AlertService,
        private userService: UserService
    ) {
    }

    unfollow() {
        this.userService.unfollow(this.me, this.toUnFollow).forEach(data => {
            this.alertService.success("You are not following " + data.username + " anymore");
            location.reload();
        }).catch(error => {
            this.alertService.error(error.error.toString());
        });
    }
}