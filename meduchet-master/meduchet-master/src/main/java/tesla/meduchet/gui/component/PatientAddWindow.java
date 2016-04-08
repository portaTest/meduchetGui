package tesla.meduchet.gui.component;


import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import tesla.meduchet.gui.domain.User;
import tesla.meduchet.gui.event.DashboardEvent.CloseOpenWindowsEvent;
import tesla.meduchet.gui.event.DashboardEvent.ProfileUpdatedEvent;
import tesla.meduchet.gui.event.DashboardEventBus;

@SuppressWarnings("serial")
public class PatientAddWindow extends Window {

    public static final String ID = "profilepreferenceswindow";

    private final BeanFieldGroup<User> fieldGroup;
    /*
     * Fields for editing the User object are defined here as class members.
     * They are later bound to a FieldGroup by calling
     * fieldGroup.bindMemberFields(this). The Fields' values don't need to be
     * explicitly set, calling fieldGroup.setItemDataSource(user) synchronizes
     * the fields with the user object.
     */
    @PropertyId("firstName")
    private TextField firstNameField;
    @PropertyId("lastName")
    private TextField lastNameField;
    @PropertyId("middleName")
    private TextField middleNameField;
	//private static final DateFormat DATEFORMAT = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
    @PropertyId("birthdayDate")
    private TextField birthdayDateField;
    @PropertyId("sex")
    private OptionGroup sexField;
    @PropertyId("housePhone")
    private TextField homePhoneField;
    @PropertyId("mobPhone")
    private TextField mobPhoneField;
    
    @PropertyId("discount")
    private TextField discountField;
    //@PropertyId("extraInfo")
    //private TextArea extraInfoField;

    private PatientAddWindow(final User user,
            final boolean preferencesTabOpen) {
        addStyleName("profile-window");
        setId(ID);
        Responsive.makeResponsive(this);

        setModal(true);
        setCloseShortcut(KeyCode.ESCAPE, null);
        setResizable(false);
        setClosable(false);
        setHeight(90.0f, Unit.PERCENTAGE);

        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        content.setMargin(new MarginInfo(true, false, false, false));
        setContent(content);

        TabSheet detailsWrapper = new TabSheet();
        detailsWrapper.setSizeFull();
        detailsWrapper.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
        detailsWrapper.addStyleName(ValoTheme.TABSHEET_ICONS_ON_TOP);
        detailsWrapper.addStyleName(ValoTheme.TABSHEET_CENTERED_TABS);
        content.addComponent(detailsWrapper);
        content.setExpandRatio(detailsWrapper, 1f);

        detailsWrapper.addComponent(buildProfileTab());

        if (preferencesTabOpen) {
            detailsWrapper.setSelectedTab(1);
        }

        content.addComponent(buildFooter());

        fieldGroup = new BeanFieldGroup<User>(User.class);
        fieldGroup.bindMemberFields(this);
        fieldGroup.setItemDataSource(user);
    }

       private Component buildProfileTab() {
        HorizontalLayout root = new HorizontalLayout();
        root.setCaption("Добавление пациента в базу данных");
        root.setWidth(100.0f, Unit.PERCENTAGE);
        root.setSpacing(true);
        root.setMargin(true);
        root.addStyleName("profile-form");

        

        FormLayout details = new FormLayout();
        details.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        root.addComponent(details);
        root.setExpandRatio(details, 1);
        Label lblHeader = new Label("Введите данные о пациенте");
        lblHeader.addStyleName(ValoTheme.LABEL_H4);
        lblHeader.addStyleName(ValoTheme.LABEL_COLORED);
        details.addComponent(lblHeader);
        
        lastNameField = new TextField("Фамилия:");
        lastNameField.setNullRepresentation("");
        details.addComponent(lastNameField);
        
        firstNameField = new TextField("Имя:");
        firstNameField.setNullRepresentation("");
        details.addComponent(firstNameField);
        
        middleNameField = new TextField("Отчество:");
        middleNameField.setNullRepresentation("");
        details.addComponent(middleNameField);
        
        birthdayDateField = new TextField("Дата рождения:");
        details.addComponent(birthdayDateField);
        
        sexField = new OptionGroup("Пол:");
        sexField.addItem(Boolean.FALSE);
        sexField.setItemCaption(Boolean.FALSE, "ж");
        sexField.addItem(Boolean.TRUE);
        sexField.setItemCaption(Boolean.TRUE, "м");
        sexField.addStyleName("horizontal");
        details.addComponent(sexField);

        homePhoneField = new TextField("Дом. телефон:");
        details.addComponent(homePhoneField);
        mobPhoneField = new TextField("Моб. телефон:");
        details.addComponent(mobPhoneField);
        
               
        discountField = new TextField("Скидка:");
        details.addComponent(discountField);
        
       /* extraInfoField = new TextArea("Дополнительная информация:");
        extraInfoField.setWidth("100%");
        extraInfoField.setRows(4);
        extraInfoField.setNullRepresentation("");
        details.addComponent(extraInfoField);
        */
        
        return root;
    }

    private Component buildFooter() {
        HorizontalLayout footer = new HorizontalLayout();
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
        footer.setWidth(100.0f, Unit.PERCENTAGE);

        Button ok = new Button("Добавить запись");
        ok.addStyleName(ValoTheme.BUTTON_PRIMARY);
        ok.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                try {
                    fieldGroup.commit();
                    // Updated user should also be persisted to database. But
                    // not in this demo.

                    Notification success = new Notification(
                            "Запись добавлена в базу данных!");
                    success.setDelayMsec(2000);
                    success.setStyleName("bar success small");
                    success.setPosition(Position.BOTTOM_CENTER);
                    success.show(Page.getCurrent());
                	DashboardEventBus dashboardEventBus = new DashboardEventBus();
                	dashboardEventBus.post(new ProfileUpdatedEvent());
                    close();
                } catch (CommitException e) {
                    Notification.show("Ошибка при добавлении записи!",
                            Type.ERROR_MESSAGE);
                }

            }
        });
        ok.focus();
        footer.addComponent(ok);
        footer.setComponentAlignment(ok, Alignment.TOP_RIGHT);
        return footer;
    }

    public static void open(final User user, final boolean preferencesTabActive) {
    	DashboardEventBus dashboardEventBus = new DashboardEventBus();
    	dashboardEventBus.post(new CloseOpenWindowsEvent());
        Window w = new PatientAddWindow(user, preferencesTabActive);
        UI.getCurrent().addWindow(w);
        w.focus();
    }
}
