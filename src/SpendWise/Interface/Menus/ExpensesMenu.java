package SpendWise.Interface.Menus;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Date;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import SpendWise.Interface.Screen;
import SpendWise.Logic.Bills.Expense;
import SpendWise.Logic.Bills.Fixed;
import SpendWise.Logic.Bills.OneTime;
import SpendWise.Logic.Bills.Recurring;
import SpendWise.Logic.Managers.ExpensesManager;
import SpendWise.Utils.Dates;
import SpendWise.Utils.Offsets;
import SpendWise.Utils.Triple;
import SpendWise.Utils.Enums.BillsFields;
import SpendWise.Utils.Enums.ExpenseType;
import SpendWise.Utils.Enums.PanelOrder;
import SpendWise.Utils.Graphics.Alerts;
import SpendWise.Utils.Graphics.Components;
import SpendWise.Utils.Graphics.Panels;

public class ExpensesMenu extends Screen {
    private ExpensesManager expensesManager;
    private JComboBox<Expense> expensesComboBox;

    private JPanel pnlCentral;
    private JPanel pnlTypeSpecific;

    private JComponent[] fields;
    private JCheckBox oneTimeCheckBox;
    private JFormattedTextField recurringEndDateField;
    private static final Dimension valueHistorySize = new Dimension(250, 25);
    private static final Dimension comboBoxSize = new Dimension(450, 30);

    private JPanel alertPanel;
    private JButton btnChangeExpense;
    private boolean isEditing;

    public ExpensesMenu(ExpensesManager expensesManager) {
        this.expensesManager = expensesManager;
        this.initialize();
    }

    @Override
    protected void initialize() {
        blankPanels = Panels.createPanelWithCenter(this, ACCENT_COLOR);

        JPanel comboBoxPanel = (JPanel) this.getComponent(0);
        Offsets comboBoxOffsets = new Offsets(0, 0, 0, 0);
        JPanel comboBoxCenter = Panels.createPanelWithCenter(comboBoxPanel, comboBoxOffsets,
                BACKGROUND_COLOR)[PanelOrder.CENTRAL
                        .ordinal()];

        expensesComboBox = new JComboBox<Expense>(expensesManager.getExpenses().toArray(new Expense[0]));
        expensesComboBox.addActionListener(e -> createBillFields(e));
        Components.defineSize(expensesComboBox, comboBoxSize);
        comboBoxCenter.add(expensesComboBox);

        pnlCentral = super.getBlankPanel(PanelOrder.CENTRAL);
        createBillFields(null);

        Offsets offsetsBtn = new Offsets(10, 10, 400, 20);
        ActionListener[] listeners = { e -> changeExpense(e), e -> removeExpense(e) };
        String[] btnLabels = { "Edit Expense", "Remove Expense" };
        btnChangeExpense = Components.initializeButtons(getBlankPanel(PanelOrder.SOUTH), offsetsBtn, btnLabels,
                ACCENT_COLOR,
                listeners)[0];

        alertPanel = getBlankPanel(PanelOrder.NORTH);
    }

    @Override
    public void refresh() {
        expensesComboBox.removeAllItems();
        for (Expense expense : expensesManager.getExpenses()) {
            expensesComboBox.addItem(expense);
        }
        expensesComboBox.setSelectedIndex(-1);
        Alerts.clearMessage(alertPanel);
        super.refresh();
    }

    public void refresh(Expense exp) {
        expensesComboBox.removeAllItems();
        expensesComboBox.setSelectedIndex(-1);
        for (Expense expense : expensesManager.getExpenses()) {
            expensesComboBox.addItem(expense);
            if (expense == exp)
                expensesComboBox.setSelectedItem(exp);
        }
        createBillFields(null);
        Alerts.clearMessage(alertPanel);
        super.refresh();
    }

    private void createBillFields(ActionEvent e) {
        Expense selectedExpense = (Expense) expensesComboBox.getSelectedItem();
        boolean expenseSelected = selectedExpense != null;

        pnlCentral.removeAll();
        pnlCentral.setLayout(new GridLayout((BillsFields.values().length - 1) * 2 + 1, 1));

        double defaultValue = expenseSelected ? selectedExpense.getValue() : 0.0;

        boolean defaultEssential = expenseSelected ? selectedExpense.isEssential() : false;

        LocalDate temp = expenseSelected ? selectedExpense.getDate() : LocalDate.now();
        Date defaultDate = Dates.convLocalDate(temp);

        String defaultDescription = expenseSelected ? selectedExpense.getDescription() : "";

        fields = new JComponent[BillsFields.values().length];

        for (BillsFields field : BillsFields.values()) {
            if (field == BillsFields.TYPE)
                continue;

            String label = field.getName() + ": ";
            JLabel lbl = new JLabel(label);
            pnlCentral.add(lbl);

            JComponent fieldType = null;
            switch (field) {
                case VALUE:
                    JFormattedTextField valueField = new JFormattedTextField(NumberFormat.getCurrencyInstance());
                    valueField.setValue(Double.valueOf(defaultValue));
                    valueField.setColumns(10);
                    valueField.setEditable(false);
                    fieldType = valueField;
                    break;

                case ESSENTIAL:
                    JCheckBox essentialCheckBox = new JCheckBox();
                    essentialCheckBox.setSelected(defaultEssential);
                    essentialCheckBox.setEnabled(false);
                    fieldType = essentialCheckBox;
                    break;

                case DATE:
                    JFormattedTextField dateField = new JFormattedTextField(Dates.DATE_FORMATTER);
                    dateField.setValue(defaultDate);
                    dateField.setEditable(false);
                    fieldType = dateField;
                    break;

                case DESCRIPTION:
                    JTextField descriptionField = new JTextField(defaultDescription);
                    descriptionField.setEditable(false);
                    fieldType = descriptionField;
                    break;

                default:
                    break;
            }

            fieldType.setBackground(ACCENT_COLOR);

            fields[field.ordinal()] = fieldType;
            pnlCentral.add(fieldType);
        }

        pnlTypeSpecific = new JPanel();
        pnlTypeSpecific.setBackground(ACCENT_COLOR);
        pnlCentral.add(pnlTypeSpecific);

        fields[BillsFields.TYPE.ordinal()] = createTypeSpecificFields(selectedExpense);
        if (fields[BillsFields.TYPE.ordinal()] != null)
            pnlTypeSpecific.add(fields[BillsFields.TYPE.ordinal()]);

        super.refresh();
    }

    private JComponent createTypeSpecificFields(Expense exp) {
        boolean expenseSelected = (exp != null);
        ExpenseType type = expenseSelected ? exp.getType() : null;

        pnlTypeSpecific.removeAll();
        pnlTypeSpecific.setLayout(new BoxLayout(pnlTypeSpecific, BoxLayout.X_AXIS));
        if (type == null)
            return null;

        JLabel lbl;

        switch (type) {
            case FIXED:
                JPanel pnlFixed = new JPanel(new FlowLayout());
                pnlFixed.setBackground(ACCENT_COLOR);
                Fixed expense = (Fixed) exp;

                Box valueHistoryBox = Box.createVerticalBox();
                valueHistoryBox.setBackground(ACCENT_COLOR);
                valueHistoryBox.setAlignmentX(JComponent.CENTER_ALIGNMENT);
                JScrollPane scrollPane = new JScrollPane(valueHistoryBox, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                scrollPane.setBackground(ACCENT_COLOR);
                scrollPane.getViewport().setBackground(ACCENT_COLOR);
                scrollPane.setPreferredSize(valueHistorySize);

                final int SpacerXSize = 50;
                JLabel lblValueHistory = new JLabel("Value History");
                lblValueHistory.setAlignmentX(JComponent.CENTER_ALIGNMENT);
                valueHistoryBox.add(lblValueHistory);

                JLabel firstSpacer = new JLabel("-".repeat(SpacerXSize) + "\n");
                firstSpacer.setAlignmentX(JComponent.CENTER_ALIGNMENT);
                valueHistoryBox.add(firstSpacer);

                for (Triple<LocalDate, LocalDate, Double> triple : expense.getValueHistory()) {
                    StringBuilder sb = new StringBuilder();
                    for (Object value : triple) {
                        boolean isLastDate = value instanceof LocalDate && LocalDate.MAX.minusDays(1).equals(value);
                        String valueString = isLastDate ? "No End Date" : value.toString();
                        sb.append(valueString);
                        sb.append(" || ");
                    }
                    sb.delete(sb.length() - 4, sb.length());
                    sb.append("\n");

                    JLabel txt = new JLabel(sb.toString());
                    txt.setAlignmentX(JComponent.CENTER_ALIGNMENT);
                    valueHistoryBox.add(txt);

                    JLabel spacer = new JLabel("-".repeat(SpacerXSize) + "\n");
                    spacer.setAlignmentX(JComponent.CENTER_ALIGNMENT);
                    valueHistoryBox.add(spacer);
                }

                scrollPane.setViewportView(valueHistoryBox);
                pnlFixed.add(scrollPane);

                return pnlFixed;

            case ONETIME:
                lbl = new JLabel("Has been paid?");
                lbl.setAlignmentX(JLabel.RIGHT_ALIGNMENT);
                pnlTypeSpecific.add(lbl);

                oneTimeCheckBox = new JCheckBox();
                oneTimeCheckBox.setBackground(ACCENT_COLOR);
                oneTimeCheckBox.setEnabled(false);
                oneTimeCheckBox.setSelected(((OneTime) exp).isPaid());
                return oneTimeCheckBox;

            case RECURRING:
                lbl = new JLabel("End date: ");
                lbl.setAlignmentX(JLabel.RIGHT_ALIGNMENT);
                pnlTypeSpecific.add(lbl);

                recurringEndDateField = new JFormattedTextField(Dates.DATE_FORMATTER);

                Date dateEnd = Dates.convLocalDate(((Recurring) exp).getEndDate());

                recurringEndDateField.setValue(dateEnd);

                Components.defineSize(recurringEndDateField, DEFAULT_FIELD_SIZE);

                recurringEndDateField.setEnabled(true);
                recurringEndDateField.setBackground(ACCENT_COLOR);
                recurringEndDateField.setEditable(false);

                return recurringEndDateField;

            default:
                return null;
        }
    }

    private void removeExpense(ActionEvent action) {
        Expense selectedExpense = (Expense) expensesComboBox.getSelectedItem();
        if (selectedExpense == null)
            return;

        expensesManager.removeExpense(selectedExpense);
        expensesComboBox.removeItem(selectedExpense);
        expensesComboBox.setSelectedIndex(-1);

        isEditing = false;
        setFieldsEditable(false);
        btnChangeExpense.setText("Edit Expense");
        expensesComboBox.setEnabled(true);

        createBillFields(null);
    }

    private void changeExpense(ActionEvent action) {
        Alerts.clearMessage(alertPanel);

        Expense originalExpense = (Expense) expensesComboBox.getSelectedItem();
        if (originalExpense == null) {
            Alerts.errorMessage(alertPanel, "No expense selected!");
            return;
        }

        if (isEditing) {
            boolean success = applyChanges(originalExpense);
            if (!success)
                return;
        }

        setFieldsEditable(!isEditing);
        btnChangeExpense.setText(isEditing ? "Edit Expense" : "Apply Changes");
        expensesComboBox.setEnabled(isEditing);
        isEditing = !isEditing;
    }

    private void setFieldsEditable(boolean isEditable) {
        for (BillsFields field : BillsFields.values()) {
            if (field == BillsFields.TYPE)
                continue;

            if (field == BillsFields.ESSENTIAL) {
                fields[field.ordinal()].setEnabled(isEditable);
                continue;
            }

            ((JTextField) fields[field.ordinal()]).setEditable(isEditable);
        }
        ExpenseType type = ((Expense) expensesComboBox.getSelectedItem()).getType();
        switch (type) {
            case ONETIME:
                oneTimeCheckBox.setEnabled(isEditable);
                break;
            case RECURRING:
                recurringEndDateField.setEditable(isEditable);
                break;
            case FIXED:
                break;
            default:
                break;
        }
    }

    private boolean applyChanges(Expense originalExpense) {
        ExpenseType type = originalExpense.getType();
        Expense exp = null;

        JFormattedTextField valueField = (JFormattedTextField) fields[BillsFields.VALUE.ordinal()];
        Alerts.clearBorder(valueField);

        double value = 0;
        try {
            value = NumberFormat.getCurrencyInstance().parse(valueField.getText()).doubleValue();
        } catch (Exception e) {
            return false;
        }
        if (value <= 0) {
            Alerts.errorMessage(alertPanel, "Value must be greater than 0!");
            Alerts.errorBorder(valueField);
            return false;
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
            return false;
        }

        switch (type) {
            case FIXED:
                Fixed fixed = (Fixed) originalExpense;
                if (!fixed.getLastDate().isEqual(date)) {
                    if (fixed.getLastDate().isAfter(date)) {
                        Alerts.errorMessage(alertPanel, "Date cannot be before last update!");
                        Alerts.errorBorder(dateField);
                        return false;
                    }
                    fixed.updateValue(value, date);
                }
                fixed.setEssential(essential);
                fixed.setDescription(description);
                exp = fixed;
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

        if (exp != null) {
            Alerts.showMessage(alertPanel, "Expense Modified successfully!", BACKGROUND_COLOR);
            if (originalExpense != exp) {
                expensesManager.removeExpense(originalExpense);
                expensesManager.addExpense(exp);
            }
            refresh(exp);
            return true;
        } else {
            Alerts.errorMessage(alertPanel, "Expense could not be modified!");
            return false;
        }
    }
}
