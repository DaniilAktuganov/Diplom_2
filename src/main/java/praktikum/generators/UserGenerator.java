package praktikum.generators;

import praktikum.models.User;

public class UserGenerator {

    public static User createDefaultUser() {
        return new User()
                .withEmail("gojosatoru@yandex.ru")
                .withPassword("password")
                .withName("gojo");
    }

    public static User createDefaultUserWithoutEmail() {
        return new User()
                .withPassword("password")
                .withName("gojo");
    }

    public static User createDefaultUserWithoutPassword() {
        return new User()
                .withEmail("gojosatoru@yandex.ru")
                .withName("gojo");
    }

        public static User createDefaultUserWithoutName () {
            return new User()
                    .withEmail("gojosatoru@yandex.ru")
                    .withPassword("password");
        }
    }