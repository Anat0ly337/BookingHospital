package by.booking.hospital.demo.dao;

import by.booking.hospital.demo.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookingRepo extends JpaRepository<Room,Long> {
    @Query("from Room e " +
            "where" +
            "   concat(e.roomType, ' ', e.isActive, ' ', e.id) like concat('%', :type, '%')")
    List<Room> findByRoomTypee(@Param("type") String type);
    Room findByRoomType(String type);


}
