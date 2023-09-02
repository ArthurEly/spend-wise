package SpendWise.Interface.Menus;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.text.NumberFormat;
import java.time.LocalDate;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import SpendWise.Interface.Screen;
import SpendWise.Logic.Bills.Expense;
import SpendWise.Logic.Bills.Fixed;
import SpendWise.Logic.Bills.OneTime;
import SpendWise.Logic.Bills.Recurring;
import SpendWise.Logic.Managers.ExpensesManager;
import SpendWise.Utils.Dates;
import SpendWise.Utils.Offsets;
import SpendWise.Utils.Enums.BillsFields;
import SpendWise.Utils.Enums.ExpenseType;
import SpendWise.Utils.Enums.PanelOrder;
import SpendWise.Utils.Graphics.Alerts;
import SpendWise.Utils.Graphics.Components;
import SpendWise.Utils.Graphics.Panels;

public class BillCreator extends Screen {
    private ExpensesManager expensesManager;

    private JComponent[] fields;
    private JComboBox<ExpenseType> typeSelector;

    JPanel pnlTypeSpecific;

    private JCheckBox oneTimeCheckBox;
    private JFormattedTextField recurringEndDateField;

    public BillCreator(ExpensesManager expensesManager) {
        this.expensesManager = expensesManager;
        this.initialize();
    }

    @Override
    protected void initialize() {
        blankPanels = Panels.createPanelWithCenter(this, ACCENT_COLOR);

        JPanel pnlCentral = super.getBlankPanel(PanelOrder.CENTRAL);
        pnlCentral.setLayout(new GridLayout(BillsFields.values().length * 2 + 1, 1));

        fields = new JComponent[BillsFields.values().length];

        for (BillsFields field : BillsFields.values()) {
            String label = field.getName() + ": ";
            JLabel lbl = new JLabel(label);
            pnlCentral.add(lbl);

            JComponent fieldType = null;
            switch (field) {
                case VALUE:
                    JFormattedTextField valueField = new JFormattedTextField(NumberFormat.getCurrencyInstance());
                    valueField.setValue(Double.valueOf(0.0));
                    valueField.setColumns(10);
                    fieldType = valueField;
                    break;

                case ESSENTIAL:
                    JCheckBox essentialCheckBox = new JCheckBox();
                    fieldType = essentialCheckBox;
                    break;

                case DATE:
                    JFormattedTextField dateField = new JFormattedTextField(Dates.DATE_FORMATTER);
                    dateField.setValue(new java.util.Date());
                    fieldType = dateField;
                    break;

                case DESCRIPTION:
                    JTextField descriptionField = new JTextField();
                    fieldType = descriptionField;
                    break;

                case TYPE:
                    typeSelector = new JComboBox<ExpenseType>(ExpenseType.values());
                    typeSelector.addActionListener(e -> createTypeSpecificFields(e));
                    fieldType = typeSelector;
                    break;

                default:
                    fieldType = new JTextField();
                    break;
            }

            Components.defineSize(fieldType, DEFAULT_FIELD_SIZE);

            fieldType.setEnabled(true);
            fieldType.setBackground(ACCENT_COLOR);

            fields[field.ordinal()] = fieldType;
            pnlCentral.add(fieldType);
        }

        pnlTypeSpecific = new JPanel();
        pnlTypeSpecific.setBackground(ACCENT_COLOR);
        pnlCentral.add(pnlTypeSpecific);

        Offsets offsetsBtn = new Offsets(10, 10, 400, 20);
        Components.initializeButton(this.getBlankPanel(PanelOrder.SOUTH), offsetsBtn,
                "Submit", ACCENT_COLOR,
                e -> submit(e));
    }

    private void createTypeSpecificFields(ActionEvent e) {
        ExpenseType type = ExpenseType.values()[typeSelector.getSelectedIndex()];

        pnlTypeSpecific.removeAll();
        pnlTypeSpecific.setLayout(new BoxLayout(pnlTypeSpecific, BoxLayout.X_AXIS));
        JLabel lbl;

        switch (type) {
            case FIXED:
                // Has no new fields;
                break;

            case ONETIME:
                lbl = new JLabel("Has been paid?");
                lbl.setAlignmentX(JLabel.RIGHT_ALIGNMENT);
                pnlTypeSpecific.add(lbl);

                oneTimeCheckBox = new JCheckBox();
                oneTimeCheckBox.setBackground(ACCENT_COLOR);
                pnlTypeSpecific.add(oneTimeCheckBox);
                break;

            case RECURRING:
                lbl = new JLabel("End date: ");
                lbl.setAlignmentX(JLabel.RIGHT_ALIGNMENT);
                pnlTypeSpecific.add(lbl);

                recurringEndDateField = new JFormattedTextField(Dates.DATE_FORMATTER);
                recurringEndDateField.setValue(new java.util.Date());

                Components.defineSize(recurringEndDateField, DEFAULT_FIELD_SIZE);

                recurringEndDateField.setEnabled(true);
                recurringEndDateField.setBackground(ACCENT_COLOR);

                pnlTypeSpecific.add(recurringEndDateField);
                break;

            default:
                break;
        }

        this.refresh();
    }

    private void cleanFields() {
        for (BillsFields field : BillsFields.values()) {
            JComponent fieldType = fields[field.ordinal()];
            switch (field) {
                case VALUE:
                    ((JFormattedTextField) fieldType).setValue(Double.valueOf(0.0));
                    break;

                case ESSENTIAL:
                    ((JCheckBox) fieldType).setSelected(false);
                    break;

                case DATE:
                    ((JFormattedTextField) fieldType).setValue(new java.util.Date());
                    break;

                case DESCRIPTION:
                    ((JTextField) fieldType).setText("");
                    break;

                case TYPE:
                    typeSelector.setSelectedIndex(0);
                    break;
                default:
                    break;
            }
        }
    }

    private void submit(ActionEvent action) {
        JPanel alertPanel = getBlankPanel(PanelOrder.NORTH);
        Alerts.clearMessage(alertPanel);
        ExpenseType type = (ExpenseType) typeSelector.getSelectedItem();
        Expense exp = null;

        JFormattedTextField valueField = (JFormattedTextField) fields[BillsFields.VALUE.ordinal()];
        Alerts.clearBorder(valueField);

        double value = 0;
        try {
            value = NumberFormat.getCurrencyInstance().parse(valueField.getText()).doubleValue();
        } catch (Exception e) {
            return;
        }
        if (value <= 0) {
            Alerts.errorMessage(alertPanel, "Value must be greater than 0!");
            Alerts.errorBorder(valueField);
            return;
        }

        JCheckBox essentialCheckBox = (JCheckBox) fields[BillsFields.ESSENTIAL.ordinal()];
        boolean essential = essentialCheckBox.isSelected();

        JFormattedTextField dateField = (JFormattedTextField) fields[BillsFields.DATE.ordinal()];
        LocalDate date = Dates.convDate(Dates.convString(dateField.getText()));

        JTextField descriptionField = (JTextField) fields[BillsFields.DESCRIPTION.ordinal()];
        Alerts.clearBorder(descriptionField);

        String description = descriptionField.getText();
        if (description.isEmpty()) {
            Alerts.errorMessage(alertPanel, "Description cannot be empty!");
            Alerts.errorBorder(descriptionField);
            return;
        }

        switch (type) {
            case FIXED:
                exp = new Fixed(value, essential, date, description);
                break;
            case ONETIME:
                boolean paid = oneTimeCheckBox.isSelected();
                exp = new OneTime(value, essential, date, description, paid);
                break;
            case RECURRING:
                LocalDate endDate = Dates.convDate(Dates.convString(recurringEndDateField.getText()));
                exp = new Recurring(value, essential, date, description, endDate);
                break;
            default:
                break;
        }

        cleanFields();
        if (exp != null) {
            Alerts.showMessage(alertPanel, "Expense added successfully!", BACKGROUND_COLOR);
            expensesManager.addExpense(exp);
        } else {
            Alerts.errorMessage(alertPanel, "Expense could not be added!");
        }

    }
}
