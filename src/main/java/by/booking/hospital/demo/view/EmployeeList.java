package by.booking.hospital.demo.view;

import by.booking.hospital.demo.components.EmployeeEditor;
import by.booking.hospital.demo.dto.EmployeeView;
import by.booking.hospital.demo.service.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.NativeButton;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
//CLASS LIKE PREVIOUS (BookingList)
@Route("register")
public class EmployeeList extends VerticalLayout{
    private final UserService userService;
    private final EmployeeEditor employeeEditor;

    private Grid<EmployeeView> employeeGrid= new Grid<>(EmployeeView.class);
    private final TextField filter = new TextField();
    private final Button addNewButton = new Button("New employee", VaadinIcon.PLUS.create());
    private final Button booking = new Button("Booking", VaadinIcon.BUILDING.create());
    private final NativeButton button = new NativeButton("Logout");
    private final HorizontalLayout toolbar = new HorizontalLayout(filter, addNewButton,booking,button);

    @Autowired
    public EmployeeList(UserService userService, EmployeeEditor employeeEditor) {
        this.userService = userService;
        this.employeeEditor = employeeEditor;

        filter.setPlaceholder("Seach by id name and type");
        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(field -> fillList(field.getValue()));

        button.addClickListener(e -> {
            button.getUI().ifPresent(ui -> ui.navigate("logout"));
        });

        booking.addClickListener(e -> {
            button.getUI().ifPresent(ui -> ui.navigate("booking"));
        });

        add(toolbar, employeeGrid, employeeEditor);

        employeeGrid
                .asSingleSelect()
                .addValueChangeListener(e -> employeeEditor.editEmployee(e.getValue()));

        addNewButton.addClickListener(e -> employeeEditor.editEmployee(new EmployeeView()));

        employeeEditor.setChangeHandler(() -> {
            employeeEditor.setVisible(false);
            fillList(filter.getValue());
        });

        fillList("");
    }

    private void fillList(String name) {
        if (name.isEmpty()) {
            employeeGrid.setItems(this.userService.findAll());
        } else {
            employeeGrid.setItems(this.userService.findUserByName(name));
        }
    }

}
