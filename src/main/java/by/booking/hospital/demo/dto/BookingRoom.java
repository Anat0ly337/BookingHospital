package by.booking.hospital.demo.dto;

import by.booking.hospital.demo.entity.Employee;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
/*
 *class DTO for booking room
 */
public class BookingRoom {
    private String roomType;
    private Date dateend;
    private boolean active;
    private Employee employee;
    private String description;

    public BookingRoom(String roomType, LocalDate localDate, boolean active,String description) {
        this.description = description;
        ZoneId defaultZoneId = ZoneId.systemDefault();
        this.roomType = roomType;
        this.dateend = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
        this.active = active;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public Date getDateend() {
        return dateend;
    }

    public void setDateend(Date dateend) {
        this.dateend = dateend;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
