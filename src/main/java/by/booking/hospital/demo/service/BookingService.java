package by.booking.hospital.demo.service;

import by.booking.hospital.demo.exceptions.CustomException;
import by.booking.hospital.demo.dao.BookingRepo;
import by.booking.hospital.demo.dao.EmployeeRepo;
import by.booking.hospital.demo.dto.BookingRoom;
import by.booking.hospital.demo.dto.BookingView;
import by.booking.hospital.demo.entity.Employee;
import by.booking.hospital.demo.entity.Manipulation;
import by.booking.hospital.demo.entity.Room;
import by.booking.hospital.demo.util.MiniCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@EnableScheduling
public class BookingService {
    private SimpleDateFormat dateFormat;
    private BookingRepo bookingRepo;
    private EmployeeRepo employeeRepo;

    { SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy"); MiniCache.CACHE = new ArrayList<>(); }

    @Autowired
    public BookingService(BookingRepo bookingRepo, EmployeeRepo employeeRepo) {
        this.bookingRepo = bookingRepo;
        this.employeeRepo = employeeRepo;
    }

    public Room createRoom(BookingRoom bookingRoom) {
        if (bookingRepo.findByRoomType(bookingRoom.getRoomType()) != null) {
            throw new CustomException("2 manipulation forbidden");
        }

        if (bookingRepo.findByRoomType(bookingRoom.getRoomType()) != null) {
            throw new CustomException("2 manipulation forbidden");
        }

        List<Employee> employees = employeeRepo.findByName(SecurityContextHolder.getContext().getAuthentication().getName());
        if (employees.stream().findFirst().get().getRoom() != null) {
            throw new CustomException("2 manipulation forbidden");
        }

        //find by security principal
        Employee employee = employeeRepo.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        bookingRoom.setEmployee(employee);
        return bookingRepo.save(convertBookingRoomToRoom(bookingRoom));
    }

    @Transactional
    public BookingView updateRoom(BookingView bookingView, BookingRoom bookingRoom) {
        Room room = bookingRepo.getOne(bookingView.getId());
        if (!room.getEmployee().getUsername().equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
            throw new CustomException("another user edit");
        }
        room.setActive(bookingRoom.isActive());
        room.setRoomType(bookingRoom.getRoomType());
        room.getManipulation().setDateEnd(bookingRoom.getDateend());
        room.getManipulation().setDescription(bookingRoom.getDescription());
        bookingRepo.save(room);
        return bookingView;
    }

    public void deleteRoom(BookingView bookingView) {
        Room room = bookingRepo.findById(bookingView.getId()).get();
        bookingRepo.delete(room);
    }

    public List<BookingView> findByRoomType(String type) {
        List<Room> room = bookingRepo.findByRoomTypee(type);
        return convertRoomToBooking(room);
    }

    public List<BookingView> findAll() {
        List<Room> rooms = bookingRepo.findAll();
        return convertRoomToBooking(rooms);
    }

    public BookingView findById(Long id) {
        Room rooms = bookingRepo.findById(id).get();
        BookingView bookingView = new BookingView();
        bookingView.setId(rooms.getId());
        bookingView.setRoomType(rooms.getRoomType());
        bookingView.setUsername(rooms.getEmployee().getUsername());
        bookingView.setPosition(rooms.getEmployee().getUserType());
        bookingView.setActive(rooms.isActive());
        bookingView.setDescription(rooms.getManipulation().getDescription());
        bookingView.setDatestart(rooms.getManipulation().getDateStart());
        bookingView.setDateend(rooms.getManipulation().getDateEnd());
        return bookingView;
    }

    private Room convertBookingRoomToRoom(BookingRoom bookingRoom) {
        Room room = new Room();
        Manipulation manipulation = new Manipulation();
        room.setManipulation(manipulation);
        room.setRoomType(bookingRoom.getRoomType());
        room.setEmployee(bookingRoom.getEmployee());
        room.setActive(bookingRoom.isActive());
        manipulation.setDateStart(new Date());
        manipulation.setDateEnd(bookingRoom.getDateend());
        manipulation.setDescription(bookingRoom.getDescription());
        return room;
    }

    protected List<BookingView> convertRoomToBooking(List<Room> rooms) {
        List<BookingView> bookingView = new ArrayList<>();
        rooms.forEach(room -> {
            Manipulation manipulation = room.getManipulation();
            Employee employee = room.getEmployee();
            bookingView.add(new BookingView(room.getId(), employee.getUsername(), employee.getUserType(), manipulation.getDateStart(), room.getRoomType(), manipulation.getDateEnd(), room.isActive(), manipulation.getDescription()));

        });
        return bookingView;
    }

    public void schedule() {
        List<BookingView> cache = MiniCache.CACHE;
        cache.stream().forEach(room -> {
            Room r = bookingRepo.findById(room.getId()).get();
            if (r.getManipulation().getDateEnd().before(new Date())) {
                updateRoom(room.getId());
            }
        });
        MiniCache.CACHE = convertRoomToBooking(bookingRepo.findAll());
    }

    @Transactional
    void updateRoom(Long id) {
        Room room = bookingRepo.findById(id).get();
        room.setActive(false);
        bookingRepo.save(room);
    }
}
