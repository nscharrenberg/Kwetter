import { Injectable } from "@angular/core";
import { Observable, Subject } from "rxjs/Rx";
import { WebsocketService } from "./websocket.service";
import {Tweet} from "../_models/tweet";
import {AuthenticationService} from "./authentication.service";
import {User} from "../_models";

export interface Message {
    author: string;
    message: string;
}

@Injectable()
export class WsTweetService {
    public messages: Subject<Tweet>;
    public loggedIn: User;

    constructor(wsService: WebsocketService,
                authenticationService: AuthenticationService) {
        this.loggedIn = authenticationService.getLoggedInUser();
        this.messages = <Subject<Tweet>>wsService.connect(this.loggedIn.username).map(
            (response: MessageEvent): Tweet => {
                let data = JSON.parse(response.data);
                console.log("d: " + data);
               return data;
            }
        );
    }
}