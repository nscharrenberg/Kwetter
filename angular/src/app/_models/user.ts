import {Role} from "./role";

export class User {
    id: number;
    username: string;
    firstName: string;
    lastName: string;
    email: string;
    biography: string;
    longitude: number;
    latitude: number;
    role: Role;
}