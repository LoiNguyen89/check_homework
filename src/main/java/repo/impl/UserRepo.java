package repo.impl;

import model.User;

public interface UserRepo {
    User findByEmail(String email);
    void save(User user);
    User findByEmailAndPassword(String email, String password);
    User findById(int id);
    void update(User user);

}
