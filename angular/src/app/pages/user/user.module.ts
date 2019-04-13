import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';


import {UserComponent} from "./user.component";
import {RouterModule} from "@angular/router";
import {NavbarModule} from "../../_components/navbar/navbar.module";
import {TweetModule} from "../../_components/tweet/tweet.module";
import {TweetService} from "../../_services";
import {TruncatePipe} from "../../_pipes";

@NgModule({
    imports: [
        CommonModule,
        RouterModule,
        NavbarModule,
        RouterModule,
        TweetModule,
    ],
    declarations: [
        UserComponent
    ],
    providers: [
        TweetService
    ]
})
export class UserModule {}
