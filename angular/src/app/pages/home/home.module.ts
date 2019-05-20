import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';


import {HomeComponent} from "./home.component";
import {RouterModule} from "@angular/router";
import {NavbarModule} from "../../_components/navbar/navbar.module";
import {TweetModule} from "../../_components/tweet/tweet.module";
import {TweetService} from "../../_services";
import {UserModule} from "../user/user.module";
import {CreateTweetModule} from "../../_components";
import {AlertModule} from "../../_directives/alert/alert.module";
import {FollowModule} from "../../_components/follow/follow.module";
import {UnfollowModule} from "../../_components/unfollow/unfollow.module";
import {WsTweetService} from "../../_services/wsTweetService";
import {WebsocketService} from "../../_services/websocket.service";

@NgModule({
    imports: [
        CommonModule,
        RouterModule,
        NavbarModule,
        RouterModule,
        TweetModule,
        UserModule,
        AlertModule,
        CreateTweetModule,
        FollowModule,
        UnfollowModule,
    ],
    declarations: [
        HomeComponent
    ],
    providers: [
        TweetService,
        WebsocketService,
        WsTweetService,
    ]
})
export class HomeModule {}
