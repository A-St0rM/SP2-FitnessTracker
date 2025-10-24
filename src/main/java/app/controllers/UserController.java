package app.controllers;

import app.DAO.ExerciseDAO;
import app.DAO.UserDAO;
import app.config.HibernateConfig;
import app.dtos.UserDTO;
import app.entities.User;
import app.mapper.UserMapper;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
    UserDAO userDAO = new UserDAO(emf);

    public void updateUser(Context ctx) {
        String email = ctx.pathParam("email");

        UserDTO dto = ctx.bodyAsClass(UserDTO.class);

        Optional<User> user = userDAO.findByEmail(email);

        if (user.isEmpty()) {
            ctx.status(HttpStatus.NOT_FOUND).result("User not found");
            return;
        }
        user.get().setEmail(dto.getEmail());
        user.get().setPassword(dto.getPassword());
        user.get().setRole(dto.getRoles());
        User updated = userDAO.update(user.get());
        ctx.status(HttpStatus.OK).json(UserMapper.toDto(updated));
    }

    public void deleteUser(Context ctx) {
        Long id = Long.parseLong(ctx.pathParam("id"));
        Optional<User> user = userDAO.findById(id);


        if (user.isEmpty()) {
            ctx.status(HttpStatus.NOT_FOUND).result("User not found");
            return;
        }

        userDAO.delete(id);
        ctx.status(HttpStatus.OK).result("User deleted");

    }

    public void getByEmail(Context ctx) {
        String email = ctx.pathParam("email");
        Optional<User> user = userDAO.findByEmail(email);

        if (user.isPresent()) {
            ctx.status(200);
            ctx.json(UserMapper.toDto(user.get()));
            logger.info("Fetched hotel with id: " + email);
        } else {
            ctx.status(HttpStatus.NOT_FOUND);
            ctx.result("Hotel not found");
            logger.warn("Hotel with id " + email + " not found");
        }
    }


    public void getAllUsers(Context ctx) {
        List<UserDTO> users = userDAO.readAll()
                .stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
        ctx.status(HttpStatus.OK).json(users);
    }

}
