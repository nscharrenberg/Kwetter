import { NgModule }      from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';

import { AppComponent }  from './app.component';
import { AuthGuard } from './_guards';
import { JwtInterceptor, ErrorInterceptor } from './_helpers';
import {AlertService, AuthenticationService, TweetService, UserService} from './_services';
import {AlertModule} from "./_directives/alert/alert.module";
import {HomeModule} from "./pages/home/home.module";
import {AppRoutingModule} from "./app.routing";
import {UserModule} from "./pages/user/user.module";
import { ProfileComponent } from './pages/profile/profile.component';
import {ProfileModule} from "./pages/profile/profile.module";

@NgModule({
    imports: [
        BrowserModule,
        ReactiveFormsModule,
        HttpClientModule,
        AppRoutingModule,
        HomeModule,
        AlertModule,
        UserModule,
        ProfileModule,
    ],
    declarations: [
        AppComponent,
    ],
    providers: [
        AuthGuard,
        AlertService,
        AuthenticationService,
        UserService,
        TweetService,
        { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true },
        { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true },
    ],
    bootstrap: [AppComponent]
})

export class AppModule { }