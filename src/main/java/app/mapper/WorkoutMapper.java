package app.mapper;

import app.dtos.ExerciseDTO;
import app.dtos.WorkoutDTO;
import app.entities.Exercise;
import app.entities.Workout;

import java.util.stream.Collectors;

public class WorkoutMapper {

    public static WorkoutDTO toDto(Workout workout){

        WorkoutDTO workoutDTO = new WorkoutDTO();

        workoutDTO.setId(workout.getId());
        workoutDTO.setName(workout.getName());
        workoutDTO.setDescription(workout.getDescription());
        workoutDTO.setUserId(workoutDTO.getUserId());

        if(workout.getExercises() != null){

            workoutDTO.setExercises(workout.getExercises().stream()
                    .map(exercise -> ExerciseMapper.toDto(exercise)).toList());
        }

        return workoutDTO;
    }

    public static Workout toEntity(WorkoutDTO workoutDTO){

        Workout workout = new Workout();

        workout.setId(workoutDTO.getId());
        workout.setName(workoutDTO.getName());
        workout.setDescription(workoutDTO.getDescription());

        if(workoutDTO.getExercises() != null){

            workout.setExercises(
                    workoutDTO.getExercises().stream()
                            .map(exercise -> ExerciseMapper.toEntity(exercise)).collect(Collectors.toSet())
            );

        }

        return workout;
    }
}
