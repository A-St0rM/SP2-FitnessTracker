package app.api;

import app.entities.Hotel;
import app.entities.Room;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.util.Map;

public class HotelPopulator {
    public static Map<String, Integer> seedTwoHotels(EntityManagerFactory emf) {
        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();
            // Enten TRUNCATE (nulstiller ID), eller DELETE (behold sekvens)
            em.createNativeQuery("TRUNCATE TABLE room, hotel RESTART IDENTITY CASCADE").executeUpdate();
            // em.createQuery("DELETE FROM Room").executeUpdate();
            // em.createQuery("DELETE FROM Hotel").executeUpdate();

            var h1 = new Hotel("TestHotel1", "Street 1");
            var h2 = new Hotel("TestHotel2", "Street 2");

            var r1 = new Room("101", 100.0, h1);
            var r2 = new Room("102", 150.0, h1);
            h1.getRooms().add(r1);
            h1.getRooms().add(r2);

            em.persist(h1);
            em.persist(h2);
            em.flush(); // tildel IDs nu
            em.getTransaction().commit();

            return Map.of(
                    "h1", h1.getId(),
                    "h2", h2.getId()
            );
        }
    }
}

