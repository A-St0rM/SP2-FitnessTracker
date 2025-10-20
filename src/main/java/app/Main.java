package app;

import app.DAO.ExerciseDAO;
import app.DAO.WorkoutDAO;
import app.config.ApplicationConfig;
import app.config.HibernateConfig;
import app.entities.Exercise;
import app.entities.User;
import app.entities.Workout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        ApplicationConfig.startServer(7007);
    }
}