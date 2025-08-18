package controller;

import jakarta.servlet.http.HttpSession;
import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import service.impl.UserService;

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



    @PostMapping("/login")
    public String login(@ModelAttribute User user, Model model, HttpSession session) {
        User dbUser = userService.findByEmailAndPassword(user.getEmail(), user.getPassword());
        if (dbUser != null) {
            session.setAttribute("currentUser", dbUser);
            return "redirect:/posts";
        } else {
            model.addAttribute("error", "Email hoặc mật khẩu không đúng");
            return "login";
        }
    }

    @GetMapping("/users")
    public String showProfile(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", currentUser);
        return "profile";
    }


    @PostMapping("/users")
    public String updateProfile(
            @ModelAttribute User user,
            @RequestParam("avatarFile") MultipartFile avatarFile,
            HttpSession session,
            Model model
    ) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/login";
        }

        try {

            User updatedUser = userService.updateProfile(currentUser.getId(), user, avatarFile);


            session.setAttribute("currentUser", updatedUser);

            model.addAttribute("user", updatedUser);
            model.addAttribute("success", "Cập nhật thông tin thành công");
            return "profile";
        } catch (Exception e) {
            model.addAttribute("user", currentUser);
            model.addAttribute("error", "Cập nhật thất bại: " + e.getMessage());
            return "profile";
        }
    }

    @GetMapping("/users/search-friend")
    public String showSearchFriendPage(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) return "redirect:/login";
        return "searchFriend";
    }

    @PostMapping("/users/search-user")
    public String searchUserByEmail(@RequestParam("email") String email,
                                    HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) return "redirect:/login";

        User foundUser = userService.findUserByEmail(email);
        if (foundUser == null) {
            model.addAttribute("message", "Không tìm thấy người dùng với email: " + email);
        } else {
            model.addAttribute("foundUser", foundUser);
            model.addAttribute("isFriend",
                    currentUser.getFriends().contains(foundUser));
        }
        return "searchFriend";
    }

    @GetMapping("/users/add-friend")
    public String addFriend(@RequestParam("id") int friendId,
                            HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) return "redirect:/login";

        User friend = userService.findUserByEmail(
                userService.findById(friendId).getEmail()
        );
        boolean success = userService.addFriend(currentUser, friend);

        if (!success) {
            model.addAttribute("message", "Thêm bạn thất bại hoặc đã là bạn bè.");
        } else {
            session.setAttribute("currentUser", currentUser);
            model.addAttribute("message", "Kết bạn thành công với " + friend.getFullName());
        }
        return "searchFriend";
    }
    @GetMapping("/users/friends")
    public String listFriends(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) return "redirect:/login";

        model.addAttribute("friends", currentUser.getFriends());
        return "friends";
    }



}
