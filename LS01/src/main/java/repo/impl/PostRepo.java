package repo.impl;

import model.Post;
import java.util.List;

public interface PostRepo {
    void save(Post post);
    List<Post> findAll();
    Post findById(Long id);
    void delete(Long id);
}
