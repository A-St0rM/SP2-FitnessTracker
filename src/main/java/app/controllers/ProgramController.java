package app.controllers;

import app.DAO.ExerciseDAO;
import app.DAO.ProgramDAO;
import app.config.HibernateConfig;
import app.dtos.ProgramExerciseDTO;
import app.dtos.WorkoutProgramDTO;
import app.entities.Exercise;
import app.entities.ProgramExercise;
import app.entities.WorkoutProgram;
import app.mapper.WorkoutProgramMapper;
import app.utils.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ProgramController {

    private static final Logger logger = LoggerFactory.getLogger(ProgramController.class);
    EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
    ProgramDAO programDAO = new ProgramDAO(emf);
    ExerciseDAO exerciseDAO = new ExerciseDAO(emf);
    private final ObjectMapper mapper = new Utils().getObjectMapper();

    public void create(Context ctx){
        try {
            WorkoutProgramDTO workoutProgramDTO = ctx.bodyAsClass(WorkoutProgramDTO.class);

            WorkoutProgram workoutProgram = WorkoutProgramMapper.toEntity(workoutProgramDTO);

            //Mangler stadig exercise og program sat på når jeg laver toEntitym derfor kommer det her
            for (int i = 0; i < workoutProgram.getItems().size(); i++) {
                //Sætter program på her
                ProgramExercise item = workoutProgram.getItems().get(i);
                ProgramExerciseDTO dtoItem = workoutProgramDTO.getItems().get(i);
                //Hver iteration sætter den Program på ProgramExercise
                item.setProgram(workoutProgram);

                //Får vi exercise udfra vores ProgramExerciseDTO og DAO pga. DTO indeholder ExerciseId
                Exercise exercise = exerciseDAO.findById(dtoItem.getExerciseId()).orElseThrow(
                        () -> new RuntimeException("Exercise with id " + dtoItem.getExerciseId() + " not found")
                );
                //Sætter exercise på her
                item.setExercise(exercise);
            }

            programDAO.create(workoutProgram);

            ctx.status(200).json(WorkoutProgramMapper.toDTO(workoutProgram));
        } catch (Exception e) {
            logger.error(e.getMessage());
            ctx.status(500).result("Something went wrong with creating a workout program");
        }
    }

    public void findById(Context ctx){
        Long id = Long.parseLong(ctx.pathParam("id"));

        WorkoutProgram workoutProgram = programDAO.findById(id).orElse(null);

        if(workoutProgram != null) {
            ctx.status(200).json(WorkoutProgramMapper.toDTO(workoutProgram));
        } else  {
            ctx.status(404).result("Workout program not found with id: "+id);
        }
    }

    public void readAll(Context ctx){
        try {
            List<WorkoutProgram> workoutPrograms = programDAO.readAll();

            List<WorkoutProgramDTO> workoutProgramDTOS = workoutPrograms.stream().map(WorkoutProgramMapper::toDTO).toList();

            ctx.status(200).json(workoutProgramDTOS);
        } catch (Exception e) {
            logger.error(e.getMessage());
            ctx.status(500).result("Something went wrong with reading all workout programs");
        }
    }

    public void deleteById(Context ctx){
        Long id = Long.parseLong(ctx.pathParam("id"));
        WorkoutProgram workoutProgram = programDAO.findById(id).orElse(null);
        if(workoutProgram != null) {
            programDAO.delete(workoutProgram.getId());
            ctx.status(204).result("Workout program deleted");
        }
        else  {
            ctx.status(404).result("Workout program not found with id: "+id);
        }
    }


}
