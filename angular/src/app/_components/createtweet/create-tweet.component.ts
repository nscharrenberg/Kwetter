import {
    Component, Input,
    NgZone,
    OnInit,
} from "@angular/core";
import {AlertService, AuthenticationService, TweetService, UserService} from "../../_services";
import {User} from "../../_models";
import {Tweet} from "../../_models/tweet";
import {ActivatedRoute, Router} from "@angular/router";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {WsTweetService} from "../../_services/wsTweetService";

@Component({
    selector: 'app-create-tweet',
    styleUrls: ['./create-tweet.component.scss'],
    templateUrl: 'create-tweet.component.html'
})
export class CreateTweetComponent  implements OnInit {
    @Input("author") userId: number;

    createForm: FormGroup;

    constructor(
        private router: Router,
        private activatedRoute: ActivatedRoute,
        private zone: NgZone,
        private alertService: AlertService,
        private tweetService: TweetService,
        private authenticationService: AuthenticationService,
        private userService: UserService,
        private wsTweetService: WsTweetService,
        private formBuilder: FormBuilder,) {
    }

    ngOnInit() {
        this.createForm = this.formBuilder.group({
            message: ['', [Validators.required]],
            userId: [this.userId, [Validators.required]]
        });
    }

    // convenience getter for easy access to form fields
    get f() { return this.createForm.controls; }

    send() {
       this.tweetService.create(this.f.message.value, this.f.userId.value).forEach((data: Tweet) => {
            this.wsTweetService.messages.next(data);
           this.alertService.success("Tweet posted");
           location.reload();
       }).catch(error => {
           console.log("ERRNA: " + JSON.stringify(error));
           this.alertService.error(error.error);
       });
    }
}