
package libmansys.model;

public class Book {
    private int bookId;    
    private String title;
    private String author;
    private String publisher;
    private String category;
    private int year;
    private int quantity;
    private double bookPrice;
    
    public Book() {
    
    }
    
    public Book(int bookId, String title, String author, String publisher, 
            String category, int year, int quantity, double bookPrice) {
    this.bookId = bookId;
    this.title = title;
    this.author = author;
    this.publisher = publisher;
    this.category = category;
    this.year = year;
    this.quantity = quantity;
    this.bookPrice = bookPrice;
}
    
    
    //Getters and Setters
    public int getBookId() { return bookId; }
    public void setBookId(int bookId) { this.bookId = bookId; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    
    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }
        
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
    
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    
    public double getBookPrice() { return bookPrice; }
    public void setBookPrice(double bookPrice) { this.bookPrice = bookPrice; }
}
