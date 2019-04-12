import { ModuleWithProviders, NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {LoginComponent} from './login/login.component';
import {RegisterComponent} from "./register";

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: '',
        children: [
          { path: '', redirectTo: 'login', pathMatch: 'full' },
          { path: 'login', component: LoginComponent, pathMatch: 'full' },
          { path: 'register', component: RegisterComponent, pathMatch: 'full' },
        ],
      },
    ]),
  ],
  exports: [RouterModule],
})
export class AuthRoutingModule {}
