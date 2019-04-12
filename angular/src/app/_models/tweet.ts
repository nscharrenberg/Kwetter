import {User} from "./user";

export class Tweet {
    id: number;
    message: string;
    createdAt: Date;
    author: User;
    mentions: User[];
    likes: User[];
}