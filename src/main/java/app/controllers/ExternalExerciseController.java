package app.controllers;

import app.DAO.ExerciseDAO;
import app.DAO.ProgramDAO;
import app.dtos.ExternalExerciseDTO;
import app.services.ExerciseImportService;
import app.services.ExternalExerciseService;
import app.utils.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;

import java.util.Map;

public class ExternalExerciseController {
    private final ExternalExerciseService externalService;
    private final ExerciseImportService importService;
    private final ObjectMapper mapper;

    public ExternalExerciseController(ObjectMapper mapper, ExerciseDAO exerciseDAO, ProgramDAO programDAO) {
        this.mapper = mapper;
        this.externalService = new ExternalExerciseService(mapper);
        this.importService = new ExerciseImportService(exerciseDAO, programDAO);
    }

    // no-args konstruktor til brug i routes
    public ExternalExerciseController() {
        this(
                new Utils().getObjectMapper(),
                new app.DAO.ExerciseDAO(new app.config.HibernateConfig().getEntityManagerFactory()),
                new app.DAO.ProgramDAO(new app.config.HibernateConfig().getEntityManagerFactory())
        );
    }

    public void search(Context ctx) {
        String name = ctx.queryParam("name");
        String muscle = ctx.queryParam("muscle");
        if ((name == null || name.isBlank()) && (muscle == null || muscle.isBlank())) {
            ctx.status(400).json(Map.of("error", "Provide ?name= or ?muscle="));
            return;
        }
        var result = (name != null && !name.isBlank())
                ? externalService.searchByName(name)
                : externalService.searchByMuscle(muscle);
        ctx.json(result);
    }

    public void addToProgram(Context ctx) {
        long programId = Long.parseLong(ctx.pathParam("programId"));
        try {
            var node = mapper.readTree(ctx.body());
            var dto  = mapper.treeToValue(node, ExternalExerciseDTO.class);
            Integer sets = node.has("sets") ? node.get("sets").asInt() : null;
            Integer reps = node.has("reps") ? node.get("reps").asInt() : null;

            var exercise = importService.findOrCreateFromExternal(dto);
            var pe = importService.addExerciseToProgram(programId, exercise, sets, reps);

            ctx.status(201).json(Map.of(
                    "message", "Exercise added to program",
                    "programExerciseId", pe.getId(),
                    "exerciseId", exercise.getId()
            ));
        } catch (Exception e) {
            ctx.status(400).json(Map.of("error", e.getMessage()));
        }
    }
}
