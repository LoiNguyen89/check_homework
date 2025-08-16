package service;

import model.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserService {
    boolean register(User user, MultipartFile avatarFile) throws IOException;
}
