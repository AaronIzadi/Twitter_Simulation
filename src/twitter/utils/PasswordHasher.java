package twitter.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class PasswordHasher {

    private static final String PREFIX = "sha256:";

    private PasswordHasher() {
    }

    public static String hash(String password) {
        return PREFIX + digest(password);
    }

    public static boolean matches(String password, String storedPassword) {
        if (isHashed(storedPassword)) {
            return hash(password).equals(storedPassword);
        }
        return storedPassword.equals(password);
    }

    public static boolean isHashed(String storedPassword) {
        return storedPassword != null && storedPassword.startsWith(PREFIX);
    }

    private static String digest(String password) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hash = messageDigest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder builder = new StringBuilder();
            for (byte value : hash) {
                builder.append(String.format("%02x", value));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 is not available", e);
        }
    }
}
