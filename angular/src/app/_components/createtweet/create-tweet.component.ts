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
       this.tweetService.create(this.f.message.value, this.f.userId.value).forEach(data => {
           this.alertService.success("Tweet posted");
       }).catch(error => {
           this.alertService.error(error.error);
       });
    }
}