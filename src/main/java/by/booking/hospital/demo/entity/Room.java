package by.booking.hospital.demo.entity;

import javax.persistence.*;

@Entity
@Table(name = "ROOM")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String roomType;
    private boolean isActive;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "manipulation_id", referencedColumnName = "id")
    private Manipulation manipulation;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    private Employee employee;

    public Room() {
    }

    public Room(String roomType, boolean isActive, Manipulation manipulation, Employee employee) {
        this.roomType = roomType;
        this.isActive = isActive;
        this.manipulation = manipulation;
        this.employee = employee;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public Manipulation getManipulation() {
        return manipulation;
    }

    public void setManipulation(Manipulation manipulation) {
        this.manipulation = manipulation;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
