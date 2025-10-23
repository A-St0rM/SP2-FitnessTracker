package app.routes;

import app.controllers.ExerciseController;
import app.controllers.ProgramController;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class ProgramRoutes {

    ProgramController programController = new ProgramController();


    public EndpointGroup getRoutes(){
        return () ->{
            get("", (ctx -> programController.readAll(ctx)));
            get("/{id}", (ctx -> programController.findById(ctx)));
            post("", (ctx -> programController.create(ctx)));
            delete("/{id}", ctx -> programController.deleteById(ctx));
            put("/{id}", (ctx -> programController.update(ctx)));
        };
    }

}
