import {platformBrowserDynamic} from "@angular/platform-browser-dynamic";
import {AppModule} from "./app";
import {ViewEncapsulation} from "@angular/core";

platformBrowserDynamic().bootstrapModule(AppModule, { defaultEncapsulation: ViewEncapsulation.None });