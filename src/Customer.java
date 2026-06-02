public class Customer extends User {
    public Customer(int id, String username, String password, String email, String fullName, String address) {
        super(id, username, password, email, fullName, address);
    }

    @Override
    public boolean isAdmin() {
        return false;
    }
}
