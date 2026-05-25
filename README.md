# 📚 INK AND LANTERN BOOKS - Online Bookstore Application

A comprehensive Java Swing-based online bookstore application with full e-commerce functionality, including user authentication, shopping cart management, order processing, and admin dashboard.

## ✨ Features Implemented

### 1. Customer Account Management
- **User Registration**: New customers can create accounts with username, password, email, full name, and address
- **User Login**: Secure authentication for both customers and administrators
- **Account Information**: View and update personal information (email, name, address)
- **Account Security**: Password-protected access with session management

### 2. Browse & Search Books
- **Book Catalogue**: View all available books with details (ID, Title, Author, Price, Stock)
- **Category Filtering**: Filter books by 4 categories:
  - Fiction
  - Non-Fiction
  - Science
  - Self-Help
- **Book Details**: Click to view full book information including description
- **Inventory Display**: Real-time stock availability for each book

### 3. Shopping Cart Management
- **Add to Cart**: Select quantity and add books to shopping cart
- **Remove Items**: Remove individual items from cart
- **Clear Cart**: Remove all items at once
- **Cart Total**: Dynamic calculation of total purchase amount
- **Quantity Management**: Update quantities in cart view
- **Stock Validation**: Prevent adding more items than available

### 4. Checkout & Order Processing
- **Checkout Form**: Enter/confirm delivery information
- **Order Confirmation**: Display order details with invoice
- **Invoice Generation**: Detailed receipts with:
  - Invoice number
  - Customer information
  - Itemized list of books
  - Price breakdown with tax calculation
  - Order status
- **Payment Processing**: Automatic payment completion upon checkout
- **Order Tracking**: Orders stored with timestamps and statuses

### 5. Admin Order & Shipping Management
- **Order Management Panel**:
  - View all orders with customer names, totals, and statuses
  - Update order status (PENDING → CONFIRMED → PROCESSING → SHIPPED → DELIVERED)
  - View detailed order information
  - Track order history

- **Shipping Management Panel**:
  - View all shipments with tracking numbers
  - Update shipping status (PENDING → SHIPPED → IN_TRANSIT → DELIVERED)
  - Track shipment dates
  - Generate tracking numbers for each shipment

## 🏗️ Application Architecture

### Core Classes

#### Model Classes
- **`Category`**: Represents book categories
- **`Book`**: Extended with category and description fields
- **`User`**: Customer and admin user profiles
- **`Cart`**: Shopping cart management
- **`OrderItem`**: Individual items in orders
- **`Order`**: Complete order with customer, items, payment, and shipment
- **`Payment`**: Payment transaction tracking
- **`Invoice`**: Invoice generation and formatting
- **`Shipment`**: Shipment tracking and status management

#### UI Components
- **Authentication Panel**: Login/Register screens with form validation
- **Main Application Panel**: CardLayout-based navigation between sections
- **Account Panel**: User profile and information management
- **Book Panel**: Catalogue with category filtering and details
- **Cart Panel**: Shopping cart display and management
- **Checkout Panel**: Order placement and invoice generation
- **Admin Panel**: Tabbed interface for order and shipping management

### User Flows

#### Customer Journey
```
1. Register/Login
2. Browse Books by Category
3. View Book Details
4. Add Books to Cart
5. Review Cart
6. Checkout with Delivery Info
7. Order Confirmation & Invoice
8. Order Tracking (future feature)
```

#### Admin Journey
```
1. Login as Admin
2. View Orders Dashboard
3. Update Order Status
4. Manage Shipments
5. Update Shipping Status
6. Track Deliveries
```

## 🎨 User Interface

### Color Scheme (Cafe-Inspired)
- **Primary Color**: Brown (139, 90, 43) - Main headers
- **Secondary Color**: Light Brown (210, 165, 104) - Accents
- **Accent Color**: Dark Goldenrod (184, 134, 11) - Highlights
- **Background**: Cream (245, 242, 235) - Main background
- **Text Color**: Dark Brown (60, 40, 20) - Default text

### Navigation
- Tab-based interface for easy section switching
- Logout button in header for session management
- User welcome message showing current logged-in user
- Responsive button hover effects

## 🚀 How to Run

### Compilation
```bash
javac OnlineBookstoreApp.java
```

### Execution
```bash
java OnlineBookstoreApp
```

### Demo Credentials
**Admin User:**
- Username: `admin`
- Password: `admin123`

**Customer User:**
- Username: `john_doe`
- Password: `password123`

## 📊 Sample Data

### Pre-loaded Books
1. Harry Potter - J.K. Rowling - $25.99 (Fiction)
2. Atomic Habits - James Clear - $22.50 (Self-Help)
3. The Alchemist - Paulo Coelho - $18.00 (Fiction)
4. Clean Code - Robert Martin - $45.00 (Science)
5. AI Basics - Tom Smith - $30.00 (Science)
6. Sapiens - Yuval Noah Harari - $28.00 (Non-Fiction)
7. Thinking, Fast and Slow - Daniel Kahneman - $35.00 (Non-Fiction)

## 🔧 Technical Details

### Technologies Used
- Java 8+
- Swing GUI Framework
- LocalDateTime for timestamps
- ArrayList for data management

### Key Features Implementation
- **Authentication**: Simple in-memory user database with password validation
- **Order Management**: Complete order lifecycle from creation to delivery
- **Invoice Generation**: Dynamic text-based invoice formatting
- **Category Filtering**: Real-time table updates based on selected category
- **Stock Management**: Automatic inventory reduction on order completion

## 📈 Future Enhancements

- Database integration (MySQL/PostgreSQL)
- Password encryption
- Payment gateway integration
- Email notifications
- Order tracking for customers
- Review and rating system
- Wishlist functionality
- Discounts and promotions
- Advanced search and filtering
- Order history for customers
- Analytics dashboard for admins

## 🛡️ Security Notes

Current implementation uses:
- Simple username/password authentication (for demo purposes)
- In-memory data storage
- No encryption (development only)

**Production Recommendations:**
- Implement proper authentication (OAuth, JWT)
- Use encrypted password storage (bcrypt)
- Add database security measures
- Implement HTTPS
- Add role-based access control

## 📝 License

This project is for educational purposes.

---

**Version**: 2.0  
**Last Updated**: May 2026  
**Status**: Feature Complete for MVP