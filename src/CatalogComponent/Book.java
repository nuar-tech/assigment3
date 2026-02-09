package CatalogComponent;
public abstract class Book {
    private int id;
    private String isbn;
    private String title;
    private String author;
    private int yearPublished;
    private boolean available;

    public enum BookType {
        PRINTED, EBOOK, REFERENCE
    }

    public Book() {}

    public Book(String isbn, String title, String author, int yearPublished) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.yearPublished = yearPublished;
        this.available = true;
    }

    public abstract BookType getBookType();

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public int getYearPublished() { return yearPublished; }
    public void setYearPublished(int yearPublished) { this.yearPublished = yearPublished; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    @Override
    public String toString() {
        return String.format("Book{id=%d, title='%s', author='%s', available=%s}",
                id, title, author, available);
    }
}