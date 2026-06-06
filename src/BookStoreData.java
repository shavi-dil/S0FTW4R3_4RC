import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * In-memory data storage for bookstore sample books, users, and orders.
 * This prototype uses in-memory collections for Assignment 3 demonstration.
 */
public class BookStoreData {
    private static final BookStoreData INSTANCE = new BookStoreData();

    private final List<Book> books = new ArrayList<>();
    private final List<Customer> customers = new ArrayList<>();
    private final List<Admin> admins = new ArrayList<>();
    private final List<Order> orders = new ArrayList<>();

    private BookStoreData() {
        seedSampleData();
    }

    public static BookStoreData getInstance() {
        return INSTANCE;
    }

    private void seedSampleData() {
        int bookId = 1;
        
        // Original 7 books with images
        books.add(new Book(bookId++, "Atomic Habits", "James Clear", "Self Help",
                "A practical guide to building good habits and breaking bad ones.", 24.99, 15, "atomichabits.jpg"));
        books.add(new Book(bookId++, "Harry Potter and the Philosopher's Stone", "J.K. Rowling", "Fiction",
                "A young wizard begins his magical journey at Hogwarts.", 19.99, 12, "harrypotter.jpg"));
        books.add(new Book(bookId++, "The Alchemist", "Paulo Coelho", "Fiction",
                "A meaningful story about dreams, destiny and personal journey.", 18.99, 14, "thealchemist.jpg"));
        books.add(new Book(bookId++, "Clean Code", "Robert C. Martin", "Science",
                "A software engineering guide to writing readable and maintainable code.", 45.99, 8, "cleancode.jpg"));
        books.add(new Book(bookId++, "Corporate Conversations with AI", "Lisa Brooks-Kift", "Science",
                "A straight-forward guide to AI conversations and workplace productivity.", 29.99, 10, "aibasics.jpg"));
        books.add(new Book(bookId++, "Sapiens", "Yuval Noah Harari", "Non-Fiction",
                "A brief history of humankind and the development of society.", 27.99, 12, "sapiens.jpg"));
        books.add(new Book(bookId++, "Thinking, Fast and Slow", "Daniel Kahneman", "Non-Fiction",
                "A psychology classic explaining two systems of human thinking.", 26.99, 9, "thinkingfastandslow.jpg"));

        // Fiction books
        String[] fictionBooks = {
            "Pride and Prejudice|Jane Austen|18.99",
            "The Great Gatsby|F. Scott Fitzgerald|16.99",
            "To Kill a Mockingbird|Harper Lee|17.99",
            "1984|George Orwell|20.99",
            "Animal Farm|George Orwell|14.99",
            "The Book Thief|Markus Zusak|18.99",
            "The Midnight Library|Matt Haig|19.99",
            "The Seven Husbands of Evelyn Hugo|Taylor Jenkins Reid|18.99",
            "The Kite Runner|Khaled Hosseini|17.99",
            "A Thousand Splendid Suns|Khaled Hosseini|19.99",
            "Little Women|Louisa May Alcott|15.99",
            "Jane Eyre|Charlotte Brontë|16.99",
            "Wuthering Heights|Emily Brontë|17.99",
            "The Catcher in the Rye|J.D. Salinger|16.99",
            "The Silent Patient|Alex Michaelides|19.99",
            "Where the Crawdads Sing|Delia Owens|18.99",
            "The Night Circus|Erin Morgenstern|19.99",
            "Normal People|Sally Rooney|17.99",
            "The Song of Achilles|Madeline Miller|18.99",
            "Circe|Madeline Miller|17.99",
            "Before the Coffee Gets Cold|Toshikazu Kawaguchi|16.99",
            "The Hobbit|J.R.R. Tolkien|17.99",
            "The Lord of the Rings|J.R.R. Tolkien|42.99",
            "Dune|Frank Herbert|18.99",
            "The Hunger Games|Suzanne Collins|16.99"
        };
        for (String fictionBook : fictionBooks) {
            String[] parts = fictionBook.split("\\|");
            books.add(new Book(bookId++, parts[0], parts[1], "Fiction", 
                    parts[0] + " is a captivating fiction novel.", Double.parseDouble(parts[2]), 
                    (int)(Math.random() * 15 + 5), ""));
        }

        // Non-Fiction books
        String[] nonFictionBooks = {
            "Becoming|Michelle Obama|19.99",
            "Educated|Tara Westover|18.99",
            "The Diary of a Young Girl|Anne Frank|15.99",
            "Born a Crime|Trevor Noah|18.99",
            "The Immortal Life of Henrietta Lacks|Rebecca Skloot|18.99",
            "Into the Wild|Jon Krakauer|17.99",
            "Man's Search for Meaning|Viktor Frankl|14.99",
            "The Wright Brothers|David McCullough|28.99",
            "Outliers|Malcolm Gladwell|17.99",
            "Blink|Malcolm Gladwell|16.99",
            "The Tipping Point|Malcolm Gladwell|16.99",
            "Freakonomics|Steven Levitt & Stephen Dubner|18.99",
            "The Power of Habit|Charles Duhigg|18.99",
            "Quiet|Susan Cain|18.99",
            "Why We Sleep|Matthew Walker|20.99",
            "The Body Keeps the Score|Bessel van der Kolk|19.99",
            "A Brief History of Time|Stephen Hawking|18.99",
            "Cosmos|Carl Sagan|22.99",
            "The Sixth Extinction|Elizabeth Kolbert|18.99",
            "Guns, Germs, and Steel|Jared Diamond|19.99",
            "The Psychology of Money|Morgan Housel|17.99",
            "Rich Dad Poor Dad|Robert Kiyosaki|17.99",
            "Deep Work|Cal Newport|18.99",
            "Digital Minimalism|Cal Newport|18.99",
            "Essentialism|Greg McKeown|18.99"
        };
        for (String nonFictionBook : nonFictionBooks) {
            String[] parts = nonFictionBook.split("\\|");
            books.add(new Book(bookId++, parts[0], parts[1], "Non-Fiction", 
                    parts[0] + " offers valuable insights and knowledge.", Double.parseDouble(parts[2]), 
                    (int)(Math.random() * 15 + 5), ""));
        }

        // Science books
        String[] scienceBooks = {
            "The Selfish Gene|Richard Dawkins|17.99",
            "The Gene|Siddhartha Mukherjee|22.99",
            "The Emperor of All Maladies|Siddhartha Mukherjee|20.99",
            "Astrophysics for People in a Hurry|Neil deGrasse Tyson|17.99",
            "The Elegant Universe|Brian Greene|19.99",
            "The Fabric of the Cosmos|Brian Greene|20.99",
            "The Order of Time|Carlo Rovelli|18.99",
            "Seven Brief Lessons on Physics|Carlo Rovelli|14.99",
            "The Code Breaker|Walter Isaacson|20.99",
            "Genome|Matt Ridley|18.99",
            "Life 3.0|Max Tegmark|24.99",
            "Superintelligence|Nick Bostrom|21.99",
            "Human Compatible|Stuart Russell|20.99",
            "Artificial Intelligence: A Guide for Thinking Humans|Melanie Mitchell|18.99",
            "Weapons of Math Destruction|Cathy O'Neil|18.99",
            "Algorithms to Live By|Brian Christian & Tom Griffiths|19.99",
            "The Master Algorithm|Pedro Domingos|18.99",
            "Data Science from Scratch|Joel Grus|44.99",
            "Python Crash Course|Eric Matthes|39.99",
            "Introduction to Algorithms|Cormen, Leiserson & Rivest|89.99",
            "Design Patterns|Gang of Four|54.99",
            "Refactoring|Martin Fowler|49.99",
            "The Pragmatic Programmer|Hunt & Thomas|39.99",
            "Head First Java|Kathy Sierra & Bert Bates|59.99",
            "Effective Java|Joshua Bloch|54.99"
        };
        for (String scienceBook : scienceBooks) {
            String[] parts = scienceBook.split("\\|");
            books.add(new Book(bookId++, parts[0], parts[1], "Science", 
                    parts[0] + " explores scientific principles and discoveries.", Double.parseDouble(parts[2]), 
                    (int)(Math.random() * 15 + 5), ""));
        }

        // Self Help books
        String[] selfHelpBooks = {
            "The 7 Habits of Highly Effective People|Stephen Covey|18.99",
            "How to Win Friends and Influence People|Dale Carnegie|16.99",
            "Think and Grow Rich|Napoleon Hill|14.99",
            "The Subtle Art of Not Giving a F*ck|Mark Manson|16.99",
            "You Are a Badass|Jen Sincero|16.99",
            "The Four Agreements|Don Miguel Ruiz|14.99",
            "The 5 AM Club|Robin Sharma|18.99",
            "Ikigai|Francesc Miralles & Héctor García|16.99",
            "Make Your Bed|William McRaven|17.99",
            "Can't Hurt Me|David Goggins|18.99",
            "Grit|Angela Duckworth|17.99",
            "Mindset|Carol Dweck|17.99",
            "The Mountain Is You|Brianna Wiest|15.99",
            "Good Vibes, Good Life|Vex King|16.99",
            "The Courage to Be Disliked|Kishimi Ichiro & Koga Fumitake|18.99",
            "The Let Them Theory|Mel Robbins|18.99",
            "The Compound Effect|Darren Hardy|16.99",
            "The Miracle Morning|Hal Elrod|16.99",
            "Eat That Frog|Brian Tracy|16.99",
            "Getting Things Done|David Allen|17.99",
            "The One Thing|Gary Keller & Jay Papasan|18.99",
            "Start With Why|Simon Sinek|17.99",
            "Dare to Lead|Brené Brown|18.99",
            "Big Magic|Elizabeth Gilbert|17.99",
            "The Artist's Way|Julia Cameron|16.99"
        };
        for (String selfHelpBook : selfHelpBooks) {
            String[] parts = selfHelpBook.split("\\|");
            books.add(new Book(bookId++, parts[0], parts[1], "Self Help", 
                    parts[0] + " provides practical advice for personal growth.", Double.parseDouble(parts[2]), 
                    (int)(Math.random() * 15 + 5), ""));
        }

        customers.add(new Customer("Ink and Lantern Reader", "customer@inklantern.com", "customer123",
                "0412345678", "1 Lantern Lane, Melbourne VIC"));

        admins.add(new Admin("Store Manager", "admin@inklantern.com", "admin123"));
    }

    public List<Book> getBooks() {
        return Collections.unmodifiableList(books);
    }

    public List<Customer> getCustomers() {
        return Collections.unmodifiableList(customers);
    }

    public List<Admin> getAdmins() {
        return Collections.unmodifiableList(admins);
    }

    public List<Order> getOrders() {
        return Collections.unmodifiableList(orders);
    }

    public Customer findCustomerByEmail(String email) {
        return customers.stream()
                .filter(customer -> customer.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }

    public Admin findAdminByEmail(String email) {
        return admins.stream()
                .filter(admin -> admin.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }

    public List<Book> searchBooks(String query, String category) {
        String lowerQuery = query == null ? "" : query.trim().toLowerCase();
        return books.stream()
                .filter(book -> (lowerQuery.isEmpty()
                        || book.getTitle().toLowerCase().contains(lowerQuery)
                        || book.getAuthor().toLowerCase().contains(lowerQuery)
                        || book.getCategory().toLowerCase().contains(lowerQuery)))
                .filter(book -> category == null || category.equals("All")
                        || book.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    public List<String> getCategories() {
        List<String> categories = new ArrayList<>();
        categories.add("All");
        books.stream()
                .map(Book::getCategory)
                .distinct()
                .sorted()
                .forEach(categories::add);
        return categories;
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public void deleteBook(Book book) {
        books.remove(book);
    }

    public List<Book> getBooksAvailable() {
        return books.stream().collect(Collectors.toList());
    }

    public Order placeOrder(Customer customer, Cart cart, String deliveryAddress) {
        Order order = new Order(customer, cart, deliveryAddress);
        orders.add(order);
        customer.addOrder(order);
        return order;
    }
}
