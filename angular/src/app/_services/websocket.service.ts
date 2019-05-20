import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import {KWETTER_WS} from "../_helpers/api-constants";
import * as Rx from 'rxjs/Rx';

@Injectable()
export class WebsocketService {
    constructor() { }

    private subject: Rx.Subject<MessageEvent>;
    private _ws: WebSocket;


    get ws(): WebSocket {
        return this._ws;
    }

    public connect(endpoint): Rx.Subject<MessageEvent> {
        if(!this.subject) {
            let url = KWETTER_WS(endpoint);
            this.subject = this.create(url);
            console.log("Successfully Connected to: " + url);
        }

        return this.subject;
    }

    private create(endpoint): Rx.Subject<MessageEvent> {
        this._ws = new WebSocket(endpoint);

        let observable = Rx.Observable.create((obs: Rx.Observer<MessageEvent>) => {
           this._ws.onmessage = obs.next.bind(obs);
            this._ws.onerror = obs.error.bind(obs);
            this._ws.onclose = obs.complete.bind(obs);

           return this._ws.close.bind(this._ws);
        });

        let observer = {
          next: (data) => {
              if(this._ws.readyState === WebSocket.OPEN) {
                  console.log("DATAAA: " + JSON.stringify(data));
                  this._ws.send(JSON.stringify(data));
              }
          }
        };

        return Rx.Subject.create(observer, observable);
    }
}
