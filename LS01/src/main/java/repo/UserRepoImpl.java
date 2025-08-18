package repo;

import model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import repo.impl.UserRepo;

@Repository
@Transactional
public class UserRepoImpl implements UserRepo {

    @Autowired
    private SessionFactory sessionFactory;

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public User findByEmail(String email) {
        Query<User> query = getCurrentSession()
                .createQuery("FROM User WHERE email = :email", User.class);
        query.setParameter("email", email);
        return query.uniqueResult();
    }

    @Override
    public void save(User user) {
        getCurrentSession().persist(user);
    }

    @Override
    public User findByEmailAndPassword(String email, String password) {
        Query<User> query = getCurrentSession()
                .createQuery("FROM User WHERE email = :email AND password = :password", User.class);
        query.setParameter("email", email);
        query.setParameter("password", password);
        return query.uniqueResult();
    }

    @Override
    public User findById(int id) {
        return getCurrentSession().get(User.class, id);
    }

    @Override
    public void update(User user) {
        getCurrentSession().merge(user);
    }
}
