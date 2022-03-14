package commonProjectWIthCommonDays;

import java.time.LocalDate;

public class Record {
    String employeeId;
    String projectId;
    LocalDate startDate;
    LocalDate endDate;

    public Record(String employeeId, String projectId, LocalDate startDate, LocalDate endDate) {
        this.employeeId = employeeId;
        this.projectId = projectId;
        this.startDate = startDate;
        this.endDate = endDate;
    }


    public String getEmployeeId() {
        return employeeId;
    }

    public String getProjectId() {
        return projectId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
}
