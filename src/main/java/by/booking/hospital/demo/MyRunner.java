package by.booking.hospital.demo;

import by.booking.hospital.demo.dao.EmployeeRepo;
import by.booking.hospital.demo.entity.Employee;
import by.booking.hospital.demo.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class MyRunner implements CommandLineRunner {
    @Autowired
    private EmployeeRepo employeeRepo;

    @Override
    public void run(String... args) throws Exception {
        Set<Role> roles =  new HashSet<>();
        roles.add(Role.USER);
        Set<Role> roles2 =  new HashSet<>();
        roles2.add(Role.ADMIN);

        Employee employee = new Employee("user","user","DOCTOR",roles);
        employeeRepo.save(employee);
        Employee employee1 = new Employee("admin","admin","DIRECTOR",roles2);
        employeeRepo.save(employee1);
    }
}
