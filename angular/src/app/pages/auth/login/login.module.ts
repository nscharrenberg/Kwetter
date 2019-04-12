import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';

import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { LoginComponent } from './login.component';
import {RouterModule} from '@angular/router';
import {AlertModule} from "../../../_directives/alert/alert.module";

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        HttpClientModule,
        RouterModule,
        AlertModule,
    ],
    declarations: [
        LoginComponent,
    ],
})
export class LoginModule { }
