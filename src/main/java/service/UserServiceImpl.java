package service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import model.User;
import org.springframework.transaction.annotation.Transactional;
import repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service

public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private Cloudinary cloudinary;


    @Override
    @Transactional
    public boolean register(User user, MultipartFile avatarFile) throws IOException {

        if (userRepo.findByEmail(user.getEmail()) != null) {
            return false;
        }


        Map uploadResult = cloudinary.uploader().upload(
                avatarFile.getBytes(),
                ObjectUtils.asMap("folder", "avatars")
        );
        user.setAvatar(uploadResult.get("secure_url").toString());


        userRepo.save(user);
        return true;
    }
}
