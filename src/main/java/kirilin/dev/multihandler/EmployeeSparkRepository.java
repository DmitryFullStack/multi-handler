package kirilin.dev.multihandler;

import java.util.List;

public interface EmployeeSparkRepository extends SparkRepository<Employee> {

    List<Employee> findByFirstNameContains(String firstName);

    List<Employee> findByYearBetween(int lowerLimit, int upperLimit);

    List<Employee> findByYearGreaterThan(int limit );

    long findByYearGreaterThanCount(int limit);

    List<Employee> findByYearBetweenOrderByYear(int lowerLimit, int upperLimit);

}
