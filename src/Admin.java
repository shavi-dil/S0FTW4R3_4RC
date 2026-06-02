public class Admin extends User {
    public Admin(int id, String username, String password, String email, String fullName, String address) {
        super(id, username, password, email, fullName, address);
    }

    @Override
    public boolean isAdmin() {
        return true;
    }
}
