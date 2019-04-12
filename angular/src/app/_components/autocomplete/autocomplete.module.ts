import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';

import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import {AutocompleteComponent} from "./autocomplete.component";

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
    ],
    declarations: [
        AutocompleteComponent,
    ],
    exports: [
        AutocompleteComponent,
    ]
})
export class AutocompleteModule { }
