import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {ReactiveFormsModule} from '@angular/forms';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';

import {AppComponent} from './app.component';
import {AuthGuard} from './_guards';
import {ErrorInterceptor, JwtInterceptor} from './_helpers';
import {AlertService, AuthenticationService, TweetService, UserService} from './_services';
import {AlertModule} from "./_directives/alert/alert.module";
import {HomeModule} from "./pages/home/home.module";
import {AppRoutingModule} from "./app.routing";
import {UserModule} from "./pages/user/user.module";
import {ProfileModule} from "./pages/profile/profile.module";
import {WebsocketService} from "./_services/websocket.service";

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
        WebsocketService,
        { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true },
        { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true },
    ],
    bootstrap: [AppComponent]
})

export class AppModule { }