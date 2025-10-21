package app.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "workout_exercises",
        indexes = {
                @Index(name="idx_plan_weekday", columnList = "plan_id, weekday")
        })
public class WorkoutExercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private WorkoutPlan plan;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DayOfWeek weekday;

    @Column(nullable = false)
    private Integer position = 0;

    private String externalExerciseId;
    private String name;
    private Integer setsCount;
    private Integer reps;
    private Double weightKg;
}
