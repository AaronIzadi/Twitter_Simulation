package twitter.utils;

public final class InputValidator {

    public static final int USERNAME_MIN_LENGTH = 3;
    public static final int USERNAME_MAX_LENGTH = 20;
    public static final int PASSWORD_MIN_LENGTH = 4;
    public static final int TWEET_MAX_LENGTH = 280;
    public static final int BIO_MAX_LENGTH = 160;

    private InputValidator() {
    }

    public static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static boolean isValidUsername(String username) {
        if (isBlank(username)) {
            return false;
        }
        String trimmed = username.trim();
        if (trimmed.length() < USERNAME_MIN_LENGTH || trimmed.length() > USERNAME_MAX_LENGTH) {
            return false;
        }
        return trimmed.matches("[a-zA-Z0-9_]+");
    }

    public static boolean isValidPassword(String password) {
        return !isBlank(password) && password.length() >= PASSWORD_MIN_LENGTH;
    }

    public static boolean isValidTweetText(String text) {
        if (isBlank(text)) {
            return false;
        }
        return text.trim().length() <= TWEET_MAX_LENGTH;
    }

    public static boolean isValidEmail(String email) {
        if (isBlank(email)) {
            return false;
        }
        String trimmed = email.trim();
        int atIndex = trimmed.indexOf('@');
        return atIndex > 0 && atIndex < trimmed.length() - 1;
    }

    public static boolean isValidBio(String bio) {
        return bio == null || bio.length() <= BIO_MAX_LENGTH;
    }

    public static boolean isValidDisplayName(String name) {
        return !isBlank(name) && name.trim().length() <= 50;
    }

    public static Long parsePhoneNumber(String input) {
        if (isBlank(input)) {
            return null;
        }
        String digits = input.trim();
        if (!digits.matches("\\d+")) {
            return null;
        }
        try {
            return Long.parseLong(digits);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
