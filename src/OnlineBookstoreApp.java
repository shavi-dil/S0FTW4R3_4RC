import javax.swing.SwingUtilities;

public class OnlineBookstoreApp {
    public static void main(String[] args) {
        BookstoreStorage storage = new BookstoreStorage();
        BookstoreService service = new BookstoreService(storage);
        SwingUtilities.invokeLater(() -> {
            BookstoreAppUI ui = new BookstoreAppUI(service);
            ui.setVisible(true);
        });
    }
}
