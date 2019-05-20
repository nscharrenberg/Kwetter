import {KWETTER_WS} from "./api-constants";
import {Tweet} from "../_models/tweet";

var ws: WebSocket;

export function wsConnect(username: string, tweetListId: string) {
    var host = KWETTER_WS(username);

    ws = new WebSocket(host);

    ws.onopen = function(event) {
        console.log("Connected");
    };

    ws.onerror = function(event) {
        alert(JSON.stringify(event));
    };

    ws.onmessage = function (event) {
        console.log(event.data);

        var tweetlist = document.getElementById(tweetListId);
        var message = JSON.parse(event.data);

        tweetlist.innerHTML += message.from + " : " + message.message + "\n";

        console.log("MESSAGE RECEIVED - " + message);
    };
}