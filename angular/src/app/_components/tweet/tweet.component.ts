/// <reference types="@types/googlemaps" />
import {
    Component, Input,
    NgZone,
    OnInit,
} from "@angular/core";
import {AlertService, AuthenticationService, TweetService, UserService} from "../../_services";
import {User} from "../../_models";
import {Tweet} from "../../_models/tweet";

@Component({
    selector: 'app-tweet',
    styleUrls: ['./tweet.component.scss'],
    templateUrl: 'tweet.component.html'
})
export class TweetComponent  implements OnInit {
    loggedIn: User;
    @Input("tweet") tweet: Tweet;
    private message: string = "";

    constructor(
        private tweetService: TweetService,
        private authenticationService: AuthenticationService,
        private alertService: AlertService,
    ) {
    }

    ngOnInit() {
        this.loggedIn = this.authenticationService.getLoggedInUser();
        this.transform();
    }

    transform() {
        let wordArray = this.tweet.message.split(' ');

        wordArray.forEach(w => {
           if(w.includes("@")) {
               if(w.charAt(0) === '@') {
                   let tempWord = w.substr(1);
                   let html = `<a href="/user/` + tempWord + `">` + w + `</a> `;

                   this.message += html;
               } else {
                   this.message += w + " ";
               }
           } else if(w.includes("#")) {
               if(w.charAt(0) === '#') {
                   let tempWord = w.substr(1);
                   let html = `<a href="/hashtag/` + tempWord + `">` + w + `</a> `;
                   this.message += html;
               } else {
                   this.message += w + " ";
               }
           } else {
             this.message += w + " ";
           }
        });
    }

    like() {
        console.log(JSON.stringify([this.findIfAlreadyLiked()]));
        if(this.findIfAlreadyLiked() == true) {
            console.log("Already Liking");
            this.tweetService.unlike(this.tweet.id, this.loggedIn.id).forEach(t => {
                this.alertService.success("Tweet unliked");
                location.reload();
            }).catch(error => {
                this.alertService.error(error.error);
            });
        } else {
            console.log("Not liking yet");
            this.tweetService.like(this.tweet.id, this.loggedIn.id).forEach(t => {
                this.alertService.success("Tweet Liked");
                location.reload();
            }).catch(error => {
                this.alertService.error(error.error);
            });

        }
    }

    findIfAlreadyLiked() : boolean {
        let liked: boolean = false;

        if(this.tweet.likes != undefined) {
            this.tweet.likes.forEach(l => {
                if(l.id === this.loggedIn.id) {
                    liked = true;
                    return;
                }
            });
        }

        return liked;
    }
}