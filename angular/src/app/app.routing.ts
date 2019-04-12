import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import {HomeComponent} from "./pages/home";


@NgModule({
    imports: [
        RouterModule.forRoot(
            [
                { path: '', redirectTo: 'home', pathMatch: 'full' },
                { path: 'home', component: HomeComponent, pathMatch: 'full' },
                { path: 'auth', loadChildren: './pages/auth/auth.module#AuthModule' },
                { path: '**', redirectTo: '/pages/404' },
            ],
            { useHash: false },
        ),
    ],
    exports: [RouterModule],
})
export class AppRoutingModule {}
