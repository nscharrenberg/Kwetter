import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';


import { AuthRoutingModule } from './auth-routing.module';
import {RegisterModule} from "./register/register.module";
import {LoginModule} from "./login/login.module";

@NgModule({
  imports: [
    CommonModule,
    AuthRoutingModule,
    RegisterModule,
    LoginModule,
  ],
})
export class AuthModule { }
