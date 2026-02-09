package CatalogComponent;

public class Ebook extends Book {
    private double fileSizeMB;
    private String format;

    public Ebook() {}

    public Ebook(String isbn, String title, String author, int yearPublished) {
        super(isbn, title, author, yearPublished);
    }

    @Override
    public BookType getBookType() {
        return BookType.EBOOK;
    }

    public double getFileSizeMB() { return fileSizeMB; }
    public void setFileSizeMB(double fileSizeMB) { this.fileSizeMB = fileSizeMB; }

    public String getFormat() { return format; }
    public void setFormat(String format) { this.format = format; }
}