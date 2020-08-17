package by.booking.hospital.demo.components;

import by.booking.hospital.demo.dto.EmployeeView;
import by.booking.hospital.demo.exceptions.CustomException;
import by.booking.hospital.demo.dto.BookingRoom;
import by.booking.hospital.demo.dto.BookingView;
import by.booking.hospital.demo.service.BookingService;
import by.booking.hospital.demo.service.UserService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;


/*
 *VAADIN LAYOUTs CLASS
 */

@SpringComponent
@UIScope
public class BookingEditor extends VerticalLayout implements KeyNotifier {
    private final BookingService bookingService;
    private final UserService userService;

    private EmployeeView employeeView;
    private BookingView bookingView;


    private TextField roomType = new TextField("room type", "Room Type");
    private TextField description = new TextField("description", "description");
    private DatePicker dateEnd = new DatePicker("DateEnd");
    private Checkbox checkbox = new Checkbox();
    private Notification notification;


    private Button save = new Button("Save");
    private Button cancel = new Button("Cancel");
    private Button delete = new Button("Delete");
    private HorizontalLayout buttons = new HorizontalLayout(save, cancel, delete);

    private Binder<BookingView> binder = new Binder<>(BookingView.class);
    @Setter
    private ChangeHandler changeHandler;

    public interface ChangeHandler {
        void onChange();
    }

    @Autowired
    public BookingEditor(BookingService bookingService, UserService userService) {
        this.userService = userService;
        this.bookingService = bookingService;

        checkbox.setLabel("ACTIVE");
        checkbox.setValue(true);
        checkbox.setEnabled(true);

        add(roomType, dateEnd, description, checkbox, buttons);

        //binder.bindInstanceFields(this);

        setSpacing(true);

        save.getElement().getThemeList().add("primary");
        delete.getElement().getThemeList().add("error");

        addKeyPressListener(Key.ENTER, e -> save());

        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> editRoom(bookingView));
        setVisible(false);
    }

    private void save() {
        try {
            if (bookingView.getId() == null) {
                bookingService.createRoom(new BookingRoom(roomType.getValue(), dateEnd.getValue(), checkbox.getValue(), description.getValue()));
            } else {
                this.bookingView = bookingService.updateRoom(bookingView, new BookingRoom(roomType.getValue(), dateEnd.getValue(), checkbox.getValue(), description.getValue()));
            }
        } catch (CustomException e) {
            notification = new Notification(
                    e.getMessage(), 3000, Notification.Position.MIDDLE);
            notification.open();
        }
        changeHandler.onChange();
    }

    private void delete() {
        bookingService.deleteRoom(bookingView);
        changeHandler.onChange();
    }


    public void editRoom(BookingView bookingView) {
        if (bookingView == null) {
            setVisible(false);
            return;
        }

        if (bookingView.getId() != null) {
            this.bookingView = bookingService.findById(bookingView.getId());
        } else {
            this.bookingView = bookingView;
        }

        binder.setBean(this.bookingView);
        setVisible(true);
        dateEnd.focus();
    }

}
