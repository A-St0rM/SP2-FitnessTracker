package app.DAO;

import app.entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class UserDAO implements IDAO<User, Integer>{

    EntityManagerFactory emf;

    public UserDAO(EntityManagerFactory emf)
    {
        this.emf = emf;
    }
    @Override
    public List<User> getAll() {
        try(EntityManager em = emf.createEntityManager()){
           return em.createQuery("SELECT u FROM User u", User.class).getResultList();
        }
    }

    @Override
    public User getById(Integer id) {
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            User foundUser = em.find(User.class, id);
            em.getTransaction().commit();
            return foundUser;
        }
    }

    @Override
    public User create(User user) {
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
            return user;
        }
    }

    @Override
    public User update(User user) {
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            User updatedUser = em.merge(user);
            em.getTransaction().commit();
            return updatedUser;
        }
    }

    @Override
    public boolean delete(Integer id) {
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            User foundUser = em.find(User.class, id);
            if(foundUser != null){
                em.remove(foundUser);
                em.getTransaction().commit();
                return true;
            } else return false;

        }
    }
}
