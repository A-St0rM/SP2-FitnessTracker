package app.controllers;

import app.DAO.ProgramDAO;
import app.DAO.ScheduleDAO;
import app.DAO.UserDAO;
import app.config.HibernateConfig;
import app.dtos.ScheduleDTO;
import app.dtos.UserDTO;
import app.entities.User;
import app.entities.WeeklySchedule;
import app.entities.WorkoutProgram;
import app.enums.Weekday;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;
import java.util.Optional;

public class ScheduleController {

    EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
    ScheduleDAO scheduleDAO = new ScheduleDAO(emf);
    UserDAO userDAO = new UserDAO(emf);
    ProgramDAO programDAO = new ProgramDAO(emf);

    public void getMySchedule(Context ctx) {
        UserDTO tokenUser = ctx.attribute("user");
        if (tokenUser == null) {
            ctx.status(HttpStatus.UNAUTHORIZED).result("Missing/invalid token");
            return;
        }

        Optional<User> userOpt = userDAO.findByEmail(tokenUser.getEmail());
        if (userOpt.isEmpty()) {
            ctx.status(HttpStatus.NOT_FOUND).result("User not found");
            return;
        }

        List<WeeklySchedule> list = scheduleDAO.findByUser(userOpt.get());
        List<ScheduleDTO> dto = list.stream()
                .map(s -> new ScheduleDTO(
                        s.getWeekday(),
                        s.getProgram().getId(),
                        s.getProgram().getName()
                ))
                .toList();
        ctx.json(dto).status(HttpStatus.OK);
    }


    public void upsertDay(Context ctx) {
        UserDTO tokenUser = ctx.attribute("user");
        if (tokenUser == null) {
            ctx.status(HttpStatus.UNAUTHORIZED).result("Missing/invalid token");
            return;
        }

        String weekdayStr = ctx.pathParam("weekday");
        Weekday weekday;
        try {
            weekday = Weekday.valueOf(weekdayStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST).result("Invalid weekday");
            return;
        }

        record UpsertReq(Long programId) {}
        UpsertReq req = ctx.bodyAsClass(UpsertReq.class);
        if (req == null || req.programId() == null) {
            ctx.status(HttpStatus.BAD_REQUEST).result("programId is required");
            return;
        }

        Optional<User> userOpt = userDAO.findByEmail(tokenUser.getEmail());
        if (userOpt.isEmpty()) {
            ctx.status(HttpStatus.NOT_FOUND).result("User not found");
            return;
        }

        Optional<WorkoutProgram> programOpt = programDAO.findById(req.programId());
        if (programOpt.isEmpty()) {
            ctx.status(HttpStatus.NOT_FOUND).result("Program not found");
            return;
        }

        WeeklySchedule saved = scheduleDAO.upsert(userOpt.get(), weekday, programOpt.get());
        ScheduleDTO dto = new ScheduleDTO(saved.getWeekday(), saved.getProgram().getId(), saved.getProgram().getName());
        ctx.json(dto).status(HttpStatus.OK);
    }
}
