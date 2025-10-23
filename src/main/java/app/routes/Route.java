package app.routes;

import app.enums.Role;
import app.security.SecurityRoute;
import app.utils.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;

public class Route {

    private SecurityRoute securityRoute = new SecurityRoute();
    private ExerciseRoutes exerciseRoutes = new ExerciseRoutes();
    private ExternalExerciseRoutes externalExerciseRoutes = new ExternalExerciseRoutes();
    private static ObjectMapper jsonMapper = new Utils().getObjectMapper();

    public EndpointGroup getRoutes() {
        return () -> {
            path("/auth", securityRoute.getSecurityRoutes());
            path("/protected", getSecuredRoutes());
            path("/exercise", exerciseRoutes.getRoutes());
            path("/exercise", externalExerciseRoutes.getRoutes());
        };
    }

    public static EndpointGroup getSecuredRoutes(){
        return ()->{
                get("/user_demo",(ctx)->ctx.json(jsonMapper.createObjectNode().put("msg",  "Hello from USER Protected")),Role.USER);
                get("/admin_demo",(ctx)->ctx.json(jsonMapper.createObjectNode().put("msg",  "Hello from ADMIN Protected")), Role.ADMIN);
        };
    }

}

