package app.DAO;

import app.config.HibernateConfig;
import app.entities.Hotel;
import app.entities.Room;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HotelDAOTest {

    private static EntityManagerFactory emf;
    private static HotelDAO hotelDAO;
    private static RoomDAO roomDAO;

    @BeforeAll
    static void setUpAll() {
        HibernateConfig.setTest(true);
        emf = HibernateConfig.getEntityManagerFactoryForTest();
        hotelDAO = new HotelDAO(emf);
        roomDAO = new RoomDAO(emf);
    }

    @BeforeEach
    void resetDb() {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Room").executeUpdate();
            em.createQuery("DELETE FROM Hotel").executeUpdate();
            em.getTransaction().commit();
        }
    }

    @Test
    void testCreateAndGetHotel() {
        Hotel h = new Hotel("HotelDAO Test", "Testvej 123");
        Hotel created = hotelDAO.create(h);

        assertNotNull(created.getId());

        Hotel found = hotelDAO.getById(created.getId());
        assertEquals("HotelDAO Test", found.getName());
        assertEquals("Testvej 123", found.getAddress());
    }

    @Test
    void testGetAllHotels() {
        hotelDAO.create(new Hotel("Hotel 1", "Street 1"));
        hotelDAO.create(new Hotel("Hotel 2", "Street 2"));

        List<Hotel> hotels = hotelDAO.getAll();
        assertEquals(2, hotels.size());
    }

    @Test
    void testUpdateHotel() {
        Hotel h = hotelDAO.create(new Hotel("OldName", "OldAddress"));
        h.setName("NewName");

        Hotel updated = hotelDAO.update(h);
        assertEquals("NewName", updated.getName());
    }

    @Test
    void testDeleteHotel() {
        Hotel h = hotelDAO.create(new Hotel("DeleteMe", "Street 9"));
        boolean deleted = hotelDAO.delete(h.getId());
        assertTrue(deleted);

        assertNull(hotelDAO.getById(h.getId()));
    }

    @Test
    void testAddRoomAndGetRoomsForHotel() {
        Hotel h = hotelDAO.create(new Hotel("RoomHotel", "Street R"));
        Room r = new Room("101", 200.0, h);

        hotelDAO.addRoom(h, r);

        List<Room> rooms = hotelDAO.getRoomsForHotel(h);
        assertEquals(1, rooms.size());
        assertEquals("101", rooms.get(0).getNumber());
    }
}