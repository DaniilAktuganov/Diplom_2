package praktikum.models;

public class UserCreds {

    private String email;
    private String password;

    public UserCreds(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static UserCreds credsFromUser(User user) {
        return new UserCreds(user.getEmail(), user.getPassword());
    }

    public static UserCreds credsFromUserWithInvalidEmail(User user) {
        return new UserCreds("invalidEmail", user.getPassword());
    }

    public static UserCreds credsFromUserWithInvalidPassword(User user) {
        return new UserCreds(user.getEmail(), "invalidPassword");
    }
}