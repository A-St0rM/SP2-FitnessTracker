package app.DAO;

import app.config.HibernateConfig;
import app.entities.Exercise;
import app.entities.ProgramExercise;
import app.entities.WorkoutProgram;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
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

    public WorkoutProgram update(WorkoutProgram workoutProgram){
        try(EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            WorkoutProgram foundWorkoutProgram = em.find(WorkoutProgram.class, workoutProgram.getId());
            if (foundWorkoutProgram != null) {
                foundWorkoutProgram.setName(workoutProgram.getName());
                foundWorkoutProgram.setDescription(workoutProgram.getDescription());

                if(workoutProgram.getItems() != null) {
                    foundWorkoutProgram.getItems().clear();
                    foundWorkoutProgram.getItems().addAll(workoutProgram.getItems());
                }

                em.merge(foundWorkoutProgram);
                em.getTransaction().commit();
                return foundWorkoutProgram;
            } else throw new EntityNotFoundException("WorkoutProgram could not update. WorkoutProgram not found with id: "+workoutProgram.getId());
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

    public ProgramExercise addExerciseToProgram(long programId, Exercise exercise, Integer sets, Integer reps) {
        if (sets == null || sets <= 0 || reps == null || reps <= 0) {
            throw new IllegalArgumentException("sets and reps must be positive");
        }

        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            WorkoutProgram program = em.find(WorkoutProgram.class, programId);
            if (program == null) {
                em.getTransaction().rollback();
                throw new IllegalArgumentException("Program not found: " + programId);
            }

            Exercise managedExercise = em.contains(exercise) ? exercise : em.merge(exercise);

            //Undgå duplicates:
            TypedQuery<ProgramExercise> q = em.createQuery(
                    "SELECT pe FROM ProgramExercise pe WHERE pe.program = :program AND pe.exercise = :exercise",
                    ProgramExercise.class
            );
            q.setParameter("program", program);
            q.setParameter("exercise", managedExercise);
            List<ProgramExercise> existing = q.getResultList();

            ProgramExercise pe;
            if (!existing.isEmpty()) {
                pe = existing.get(0);
                pe.setSets(sets);
                pe.setReps(reps);
                em.merge(pe);
            } else {
                pe = ProgramExercise.builder()
                        .program(program)
                        .exercise(managedExercise)
                        .sets(sets)
                        .reps(reps)
                        .build();
                em.persist(pe);
            }

            em.getTransaction().commit();
            return pe;
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

}

