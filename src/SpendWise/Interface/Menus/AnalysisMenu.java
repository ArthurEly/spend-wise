package SpendWise.Interface.Menus;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Date;
import java.util.Set;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.style.PieStyler.LabelType;
import org.knowm.xchart.style.Styler.ChartTheme;

import SpendWise.Interface.Screen;
import SpendWise.Logic.Group;
import SpendWise.Logic.User;
import SpendWise.Logic.Bills.Expense;
import SpendWise.Logic.Managers.ExpensesManager;
import SpendWise.Logic.Managers.GroupManager;
import SpendWise.Utils.Dates;
import SpendWise.Utils.Offsets;
import SpendWise.Utils.Enums.AnalysisType;
import SpendWise.Utils.Enums.AnalysisUserChoice;
import SpendWise.Utils.Enums.ExpenseType;
import SpendWise.Utils.Enums.PanelOrder;
import SpendWise.Utils.Graphics.Components;
import SpendWise.Utils.Graphics.Panels;

public class AnalysisMenu extends Screen {
    private JPanel pnlChartsMenu;
    private JComboBox<AnalysisType> cmbAnalysisType;
    private static final Dimension comboBoxSize = new Dimension(450, 30);
    private User user;

    public AnalysisMenu(User user) {
        this.user = user;
        initialize();
    }

    @Override
    protected void initialize() {
        blankPanels = Panels.createPanelWithCenter(this, ACCENT_COLOR);

        JPanel comboBoxPanel = (JPanel) this.getComponent(0);
        Offsets comboBoxOffsets = new Offsets(0, 0, 0, 0);
        JPanel comboBoxCenter = Panels.createPanelWithCenter(comboBoxPanel, comboBoxOffsets,
                BACKGROUND_COLOR)[PanelOrder.CENTRAL
                        .ordinal()];

        cmbAnalysisType = new JComboBox<AnalysisType>(AnalysisType.values());
        cmbAnalysisType.addActionListener(e -> changeChart(e));
        Components.defineSize(comboBoxCenter, comboBoxSize);
        comboBoxCenter.add(cmbAnalysisType);

        pnlChartsMenu = super.getBlankPanel(PanelOrder.CENTRAL);

        changeChart(null);
    }

    private void changeChart(ActionEvent action) {
        AnalysisType analysisType = (AnalysisType) cmbAnalysisType.getSelectedItem();
        switch (analysisType) {
            case USER:
                createUserScreen();
                break;
            case GROUP:
                createGroupScreen();
                break;
            case YEAR:
                createYearlyScreen();
                break;
        }
    }

    @Override
    public void refresh() {
        changeChart(null);
        super.refresh();
    }

    private String makeLabel(Expense exp, double value) {
        return exp.getDescription() + " R$" + exp.getValue(LocalDate.now());
    }

    private void createUserScreen() {
        JPanel userChangePanel = super.getBlankPanel(PanelOrder.NORTH);
        userChangePanel.removeAll();

        JFormattedTextField dateField = new JFormattedTextField(Dates.MONTH_AND_YEAR_FORMATTER);
        Date defaultDate = Dates.convLocalDate(LocalDate.now());
        dateField.setValue(defaultDate);
        dateField.setHorizontalAlignment(JFormattedTextField.CENTER);
        Components.defineSize(dateField, new Dimension(100, 25));
        userChangePanel.add(dateField);

        JCheckBox chkFixed = new JCheckBox("Fixed");
        chkFixed.setSelected(true);
        chkFixed.setBackground(ACCENT_COLOR);
        userChangePanel.add(chkFixed);

        JCheckBox chkRecurring = new JCheckBox("Recurring");
        chkRecurring.setSelected(true);
        chkRecurring.setBackground(ACCENT_COLOR);
        userChangePanel.add(chkRecurring);

        JCheckBox chkOneTime = new JCheckBox("One Time");
        chkOneTime.setSelected(true);
        chkOneTime.setBackground(ACCENT_COLOR);
        userChangePanel.add(chkOneTime);

        JComboBox<AnalysisUserChoice> cmbEssential = new JComboBox<AnalysisUserChoice>(
                AnalysisUserChoice.values());
        userChangePanel.add(cmbEssential);


        ActionListener action = e -> {
            createUserChart(Dates.convDate((Date) dateField.getValue()), chkFixed.isSelected(),
                    chkRecurring.isSelected(), chkOneTime.isSelected(),
                    (AnalysisUserChoice) cmbEssential.getSelectedItem());
        };

        dateField.addActionListener(action);
        chkFixed.addActionListener(action);
        chkRecurring.addActionListener(action);
        chkOneTime.addActionListener(action);
        cmbEssential.addActionListener(action);

        action.actionPerformed(null);

        Components.refresh(userChangePanel);
    }

    private void createGroupScreen() {
        JPanel groupChangePanel = super.getBlankPanel(PanelOrder.NORTH);
        groupChangePanel.removeAll();

        GroupManager groupManager = user.getGroupManager();
        JComboBox<Group> cmbGroups = new JComboBox<Group>(groupManager.getGroups().toArray(new Group[0]));
        cmbGroups.addActionListener(e -> createGroupChart((Group) cmbGroups.getSelectedItem()));
        groupChangePanel.add(cmbGroups);

        createGroupChart((Group) cmbGroups.getSelectedItem());
        Components.refresh(groupChangePanel);
    }

    private void createYearlyScreen() {
        JPanel yearChangePanel = super.getBlankPanel(PanelOrder.NORTH);
        yearChangePanel.removeAll();

        JFormattedTextField dateField = new JFormattedTextField(Dates.YEAR_FORMATTER);
        Date defaultDate = Dates.convLocalDate(LocalDate.now());
        dateField.addActionListener(e -> createYearChart(Dates.convDate((Date) dateField.getValue())));
        dateField.setValue(defaultDate);

        yearChangePanel.add(dateField);

        createYearChart(Dates.convDate((Date) dateField.getValue()));
        Components.refresh(yearChangePanel);
    }

    private PieChart buildPieChart(String name) {

        PieChart pieChart = new PieChartBuilder().width(500).height(300).theme(ChartTheme.GGPlot2).build();
        pieChart.getStyler().setChartBackgroundColor(ACCENT_COLOR);
        pieChart.getStyler().setPlotBackgroundColor(ACCENT_COLOR);
        pieChart.getStyler().setLegendBackgroundColor(ACCENT_COLOR);
        pieChart.getStyler().setChartTitleBoxBackgroundColor(ACCENT_COLOR);
        pieChart.getStyler().setChartTitleBoxBorderColor(ACCENT_COLOR);
        pieChart.getStyler().setChartTitleBoxVisible(false);

        pieChart.setTitle("Expenses for " + name);

        pieChart.getStyler().setPlotBorderVisible(false);

        pieChart.getStyler().setPlotContentSize(0.7);

        pieChart.getStyler().setLabelType(LabelType.Percentage);
        pieChart.getStyler().setLabelsDistance(1.15);

        pieChart.getStyler().setLegendSeriesLineLength(1);

        return pieChart;
    }

    private void addPieChart(JPanel panel, PieChart chart) {
        JPanel chartPanel = new XChartPanel<PieChart>(chart);
        panel.removeAll();
        panel.add(chartPanel);
        Components.refresh(panel);
    }

    private void showTotal(JPanel panel, double total) {
        panel.removeAll();
        panel.setAlignmentX(CENTER_ALIGNMENT);
        JLabel lblTotal = new JLabel("Total: R$" + total);
        panel.add(lblTotal);
    }

    private void createUserChart(LocalDate date, boolean fixed, boolean recurring, boolean oneTime,
            AnalysisUserChoice choice) {
        date = Dates.monthStart(date).plusDays(1);
        ExpensesManager expManager = user.getExpensesManager();
        Set<Expense> monthExpenses = expManager.getMonthExpenses(date);

        String chartTitle = user.getName() + " in " + Dates.MONTH_AND_YEAR_FORMATTER.format(Dates.convLocalDate(date));
        PieChart pieChart = buildPieChart(chartTitle);

        double total = 0;
        for (Expense exp : monthExpenses) {
            if (choice == AnalysisUserChoice.ESSENTIAL && !exp.isEssential()) {
                continue;
            } else if (choice == AnalysisUserChoice.NONESSENTIAL && exp.isEssential()) {
                continue;
            }

            ExpenseType type = exp.getType();
            double value = exp.getValue(date);
            String description = makeLabel(exp, value);

            switch (type) {
                case FIXED:
                    if (fixed) {
                        total += value;
                        pieChart.addSeries(description, value);
                    }
                    break;
                case RECURRING:
                    if (recurring) {
                        total += value;
                        pieChart.addSeries(description, value);
                    }
                    break;
                case ONETIME:
                    if (oneTime) {
                        total += value;
                        pieChart.addSeries(description, value);
                    }
                    break;
            }
        }

        javax.swing.SwingUtilities.invokeLater(() -> addPieChart(pnlChartsMenu, pieChart));

        JPanel infoPanel = super.getBlankPanel(PanelOrder.SOUTH);
        showTotal(infoPanel, total);
    }

    private void createGroupChart(Group group) {
        JPanel infoPanel = super.getBlankPanel(PanelOrder.SOUTH);
        if (group == null) {
            pnlChartsMenu.removeAll();
            pnlChartsMenu.add(new JLabel("No group selected"));
            showTotal(infoPanel, 0);
            Components.refresh(pnlChartsMenu);
            return;
        }

        PieChart pieChart = buildPieChart(group.getGroupName());

        double total = 0;
        for (User user : group.getUsers()) {
            ExpensesManager expManager = user.getExpensesManager();
            Set<Expense> monthExpenses = expManager.getMonthExpenses(LocalDate.now());

            double userTotal = 0;
            for (Expense exp : monthExpenses) {
                double value = exp.getValue(LocalDate.now());
                userTotal += value;
            }
            if (userTotal == 0) {
                continue;
            }

            total += userTotal;
            pieChart.addSeries(user.getName(), userTotal);
        }

        javax.swing.SwingUtilities.invokeLater(() -> addPieChart(pnlChartsMenu, pieChart));

        showTotal(infoPanel, total);
    }

    private void createYearChart(LocalDate date) {
        ExpensesManager expManager = user.getExpensesManager();

        PieChart pieChart = buildPieChart(Dates.YEAR_FORMATTER.format(Dates.convLocalDate(date)));

        double total = 0;

        for (LocalDate month = Dates.yearStart(date); month
                .isBefore(Dates.yearEnd(date)); month = Dates.nextMonth(month)) {
            Set<Expense> monthExpenses = expManager.getMonthExpenses(month);

            double monthTotal = 0;
            for (Expense exp : monthExpenses) {
                monthTotal += exp.getValue(month);
            }

            if (monthTotal == 0) {
                continue;
            }

            String monthName = month.getMonth().getDisplayName(TextStyle.SHORT, getLocale());

            pieChart.addSeries(monthName, monthTotal);

            total += monthTotal;
        }

        javax.swing.SwingUtilities.invokeLater(() -> addPieChart(pnlChartsMenu, pieChart));

        JPanel infoPanel = super.getBlankPanel(PanelOrder.SOUTH);
        showTotal(infoPanel, total);
    }

}
