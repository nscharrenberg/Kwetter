/// <reference types="@types/googlemaps" />
import {
    Component, Input,
    NgZone,
    OnInit,
} from "@angular/core";
import {AuthenticationService} from "../../_services";
import {User} from "../../_models";
import {Tweet} from "../../_models/tweet";

@Component({
    selector: 'app-tweet',
    styleUrls: ['./tweet.component.scss'],
    templateUrl: 'tweet.component.html'
})
export class TweetComponent  implements OnInit {
    @Input("tweet") tweet: Tweet;

    constructor() {
    }

    ngOnInit() {

    }
}