package kirilin.dev.multihandler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Source("data/orders.csv")
public class Order {
    private String name;
    private String desc;
    private int price;
    private String employeeId;
}
