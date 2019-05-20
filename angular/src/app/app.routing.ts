import {NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';
import {HomeComponent} from "./pages/home";
import {AuthGuard} from "./_guards";
import {UserComponent} from "./pages/user";
import {ProfileComponent} from "./pages/profile/profile.component";


@NgModule({
    imports: [
        RouterModule.forRoot(
            [
                { path: '', redirectTo: 'home', pathMatch: 'full' },
                { path: 'home', component: HomeComponent, pathMatch: 'full', canActivate: [AuthGuard] },
                { path: 'auth', loadChildren: './pages/auth/auth.module#AuthModule' },
                { path: 'user/:username', component: UserComponent, pathMatch: 'full', canActivate: [AuthGuard] },
                { path: 'profile', component: ProfileComponent, pathMatch: 'full' },
                { path: '**', redirectTo: '/pages/404' },
            ],
            { useHash: false },
        ),
    ],
    exports: [RouterModule],
})
export class AppRoutingModule {}
