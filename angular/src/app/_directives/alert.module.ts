import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';


import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import {RouterModule} from '@angular/router';
import {AlertComponent} from './alert.component';

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        HttpClientModule,
        RouterModule,
    ],
    declarations: [AlertComponent],
    exports: [AlertComponent]
})
export class AlertModule { }
