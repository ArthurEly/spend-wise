package SpendWise.Interface.Menus;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import SpendWise.Interface.Screen;
import SpendWise.Interface.PopUps.CreateGroup;
import SpendWise.Logic.Group;
import SpendWise.Logic.User;
import SpendWise.Logic.Managers.GroupManager;
import SpendWise.Logic.Managers.UserManager;
import SpendWise.Utils.Offsets;
import SpendWise.Utils.Enums.GroupActions;
import SpendWise.Utils.Enums.GroupFields;
import SpendWise.Utils.Enums.PanelOrder;
import SpendWise.Utils.Graphics.Components;
import SpendWise.Utils.Graphics.Images;
import SpendWise.Utils.Graphics.Panels;

public class GroupsMenu extends Screen {
    private JPanel pnlGroupManagement;
    private final String IMAGE_PATH = IMAGES_PATH + "groupsImage" + IMAGES_EXT;
    private JComponent[] fields;
    private JComboBox<User> userBox;
    private JComboBox<GroupActions> operationBox;
    private JComboBox<Group> groupBox;
    private JLabel lblOperation;
    private JLabel lblUsername;
    private UserManager userManager;
    private User loggedUser;
    private GroupManager groupManager;
    private Group currentGroup;

    public GroupsMenu(UserManager userManager) {
        this.userManager = userManager;
        this.loggedUser = userManager.getLoggedUser();
        this.groupManager = loggedUser.getGroupManager();
        pnlGroupManagement = new JPanel();
        initialize();
    }

    @Override
    protected void initialize() {
        blankPanels = Panels.createPanelWithCenter(this, ACCENT_COLOR);

        JPanel centerPanel = getBlankPanel(PanelOrder.CENTRAL);
        JPanel[] innerPanels = Panels.initializeOffsets(centerPanel, new Offsets(0, 0, 0, 150), ACCENT_COLOR);

        addImage(innerPanels[PanelOrder.EAST.ordinal()]);


        pnlGroupManagement = innerPanels[PanelOrder.CENTRAL.ordinal()];
        pnlGroupManagement.setLayout(new GridLayout(GroupFields.values().length*2, 2));

        fields = new JComponent[GroupFields.values().length];

        JComponent fieldType = null;
        for (GroupFields field : GroupFields.values()) {
            String label = field.getName() + ": ";
            JLabel lbl = new JLabel(label);
            lbl.setVisible(false);
            pnlGroupManagement.add(lbl);
            
            switch (field) {
                case SELECT_GROUP:
                    lbl.setVisible(true);
                    Group[] groups = groupManager.getGroups().toArray(new Group[0]);
                    groupBox = new JComboBox<Group>(groups);
                    groupBox.setSelectedIndex(-1);
                    groupBox.addActionListener(e -> showOptionsField(e));
                    fieldType = groupBox;
                    break;
                    case SELECT_OPERATION:
                    operationBox = new JComboBox<GroupActions>(GroupActions.values());
                    operationBox.setVisible(false);
                    operationBox.setSelectedIndex(-1);
                    operationBox.addActionListener(e -> updateUsernameField(e));
                    fieldType = operationBox;
                    lblOperation = lbl;
                    break;
                case USERNAME:
                    userBox = new JComboBox<User>();
                    userBox.setSelectedIndex(-1);
                    userBox.setVisible(false);
                    fieldType = userBox;
                    lblUsername = lbl;
                    break;
            }

            if (fieldType == null)
                continue;

            Components.defineSize(fieldType, DEFAULT_FIELD_SIZE);

            fieldType.setEnabled(true);
            fieldType.setBackground(ACCENT_COLOR);

            fields[field.ordinal()] = fieldType;
            pnlGroupManagement.add(fieldType);

        }

        centerPanel.add(pnlGroupManagement, BorderLayout.CENTER);
        String[] buttonsTexts = { "Add Group","Save", "Delete Group" };
        ActionListener[] buttonListeners = { e -> addGroup(e), e -> saveButton(e), e -> deleteGroup(e)};
        Components.initializeButtons(this.getBlankPanel(PanelOrder.SOUTH), new Offsets(10, 10, 400, 20), buttonsTexts,
                ACCENT_COLOR,
                buttonListeners);
    }

    private void showOptionsField(ActionEvent e) {
        // Make operation fields visible, but keep username fields invisible
        lblOperation.setVisible(true);
        operationBox.setVisible(true);

        userBox.setVisible(false);
        lblUsername.setVisible(false);
        
        updateGroupName();
    }

    private void updateGroupName(){
        currentGroup = (Group) groupBox.getSelectedItem();
    }

    private void centralizeImage(JPanel panel, JLabel lblImage) {
        JPanel[] imageOffsets = Panels.initializeOffsets(panel, new Offsets(90, 90, 25, 0), ACCENT_COLOR);
        imageOffsets[PanelOrder.CENTRAL.ordinal()].add(lblImage);
    }
    
    private void addImage(JPanel panel) {
        ImageIcon resizedImageIcon = Images.resizedIcon(IMAGE_PATH, 100, 100);
        centralizeImage(panel, new JLabel(resizedImageIcon));
    }

    private void updateUsernameField(ActionEvent e) {
        GroupActions operation = (GroupActions) operationBox.getSelectedItem();
        
        userBox.setVisible(true);
        lblUsername.setVisible(true);
        
        this.userBox.removeAllItems();
        
        if (operation == GroupActions.ADD) {
            for (User user : userManager.getUsers().values()) {
                if (user.getGroupManager().findGroup(currentGroup.getGroupName()) != null)
                    continue;

                this.userBox.addItem(user);
            }
        }
        else {
            for(User user : currentGroup.getUsers()){
                if(user != loggedUser)
                    this.userBox.addItem(user);
            }    
        }
        this.userBox.setSelectedIndex(-1);
    }

    private void addGroup(ActionEvent e) {
        CreateGroup createGroup = new CreateGroup(this, "Create Group", loggedUser, () -> refreshGroupField());
        createGroup.run();
    }

    private void refreshGroupField() {
        Group[] groups = groupManager.getGroups().toArray(new Group[0]);
        groupBox.removeAllItems();
        groupBox.setModel(new DefaultComboBoxModel<Group>(groups));
        groupBox.setSelectedIndex(-1);
        //if(groupBox.getItemCount() == 0){
            operationBox.setVisible(false);
            userBox.setVisible(false); 
            lblOperation.setVisible(false);
            lblUsername.setVisible(false); 
        //}
    }

    private void deleteGroup(ActionEvent e) {
        Group givenGroup = (Group) groupBox.getSelectedItem();
        if (givenGroup == null)
            return;
            
        groupManager.removeGroup(givenGroup);
        groupBox.setSelectedIndex(-1);
        refreshGroupField();
    }

    private void saveButton(ActionEvent e) {
        User givenUser = (User) userBox.getSelectedItem();     
        if (givenUser == null)
            return;

        GroupActions operation = (GroupActions) operationBox.getSelectedItem();

        if (operation == GroupActions.DELETE) {
            currentGroup.removeUser(givenUser);
        }
        else {
            currentGroup.addUser(givenUser);
        }

        operationBox.setSelectedIndex(-1);
        userBox.setSelectedIndex(-1);
    }

}
