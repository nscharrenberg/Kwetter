import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';

import {NavbarComponent} from "./navbar.component";
import {RouterModule} from "@angular/router";
import {AlertModule} from "../../_directives/alert/alert.module";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";

@NgModule({
    imports: [
        CommonModule,
        RouterModule,
        AlertModule,
        ReactiveFormsModule,
    ],
    declarations: [
        NavbarComponent,
    ],
    exports: [
        NavbarComponent
    ]
})
export class NavbarModule { }
