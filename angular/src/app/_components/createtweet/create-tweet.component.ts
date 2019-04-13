import {
    Component, Input,
    NgZone,
    OnInit,
} from "@angular/core";
import {AlertService, AuthenticationService, TweetService, UserService} from "../../_services";
import {User} from "../../_models";
import {Tweet} from "../../_models/tweet";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
    selector: 'app-create-tweet',
    styleUrls: ['./create-tweet.component.scss'],
    templateUrl: 'create-tweet.component.html'
})
export class CreateTweetComponent  implements OnInit {
    @Input("author") user: User;
    message: string;

    constructor(
        private router: Router,
        private activatedRoute: ActivatedRoute,
        private zone: NgZone,
        private alertService: AlertService,
        private tweetService: TweetService,
        private authenticationService: AuthenticationService,
        private userService: UserService) {
    }

    ngOnInit() {

    }

    send() {
        let tweet : Tweet = new Tweet();
       this.tweetService.create(this.message, this.user.id).forEach(data => {
           console.log(JSON.stringify(data));
           this.alertService.success(data.toString());
       }).catch(error => {
           this.alertService.error(error.error.toString());
       });
    }
}