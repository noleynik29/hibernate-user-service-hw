package mate.academy.service.impl;

import mate.academy.dao.UserDao;
import mate.academy.lib.Inject;
import mate.academy.model.User;
import mate.academy.service.UserService;

import java.util.Optional;

public class UserServiceImpl implements UserService{
    @Inject
    private UserDao userDao;

    @Override
    public User add(User user) {
        return userDao.add(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userDao.findByEmail(email);
    }
}
