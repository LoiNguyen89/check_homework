package controller;

import jakarta.servlet.http.HttpSession;
import model.Post;
import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import service.impl.PostService;
import service.impl.UserService;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    // Xóa bài viết
    @GetMapping("/delete/{id}")
    public String deletePost(@PathVariable("id") Long id, HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/auth/login";
        }

        Post post = postService.findById(id);
        if (post != null && post.getUser().getId() == currentUser.getId()) {
            postService.delete(id);
        } else {
            model.addAttribute("error", "Bạn không có quyền xóa bài viết này!");
        }
        return "redirect:/posts";
    }

    // Danh sách bài viết
    @GetMapping
    public String listPosts(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) return "redirect:/auth/login";

        List<Post> allPosts = postService.findAll();

        List<Post> visiblePosts = allPosts.stream()
                .filter(p -> p.getUser().getId() == currentUser.getId()
                        || currentUser.getFriends().contains(p.getUser()))
                .toList();

        model.addAttribute("posts", visiblePosts);
        return "postlist"; // bạn đang có postlist.html
    }


    @GetMapping("/write-on-the-wall/{id}")
    public String showWriteOnWallForm(@PathVariable("id") int friendId,
                                      HttpSession session,
                                      Model model) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) return "redirect:/auth/login";

        User friend = userService.findById(friendId);
        if (friend == null) {
            model.addAttribute("error", "Không tìm thấy người bạn này!");
            return "redirect:/posts";
        }

        model.addAttribute("friend", friend);
        model.addAttribute("post", new Post());
        return "writeOnFriendPost";
    }

    @PostMapping("/write-on-the-wall/{id}")
    public String writeOnWall(@PathVariable("id") int friendId,
                              @ModelAttribute("post") Post post,
                              HttpSession session,
                              Model model) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) return "redirect:/auth/login";

        User friend = userService.findById(friendId);
        if (friend == null) {
            model.addAttribute("error", "Không tìm thấy người bạn này!");
            return "redirect:/posts";
        }

        post.setUser(friend);
        post.setFriend(currentUser);
        post.setCreatedDate(LocalDate.now());

        postService.save(post);

        return "redirect:/posts";
    }
}
