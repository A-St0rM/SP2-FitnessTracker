package app.routes;

import app.controllers.ExternalExerciseController;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class ExternalExerciseRoutes {

    private final ExternalExerciseController controller = new ExternalExerciseController();

    public EndpointGroup getRoutes() {
        return () -> {

            get("/external/search", controller::search);
            post("/program/{programId}/external", controller::addToProgram);
        };
    }
}
