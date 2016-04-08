package tesla.meduchet.gui.view;

import com.vaadin.navigator.View;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;

import tesla.meduchet.gui.view.transactions.DoctorDBView;
import tesla.meduchet.gui.view.transactions.PatientDBView;
import tesla.meduchet.gui.view.transactions.RecordsKTView;
import tesla.meduchet.gui.view.transactions.RecordsMRTView;
import tesla.meduchet.gui.view.transactions.RecordsUZIView;

public enum DashboardViewType {
	DASHBOARD("БД врачей", DoctorDBView.class, FontAwesome.TABLE, false),
	SALES("БД пациентов", PatientDBView.class, FontAwesome.TABLE, false),
	TRANSACTIONS("Записи на МРТ", RecordsMRTView.class, FontAwesome.TABLE, false), 
	REPORTS("Записи на КТ", RecordsKTView.class, FontAwesome.TABLE, false),
	SCHEDULE("Записи на УЗИ", RecordsUZIView.class, FontAwesome.TABLE, false);

	private final String viewName;
	private final Class<? extends View> viewClass;
	private final Resource icon;
	private final boolean stateful;

	private DashboardViewType(final String viewName, final Class<? extends View> viewClass, final Resource icon,
			final boolean stateful) {
		this.viewName = viewName;
		this.viewClass = viewClass;
		this.icon = icon;
		this.stateful = stateful;
	}

	public boolean isStateful() {
		return stateful;
	}

	public String getViewName() {
		return viewName;
	}

	public Class<? extends View> getViewClass() {
		return viewClass;
	}

	public Resource getIcon() {
		return icon;
	}

	public static DashboardViewType getByViewName(final String viewName) {
		DashboardViewType result = null;
		for (DashboardViewType viewType : values()) {
			if (viewType.getViewName().equals(viewName)) {
				result = viewType;
				break;
			}
		}
		return result;
	}

}
