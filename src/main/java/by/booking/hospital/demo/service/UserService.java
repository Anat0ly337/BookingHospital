package by.booking.hospital.demo.service;

import by.booking.hospital.demo.dao.EmployeeRepo;
import by.booking.hospital.demo.dto.BookingRoom;
import by.booking.hospital.demo.dto.BookingView;
import by.booking.hospital.demo.dto.EmployeeView;
import by.booking.hospital.demo.entity.Employee;
import by.booking.hospital.demo.entity.Role;
import by.booking.hospital.demo.entity.Room;
import by.booking.hospital.demo.exceptions.CustomException;
import com.vaadin.flow.component.notification.Notification;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@EnableScheduling
public class UserService implements UserDetailsService {
    private EmployeeRepo employeeRepo;
    private ModelMapper modelMapper;

    @Autowired
    public UserService(EmployeeRepo employeeRepo, ModelMapper modelMapper) {
        this.employeeRepo = employeeRepo;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employee employee = employeeRepo.findByUsername(username);
        return new User(employee.getUsername(), employee.getPassword(), employee.getRoles());
    }

    public List<EmployeeView> findUserByName(String name) {
        List<EmployeeView> employeeViewList = new ArrayList<>();
        List<Employee> employees = employeeRepo.findByName(name);
        employees.forEach(employee -> {
            employeeViewList.add(convertEntityToDto(employee));
        });
        return employeeViewList;
    }

    public EmployeeView findById(Long id) {
        Employee employee = employeeRepo.findById(id).get();
        EmployeeView employeeView = new EmployeeView();
        employeeView.setUserType(employee.getUserType());
        employeeView.setUsername(employee.getUsername());
        employeeView.setId(employee.getId());
        return employeeView;
    }

    public Employee createEmployee(String username, String userType) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().stream().findFirst().get().getAuthority().equals("USER")) {
            throw new CustomException("you haven't ADMIN authority");
        }
        Set<Role> roles = new HashSet<>();
        roles.add(Role.USER);
        Employee employee = new Employee();
        employee.setUsername(username);
        employee.setPassword("12345");
        employee.setUserType(userType);
        employee.setRoles(roles);
        return employeeRepo.save(employee);
    }

    @Transactional
    public EmployeeView updateEmployee(EmployeeView employeeView, String username, String userType) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().stream().findFirst().get().getAuthority().equals("USER")) {
            throw new CustomException("FORBIDDEN");
        }
        Employee employee = employeeRepo.getOne(employeeView.getId());
        employeeView.setUsername(username);
        employeeView.setUserType(userType);
        employeeView.setId(employee.getId());
        employee.setUsername(username);
        employee.setUserType(userType);
        employeeRepo.save(employee);
        return employeeView;
    }

    public void deleteEmployee(EmployeeView employeeView) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().stream().findFirst().get().getAuthority().equals("USER")) {
            throw new CustomException("FORBIDDEN");
        }
        Employee room = employeeRepo.findById(employeeView.getId()).get();
        employeeRepo.delete(room);
    }

    public List<EmployeeView> findAll() {
        List<EmployeeView> employeeViewList = new ArrayList<>();
        List<Employee> employees = employeeRepo.findAll();

        employees.forEach(employee -> {
            employeeViewList.add(convertEntityToDto(employee));
        });
        return employeeViewList;
    }


    private EmployeeView convertEntityToDto(Employee employee) {
        EmployeeView employeeView = new EmployeeView();
        employeeView.setUsername(employee.getUsername());
        employeeView.setId(employee.getId());
        employeeView.setUserType(employee.getUserType());
        return employeeView;
    }

    private EmployeeView convertDtoToEntity(EmployeeView employeeView) {
        Employee employee = new Employee();
        employee.setUsername(employee.getUsername());
        employee.setId(employee.getId());
        employee.setUserType(employee.getUserType());
        return employeeView;
    }
}
