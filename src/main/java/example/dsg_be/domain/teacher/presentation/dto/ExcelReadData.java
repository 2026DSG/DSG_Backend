package example.dsg_be.domain.teacher.presentation.dto;

import lombok.Getter;

@Getter
public class ExcelReadData {
    private String name;
    private String department;
    private String position;

    public ExcelReadData(String name, String department, String position)
    {
        this.name = name;
        this.department = department;
        this.position = position;
    }
}
