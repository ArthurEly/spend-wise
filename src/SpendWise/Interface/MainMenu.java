package SpendWise.Interface;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import SpendWise.Interface.Menus.AccountMenu;
import SpendWise.Interface.Menus.BillCreator;
import SpendWise.Interface.Menus.AnalysisMenu;
import SpendWise.Interface.Menus.ExpensesMenu;
import SpendWise.Interface.Menus.GroupsMenu;
import SpendWise.Interface.Menus.LoginMenu;
import SpendWise.Logic.User;
import SpendWise.Logic.Managers.ExpensesManager;
import SpendWise.Logic.Managers.UserManager;
import SpendWise.Utils.Database;
import SpendWise.Utils.Offsets;
import SpendWise.Utils.Enums.Contexts;
import SpendWise.Utils.Enums.PanelOrder;
import SpendWise.Utils.Graphics.Colors;
import SpendWise.Utils.Graphics.Fonts;
import SpendWise.Utils.Graphics.Icons;
import SpendWise.Utils.Graphics.Panels;

public class MainMenu extends JFrame implements Colors, Fonts, Icons {
    private final static String APP_NAME = "SpendWise";
    private final static int SCREEN_WIDTH = 1920 / 2;
    private final static int SCREEN_HEIGHT = 1080 / 2;
    private final static int BUTTON_MAX_WIDTH = 160;
    private final static int BUTTON_SPACING = 25;
    private final static Contexts DEFAULT_CONTEXT = Contexts.ACCOUNT;
    private Contexts currentContext;
    private Screen[] screens;
    private JButton[] buttons;
    private Box centerLayout;
    private JPanel sidePanel;
    private UserManager userManager;

    public MainMenu() {
        screens = new Screen[Contexts.values().length];
        this.setTitle(APP_NAME);
        this.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        this.getContentPane().setBackground(BACKGROUND_COLOR);
        this.setLocationRelativeTo(null);

        this.setVisible(true);

        /*
         * The following line can be removed later on, if we
         * wish to make the JFrames of the application occupy
         * a fixed amount of space in the screen, even when it is resized.
         * 
         * Note that we are now hardcoding the size of the JFrames,
         * what is a bad practice.
         */
        this.setResizable(false);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                Database.saveUserManager(userManager);
                System.exit(0);
            }
        });

        this.getContentPane().setLayout(new BorderLayout());
        this.setIconImage(APP_LOGO.getImage());

        this.currentContext = Contexts.LOGIN;
        this.centerLayout = Box.createVerticalBox();
        this.sidePanel = new JPanel();
    }

    public boolean run() {
        this.userManager = Database.loadUserManager();
        this.createLogin();
        this.logout(null);
        this.getContentPane().add(centerLayout, BorderLayout.CENTER);
        this.createButtons();
        this.getContentPane().add(sidePanel, BorderLayout.EAST);
        this.refresh();

        return true;
    }

    private void refresh() {
        this.revalidate();
        this.repaint();
    }

    private void createLogin() {
        screens[Contexts.LOGIN.ordinal()] = new LoginMenu(e -> login(e), userManager);
    }

    private void createMenus() {
        User loggedUser = userManager.getLoggedUser();
        ExpensesManager expensesManager = loggedUser.getExpensesManager();
        screens[Contexts.ACCOUNT.ordinal()] = new AccountMenu(userManager, () -> logout(null));
        screens[Contexts.BILL.ordinal()] = new BillCreator(expensesManager);
        screens[Contexts.ANALYSIS.ordinal()] = new AnalysisMenu(loggedUser);
        screens[Contexts.GROUPS.ordinal()] = new GroupsMenu(userManager);
        screens[Contexts.EXPENSES.ordinal()] = new ExpensesMenu(expensesManager);
    }

    private void createButtons() {
        Offsets offsets = new Offsets(20, 35, 0, 0);
        JPanel[] blankPanels = Panels.createPanelWithCenter(sidePanel, offsets, BACKGROUND_COLOR);

        JPanel mainSidePanel = blankPanels[PanelOrder.CENTRAL.ordinal()];

        mainSidePanel.setLayout(new BoxLayout(mainSidePanel, BoxLayout.Y_AXIS));

        buttons = new JButton[Contexts.values().length];

        // Creates the buttons for each context except for the logout button
        for (Contexts ctx : Contexts.values()) {
            if (ctx == Contexts.LOGIN)
                continue;

            Dimension buttonSize = new Dimension(BUTTON_MAX_WIDTH, 50);
            buttons[ctx.ordinal()] = new MenuButton(ctx.getName(), ACCENT_COLOR,
                    BACKGROUND_COLOR, buttonSize);
            buttons[ctx.ordinal()].addActionListener(e -> updateContext(e));

            mainSidePanel.add(buttons[ctx.ordinal()]);
            mainSidePanel.add(Box.createVerticalStrut(BUTTON_SPACING));
        }

        // Easier manipulation of the logout button
        buttons[Contexts.LOGIN.ordinal()] = new JButton(Contexts.LOGIN.getName());
        JButton logoutButton = buttons[Contexts.LOGIN.ordinal()];

        logoutButton.setBackground(ACCENT_COLOR);
        logoutButton.setFont(STD_FONT_BOLD);
        logoutButton.setSize(50, 50);
        logoutButton.setVisible(true);

        logoutButton.addActionListener(e -> logout(e));

        blankPanels[PanelOrder.SOUTH.ordinal()].add(logoutButton);
    }

    private void updateContext(ActionEvent e) {
        // Clears the screen for the new context
        this.centerLayout.removeAll();

        // Sets the new context
        if (e != null) {
            for (int i = 0; i < buttons.length; i++) {
                buttons[i].setSelected(false);
                if (e.getSource() == buttons[i]) {
                    this.currentContext = Contexts.values()[i];
                    buttons[i].setSelected(true);
                }
            }
        }

        // Adds the new context to the screen
        this.centerLayout.add(screens[this.currentContext.ordinal()]);
        screens[this.currentContext.ordinal()].refresh();

        switch (this.currentContext) {
            case LOGIN:
                this.sidePanel.setVisible(false);
                break;
            default:
                this.sidePanel.setVisible(true);
                break;
        }

        this.refresh();
    }

    private void login(ActionEvent e) {
        LoginMenu login = (LoginMenu) this.screens[Contexts.LOGIN.ordinal()];

        if (login.authorizeUser()) {
            createMenus();
            this.currentContext = DEFAULT_CONTEXT;
            this.buttons[DEFAULT_CONTEXT.ordinal()].setSelected(true);
        } else {
            this.currentContext = Contexts.LOGIN;
        }

        this.updateContext(null);
    }

    private void logout(ActionEvent e) {
        this.currentContext = Contexts.LOGIN;
        this.updateContext(null);
    }
}
