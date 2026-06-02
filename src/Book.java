public class Book {
    private final int id;
    private String title;
    private String author;
    private double price;
    private int stock;
    private Category category;
    private String description;

    public Book(int id, String title, String author, double price, int stock, Category category, String description) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.price = price;
        this.stock = stock;
        this.category = category;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void reduceStock(int quantity) {
        if (quantity <= 0) {
            return;
        }
        this.stock = Math.max(0, this.stock - quantity);
    }

    public void increaseStock(int quantity) {
        if (quantity <= 0) {
            return;
        }
        this.stock += quantity;
    }

    @Override
    public String toString() {
        return String.format("%s by %s ($%.2f) - %s", title, author, price, category.getName());
    }
}
