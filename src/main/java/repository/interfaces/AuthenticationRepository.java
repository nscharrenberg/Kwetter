/*
 * Copyright (c) 2019. Noah Scharrenberg
 */

package repository.interfaces;

public interface AuthenticationRepository {
    public boolean login(String username, String password);
}
