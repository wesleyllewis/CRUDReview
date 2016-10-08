import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    static User user;
    static HashMap<String, User> users = new HashMap<>();
    static ArrayList<Book> books = new ArrayList<>();

    public static void main(String[] args) {
        Spark.init();

        Spark.get("/", ((request, response) -> {
                    Session session = request.session();
                    String userName = session.attribute("userName");
                    User user = users.get(userName);
                    HashMap m = new HashMap();
                    m.put("books", books);
                    m.put("userName", userName);
                    for (Book b : books) {
                        if (b.addedBy.equalsIgnoreCase(userName)) {
                            b.editable = "editable";
                        } else {
                            b.editable = null;
                        }
                    }
                    if (user == null) {
                        return new ModelAndView(m, "index.html");
                    }
                    return new ModelAndView(m, "index.html");
                }),
                new MustacheTemplateEngine()
        );
        Spark.get("/edit-book", ((request, response) -> {
                    Session session = request.session();
                    String userName = session.attribute("userName");
                    int index = Integer.parseInt(request.queryParams("index"));
                    session.attribute("index", index);
                    HashMap m = new HashMap();
                    m.put("book", books.get(index));
                    return new ModelAndView(m, "edit.html");
                }),
                new MustacheTemplateEngine()
        );
        Spark.post(
                "/create-user",
                ((request, response) -> {
                    String userName = request.queryParams("userName");
                    String password = request.queryParams("password");
                    if (users.containsKey(userName)) {
                        if (users.get(userName).isPasswordValid(password)) {
                            Session session = request.session();
                            session.attribute("userName", userName);
                            response.redirect("/");
                        } else {
                            response.redirect("/");
                        }
                    } else {
                        user = new User(userName, password);
                        users.put(userName, user);
                        Session session = request.session();
                        session.attribute("userName", userName);
                        response.redirect("/");
                        return "";
                    }
                    return "";
                })
        );
        Spark.post(
                "/create-book",
                ((request, response) -> {
                    Session session = request.session();
                    String userName = session.attribute("userName");
                    User user = users.get(userName);
                    String bookName = request.queryParams("bookTitle");
                    String author = request.queryParams("author");
                    String genre = request.queryParams("bookGenre");
                    int yearPublished = Integer.parseInt(request.queryParams("year"));
                    if (user == null) {
                        throw new Exception("User is not logged in.");
                    }
                    Book book = new Book(bookName, author, genre, yearPublished, userName, books.size());
                    books.add(book);

                    response.redirect("/");
                    return "";
                })
        );
        Spark.post(
                "/edit-book",
                ((request, response) -> {
                    Session session = request.session();
                    String userName = session.attribute("userName");
                    int index = session.attribute("index");
                    String bookName = request.queryParams("bookTitle");
                    String author = request.queryParams("author");
                    String genre = request.queryParams("bookGenre");
                    int yearPublished = Integer.parseInt(request.queryParams("year"));
                    Book editedBook = new Book(bookName, author, genre, yearPublished, userName, index);
                    books.set(index, editedBook);

                    response.redirect("/");
                    return "";
                })
        );
        Spark.post("/delete-book", ((request, response) -> {
                    Session session = request.session();
                    String userName = session.attribute("userName");
                    Book deleteBook = new Book();
                    for (Book b : books) {
                        if (b.addedBy.equalsIgnoreCase(userName)) {
                            deleteBook = b;
                        }
                    }
                    books.remove(deleteBook);

                    response.redirect("/");
                    return "";
                })
        );
        Spark.post(
                "/logout",
                ((request, response) -> {
                    Session session = request.session();
                    session.invalidate();

                    response.redirect("/");
                    return "";
                })
        );

    }

}
