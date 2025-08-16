package controller;

import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import service.UserService;

@Controller
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }


    @PostMapping("/register")
    public String register(
            @ModelAttribute User user,
            @RequestParam("avatarFile") MultipartFile avatarFile,
            Model model
    ) {
        try {
            boolean success = userService.register(user, avatarFile);
            if (!success) {
                model.addAttribute("error", "Email đã tồn tại");
                model.addAttribute("user", user);
                return "register";
            }
            return "redirect:/";
        } catch (Exception e) {
            model.addAttribute("error", "Đăng ký thất bại: " + e.getMessage());
            model.addAttribute("user", user);
            return "register";
        }
    }
}
