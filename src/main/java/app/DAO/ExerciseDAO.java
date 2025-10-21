package app.DAO;

import app.entities.Exercise;
import app.entities.Workout;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class ExerciseDAO implements IDAO<Exercise, Integer>{
    EntityManagerFactory emf;

    public ExerciseDAO(EntityManagerFactory emf){
        this.emf = emf;
    }


    @Override
    public List<Exercise> getAll() {
        try(EntityManager em = emf.createEntityManager()){
            return em.createQuery("SELECT e FROM Exercise e", Exercise.class).getResultList();
        }
    }

    @Override
    public Exercise getById(Integer id) {
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            Exercise foundExercise = em.find(Exercise.class, id);
            em.getTransaction().commit();
            return foundExercise;
        }
    }

    @Override
    public Exercise create(Exercise exercise) {
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            em.persist(exercise);
            em.getTransaction().commit();
            return exercise;
        }
    }

    @Override
    public Exercise update(Exercise exercise) {
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            Exercise updatedExercise = em.merge(exercise);
            em.getTransaction().commit();
            return updatedExercise;
        }
    }

    @Override
    public boolean delete(Integer id) {
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            Exercise foundExercise = em.find(Exercise.class, id);
            em.remove(foundExercise);
            em.getTransaction().commit();
            if(foundExercise == null){
                return false;
            } else {
                return true;
            }
        }
    }

    public void addExerciseToWorkout(Exercise exercise, Workout workout){
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();

            Workout specificWorkout = em.find(Workout.class, workout.getWorkoutId());
            Exercise specifikExercise = em.find(Exercise.class, exercise.getExerciseId());


            if(specificWorkout != null && specifikExercise != null){
                specifikExercise.getWorkouts().add(specificWorkout);
                specificWorkout.getExercises().add(specifikExercise);
            }

            em.persist(exercise);
            em.persist(specificWorkout);
            em.getTransaction().commit();
        }
    }
}
