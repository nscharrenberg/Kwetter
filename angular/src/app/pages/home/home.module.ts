import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';


import {HomeComponent} from "./home.component";
import {RouterModule} from "@angular/router";

@NgModule({
    imports: [
        CommonModule,
        RouterModule,
    ],
    declarations: [
        HomeComponent,
    ],
})
export class HomeModule {}
