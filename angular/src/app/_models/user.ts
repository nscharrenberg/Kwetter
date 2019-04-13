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
    role: Role;
    followers: User[];
    following: User[];
    tweets: Tweet[];
}