import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';

import {RouterModule} from "@angular/router";
import {CreateTweetComponent} from "./create-tweet.component";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {AlertModule} from "../../_directives/alert/alert.module";
import {WebsocketService} from "../../_services/websocket.service";
import {WsTweetService} from "../../_services/wsTweetService";

@NgModule({
    imports: [
        CommonModule,
        RouterModule,
        AlertModule,
        ReactiveFormsModule,
    ],
    declarations: [
        CreateTweetComponent,
    ],
    providers: [
        WebsocketService,
        WsTweetService,
    ],
    exports: [
        CreateTweetComponent
    ]
})
export class CreateTweetModule { }
