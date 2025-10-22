package app.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "exercises")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String muscleGroup;
    private String equipment;

    @Column(unique = true, nullable = false)
    private String externalId;

    @Column(length = 2000)
    private String instructions;


    //Exercise
    //ProgramExercise
    //WorkoutProgram
    //WeeklySchedule
    //User

  //En exercise kan blive brugt i mange ProgramExercise

    //ProgramExercise forbinder WorkoutProgram og Exercise

}

