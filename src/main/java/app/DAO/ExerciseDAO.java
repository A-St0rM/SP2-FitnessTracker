package app.DAO;


import app.config.HibernateConfig;
import app.entities.Exercise;
import app.mapper.ExerciseMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.jdbc.Work;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;

public class ExerciseDAO{

    private final EntityManagerFactory emf;

    public ExerciseDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public ExerciseDAO() {
        this(HibernateConfig.getEntityManagerFactory());
    }

    public Exercise create(Exercise exercise) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(exercise);
            em.getTransaction().commit();
            return exercise;
        }
    }

    public Optional<Exercise> findById(Long id) {
        try (EntityManager em = emf.createEntityManager()) {
            Exercise e = em.find(Exercise.class, id);
            return Optional.ofNullable(e);
        }
    }

    public Optional<Exercise> findByExternalId(String externalId) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Exercise> q = em.createQuery(
                    "SELECT e FROM Exercise e WHERE e.externalId = :externalId", Exercise.class);
            q.setParameter("externalId", externalId);
            List<Exercise> result = q.getResultList();
            return result.stream().findFirst();
        }
    }


    public List<Exercise> search(String query, int limit) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery("""
                SELECT e FROM Exercise e
                WHERE LOWER(e.name) LIKE :q
                   OR LOWER(e.muscleGroup) LIKE :q
                   OR LOWER(e.equipment) LIKE :q
                """, Exercise.class)
                    .setParameter("q", "%" + query.toLowerCase() + "%")
                    .setMaxResults(limit)
                    .getResultList();
        }
    }


    public List<Exercise> readAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Exercise> q = em.createQuery("SELECT e FROM Exercise e", Exercise.class);
            return q.getResultList();
        }
    }

    public void delete(Long id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Exercise e = em.find(Exercise.class, id);
            if (e != null) em.remove(e);
            em.getTransaction().commit();
        }
    }
}
