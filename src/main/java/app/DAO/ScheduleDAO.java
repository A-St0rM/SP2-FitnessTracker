package app.DAO;

import app.config.HibernateConfig;
import app.entities.User;
import app.entities.WeeklySchedule;
import app.entities.WorkoutProgram;
import app.enums.Weekday;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class ScheduleDAO {

    private final EntityManagerFactory emf;

    public ScheduleDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public ScheduleDAO() {
        this(HibernateConfig.getEntityManagerFactory());
    }

    public WeeklySchedule upsert(User user, Weekday weekday, WorkoutProgram program) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            TypedQuery<WeeklySchedule> q = em.createQuery(
                    "SELECT s FROM WeeklySchedule s WHERE s.user = :user AND s.weekday = :weekday",
                    WeeklySchedule.class
            );
            q.setParameter("user", user);
            q.setParameter("weekday", weekday);
            List<WeeklySchedule> existing = q.getResultList();

            WeeklySchedule s;
            if (existing.isEmpty()) {
                s = new WeeklySchedule();
                s.setUser(user);
                s.setWeekday(weekday);
                s.setProgram(program);
                em.persist(s);
            } else {
                s = existing.get(0);
                s.setProgram(program);
                em.merge(s);
            }

            em.getTransaction().commit();
            return s;
        }
    }

    public List<WeeklySchedule> findByUser(User user) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<WeeklySchedule> q = em.createQuery(
                    "SELECT s FROM WeeklySchedule s " +
                            "JOIN FETCH s.program " +
                            "WHERE s.user = :user ORDER BY s.weekday",
                    WeeklySchedule.class
            );
            q.setParameter("user", user);
            return q.getResultList();
        }
    }
}
