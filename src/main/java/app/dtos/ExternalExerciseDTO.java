package app.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExternalExerciseDTO {
    public String name;
    public String type;
    public String muscle;
    public String equipment;
    public String difficulty;
    public String instructions;
}
