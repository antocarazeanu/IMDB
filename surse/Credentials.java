public class Credentials {
    // fields
    private String email;
    private String password;
    public Credentials(String email, String password) {
        this.email = email;
        this.password = password;
    }
    // getters
    public String getEmail() {
        return this.email;
    }
    public String getPassword() {
        return this.password;
    }
    // setters
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
