package app.security;


import app.enums.Role;
import app.security.interfaces.ISecurityController;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class SecurityRoute {

    ISecurityController securityController = new SecurityController();
    SecurityController securityController1 = new SecurityController();

    public EndpointGroup getSecurityRoutes () {
        return () -> {
            post("/login", securityController.login(), Role.ANYONE);
            post("/register", securityController.register(), Role.ANYONE);
            get("/healthcheck", securityController1::healthCheck, Role.ANYONE);

        };
    }
}
