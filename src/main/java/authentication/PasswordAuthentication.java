package authentication;

import com.google.common.hash.Hashing;
import org.mindrot.jbcrypt.BCrypt;

import java.nio.charset.StandardCharsets;

public class PasswordAuthentication {
    public static String hash_deprecated(String password) {
        return Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
    }

    /*
     * Bcrypt not supported by glassfish and payara
     */
    public static String hash(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean verify(String password, String hash) {
        return BCrypt.checkpw(password, hash);
    }
}
