package app.routes;

import app.controllers.ExerciseController;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class ExerciseRoutes {

    ExerciseController exerciseController = new ExerciseController();


    public EndpointGroup getRoutes(){
        return () ->{
            get("", (ctx -> exerciseController.getALlExercises(ctx)));
            get("/{id}", (ctx -> exerciseController.findById(ctx)));
            post("", (ctx -> exerciseController.createExercise(ctx)));
            delete("/{id}", ctx -> exerciseController.deleteExercise(ctx));
        };
    }
}
