package tesla.meduchet.gui.view.transactions;




import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import tesla.meduchet.gui.DashboardUI;
import tesla.meduchet.gui.component.RecordAddWindow;
import tesla.meduchet.gui.domain.User;
import tesla.meduchet.gui.view.DashboardMenu;

@SuppressWarnings({ "serial", "unchecked" })
public class RecordsMRTView extends VerticalLayout implements View {

	private/* final */Table table;
	/*private static final DateFormat DATEFORMAT = new SimpleDateFormat(
			"MM/dd/yyyy");
	private static final DecimalFormat DECIMALFORMAT = new DecimalFormat("#.##");
	private static final String[] DEFAULT_COLLAPSIBLE = { "country", "city",
			"theater", "room", "title", "seats" };
*/
	public RecordsMRTView() {
		setSizeFull();
		addStyleName("transactions");
		DashboardUI.getDashboardEventbus().register(this);

		addComponent(buildToolbar());

		table = buildTableOfRecords();
		addComponent(table);
		setExpandRatio(table, 1);
	}

	/**
	 * @return
	 */
	private Table buildTableOfRecords() {
		table = new Table();
		table.setSizeFull();
		table.addStyleName(ValoTheme.TABLE_BORDERLESS);
		table.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
		table.addStyleName(ValoTheme.TABLE_COMPACT);

		table.setSelectable(true);
		table.setColumnCollapsingAllowed(true);
		table.setColumnReorderingAllowed(true);

		table.addContainerProperty("id", Integer.class, null);
		table.addContainerProperty("date", String.class, null);
		table.addContainerProperty("duration", Integer.class, null);
		table.addContainerProperty("startTime", String.class, null);
		table.addContainerProperty("endTime", String.class, null);
		table.addContainerProperty("patient", String.class, null);
		table.addContainerProperty("doctor", String.class, null);
		table.addContainerProperty("doctor napravlenie", String.class, null);
		table.addContainerProperty("status", String.class, null);

		addItems();
		return table;
	}
	
	public void addItems(){
		table.addItem(new Object[] { 1, null, 20, null, null, "Ефименко Неля Петровна", 
				"Семенова Валентина Ивановна", "Кузьменко Игорь Валерьевич", "Оплачено" }, 1);
		table.addItem(new Object[] { 2, null, 20, null, null, "Дмитренко Ольга Сергеевна", 
				"Иванченко Петр Сергеевич", "Никитенко Илья Борисович", "Не пришел" }, 2);
		table.addItem(new Object[] { 3, null, 20, null, null, "Балькевич Николай Васильевич", 
				"Никитенко Илья Борисович", "Кузьменко Игорь Валерьевич", "Оплачено" }, 3);
		
	}

	@Override
	public void detach() {
		super.detach();
		DashboardUI.getDashboardEventbus().unregister(this);
	}

	private Component buildToolbar() {
		HorizontalLayout header = new HorizontalLayout();
		header.addStyleName("viewheader");
		header.setSpacing(true);
		Responsive.makeResponsive(header);

		Label title = new Label("Записи на МРТ");
		title.setSizeUndefined();
		title.addStyleName(ValoTheme.LABEL_H1);
		title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
		header.addComponent(title);

		final Button btnEdit = new Button("Редактировать");
		btnEdit.addStyleName("clearbutton");
		
		final Button btnAdd = new Button("Добавить");
		btnAdd.addStyleName(ValoTheme.BUTTON_PRIMARY);
		
		final Button btnDel = new Button("Удалить");
		btnAdd.addStyleName(ValoTheme.BUTTON_PRIMARY);
		
		header.addComponent(btnAdd);
		header.addComponent(btnEdit);
		header.addComponent(btnDel);
		
		btnEdit.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(final ClickEvent event) {

				if(table.isEditable()){
					table.setEditable(false);
					btnAdd.setEnabled(true);

				} else {
				table.setEditable(true);
				btnAdd.setEnabled(false);
				}
			}
		});
		
		btnAdd.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(final ClickEvent event) {
				DashboardMenu dashboasdMenu = new DashboardMenu();
				final User user = dashboasdMenu.getCurrentUser();
				RecordAddWindow.open(user, false);
			}
		});

		return header;
	}

	@Override
	public void enter(final ViewChangeEvent event) {
	}

}
