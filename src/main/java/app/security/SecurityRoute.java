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
            post("/login", securityController.login());
            post("/register", securityController.register());
            get("/healthcheck", securityController1::healthCheck, Role.ANYONE);

        };
    }
}
