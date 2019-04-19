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
    private message: string = "";

    constructor() {
    }

    ngOnInit() {
        this.transform();
    }

    transform() {
        let wordArray = this.tweet.message.split(' ');

        wordArray.forEach(w => {
           if(w.includes("@")) {
               if(w.charAt(0) === '@') {
                   console.log("3 " + w);
                   let tempWord = w.substr(1);
                   console.log("4 " + tempWord);
                   let html = `<a href="/user/` + tempWord + `">` + w + `</a> `;

                   console.log("5 " + html);
                   this.message += html;
               } else {
                   console.log("2: " + w);
                   this.message += w + " ";
               }
           } else {
               console.log("1: " + w);
             this.message += w + " ";
           }
        });
    }
}