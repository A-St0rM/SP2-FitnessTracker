package app.DAO;

import app.entities.User;
import app.entities.WeeklySchedule;
import app.entities.WorkoutProgram;
import app.enums.Weekday;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class ScheduleDAO {

    private static ScheduleDAO instance;
    private static EntityManagerFactory emf;

    private ScheduleDAO() {}

    public static ScheduleDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new ScheduleDAO();
        }
        return instance;
    }

    public WeeklySchedule upsert(User user, Weekday weekday, WorkoutProgram program) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            TypedQuery<WeeklySchedule> q = em.createQuery(
                    "SELECT s FROM WeeklySchedule s WHERE s.user = :user AND s.weekday = :weekday", WeeklySchedule.class);
            q.setParameter("user", user);
            q.setParameter("weekday", weekday);
            List<WeeklySchedule> result = q.getResultList();

            WeeklySchedule schedule;
            if (result.isEmpty()) {
                schedule = WeeklySchedule.builder()
                        .user(user)
                        .weekday(weekday)
                        .program(program)
                        .build();
                em.persist(schedule);
            } else {
                schedule = result.get(0);
                schedule.setProgram(program);
                em.merge(schedule);
            }

            em.getTransaction().commit();
            return schedule;
        }
    }

    public List<WeeklySchedule> findByUser(User user) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<WeeklySchedule> q = em.createQuery(
                    "SELECT s FROM WeeklySchedule s JOIN FETCH s.program WHERE s.user = :user", WeeklySchedule.class);
            q.setParameter("user", user);
            return q.getResultList();
        }
    }
}
