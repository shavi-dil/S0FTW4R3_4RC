import java.text.DecimalFormat;

/**
 * Represents a book sold by the online bookstore.
 */
public class Book {
    private final int id;
    private String title;
    private String author;
    private String category;
    private String description;
    private double price;
    private int stock;
    private String imageFileName;

    public Book(int id, String title, String author, String category,
                String description, double price, int stock, String imageFileName) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.category = category;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.imageFileName = imageFileName;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }
    public String getImageFileName() { return imageFileName; }

    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setCategory(String category) { this.category = category; }
    public void setDescription(String description) { this.description = description; }
    public void setPrice(double price) { this.price = price; }
    public void setStock(int stock) { this.stock = stock; }
    public void setImageFileName(String imageFileName) { this.imageFileName = imageFileName; }

    public boolean isAvailable() {
        return stock > 0;
    }

    public void reduceStock(int quantity) {
        if (quantity <= 0 || quantity > stock) {
            throw new IllegalArgumentException("Invalid quantity for stock update.");
        }
        stock -= quantity;
    }

    public String getFormattedPrice() {
        return "$" + new DecimalFormat("0.00").format(price);
    }

    @Override
    public String toString() {
        return title + " by " + author;
    }
}
