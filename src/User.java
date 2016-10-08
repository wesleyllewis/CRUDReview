public class User {
    String userName;
    String password;

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
    public boolean isPasswordValid(String password){
        return this.password.equals(password);
    }
}
