package repo;

import model.Post;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import repo.impl.PostRepo;


import java.util.List;

@Repository
public class PostRepoImpl implements PostRepo {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void save(Post post) {
        Session session = sessionFactory.getCurrentSession();
        session.merge(post);
    }

    @Override
    public List<Post> findAll() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from Post", Post.class).list();
    }

    @Override
    public Post findById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.find(Post.class, id);
    }

    @Override
    public void delete(Long id) {
        Session session = sessionFactory.getCurrentSession();
        Post post = session.find(Post.class, id);
        if (post != null) {
            session.remove(post);
        }
    }
}

