package com.sample.client;

import java.util.ArrayList;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.data.client.loader.RpcProxy;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.loader.BeforeLoadEvent;
import com.sencha.gxt.data.shared.loader.BeforeLoadEvent.BeforeLoadHandler;
import com.sencha.gxt.data.shared.loader.LoadResultListStoreBinding;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.FramedPanel;

import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.RefreshEvent;

import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.grid.CheckBoxSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.toolbar.PagingToolBar;

public class GridSample implements EntryPoint, IsWidget {
	private ContentPanel panel;
	private static final int REFRESH_INTERVAL = 5000; // ms
	private PersonServiceAsync Service = GWT.create(PersonService.class);

	@Override
	public Widget asWidget() {

		if (panel == null) {
			RpcProxy<PagingLoadConfig, PagingLoadResult<Person>> rpxProxy = new RpcProxy<PagingLoadConfig, PagingLoadResult<Person>>() {
				@Override
				public void load(PagingLoadConfig loadConfig,
						AsyncCallback<PagingLoadResult<Person>> callback) {
					/*
					 * List<? extends SortInfo> sortInfo =
					 * loadConfig.getSortInfo();
					 */
					Service.getAllPersons(loadConfig, callback);
				}
			};

			PersonProperties props = GWT.create(PersonProperties.class);
			ListStore<Person> store = new ListStore<Person>(
					new ModelKeyProvider<Person>() {
						public String getKey(Person item) {
							return "" + item.getId();
						}
					});
			final PagingLoader<PagingLoadConfig, PagingLoadResult<Person>> loader = new PagingLoader<PagingLoadConfig, PagingLoadResult<Person>>(
					rpxProxy);
			loader.setRemoteSort(true);
			/* loader.load(0, 10); */
			loader.addLoadHandler(new LoadResultListStoreBinding<PagingLoadConfig, Person, PagingLoadResult<Person>>(
					store));

			IdentityValueProvider<Person> identity = new IdentityValueProvider<Person>();
			final CheckBoxSelectionModel<Person> selectionModel = new CheckBoxSelectionModel<Person>(
					identity) {
				@Override
				protected void onRefresh(RefreshEvent event) {
					// this code selects all rows when paging if the header
					// Checkbooks is selected
					if (isSelectAllChecked()) {
						selectAll();
					}
					super.onRefresh(event);
				}
			};

			ColumnConfig<Person, Integer> idColumn = new ColumnConfig<Person, Integer>(
					props.id(), 150, "ID");
			ColumnConfig<Person, String> fnameColumn = new ColumnConfig<Person, String>(
					props.fname(), 150, "First Name");
			ColumnConfig<Person, String> lnameColumn = new ColumnConfig<Person, String>(
					props.lname(), 150, "Last Name");
			ColumnConfig<Person, String> phoneColumn = new ColumnConfig<Person, String>(
					props.phone(), 150, "Phone");
			ColumnConfig<Person, String> emailColumn = new ColumnConfig<Person, String>(
					props.emailId(), 150, "Email ID");

			ArrayList<ColumnConfig<Person, ?>> columns = new ArrayList<ColumnConfig<Person, ?>>();
			// The selection model provides the first column config
			columns.add(selectionModel.getColumn());
			columns.add(idColumn);
			columns.add(fnameColumn);
			columns.add(lnameColumn);
			columns.add(phoneColumn);
			columns.add(emailColumn);

			ColumnModel<Person> cm = new ColumnModel<Person>(columns);

			Grid<Person> grid = new Grid<Person>(store, cm) {
				@Override
				protected void onAfterFirstAttach() {
					super.onAfterFirstAttach();
					Scheduler.get().scheduleDeferred(new ScheduledCommand() {
						@Override
						public void execute() {
							loader.load();
						}
					});
				}
			};

			grid.setSelectionModel(selectionModel);
			grid.getView().setForceFit(true);
			grid.setLoadMask(true);
			grid.setLoader(loader);
			grid.setColumnReordering(true);

			final CheckBox warnLoad = new CheckBox();
			warnLoad.setBoxLabel("Warn before loading new data");
			warnLoad.setValue(false);

			final PagingToolBar toolBar = new PagingToolBar(10);
			toolBar.getElement().getStyle().setProperty("borderBottom", "none");
			toolBar.bind(loader);

			VerticalLayoutContainer verticalLayoutContainer = new VerticalLayoutContainer();
			verticalLayoutContainer.setBorders(true);
			verticalLayoutContainer.add(grid, new VerticalLayoutData(1, 1));
			verticalLayoutContainer.add(toolBar, new VerticalLayoutData(1, -1));
			verticalLayoutContainer.add(warnLoad,
					new VerticalLayoutData(-1, -1));

			// If the warn checkbooks is active, then present a warning and stop
			// any load
			// after the first if the user clicks cancel

			loader.addBeforeLoadHandler(new BeforeLoadHandler<PagingLoadConfig>() {
				boolean initialLoad = true;

				@Override
				public void onBeforeLoad(BeforeLoadEvent<PagingLoadConfig> event) {
					if (!initialLoad && warnLoad.getValue()) {
						event.setCancelled(!Window
								.confirm("Are you sure you want to do that?"));
					}
					initialLoad = false;
				}
			});

			panel = new FramedPanel();
			panel.setCollapsible(true);
			panel.setHeadingText("Paging Grid Example");
			panel.setPixelSize(500, 330);
			panel.addStyleName("margin-10");
			panel.setWidget(verticalLayoutContainer);

		}
		return panel;
	}

	@Override
	public void onModuleLoad() {
		Timer refreshTimer = new Timer() {
			@Override
			public void run() {
				asWidget();
			}
		};
		refreshTimer.scheduleRepeating(REFRESH_INTERVAL);
		RootPanel.get("gxtGrid").add(asWidget());

	}

}
