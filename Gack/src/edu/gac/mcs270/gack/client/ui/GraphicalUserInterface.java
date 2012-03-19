package edu.gac.mcs270.gack.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.gac.mcs270.gack.client.MessageDisplay;
import edu.gac.mcs270.gack.client.Registry;
import edu.gac.mcs270.gack.client.Utility;
import edu.gac.mcs270.gack.shared.Loader;
import edu.gac.mcs270.gack.shared.LoaderAsync;
import edu.gac.mcs270.gack.shared.Persistence;
import edu.gac.mcs270.gack.shared.PersistenceAsync;
import edu.gac.mcs270.gack.shared.domain.AutoPerson;
import edu.gac.mcs270.gack.shared.domain.Person;
import edu.gac.mcs270.gack.shared.domain.Scroll;
import edu.gac.mcs270.gack.shared.domain.Thing;

public class GraphicalUserInterface implements EntryPoint, MessageDisplay {
	
	private Person player;
	private Registry registry;
	private int pace = 2;
	private TextArea textArea;
	private MenuItem mntmGo;
	private MenuItem mntmTake;
	private MenuItem mntmDrop;
	private MenuItem mntmRead;
	private List<MenuItem> paceMenuItems;
	private MenuItem mntmGive;
	private MenuItem mntmLookAround;
	private MenuItem mntmListPossessions;
	private MenuItem mntmChangePlayersName;
	private MenuItem mntmInit;
	private MenuItem mntmLoad;
	private MenuItem mntmSave;
	
	private void playTurn() {
		registry.trigger(pace);
		configureMenus();
	}
	
	public void displayMessage(String message){
		textArea.setText(textArea.getText() + message + "\n");
		Element element = textArea.getElement();
		element.setScrollTop(element.getScrollHeight());
	}
	
	private void start(Person player, String heading){
		this.player = player;
		this.registry = new Registry();
		AutoPerson.setRegistry(registry);
		textArea.setText(heading + "\n");
		configureMenus();
	}

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		final LoaderAsync loader = GWT.create(Loader.class);
		final AsyncCallback<Person> loaderCallback = new AsyncCallback<Person>(){
			public void onSuccess(Person player){
				start(player, "Welcome to the Imaginary Land of Gack");
			}
			
			public void onFailure(Throwable caught){
				throw new Error("Server trouble", caught);
			}			
		};
		loader.getPlayer(loaderCallback);

		Utility.setMessageDisplay(this);
		RootPanel rootPanel = RootPanel.get();
		
		VerticalPanel verticalPanel = new VerticalPanel();
		rootPanel.add(verticalPanel, 10, 10);
		verticalPanel.setWidth("90%");
		
		MenuBar menuBar = new MenuBar(false);
		verticalPanel.add(menuBar);
		menuBar.setWidth("100%");
		
		final PersistenceAsync persistence = GWT.create(Persistence.class);
		MenuBar persistenceBar = new MenuBar(true);
		MenuItem mntmPersistence = new MenuItem("World", false, persistenceBar);
		
		mntmInit = new MenuItem("Reinitialize", false, new Command() {
			public void execute() {
				displayMessage("\n>>> Reinitialize world");
				loader.getPlayer(loaderCallback);
			}
		});
		persistenceBar.addItem(mntmInit);

		mntmLoad = new MenuItem("Load...", false, new Command() {
			public void execute() {
				new LoadDialog(persistence, GraphicalUserInterface.this);
			}
		});
		persistenceBar.addItem(mntmLoad);
		
		mntmSave = new MenuItem("Save", false, new Command() {
			public void execute() {
				displayMessage("\n>>> Save world");
				persistence.savePlayer(player,
						new AsyncCallback<Void>(){
					public void onSuccess(Void v){
						displayMessage("Saving of " + player + "'s world succeeded.");
					}

					public void onFailure(Throwable caught){
						displayMessage("WARNING: Saving of " + player + "'s world failed.");
					}			
				});

			}
		});
		persistenceBar.addItem(mntmSave);
		menuBar.addItem(mntmPersistence);

		MenuBar menuBar_1 = new MenuBar(true);
		
		MenuItem mntmGetInfo = new MenuItem("Get info", false, menuBar_1);
		
		mntmLookAround = new MenuItem("Look around", false, new Command() {
			public void execute() {
				displayMessage("\n>>> Look around");
				player.lookAround();
			}
		});
		menuBar_1.addItem(mntmLookAround);
		
		mntmListPossessions = new MenuItem("List possessions", false, new Command() {
			public void execute() {
				displayMessage("\n>>> List possessions");
				player.listPossessions();
			}
		});
		menuBar_1.addItem(mntmListPossessions);
		menuBar.addItem(mntmGetInfo);
		MenuBar menuBar_2 = new MenuBar(true);
		
		MenuItem mntmAct = new MenuItem("Act", false, menuBar_2);
		MenuBar goMenu = new MenuBar(true);
		
		mntmGo = new MenuItem("Go", false, goMenu);
		menuBar_2.addItem(mntmGo);
		MenuBar menuBar_4 = new MenuBar(true);
		
		mntmTake = new MenuItem("Take", false, menuBar_4);
		menuBar_2.addItem(mntmTake);
		MenuBar menuBar_5 = new MenuBar(true);
		
		mntmDrop = new MenuItem("Drop", false, menuBar_5);
		menuBar_2.addItem(mntmDrop);
		MenuBar menuBar_6 = new MenuBar(true);
		
		mntmRead = new MenuItem("Read", false, menuBar_6);
		menuBar_2.addItem(mntmRead);
		MenuBar menuBar_7 = new MenuBar(true);
		
		mntmGive = new MenuItem("Give", false, menuBar_7);
		menuBar_2.addItem(mntmGive);
		menuBar.addItem(mntmAct);
		MenuBar menuBar_8 = new MenuBar(true);
		
		MenuItem mntmConfigure = new MenuItem("Configure", false, menuBar_8);
		
		mntmChangePlayersName = new MenuItem("Change player's name...", false, new Command() {
			public void execute() {
				displayMessage("\n>>> Change player's name");
				new NameDialog(GraphicalUserInterface.this);
			}
		});
		menuBar_8.addItem(mntmChangePlayersName);
		MenuBar paceMenuBar = new MenuBar(true);
		
		MenuItem mntmSetPace = new MenuItem("Set pace", false, paceMenuBar);
		paceMenuItems = new ArrayList<MenuItem>();
		for(int p = 1; p <= 5; p++){
			final int finalP = p;
			MenuItem menuItem = new MenuItem(""+p, false, new Command(){
				@Override
				public void execute() {
					selectPace(finalP);
				}
			});
			paceMenuItems.add(menuItem);
			paceMenuBar.addItem(menuItem);
		}
		selectPace(pace);
		menuBar_8.addItem(mntmSetPace);
		menuBar.addItem(mntmConfigure);
		
		disableMenuItems();
		
		textArea = new TextArea();
		verticalPanel.add(textArea);
		textArea.setWidth("100%");
		textArea.setVisibleLines(30);
		textArea.setText("Loading ...\n");
	}

	private void selectPace(int newPace) {
		for(MenuItem i : paceMenuItems){
			i.setStyleDependentName("chosen", i.getText().equals(""+newPace));
		}
		pace = newPace;
	}

	private void configureMenus() {
		configureWorldMenu();
		configureGetInfoMenu();
		configureActMenu();
		configureConfigureMenu();
	}
	
	private void configureWorldMenu(){
		mntmInit.setEnabled(true);
		mntmLoad.setEnabled(true);
		mntmSave.setEnabled(true);
	}

	private void configureActMenu() {
		configureGoMenu();
		configureTakeMenu();
		configureDropMenu();
		configureReadMenu();
		configureGiveMenu();
	}

	private void configureConfigureMenu() {
		mntmChangePlayersName.setEnabled(true);
	}

	private void configureGetInfoMenu() {
		mntmLookAround.setEnabled(true);
		mntmListPossessions.setEnabled(true);
	}

	private void configureGiveMenu() {
		MenuBar menu = mntmGive.getSubMenu();
		menu.clearItems();
		List<Thing> things = player.getPossessions();
		List<Person> people = player.otherPeopleAtSamePlace();
		mntmGive.setEnabled(false);
		for(final Person p : people){
			MenuBar subMenu = new MenuBar(true);
			menu.addItem(new MenuItem(p.toString(), subMenu));
			for(final Thing t : things){
				mntmGive.setEnabled(true);
				subMenu.addItem(new MenuItem(t.toString(), new Command(){
					@Override
					public void execute() {
						displayMessage("\n>>> Give " + p + " " + t);
						displayMessage("You need to write the code to have " + player + 
								" give " + t + " to " + p + ".");
					}
				}));
			}
		}
	}

	private void configureGoMenu() {
		MenuBar menu = mntmGo.getSubMenu();
		menu.clearItems();
		mntmGo.setEnabled(false);
		for(final String exit : player.getPlace().exits()){
			menu.addItem(new MenuItem(exit, new Command(){
				@Override
				public void execute() {
					displayMessage("\n>>> Go " + exit);
					player.go(exit);
					playTurn();
				}
			}));
			mntmGo.setEnabled(true);
		}
	}

	private void configureTakeMenu() {
		MenuBar menu = mntmTake.getSubMenu();
		menu.clearItems();
		mntmTake.setEnabled(false);
		for(final Thing t : player.otherThingsAtSamePlace()){
			menu.addItem(new MenuItem(t.toString(), new Command(){
				@Override
				public void execute() {
					displayMessage("\n>>> Take " + t);
					player.take(t);
					playTurn();
				}
			}));
			mntmTake.setEnabled(true);
		}
	}

	private void configureDropMenu() {
		MenuBar menu = mntmDrop.getSubMenu();
		menu.clearItems();
		mntmDrop.setEnabled(false);
		for(final Thing t : player.getPossessions()){
			menu.addItem(new MenuItem(t.toString(), new Command(){
				@Override
				public void execute() {
					displayMessage("\n>>> Drop " + t);
					player.lose(t);
					playTurn();
				}
			}));
			mntmDrop.setEnabled(true);
		}
	}

	private void configureReadMenu() {
		MenuBar menu = mntmRead.getSubMenu();
		menu.clearItems();
		mntmRead.setEnabled(false);
		for(final Scroll t : Scroll.scrollsIn(player.getPlace())){
			menu.addItem(new MenuItem(t.toString(), new Command(){
				@Override
				public void execute() {
					displayMessage("\n>>> Read " + t);
					player.read(t);
					playTurn();
				}
			}));
			mntmRead.setEnabled(true);
		}
	}

	private static class NameDialog extends DialogBox {
		public NameDialog(final GraphicalUserInterface gui) {
			this.setModal(true);
			this.setText("Input name");
			final TextBox nameBox = new TextBox();
			this.setWidget(nameBox);
			nameBox.addKeyPressHandler(new KeyPressHandler() {
				public void onKeyPress(KeyPressEvent event) {
					if (event.getCharCode() == KeyCodes.KEY_ENTER) {
						gui.player.setName(nameBox.getText());
						gui.displayMessage("Player's name changed to " + gui.player);
						NameDialog.this.hide();
					}
				}});
			this.show();
			nameBox.setFocus(true);
		}
	}
	
	private static class LoadDialog extends DialogBox {
		public LoadDialog(final PersistenceAsync persistence, 
				final GraphicalUserInterface gui) {
			this.setModal(true);
			this.setText("Input name");
			final TextBox nameBox = new TextBox();
			this.setWidget(nameBox);
			nameBox.addKeyPressHandler(new KeyPressHandler() {
				public void onKeyPress(KeyPressEvent event) {
					if (event.getCharCode() == KeyCodes.KEY_ENTER) {
						final String name = nameBox.getText();
						gui.displayMessage("\n>>> Load world of " + name);
						gui.disableMenuItems();
						persistence.getPlayer(name,
								new AsyncCallback<Person>(){
							public void onSuccess(Person player){
								if(player == null){
									gui.displayMessage("No saved world existed for " + name);
									gui.configureMenus();
								} else {
									gui.start(player, "Welcome back, " + name + ".");
								}
							}

							public void onFailure(Throwable caught){
								gui.displayMessage("Loading failed due to technical difficulties.");
								gui.displayMessage(caught.getMessage());
								gui.configureMenus();
							}			
						});
						LoadDialog.this.hide();
					}
				}});
			this.show();
			nameBox.setFocus(true);
		}
	}
	
	private void disableMenuItems(){
		mntmGo.setEnabled(false);
		mntmTake.setEnabled(false);
		mntmDrop.setEnabled(false);
		mntmRead.setEnabled(false);
		mntmGive.setEnabled(false);
		mntmLookAround.setEnabled(false);
		mntmListPossessions.setEnabled(false);
		mntmChangePlayersName.setEnabled(false);
		mntmInit.setEnabled(false);
		mntmLoad.setEnabled(false);
		mntmSave.setEnabled(false);
	}
}