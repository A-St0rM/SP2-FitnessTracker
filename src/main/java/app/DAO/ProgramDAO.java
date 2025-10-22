package app.DAO;

import app.config.HibernateConfig;
import app.entities.WorkoutProgram;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class ProgramDAO {

    private final EntityManagerFactory emf;

    public ProgramDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public ProgramDAO() {
        this(HibernateConfig.getEntityManagerFactory());
    }

    public WorkoutProgram create(WorkoutProgram program) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(program);
            em.getTransaction().commit();
            return program;
        }
    }

    public Optional<WorkoutProgram> findById(Long id) {
        try (EntityManager em = emf.createEntityManager()) {
            return Optional.ofNullable(em.find(WorkoutProgram.class, id));
        }
    }

    public List<WorkoutProgram> readAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<WorkoutProgram> q = em.createQuery("SELECT p FROM WorkoutProgram p", WorkoutProgram.class);
            return q.getResultList();
        }
    }

    public void delete(Long id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            WorkoutProgram p = em.find(WorkoutProgram.class, id);
            if (p != null) em.remove(p);
            em.getTransaction().commit();
        }
    }
}

