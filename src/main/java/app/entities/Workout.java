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
public class Workout {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int workoutId;
    private int exerciseId;
    private String name;
    private String description;

    @ManyToOne()
    private User user;

    @ManyToMany()
    @JoinTable(
            name = "workout_exercise",
            joinColumns = @JoinColumn(name = "workout_id"),
            inverseJoinColumns = @JoinColumn(name = "exercise_id")
    )
    private Set<Exercise> exercises = new HashSet<>();
}
