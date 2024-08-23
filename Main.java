public class Main {
    public static void main(String[] args) {
        InMemoryDatabase db = new InMemoryDatabase();
        QueryExecutor executor = new QueryExecutor(db);
        QueryParser parser = new QueryParser();

        //Test Insert
        System.out.println(executor.executeQuery(parser.parse("INSERT INTO users (name, email, age) VALUES (Alice, alice@example.com, 30)")));
        System.out.println(executor.executeQuery(parser.parse("INSERT INTO users (name, email, age) VALUES (Bob, bob@example.com, 40)")));
        System.out.println(executor.executeQuery(parser.parse("INSERT INTO users (name, email, age) VALUES (Cat, cat@example.com, 63)")));
        System.out.println(executor.executeQuery(parser.parse("INSERT INTO users (name, email, age) VALUES (Alice, dog@example.com, 12)")));
        System.out.println(executor.executeQuery(parser.parse("INSERT INTO users (name, email, age) VALUES (Egg, egg@example.com, 22)")));
        System.out.println(executor.executeQuery(parser.parse("INSERT INTO users (name, email, age) VALUES (Fish, fish@example.com, 25)")));
        System.out.println(executor.executeQuery(parser.parse("INSERT INTO users (name, email, age) VALUES (Goldy, goldy@example.com, 36)")));

        //Test Select
        System.out.println(executor.executeQuery(parser.parse("SELECT * FROM users")));


        //Test Update
        System.out.println(executor.executeQuery(parser.parse("UPDATE users SET email = 'newalice@example.com' WHERE name = 'Alice'")));
        System.out.println(executor.executeQuery(parser.parse("SELECT * FROM users")));

        //Test Delete
        System.out.println(executor.executeQuery(parser.parse("DELETE FROM users WHERE name = Alice")));
        System.out.println(executor.executeQuery(parser.parse("SELECT * FROM users")));
    }
}
