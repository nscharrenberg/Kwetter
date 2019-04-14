import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';

import {RouterModule} from "@angular/router";
import {FollowComponent} from "./follow.component";

@NgModule({
    imports: [
        CommonModule,
        RouterModule,
    ],
    declarations: [
        FollowComponent,
    ],
    exports: [
        FollowComponent
    ]
})
export class FollowModule { }
