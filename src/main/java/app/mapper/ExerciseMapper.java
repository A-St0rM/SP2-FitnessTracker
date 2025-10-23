package app.mapper;

import app.dtos.ExerciseDTO;
import app.entities.Exercise;

import java.util.stream.Collectors;

public class ExerciseMapper {

    public static ExerciseDTO toDto(Exercise exercise){

        ExerciseDTO exerciseDTO = new ExerciseDTO();

        exerciseDTO.setId(exercise.getId());
        exerciseDTO.setName(exercise.getName());
        exerciseDTO.setMuscleGroup(exercise.getMuscleGroup());
        exerciseDTO.setEquipment(exercise.getEquipment());
        exerciseDTO.setInstructions(exercise.getInstructions());


        return exerciseDTO;
    }

    public static Exercise toEntity(ExerciseDTO exerciseDTO){
        Exercise exercise = new Exercise();

        exercise.setId(exerciseDTO.getId());
        exercise.setName(exerciseDTO.getName());
        exercise.setMuscleGroup(exerciseDTO.getMuscleGroup());
        exercise.setEquipment(exerciseDTO.getEquipment());
        exercise.setInstructions(exerciseDTO.getInstructions());

        return exercise;
    }

}


