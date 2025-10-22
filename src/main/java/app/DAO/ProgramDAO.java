package app.DAO;

import app.entities.WorkoutProgram;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class ProgramDAO {

    private static ProgramDAO instance;
    private static EntityManagerFactory emf;

    private ProgramDAO() {}

    public static ProgramDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new ProgramDAO();
        }
        return instance;
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

