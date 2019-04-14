import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';

import {RouterModule} from "@angular/router";
import {UnfollowComponent} from "./unfollow.component";

@NgModule({
    imports: [
        CommonModule,
        RouterModule,
    ],
    declarations: [
        UnfollowComponent,
    ],
    exports: [
        UnfollowComponent
    ]
})
export class UnfollowModule { }
