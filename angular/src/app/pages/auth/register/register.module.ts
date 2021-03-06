import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';


import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import {RouterModule} from '@angular/router';
import {RegisterComponent} from "./register.component";
import {AlertModule} from "../../../_directives/alert/alert.module";
import { CKEditorModule } from '@ckeditor/ckeditor5-angular';

@NgModule({
    imports: [
        CommonModule,
        ReactiveFormsModule,
        HttpClientModule,
        RouterModule,
        AlertModule,
        CKEditorModule
    ],
    declarations: [
        RegisterComponent
    ],
})
export class RegisterModule { }
