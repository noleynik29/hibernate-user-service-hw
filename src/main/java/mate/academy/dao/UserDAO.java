package mate.academy.dao;

import mate.academy.model.User;
import java.util.Optional;

public interface UserDAO {
    User add(User user);

    Optional<User> findByEmail(String email);
}
