import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';


import {UserComponent} from "./user.component";
import {RouterModule} from "@angular/router";
import {NavbarModule} from "../../_components/navbar/navbar.module";
import {TweetModule} from "../../_components/tweet/tweet.module";
import {TweetService} from "../../_services";
import {AlertModule} from "../../_directives/alert/alert.module";
import {FollowModule} from "../../_components/follow/follow.module";
import {UnfollowModule} from "../../_components/unfollow/unfollow.module";
import {ReactiveFormsModule} from "@angular/forms";

@NgModule({
    imports: [
        CommonModule,
        ReactiveFormsModule,
        RouterModule,
        NavbarModule,
        RouterModule,
        TweetModule,
        AlertModule,
        FollowModule,
        UnfollowModule,
    ],
    declarations: [
        UserComponent
    ],
    providers: [
        TweetService
    ]
})
export class UserModule {}
