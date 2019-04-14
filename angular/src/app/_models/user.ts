import {Role} from "./role";
import {Tweet} from "./tweet";

export class User {
    id: number;
    username: string;
    email: string;
    biography: string;
    website: string;
    longitude: number;
    latitude: number;
    firstname: string;
    lastname: string;
    avatar: string;
    role: Role;
    followers: User[];
    following: User[];
    tweets: Tweet[];

    defaultAvatar: string = "https://en.gravatar.com/userimage/155883735/f61270db7a03be084dcfa712f19388a7.png";
}