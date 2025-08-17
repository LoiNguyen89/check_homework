package repo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import model.Message;
import org.springframework.stereotype.Repository;
import repo.impl.MessageRepo;


import java.util.List;

@Repository
@Transactional
public class MessageRepoImpl implements MessageRepo {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void save(Message message) {
        entityManager.persist(message);
    }

    @Override
    public List<Message> findConversation(Long senderId, Long receiverId) {
        return entityManager.createQuery(
                        "SELECT m FROM Message m " +
                                "WHERE (m.sender.id = :senderId AND m.receiver.id = :receiverId) " +
                                "   OR (m.sender.id = :receiverId AND m.receiver.id = :senderId) " +
                                "ORDER BY m.sentDate ASC", Message.class)
                .setParameter("senderId", senderId)
                .setParameter("receiverId", receiverId)
                .getResultList();
    }
}
