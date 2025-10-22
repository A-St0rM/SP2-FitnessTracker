package app;

import app.DAO.ExerciseDAO;
import app.DAO.WorkoutDAO;
import app.config.ApplicationConfig;
import app.entities.Exercise;
import app.entities.Workout;

public class Main {

    public static void main(String[] args) {
        ApplicationConfig.startServer(7007);
    }
}