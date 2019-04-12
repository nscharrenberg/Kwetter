import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';


import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import {RouterModule} from '@angular/router';
import {RegisterComponent} from "./register.component";
import {AlertModule} from "../../../_directives/alert.module";

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        HttpClientModule,
        RouterModule,
        AlertModule
    ],
    declarations: [
        RegisterComponent,
    ],
})
export class RegisterModule { }
