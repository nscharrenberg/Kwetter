import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';

import {RouterModule} from "@angular/router";
import {TweetComponent} from "./tweet.component";

@NgModule({
    imports: [
        CommonModule,
        RouterModule,
    ],
    declarations: [
        TweetComponent,
    ],
    exports: [
        TweetComponent
    ]
})
export class TweetModule { }
