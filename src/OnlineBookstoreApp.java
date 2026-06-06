public class OnlineBookstoreApp {
    public static void main(String[] args) {
        BookstoreStorage storage = new BookstoreStorage();
        BookstoreService service = new BookstoreService(storage);
        javax.swing.SwingUtilities.invokeLater(() -> {
            BookstoreAppUI app = new BookstoreAppUI(service);
            app.setVisible(true);
        });
    }
}
