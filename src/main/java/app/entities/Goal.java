package app.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
public class Goal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int goal_id;
    private String name;
    private String goalDescription;
    private boolean achieved;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;

}
