package app.DAO;

import app.config.HibernateConfig;
import app.entities.Exercise;
import app.entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class UserDAO {

    private final EntityManagerFactory emf;

    public UserDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public UserDAO() {
        this(HibernateConfig.getEntityManagerFactory());
    }

    public Optional<User> findByEmail(String email) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<User> q = em.createQuery(
                    "SELECT u FROM User u WHERE u.email = :email", User.class);
            q.setParameter("email", email);
            List<User> result = q.getResultList();
            return result.stream().findFirst();
        }
    }

    public User create(User user) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
            return user;
        }
    }

    public List<User> readAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<User> q = em.createQuery("SELECT u FROM User u", User.class);
            return q.getResultList();
        }
    }

    public void delete(Long id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            User user = em.find(User.class, id);
            if (user != null) em.remove(user);
            em.getTransaction().commit();
        }
    }

    public User update(User user) {
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            User updatedUser = em.merge(user);
            em.getTransaction().commit();
            return updatedUser;
        }
    }

    public Optional<User> findById(Long id) {
        try (EntityManager em = emf.createEntityManager()) {
            User e = em.find(User.class, id);
            return Optional.ofNullable(e);
        }
    }
}
