package kirilin.dev.multihandler;

import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Source("data/employees.csv")
public class Employee {

    private String id;
    private String firstName;
    private String lastName;
    private Integer year;

    @ForeignKeyName("employeeId")
    private List<Order> orders;

}
