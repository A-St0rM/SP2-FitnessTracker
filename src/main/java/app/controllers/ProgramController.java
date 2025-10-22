package app.controllers;

import app.DAO.ProgramDAO;
import app.config.HibernateConfig;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProgramController {

    private static final Logger logger = LoggerFactory.getLogger(ProgramController.class);
    EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
    ProgramDAO programDAO = new ProgramDAO(emf);




}
