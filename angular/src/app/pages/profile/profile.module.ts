import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {ProfileComponent} from "./profile.component";
import {ReactiveFormsModule} from "@angular/forms";
import {AlertModule} from "../../_directives/alert/alert.module";
import {HttpClientModule} from "@angular/common/http";
import {RouterModule} from "@angular/router";
import {UserModule} from "../user/user.module";
import {UserService} from "../../_services";
import {NavbarModule} from "../../_components/navbar/navbar.module";

@NgModule({
  declarations: [
      ProfileComponent
  ],
  imports: [
      CommonModule,
      ReactiveFormsModule,
      HttpClientModule,
      RouterModule,
      NavbarModule,
      AlertModule,
      UserModule,
  ],
    providers: [
        UserService
    ]
})
export class ProfileModule { }
