package app.controllers;

import app.DAO.ExerciseDAO;
import app.config.HibernateConfig;
import app.dtos.ExerciseDTO;
import app.entities.Exercise;
import app.mapper.ExerciseMapper;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class ExerciseController {

    private static final Logger logger = LoggerFactory.getLogger(ExerciseController.class);
    EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
    ExerciseDAO exerciseDAO = new ExerciseDAO(emf);

    public void getALlExercises(Context ctx){
        List<ExerciseDTO> exercises = exerciseDAO.readAll().stream()
                .map(exercise -> ExerciseMapper.toDto(exercise)).toList();

        ctx.status(HttpStatus.OK).json(exercises);

    }

    public void findById(Context ctx){
        Long id = Long.parseLong(ctx.pathParam("id"));

        Optional<Exercise> exercise = exerciseDAO.findById(id);

        if(exercise.isPresent()){
            ctx.status(200);
            ctx.json(ExerciseMapper.toDto(exercise.orElse(null)));
            logger.info("Fetched exercise with id: " + id);

        } else {
            ctx.status(HttpStatus.NOT_FOUND);
            ctx.result("Exercise not found");
            logger.warn("Exercise with id: " + id + " not found");
        }
    }

    public void createExercise(Context ctx){
        ExerciseDTO exerciseDTO = ctx.bodyAsClass(ExerciseDTO.class);
        Exercise exerciseCreated = exerciseDAO.create(ExerciseMapper.toEntity(exerciseDTO));
        ctx.status(HttpStatus.CREATED).json(ExerciseMapper.toDto(exerciseCreated));
    }


    public void updateExercise(Context ctx){
        Long id = Long.parseLong(ctx.pathParam("id"));

        ExerciseDTO exerciseDTO = ctx.bodyAsClass(ExerciseDTO.class);

        Optional<Exercise> exercise = exerciseDAO.findById(id);
            if(exercise.isEmpty()){
                ctx.status(HttpStatus.NOT_FOUND).result("Exercise not found");
                return;
            }


            if(!exercise.get().getExternalId().equals(exerciseDTO.getExternalId())){
                Optional<Exercise> duplicate = exerciseDAO.findByExternalId(exerciseDTO.getExternalId());
                if(duplicate.isPresent()){
                    ctx.status(HttpStatus.CONFLICT).result("ExternalId already exists");
                    return;
                }
                exercise.get().setExternalId(exerciseDTO.getExternalId());
            }





            exercise.get().setInstructions(exerciseDTO.getInstructions());
            exercise.get().setName(exerciseDTO.getName());
            exercise.get().setEquipment(exerciseDTO.getEquipment());
            //exercise.setId(exerciseDTO.getId());
            exercise.get().setMuscleGroup(exerciseDTO.getMuscleGroup());
            //exercise.setExternalId(exerciseDTO.getExternalId());

        Exercise updatedExercise = exerciseDAO.update(exercise.get());

        ctx.status(HttpStatus.OK).json(ExerciseMapper.toDto(updatedExercise));
    }



    public void deleteExercise(Context ctx){
        Long id = Long.parseLong(ctx.pathParam("id"));

        exerciseDAO.delete(id);

        Optional<Exercise> deleted = exerciseDAO.findById(id);

        if(deleted.isEmpty()){
            ctx.status(HttpStatus.OK).result("Exercise deleted");
        }else {
            ctx.status(HttpStatus.NOT_FOUND).result("Exercise not found");
        }
    }
}
