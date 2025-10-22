package app.mapper;

import app.dtos.ExerciseDTO;
import app.entities.Exercise;

import java.util.stream.Collectors;

public class ExerciseMapper {

    public static ExerciseDTO toDto(Exercise exercise){

        ExerciseDTO exerciseDTO = new ExerciseDTO();

        exerciseDTO.setId(exercise.getId());
        exerciseDTO.setReps(exercise.getReps());
        exerciseDTO.setName(exercise.getName());
        exerciseDTO.setMuscleGroup(exercise.getMuscleGroup());

        if(exercise.getWorkouts() != null){
            exerciseDTO.setWorkoutList(
                    exercise.getWorkouts().stream()
                            .map(workout -> WorkoutMapper.toDto(workout)).toList());
        }

        return exerciseDTO;
    }

    public static Exercise toEntity(ExerciseDTO exerciseDTO){
        Exercise exercise = new Exercise();

        exercise.setId(exerciseDTO.getId());
        exercise.setReps(exerciseDTO.getReps());
        exercise.setName(exerciseDTO.getName());
        exercise.setMuscleGroup(exerciseDTO.getMuscleGroup());

        if(exerciseDTO.getWorkoutList() != null){
            exercise.setWorkouts(
                    exerciseDTO.getWorkoutList().stream()
                            .map(workout -> WorkoutMapper.toEntity(workout)).collect(Collectors.toSet()));
        }

        return exercise;

    }

}
