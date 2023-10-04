import { User } from "./user";
import { Expense } from "./expense";

export class Category {
    categoryId: string;
    userId: User;
    title: string;
    description: string;
    expenses: Expense[];
}
