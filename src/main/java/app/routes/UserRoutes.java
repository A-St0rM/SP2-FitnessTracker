package app.routes;

import app.controllers.UserController;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;
import static io.javalin.apibuilder.ApiBuilder.put;

public class UserRoutes {
UserController userController = new UserController();

    public EndpointGroup getRoutes(){
        return () ->{
            get("", (ctx -> userController.getAllUsers(ctx)));
            get("/{email}", (ctx -> userController.getByEmail(ctx)));
            delete("/{id}", ctx -> userController.deleteUser(ctx));
            put("/{email}", ctx -> userController.updateUser(ctx));
        };
    }
}
