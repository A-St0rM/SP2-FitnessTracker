package app;

import app.config.ApplicationConfig;
import app.entities.User;

public class Main {

    public static void main(String[] args) {

        ApplicationConfig.startServer(7007);
    }
}