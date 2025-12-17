package app.dtos;

import app.enums.Weekday;

public class ScheduleDTO {
    private Weekday weekday;
    private Long programId;
    private String programName;

    public ScheduleDTO() {}

    public ScheduleDTO(Weekday weekday, Long programId, String programName) {
        this.weekday = weekday;
        this.programId = programId;
        this.programName = programName;
    }

    public Weekday getWeekday() { return weekday; }
    public void setWeekday(Weekday weekday) { this.weekday = weekday; }

    public Long getProgramId() { return programId; }
    public void setProgramId(Long programId) { this.programId = programId; }

    public String getProgramName() { return programName; }
    public void setProgramName(String programName) { this.programName = programName; }
}
