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
            TypedQuery<Exercise> q = em.createQuery("SELECT e FROM Exercise e ORDER BY id ASC", Exercise.class);
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


    public Exercise update(Exercise exercise) {
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            Exercise updatedExercise = em.merge(exercise);
            em.getTransaction().commit();
            return updatedExercise;
        }
    }

    public Optional<Exercise> findByNameMuscleEquipment(String name, String muscle, String equipment) {
        var em = emf.createEntityManager();
        try {
            var q = em.createQuery("""
            SELECT e FROM Exercise e
            WHERE LOWER(e.name) = LOWER(:n)
              AND COALESCE(LOWER(e.muscleGroup),'') = COALESCE(LOWER(:m),'')
              AND COALESCE(LOWER(e.equipment),'')  = COALESCE(LOWER(:eq),'')
        """, Exercise.class);
            q.setParameter("n", name == null ? "" : name.trim());
            q.setParameter("m", muscle == null ? "" : muscle.trim());
            q.setParameter("eq", equipment == null ? "" : equipment.trim());
            var list = q.getResultList();
            return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
        } finally {
            em.close();
        }
    }


}

