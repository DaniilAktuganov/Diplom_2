package praktikum.models;

public class UpdatedUserCreds {

    public static User updateUserEmail(User user) {
        return new User()
                .withEmail("grg-data@yandex.ru");
    }

    public static User updateUserName(User user) {
        return new User()
                .withName("few3f4");
    }
}