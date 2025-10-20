package app.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int exerciseId;
    private String name;
    private String muscleGroup;
    private int reps;
    private double personalRecord;

    @ManyToMany(mappedBy = "exercises")
    private Set<Workout> workouts = new HashSet<>();
}

