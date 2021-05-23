package kirilin.dev.multihandler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;

@SpringBootApplication
public class MultiHandlerApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(MultiHandlerApplication.class, args);
        EmployeeSparkRepository employeeRepository = context.getBean(EmployeeSparkRepository.class);
        List<Employee> result = employeeRepository.findByYearBetweenOrderByYear(1900, 2000);
        for (Employee employee : result) {
            System.out.println("Employee: " + employee);
        }
        for (Order order : result.get(0).getOrders()) {
            System.out.println(order);
        }
//        System.out.println("Count: " + employeeRepository.findByYearGreaterThanCount(1960));
    }

}
