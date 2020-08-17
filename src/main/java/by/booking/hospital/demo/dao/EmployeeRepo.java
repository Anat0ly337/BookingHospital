package by.booking.hospital.demo.dao;

import by.booking.hospital.demo.entity.Employee;
import by.booking.hospital.demo.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee, Long> {
    @Query("from Employee e " +
            "where " +
            "   concat(e.username, ' ', e.userType, ' ', e.id) like concat('%', :name, '%')")
    List<Employee> findByName(@Param("name") String name);

    Employee findByUsername(String s);
}
