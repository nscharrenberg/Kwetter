import { NgModule }      from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';

import { AppComponent }  from './app.component';
import { AuthGuard } from './_guards';
import { JwtInterceptor, ErrorInterceptor } from './_helpers';
import { AlertService, AuthenticationService, UserService } from './_services';
import {AlertModule} from "./_directives/alert/alert.module";
import {HomeModule} from "./pages/home/home.module";
import {AppRoutingModule} from "./app.routing";
import {NavbarModule} from "./_components/navbar/navbar.module";
import {UserModule} from "./pages/user/user.module";

@NgModule({
    imports: [
        BrowserModule,
        FormsModule,
        ReactiveFormsModule,
        HttpClientModule,
        AppRoutingModule,
        HomeModule,
        AlertModule,
        UserModule
    ],
    declarations: [
        AppComponent
    ],
    providers: [
        AuthGuard,
        AlertService,
        AuthenticationService,
        UserService,
        { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true },
        { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true },
    ],
    bootstrap: [AppComponent]
})

export class AppModule { }