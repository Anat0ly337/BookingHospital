package by.booking.hospital.demo.dto;
import java.util.Date;

/*
 *Class View for /booking page
 */
public class BookingView {
    private Long id;
    private String username;
    private String position;
    private String description;
    private Date datestart;
    private String roomType;
    private Date dateend;
    private boolean active;


    public BookingView(String username, String position, Date datestart, String roomType, Date dateend, boolean active,String description) {
        this.description = description;
        this.username = username;
        this.position = position;
        this.datestart = datestart;
        this.roomType = roomType;
        this.dateend = dateend;
        this.active = active;
    }

    public BookingView(Long id, String username, String position, Date datestart, String roomType, Date dateend, boolean active,String description) {
        this.description = description;
        this.id = id;
        this.username = username;
        this.position = position;
        this.datestart = datestart;
        this.roomType = roomType;
        this.dateend = dateend;
        this.active = active;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BookingView() {
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Date getDatestart() {
        return datestart;
    }

    public void setDatestart(Date datestart) {
        this.datestart = datestart;
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
