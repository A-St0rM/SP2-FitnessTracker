package app.mapper;

import app.dtos.WorkoutDTO;
import app.dtos.WorkoutProgramDTO;
import app.entities.ProgramExercise;
import app.entities.WorkoutProgram;

import java.util.ArrayList;
import java.util.List;

public class WorkoutProgramMapper {

    public static WorkoutProgramDTO toDTO(WorkoutProgram workoutProgram) {
        WorkoutProgramDTO workoutProgramDTO = new WorkoutProgramDTO();
        workoutProgramDTO.setId(workoutProgram.getId());
        workoutProgramDTO.setName(workoutProgram.getName());
        workoutProgramDTO.setDescription(workoutProgram.getDescription());

       workoutProgramDTO.setItems(
               workoutProgram.getItems().stream()
                       .map(ProgramExerciseMapper::toDTO)
                       .toList()
       );


        return workoutProgramDTO;
    }

    public static WorkoutProgram toEntity(WorkoutProgramDTO dto) {
        WorkoutProgram entity = new WorkoutProgram();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setItems(
                dto.getItems().stream()
                        .map(ProgramExerciseMapper::toEntity)
                        .toList());
        return entity;
    }



}
