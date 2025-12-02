package mate.academy.service.impl;

import java.security.SecureRandom;
import java.util.Optional;
import mate.academy.dao.UserDao;
import mate.academy.lib.Inject;
import mate.academy.model.User;
import mate.academy.service.UserService;
import mate.academy.util.HashUtil;

public class UserServiceImpl implements UserService {

    private static final SecureRandom secureRandom = new SecureRandom();
    @Inject
    private UserDao userDao;

    @Override
    public User add(User user) {
        String salt = HashUtil.generateSalt();
        String hashedPassword = HashUtil.hashPassword(user.getPassword(), salt);

        user.setSalt(salt);
        user.setPassword(hashedPassword);

        return userDao.add(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userDao.findByEmail(email);
    }
}
