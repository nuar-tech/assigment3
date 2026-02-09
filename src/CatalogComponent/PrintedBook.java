package CatalogComponent;

public class PrintedBook extends Book {
    private int pages;

    public PrintedBook() {}

    public PrintedBook(String isbn, String title, String author, int yearPublished) {
        super(isbn, title, author, yearPublished);
    }

    @Override
    public BookType getBookType() {
        return BookType.PRINTED;
    }

    public int getPages() { return pages; }
    public void setPages(int pages) { this.pages = pages; }
}