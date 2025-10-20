package app.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String muscleGroup;
    private int reps;
    private double personalRecord;

    @ManyToMany(mappedBy = "exercises")
    private Set<Workout> workouts = new HashSet<>();
}

