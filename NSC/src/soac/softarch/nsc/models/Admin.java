package soac.softarch.nsc.models;

/**
 *
 * @author Vijay
 */
public class Admin {

    private final int id;
    private final String first_name;
    private final String last_name;
    private final String email;
    private final String password;

    public Admin(int id, String first_name, String last_name, String email, String password) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
