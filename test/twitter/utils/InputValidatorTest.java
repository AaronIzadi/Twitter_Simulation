package twitter.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class InputValidatorTest {

    @Test
    public void acceptsValidUsername() {
        assertTrue(InputValidator.isValidUsername("alice"));
        assertTrue(InputValidator.isValidUsername("user_123"));
    }

    @Test
    public void rejectsInvalidUsername() {
        assertFalse(InputValidator.isValidUsername(""));
        assertFalse(InputValidator.isValidUsername("ab"));
        assertFalse(InputValidator.isValidUsername("bad name"));
        assertFalse(InputValidator.isValidUsername("name-with-dash"));
    }

    @Test
    public void acceptsValidPasswordAndTweet() {
        assertTrue(InputValidator.isValidPassword("pass"));
        assertTrue(InputValidator.isValidTweetText("Hello Twitter"));
    }

    @Test
    public void rejectsInvalidPasswordAndTweet() {
        assertFalse(InputValidator.isValidPassword(""));
        assertFalse(InputValidator.isValidPassword("abc"));
        assertFalse(InputValidator.isValidTweetText(""));
        assertFalse(InputValidator.isValidTweetText(buildString(InputValidator.TWEET_MAX_LENGTH + 1)));
    }

    @Test
    public void validatesEmailAndPhone() {
        assertTrue(InputValidator.isValidEmail("user@example.com"));
        assertFalse(InputValidator.isValidEmail("invalid-email"));
        assertEquals(Long.valueOf(9123456789L), InputValidator.parsePhoneNumber("9123456789"));
        assertNull(InputValidator.parsePhoneNumber("12abc"));
    }

    private String buildString(int length) {
        StringBuilder builder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            builder.append('a');
        }
        return builder.toString();
    }
}
