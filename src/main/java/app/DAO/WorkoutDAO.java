package app.DAO;

import app.entities.Exercise;
import app.entities.Workout;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.jdbc.Work;

import java.util.List;

public class WorkoutDAO implements IDAO<Workout, Integer>{
    EntityManagerFactory emf;

    public WorkoutDAO(EntityManagerFactory emf){
        this.emf = emf;
    }


    @Override
    public List<Workout> getAll() {
        try(EntityManager em = emf.createEntityManager()){
            return em.createQuery("SELECT w FROM Workout w", Workout.class).getResultList();
        }


    }

    @Override
    public Workout getById(Integer id) {
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            Workout foundWorkout = em.find(Workout.class, id);
            em.getTransaction().commit();
            return foundWorkout;
        }
    }

    @Override
    public Workout create(Workout workout) {
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            em.persist(workout);
            em.getTransaction().commit();
            return workout;
        }
    }

    @Override
    public Workout update(Workout workout) {
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            Workout updatedWorkout = em.merge(workout);
            em.getTransaction().commit();
            return updatedWorkout;
        }
    }

    @Override
    public boolean delete(Integer id) {
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            Workout foundWorkout = em.find(Workout.class, id);
            em.remove(foundWorkout);
        }
        Workout workout = getById(id);
        if(workout == null){
            return false;
        } else {
            return true;
        }
    }


}
