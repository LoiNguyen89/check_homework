package repo;

import model.User;
import java.util.List;



public interface UserRepo {
    void save(User user);
    User findByEmail(String email);
    List<User> findAll();
}
