package com.sample.client;

import java.util.ArrayList;
import java.util.Random;

import java_cup.terminal;

import com.gargoylesoftware.htmlunit.javascript.host.Text;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sample.shared.FieldVerifier;
import com.sencha.gxt.cell.core.client.ButtonCell.ButtonArrowAlign;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.client.loader.RpcProxy;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.loader.BeforeLoadEvent;
import com.sencha.gxt.data.shared.loader.LoadResultListStoreBinding;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.data.shared.loader.BeforeLoadEvent.BeforeLoadHandler;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutPack;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.RefreshEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.NumberField;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.validator.MinLengthValidator;
import com.sencha.gxt.widget.core.client.grid.CheckBoxSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.Grid.GridCell;
import com.sencha.gxt.widget.core.client.grid.RowNumberer;
import com.sencha.gxt.widget.core.client.grid.editing.GridEditing;
import com.sencha.gxt.widget.core.client.grid.editing.GridInlineEditing;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;
import com.sencha.gxt.widget.core.client.toolbar.PagingToolBar;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

public class AddDeleteUpdateGird implements EntryPoint, IsWidget {

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

					Service.getAllPersons(loadConfig, callback);
				}
			};

			PersonProperties props = GWT.create(PersonProperties.class);
			final ListStore<Person> store = new ListStore<Person>(
					new ModelKeyProvider<Person>() {
						public String getKey(Person item) {
							return "" + item.getId();
						}
					});
			final PagingLoader<PagingLoadConfig, PagingLoadResult<Person>> loader = new PagingLoader<PagingLoadConfig, PagingLoadResult<Person>>(
					rpxProxy);
			loader.setRemoteSort(true);
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
			/*
			 * RowNumberer<Person> numbererColumn = new
			 * RowNumberer<Person>(identity);
			 */
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
			/* columns.add(numbererColumn); */
			columns.add(selectionModel.getColumn());
			columns.add(idColumn);
			columns.add(fnameColumn);
			columns.add(lnameColumn);
			columns.add(phoneColumn);
			columns.add(emailColumn);

			ColumnModel<Person> cm = new ColumnModel<Person>(columns);

			final Grid<Person> grid = new Grid<Person>(store, cm) {
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
			grid.getView().setAutoExpandColumn(fnameColumn);
			/* numbererColumn.initPlugin(grid); */

			 	final TextField fname = new TextField();
			 	final TextField lname = new TextField();
				final TextField phone = new TextField();
				final TextField email = new TextField();
			final GridEditing<Person> editing = new GridInlineEditing<Person>(
					grid);
			/* editing.addEditor(idColumn, , Integer.parseInt(id)); */
			editing.addEditor(fnameColumn, fname);
			editing.addEditor(lnameColumn, lname);
			editing.addEditor(phoneColumn, phone);
			editing.addEditor(emailColumn, email);

			TextButton addButton = new TextButton("Add Persons");
			addButton.addSelectHandler(new SelectHandler() {
				@Override
				public void onSelect(SelectEvent event) {
					Person p = new Person();

					/*
					 * p.setFname(fname.getText()); p.setFname(fname.getText());
					 * p.setFname(fname.getText()); p.setFname(fname.getText());
					 */

					store.add(0, p);
					editing.cancelEditing();
					int row = store.indexOf(p);
					editing.startEditing((new GridCell(row, 0)));

				}
			});

			final CheckBox warnLoad = new CheckBox();
			warnLoad.setBoxLabel("Warn before loading new data");
			warnLoad.setValue(false);

			final PagingToolBar toolBar = new PagingToolBar(10);
			toolBar.getElement().getStyle().setProperty("borderBottom", "none");
			toolBar.bind(loader);

			VerticalLayoutContainer verticalLayoutContainer = new VerticalLayoutContainer();
			verticalLayoutContainer.setBorders(true);
			verticalLayoutContainer.add(addButton);
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
			
			email.addKeyDownHandler(new KeyDownHandler() {
				public void onKeyDown(KeyDownEvent event) {
					if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
			
							addPerson(fname.getText(), lname.getText(), phone.getText(), email.getText());
							loader.load();
					}
				}
			});

			Button reset = new Button("Reset");
			reset.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					loader.load();
					store.rejectChanges();
					
				}
			});
			Button save = new Button("Save");
			save.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					
						addPerson(fname.getText(), lname.getText(), phone.getText(), email.getText());
						loader.load();
				}
			});

			final TextButton removeButton = new TextButton(
					"Remove Selected Row(s)");
			removeButton.setEnabled(false);
			SelectHandler removeSelectHandler = new SelectHandler() {
				@Override
				public void onSelect(SelectEvent event) {
					for (Person person : grid.getSelectionModel()
							.getSelectedItems()) {
						grid.getStore().remove(person);
						int id = person.getId();
						ServiceDefTarget serviceDef = (ServiceDefTarget) Service;
						serviceDef.setServiceEntryPoint(GWT.getModuleBaseURL()
								+ "person");
						Service.deletePerson(id, new AsyncCallback<Void>() {
							@Override
							public void onFailure(Throwable caught) {
							}

							@Override
							public void onSuccess(Void result) {

							}
						});
					}
				}
			};
			removeButton.addSelectHandler(removeSelectHandler);
			grid.getSelectionModel().addSelectionChangedHandler(
					new SelectionChangedHandler<Person>() {
						@Override
						public void onSelectionChanged(
								SelectionChangedEvent<Person> event) {
							removeButton.setEnabled(!event.getSelection()
									.isEmpty());
						}
					});

			panel = new FramedPanel();
			panel.setCollapsible(true);
			panel.setHeadingText("Paging Grid Example");
			panel.setPixelSize(500, 405);
			panel.addStyleName("margin-10");
			panel.setWidget(verticalLayoutContainer);
			panel.setButtonAlign(BoxLayoutPack.CENTER);
			panel.addButton(reset);
			panel.addButton(save);
			panel.addButton(removeButton);

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
		RootPanel.get("editableGrid").add(asWidget());
	}

	private void addPerson(String fname,String lname,String phone,String email) {

		Person person = new Person();
		Random random = new Random();
		person.setId(random.nextInt(1000));
		person.setFname(fname);
		person.setLname(lname);
		person.setPhone(phone);
		person.setEmailId(email);

		ServiceDefTarget serviceDef = (ServiceDefTarget) Service;
		serviceDef.setServiceEntryPoint(GWT.getModuleBaseURL() + "person");

		Service.addPerson(person, new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(Void result) {
				Window.alert("Person addition successful");
			}
		});
		
		
	}
}
