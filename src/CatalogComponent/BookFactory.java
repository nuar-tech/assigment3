package CatalogComponent;

public class BookFactory {

    public static Book createBook(Book.BookType type) {
        switch (type) {
            case PRINTED:
                return new PrintedBook();
            case EBOOK:
                return new Ebook();
            case REFERENCE:
                return new ReferenceBook();
            default:
                return new PrintedBook();
        }
    }

    public static Book createBook(Book.BookType type, String isbn, String title,
                                  String author, int yearPublished) {
        Book book = createBook(type);
        book.setIsbn(isbn);
        book.setTitle(title);
        book.setAuthor(author);
        book.setYearPublished(yearPublished);
        return book;
    }
}
