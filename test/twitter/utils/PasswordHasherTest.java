package twitter.utils;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PasswordHasherTest {

    @Test
    public void hashAndMatchStoredPassword() {
        String hashed = PasswordHasher.hash("secret");

        assertTrue(PasswordHasher.isHashed(hashed));
        assertTrue(PasswordHasher.matches("secret", hashed));
        assertFalse(PasswordHasher.matches("wrong", hashed));
    }

    @Test
    public void matchesLegacyPlainTextPassword() {
        assertTrue(PasswordHasher.matches("legacy", "legacy"));
        assertFalse(PasswordHasher.matches("other", "legacy"));
    }
}
