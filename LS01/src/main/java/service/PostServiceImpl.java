package service;

import model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repo.impl.PostRepo;
import service.impl.PostService;


import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepo postRepo;

    @Override
    public void save(Post post) {
        postRepo.save(post);
    }

    @Override
    public List<Post> findAll() {
        return postRepo.findAll();
    }

    @Override
    public Post findById(Long id) {
        return postRepo.findById(id);
    }

    @Override
    public void delete(Long id) {
        postRepo.delete(id);
    }
}
