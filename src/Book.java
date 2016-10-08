public class Book {
    String bookName;
    String author;
    String genre;
    int yearPublished;
    String addedBy;
    int index;
    String editable;

    public Book() {
    }
    public Book(String bookName, String author, String genre, int yearPublished, String addedBy, int index) {
        this.bookName = bookName;
        this.author = author;
        this.genre = genre;
        this.yearPublished = yearPublished;
        this.addedBy = addedBy;
        this.index = index;
    }

}
