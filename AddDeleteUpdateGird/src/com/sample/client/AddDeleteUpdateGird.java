package com.sample.client;


import java.util.ArrayList;

import com.gargoylesoftware.htmlunit.javascript.host.Text;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.IdentityValueProvider;
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
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.CheckBoxSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.Grid.GridCell;
import com.sencha.gxt.widget.core.client.grid.RowNumberer;
import com.sencha.gxt.widget.core.client.grid.editing.GridEditing;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;
import com.sencha.gxt.widget.core.client.toolbar.PagingToolBar;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

public class AddDeleteUpdateGird implements EntryPoint ,IsWidget{

	private FramedPanel  panel;
	protected Grid<Person> grid;
	private PersonServiceAsync Service = GWT.create(PersonService.class);
	private static final PersonProperties props = GWT.create(PersonProperties.class);
	
	@Override
	public Widget asWidget() {

		if (panel == null) {
			ColumnConfig<Person, String> fnameColumn = new ColumnConfig<Person, String>(
					props.fname(), 150, "First Name");
			ColumnConfig<Person, String> lnameColumn = new ColumnConfig<Person, String>(
					props.lname(), 150, "Last Name");
			ColumnConfig<Person, String> phoneColumn = new ColumnConfig<Person, String>(
					props.phone(), 150, "Phone");
			ColumnConfig<Person, String> emailColumn = new ColumnConfig<Person, String>(
					props.emailId(), 150, "Email ID");
			
			
			RpcProxy<PagingLoadConfig, PagingLoadResult<Person>> rpxProxy = new RpcProxy<PagingLoadConfig, PagingLoadResult<Person>>() {
				@Override
				public void load(PagingLoadConfig loadConfig,
						AsyncCallback<PagingLoadResult<Person>> callback) {
					
					Service.getAllPersons(loadConfig, callback);
				}
			};

			
			final ListStore<Person> store = new ListStore<Person>(
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

	
			

			ArrayList<ColumnConfig<Person, ?>> columns = new ArrayList<ColumnConfig<Person, ?>>();
			// The selection model provides the first column config
			columns.add(selectionModel.getColumn());

			columns.add(fnameColumn);
			columns.add(lnameColumn);
			columns.add(phoneColumn);
			columns.add(emailColumn);

			ColumnModel<Person> cm = new ColumnModel<Person>(columns);
			grid = new Grid<Person>(store, cm) {
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
			
			 final GridEditing<Person> editing = createGridEditing(grid);
		 /*    editing.addEditor(fnameColumn, new TextField());
		      editing.addEditor(lnameColumn, new TextField());
		      editing.addEditor(phoneColumn, new TextField());
		      editing.addEditor(emailColumn, new TextField());*/
		
		 
		      // EDITING //
		      TextButton addButton = new TextButton("Add Person");
		      addButton.addSelectHandler(new SelectHandler() {
		        @Override
		        public void onSelect(SelectEvent event) {
		          Person Person = new Person();  
		          Person.setId(0);
		          Person.setFname("ghgh");
		          Person.setLname("ghg");
		          Person.setPhone("jlk");
		          Person.setEmailId("jl");
		          editing.cancelEditing();
		          store.add(0, Person);
		 
		          int row = store.indexOf(Person);
		          editing.startEditing(new GridCell(row, 0));
		        }

		      });
		 
		      
			
			
			final CheckBox warnLoad = new CheckBox();
			warnLoad.setBoxLabel("Warn before loading new data");
			warnLoad.setValue(false);

			final PagingToolBar toolBar = new PagingToolBar(10);
			toolBar.getElement().getStyle().setProperty("borderBottom", "none");
			toolBar.bind(loader);
			ToolBar toolBar1 = new ToolBar();
		      toolBar1.add(addButton);
		      toolBar1.add(toolBar);

			VerticalLayoutContainer verticalLayoutContainer = new VerticalLayoutContainer();
			verticalLayoutContainer.setBorders(true);
			verticalLayoutContainer.add(grid, new VerticalLayoutData(1, 1));
			verticalLayoutContainer.add(toolBar1, new VerticalLayoutData(1, -1));
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
			panel.setPixelSize(500, 365);
			panel.addStyleName("margin-10");
			panel.setWidget(verticalLayoutContainer);
			panel.setButtonAlign(BoxLayoutPack.CENTER);
		      panel.addButton(new TextButton("Reset", new SelectHandler() {
		        @Override
		        public void onSelect(SelectEvent event) {
		          store.rejectChanges();
		        }
		      }));
		 
		      panel.addButton(new TextButton("Save", new SelectHandler() {
		        @Override
		        public void onSelect(SelectEvent event) {
		          store.commitChanges();
		        }
		      }));

		}
		return panel;
	}

	private GridEditing<Person> createGridEditing(Grid<Person> grid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onModuleLoad() {
		
		RootPanel.get("editableGrid").add(asWidget());

	}

	
}
