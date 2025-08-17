package service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import repo.impl.UserRepo;
import service.impl.UserService;

import java.io.IOException;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private Cloudinary cloudinary;

    @Override
    public boolean register(User user, MultipartFile avatarFile) {
        // check email tồn tại chưa
        if (userRepo.findByEmail(user.getEmail()) != null) {
            return false;
        }

        try {

            Map uploadResult = cloudinary.uploader().upload(
                    avatarFile.getBytes(),
                    ObjectUtils.asMap("folder", "avatars")
            );
            String avatarUrl = uploadResult.get("secure_url").toString();

            user.setAvatar(avatarUrl);
            userRepo.save(user);
            return true;
        } catch (IOException e) {
            throw new RuntimeException("Upload avatar failed", e);
        }
    }

    @Override
    public User findByEmailAndPassword(String email, String password) {
        return userRepo.findByEmailAndPassword(email, password);
    }

    @Override
    public User updateProfile(int userId, User updatedData, MultipartFile avatarFile) {
        User existingUser = userRepo.findById(userId);
        if (existingUser == null) throw new RuntimeException("User not found");

        existingUser.setFullName(updatedData.getFullName());
        existingUser.setPassword(updatedData.getPassword());


        if (avatarFile != null && !avatarFile.isEmpty()) {
            try {
                Map uploadResult = cloudinary.uploader().upload(
                        avatarFile.getBytes(),
                        ObjectUtils.asMap("folder", "avatars")
                );
                String avatarUrl = uploadResult.get("secure_url").toString();
                existingUser.setAvatar(avatarUrl);
            } catch (IOException e) {
                throw new RuntimeException("Upload avatar failed", e);
            }
        }

        userRepo.update(existingUser);
        return existingUser;
    }
    @Override
    public User findUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    @Override
    public boolean addFriend(User currentUser, User friend) {
        if (currentUser == null || friend == null) return false;
        if (currentUser.getFriends().contains(friend)) {
            return false;
        }
        currentUser.getFriends().add(friend);
        userRepo.update(currentUser);
        return true;
    }

    @Override
    public User findById(int id) {
        return userRepo.findById(id);
    }
}
