import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';

import {RouterModule} from "@angular/router";
import {CreateTweetComponent} from "./create-tweet.component";
import {FormsModule} from "@angular/forms";

@NgModule({
    imports: [
        CommonModule,
        RouterModule,
        FormsModule,
    ],
    declarations: [
        CreateTweetComponent,
    ],
    exports: [
        CreateTweetComponent
    ]
})
export class CreateTweetModule { }
