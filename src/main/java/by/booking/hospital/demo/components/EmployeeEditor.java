package by.booking.hospital.demo.components;

import by.booking.hospital.demo.annotation.IsAdmin;
import by.booking.hospital.demo.dto.BookingRoom;
import by.booking.hospital.demo.dto.BookingView;
import by.booking.hospital.demo.dto.EmployeeView;
import by.booking.hospital.demo.exceptions.CustomException;
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
import org.springframework.scheduling.annotation.Scheduled;

/*
 *VAADIN LAYOUTs CLASS
 */
@SpringComponent
@UIScope
public class EmployeeEditor extends VerticalLayout implements KeyNotifier {
    private final UserService userService;
    private EmployeeView employeeView;
    private TextField name = new TextField("", "Name");
    private TextField userType = new TextField("", "usertype");
    private Notification notification;

    private Button save = new Button("Save");
    private Button cancel = new Button("Cancel");
    private Button delete = new Button("Delete");
    private HorizontalLayout buttons = new HorizontalLayout(save, cancel, delete);

    private Binder<EmployeeView> binder = new Binder<>(EmployeeView.class);
    @Setter
    private ChangeHandler changeHandler;

    public interface ChangeHandler {
        void onChange();
    }

    @Autowired
    public EmployeeEditor(BookingService bookingService, UserService userService) {
        this.userService = userService;


        add(name, userType, buttons);

        //binder.bindInstanceFields(this);

        setSpacing(true);

        save.getElement().getThemeList().add("primary");
        delete.getElement().getThemeList().add("error");

        addKeyPressListener(Key.ENTER, e -> save());

        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> editEmployee(employeeView));
        setVisible(false);
    }

    @IsAdmin
    private void save() {
        try {
            if (employeeView.getId() == null) {
                userService.createEmployee(name.getValue(), userType.getValue());
            } else {
                this.employeeView = userService.updateEmployee(employeeView, name.getValue(), userType.getValue());
            }
        } catch (CustomException e) {
            notification = new Notification(
                    e.getMessage(), 3000, Notification.Position.TOP_CENTER);
            notification.open();
        }
        changeHandler.onChange();
    }

    @IsAdmin
    private void delete() {
        try {
            userService.deleteEmployee(employeeView);
        } catch (CustomException e) {
            notification = new Notification(
                    e.getMessage(), 3000, Notification.Position.TOP_CENTER);
            notification.open();
        }
        changeHandler.onChange();
    }


    public void editEmployee(EmployeeView employeeView) {
        if (employeeView == null) {
            setVisible(false);
            return;
        }

        if (employeeView.getId() != null) {
            this.employeeView = userService.findById(employeeView.getId());
        } else {
            this.employeeView = employeeView;
        }

        binder.setBean(this.employeeView);
        setVisible(true);
        userType.focus();
    }



}
