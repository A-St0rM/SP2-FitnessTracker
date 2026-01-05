package app.config;

import app.Main;
import app.enums.Role;
import app.routes.Route;
import app.security.SecurityController;
import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import io.javalin.http.InternalServerErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class ApplicationConfig {
    private static Route routes = new Route();

    private static final Logger logger = LoggerFactory.getLogger(ApplicationConfig.class);
    private static final Logger debugLogger = LoggerFactory.getLogger("app");
    private static SecurityController securityController = new SecurityController();
    private static ApplicationConfig appConfig;
    private static Javalin app;



    public static void configuration(JavalinConfig config){



        config.showJavalinBanner = false;
        config.bundledPlugins.enableRouteOverview("/routes");
        config.router.contextPath = "/api/v1"; // base path for all endpoints
        config.router.apiBuilder(routes.getRoutes());
    }

    private static boolean isTest() {
        return "test".equalsIgnoreCase(System.getProperty("env"));
    }


    public static Javalin startServer(int port) {
        routes = new Route();
        app = Javalin.create(ApplicationConfig::configuration);

        logger.info("Java application started!");

        // Set up routes
        app.get("/", ctx -> {
            logger.info("Handling request to /");
            ctx.result("Hello, Javalin with Logging!");
        });

        app.get("/error", ctx -> {
            logger.error("An error endpoint was accessed");
            throw new RuntimeException("This is an intentional error for logging demonstration.");
        });

        // Log the server start
        logger.info("Javalin application started on http://localhost:7070");
        debugLogger.debug("Debug log message from Main class during startup");

        // Exception handling example
        app.exception(Exception.class, (e, ctx) -> {
            logger.error("An exception occurred: {}", e.getMessage(), e);
            ctx.status(500).result("Internal Server Error");
        });


        app.error(HttpStatus.INTERNAL_SERVER_ERROR, ctx -> {
            logger.error("Bummer: {}", ctx.status());
            Map<String, String> msg = Map.of("error", "Internal Server Error, dude!", "status", String.valueOf(ctx.status()));
            throw new InternalServerErrorResponse("Off limits!", msg);
        });

        app.beforeMatched(ctx -> {
            if (ctx.method().name().equals("OPTIONS")) {
                ctx.status(200);
                return;
            }
        });

        boolean isTest = "test".equalsIgnoreCase(System.getProperty("env"));

        if (!isTest) {
            securityController = new SecurityController(); // kun i non-test
            app.beforeMatched(ctx -> {
                if (ctx.method().name().equals("OPTIONS")){ return;};
            app.beforeMatched(securityController.authenticate());
            app.beforeMatched(securityController.authorize());
            });
        }


        app.before(ctx -> setCorsHeaders(ctx));

        app.options("/*", ctx -> { // Burde nok ikke være nødvendig?
            setCorsHeaders(ctx);
            ctx.status(200);
        });

        app.start(port);
        return app;
    }

    public static void stopServer(Javalin app) {
        app.stop();
    }


    public ApplicationConfig setCORS() {
        app.before(ctx -> {
            setCorsHeaders(ctx);
        });
        app.options("/*", ctx -> { // Burde nok ikke være nødvendig?
            setCorsHeaders(ctx);
        });
        return appConfig;
    }

    private static void setCorsHeaders(Context ctx) {
        ctx.header("Access-Control-Allow-Origin", "*");
        ctx.header("Access-Control-Allow-Methods", "GET, POST, PUT, PATCH, DELETE, OPTIONS");
        ctx.header("Access-Control-Allow-Headers", "Content-Type, Authorization");
        ctx.header("Access-Control-Allow-Credentials", "true");
    }
}
