package com.nscharrenberg.kwetter.authentication;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordAuthentication {

    /**
     * Encrypt the password using bcrypt
     * @param password
     * @return
     */
    public static String hash(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    /**
     * Verify the given password using bcrypt
     * @param password
     * @param hash
     * @return
     */
    public static boolean verify(String password, String hash) {
        try {
            return BCrypt.checkpw(password, hash);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
