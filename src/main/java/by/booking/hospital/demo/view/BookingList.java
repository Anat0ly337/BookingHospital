package by.booking.hospital.demo.view;

import by.booking.hospital.demo.components.BookingEditor;
import by.booking.hospital.demo.dao.BookingRepo;
import by.booking.hospital.demo.dto.BookingView;
import by.booking.hospital.demo.service.BookingService;
import by.booking.hospital.demo.util.MiniCache;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.NativeButton;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Route("booking")
@Push
public class BookingList extends VerticalLayout {
    private final BookingService bookingService;

    private Grid<BookingView> employeeGrid = new Grid<>(BookingView.class);
    private final TextField filter = new TextField();
    private final Button addNewButton = new Button("New Room", VaadinIcon.PLUS.create());
    private final Button registerUser = new Button("Register User", VaadinIcon.USERS.create());
    private final NativeButton button = new NativeButton("Logout");

    private final HorizontalLayout toolbar = new HorizontalLayout(filter, addNewButton, registerUser, button);

    private UI currentUi;
    Notification notification;
    private FeederThread thread;

    {
        employeeGrid.setColumns("username", "position", "datestart", "roomType", "dateend", "active", "description");
    }

    @Autowired
    public BookingList(BookingService bookingService, BookingEditor employeeEditor, UI currentUi) {
        this.bookingService = bookingService;
        this.currentUi = currentUi;
        //click listener routing
        button.addClickListener(e -> {
            button.getUI().ifPresent(ui -> ui.navigate("logout"));
        });

        //click listener routing
        registerUser.addClickListener(e -> {
            button.getUI().ifPresent(ui -> ui.navigate("register"));
        });

        //search filter
        filter.setPlaceholder("Search By Type");
        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(field -> fillList(field.getValue()));

        add(toolbar, employeeGrid, employeeEditor);

        //click by row
        employeeGrid
                .asSingleSelect()
                .addValueChangeListener(e -> employeeEditor.editRoom(e.getValue()));

        addNewButton.addClickListener(e -> employeeEditor.editRoom(new BookingView()));

        //change handler
        employeeEditor.setChangeHandler(() -> {
            employeeEditor.setVisible(false);
            fillList(filter.getValue());
        });

        fillList("");
    }

    private void fillList(String type) {
        if (type.isEmpty()) {
            employeeGrid.setItems(this.bookingService.findAll());
        } else {
            employeeGrid.setItems(this.bookingService.findByRoomType(type));
        }
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        // Start the data feed thread
        thread = new FeederThread(attachEvent.getUI(), this);
        thread.start();
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
    }

    //class-thread for retrieve push notification about check date expiration from the server
    private static class FeederThread extends Thread {
        private final UI ui;
        private final BookingList view;

        public FeederThread(UI ui, BookingList view) {
            this.ui = ui;
            this.view = view;
        }

        @Override
        public void run() {
            try {
                // Update the data for a while
                while (true) {
                    // Sleep to emulate background work every 30 sec
                    Thread.sleep(30000);
                    ui.access(() -> {
                        if (MiniCache.CACHE.size()>0){
                            view.employeeGrid.setItems(MiniCache.CACHE);
                        }
                    });
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
