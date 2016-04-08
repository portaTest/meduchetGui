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

import tesla.meduchet.gui.domain.User;
import tesla.meduchet.gui.DashboardUI;
import tesla.meduchet.gui.component.PatientAddWindow;
import tesla.meduchet.gui.view.DashboardMenu;

@SuppressWarnings({ "serial", "unchecked" })
public class PatientDBView extends VerticalLayout implements View {

	private/* final */Table table;
	/*private static final DateFormat DATEFORMAT = new SimpleDateFormat(
			"MM/dd/yyyy");
	private static final DecimalFormat DECIMALFORMAT = new DecimalFormat("#.##");
	private static final String[] DEFAULT_COLLAPSIBLE = { "country", "city",
			"theater", "room", "title", "seats" };
*/
	public PatientDBView() {
		setSizeFull();
		addStyleName("transactions");
		DashboardUI.getDashboardEventbus().register(this);

		addComponent(buildToolbar());

		table = buildTableOfPatients();
		addComponent(table);
		setExpandRatio(table, 1);
	}

	/**
	 * @return
	 */
	private Table buildTableOfPatients() {
		table = new Table();
		table.setSizeFull();
		table.addStyleName(ValoTheme.TABLE_BORDERLESS);
		table.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
		table.addStyleName(ValoTheme.TABLE_COMPACT);

		table.setSelectable(true);
		table.setColumnCollapsingAllowed(true);
		table.setColumnReorderingAllowed(true);

		table.addContainerProperty("firstName", String.class, null);
		table.addContainerProperty("lastName", String.class, null);
		table.addContainerProperty("middleName", String.class, null);
		table.addContainerProperty("birthdayDate", String.class, null);
		table.addContainerProperty("sex", String.class, null);
		table.addContainerProperty("housePhone", String.class, null);
		table.addContainerProperty("mobPhone", String.class, null);
		table.addContainerProperty("discount", Integer.class, null);

		addItems();
		return table;
	}
	
	public void addItems(){
		table.addItem(new Object[] { "Иваненко", "Марина", "Ивановна", null,
				"ж", "785963", "0938756963",10 }, 1);
		table.addItem(new Object[] { "Николаенко", "Александр", "Павлович", null,
				"м", "785963", "0938756963", 50 }, 2);
		table.addItem(new Object[] { "Капитоненко", "Сергей", "Витальевич", null,
				"м", "785963", "0938756963", 25 }, 3);
		
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

		Label title = new Label("База данных пациентов");
		title.setSizeUndefined();
		title.addStyleName(ValoTheme.LABEL_H1);
		title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
		header.addComponent(title);

		final Button btnEdit = new Button("Редактировать");
		//btnEdit.addStyleName(ValoTheme.BUTTON_PRIMARY);
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
				/*
				 * DashboardMenu dashboasdMenu = new DashboardMenu(); final User
				 * user = dashboasdMenu.getCurrentUser();
				 * DoctorAddWindow.open(user, false);
				 */
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
				PatientAddWindow.open(user, false);
			}
		});

		return header;
	}

	/*private Component buildFilter() {
		final TextField filter = new TextField();
		filter.addTextChangeListener(new TextChangeListener() {
			@Override
			public void textChange(final TextChangeEvent event) {
				Filterable data = (Filterable) table.getContainerDataSource();
				data.removeAllContainerFilters();
				data.addContainerFilter(new Filter() {
					@Override
					public boolean passesFilter(final Object itemId,
							final Item item) {

						if (event.getText() == null
								|| event.getText().equals("")) {
							return true;
						}

						return filterByProperty("country", item,
								event.getText())
								|| filterByProperty("city", item,
										event.getText())
								|| filterByProperty("title", item,
										event.getText());

					}

					@Override
					public boolean appliesToProperty(final Object propertyId) {
						if (propertyId.equals("country")
								|| propertyId.equals("city")
								|| propertyId.equals("title")) {
							return true;
						}
						return false;
					}
				});
			}
		});

		filter.setInputPrompt("Filter");
		filter.setIcon(FontAwesome.SEARCH);
		filter.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
		filter.addShortcutListener(new ShortcutListener("Clear",
				KeyCode.ESCAPE, null) {
			@Override
			public void handleAction(final Object sender, final Object target) {
				filter.setValue("");
				((Filterable) table.getContainerDataSource())
						.removeAllContainerFilters();
			}
		});
		return filter;
	}

	

	private boolean filterByProperty(final String prop, final Item item,
			final String text) {
		if (item == null || item.getItemProperty(prop) == null
				|| item.getItemProperty(prop).getValue() == null) {
			return false;
		}
		String val = item.getItemProperty(prop).getValue().toString().trim()
				.toLowerCase();
		if (val.contains(text.toLowerCase().trim())) {
			return true;
		}
		return false;
	}
*/
	

	@Override
	public void enter(final ViewChangeEvent event) {
	}

	


}
