package service.impl;

import model.User;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    boolean register(User user, MultipartFile avatarFile);
    User findByEmailAndPassword(String email, String password);
    User updateProfile(int userId, User user, MultipartFile avatarFile);
    User findUserByEmail(String email);
    boolean addFriend(User currentUser, User friend);
    User findById(int id);
}
