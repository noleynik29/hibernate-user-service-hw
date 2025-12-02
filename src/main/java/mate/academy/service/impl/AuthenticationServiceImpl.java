package mate.academy.service.impl;

import mate.academy.dao.UserDAO;
import mate.academy.exception.AuthenticationException;
import mate.academy.exception.RegistrationException;
import mate.academy.lib.Inject;
import mate.academy.model.User;
import mate.academy.service.AuthenticationService;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class AuthenticationServiceImpl implements AuthenticationService {

    @Inject
    private final UserDAO userDao;
    private static final SecureRandom secureRandom = new SecureRandom();

    public AuthenticationServiceImpl(UserDAO userDao) {
        this.userDao = userDao;
    }

    @Override
    public User login(String email, String password) throws AuthenticationException {
        User user = userDao.findByEmail(email)
                .orElseThrow(() -> new AuthenticationException("User not found"));

        String hashed = hashPassword(password, user.getSalt());

        if (!hashed.equals(user.getPassword())) {
            throw new AuthenticationException("Incorrect password");
        }

        return user;
    }

    @Override
    public User register(String email, String password) throws RegistrationException {
        if (userDao.findByEmail(email).isPresent()) {
            throw new RegistrationException("User with email already exists: " + email);
        }

        String salt = generateSalt();
        String hash = hashPassword(password, salt);

        User user = new User();
        user.setEmail(email);
        user.setPassword(hash);
        user.setSalt(salt);

        return userDao.add(user);
    }

    private String generateSalt() {
        byte[] saltBytes = new byte[16];
        secureRandom.nextBytes(saltBytes);
        return Base64.getEncoder().encodeToString(saltBytes);
    }

    private String hashPassword(String password, String salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = digest.digest((password + salt).getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Cannot hash password", e);
        }
    }
}
