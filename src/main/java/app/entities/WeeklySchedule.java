package app.entities;

import app.enums.Weekday;
import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name="weekly_schedule",
        uniqueConstraints = @UniqueConstraint(name="uk_user_weekday", columnNames={"user_id","weekday"}))
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class WeeklySchedule {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false) @JoinColumn(name="user_id")
    private User user;

    @Enumerated(EnumType.STRING) @Column(nullable=false)
    private Weekday weekday;

    @ManyToOne(optional=false) @JoinColumn(name="program_id")
    private WorkoutProgram program;
}


