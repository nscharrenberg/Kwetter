import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';

import {RouterModule} from "@angular/router";
import {CreateTweetComponent} from "./create-tweet.component";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {AlertModule} from "../../_directives/alert/alert.module";

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
    exports: [
        CreateTweetComponent
    ]
})
export class CreateTweetModule { }
