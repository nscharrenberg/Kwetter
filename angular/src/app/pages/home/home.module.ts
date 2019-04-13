import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';


import {HomeComponent} from "./home.component";
import {RouterModule} from "@angular/router";
import {NavbarModule} from "../../_components/navbar/navbar.module";
import {TweetModule} from "../../_components/tweet/tweet.module";
import {TweetService} from "../../_services";
import {TruncatePipe} from "../../_pipes";
import {UserModule} from "../user/user.module";
import {CreateTweetModule} from "../../_components";
import {AlertModule} from "../../_directives/alert/alert.module";

@NgModule({
    imports: [
        CommonModule,
        RouterModule,
        NavbarModule,
        RouterModule,
        TweetModule,
        UserModule,
        AlertModule,
        CreateTweetModule
    ],
    declarations: [
        HomeComponent
    ],
    providers: [
        TweetService
    ]
})
export class HomeModule {}
