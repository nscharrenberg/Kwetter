package authentication;

import org.mindrot.jbcrypt.BCrypt;

import java.util.function.Function;

public class PasswordAuthentication {
    public static String hash(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean verifyHash(String password, String hash) {
        return BCrypt.checkpw(password, hash);
    }
}
