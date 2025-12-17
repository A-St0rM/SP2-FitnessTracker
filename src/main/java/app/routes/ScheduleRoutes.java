package app.routes;

import app.controllers.ScheduleController;
import app.enums.Role;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class ScheduleRoutes {

    ScheduleController scheduleController = new ScheduleController();

    public EndpointGroup getRoutes() {
        return () -> {
            get("", scheduleController::getMySchedule, Role.USER);
            put("/{weekday}", scheduleController::upsertDay, Role.USER);
        };
    }
}
