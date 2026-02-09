package CatalogComponent;

public class ReferenceBook extends Book {
    private String location;

    public ReferenceBook() {}

    public ReferenceBook(String isbn, String title, String author, int yearPublished) {
        super(isbn, title, author, yearPublished);
    }

    @Override
    public BookType getBookType() {
        return BookType.REFERENCE;
    }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
}