import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MtpFrame extends JFrame {
    class MtpActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource().equals(addBtn))
                doClickAdd();
            else if (e.getSource().equals(searchBtn))
                doClickSearch();
        }
    }

    private JTable table;
    private JButton addBtn, searchBtn;
    private JTextField mtpField, contField, popField;
    private JComboBox<String> popBox, matchBox;
    private boolean addMode;

    public MtpFrame() {
        super("Metropolises Viewer");
        setLayout(new BorderLayout(4, 4));
        addComponents();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
        addMode = false;
    }

    private void doClickSearch() {
        addMode = false;
        String[] criteria = {mtpField.getText(), contField.getText(), popField.getText()};
        searchQuery(criteria);
    }

    private void searchQuery(String[] criteria) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM metropolises WHERE ");

        boolean partialMatch = matchBox.getSelectedIndex() == 1;
        char[] operators = {'>', '<', '='};
        char operator = operators[popBox.getSelectedIndex()];

        if (addMode) {
            partialMatch = false;
            operator = '=';
        }

        for (int i = 0; i < 3; i++) {
            sb.append(table.getColumnName(i).toLowerCase());
            if (i == 2) {
                if (criteria[i].isEmpty()) {
                    sb.append(" LIKE '%'");
                } else {
                    sb.append(operator);
                    sb.append(criteria[i]);
                }
            } else {
                if (criteria[i].isEmpty()) {
                    sb.append(" LIKE '%' AND ");
                } else {
                    sb.append(" LIKE '");
                    if (partialMatch)
                        sb.append('%');
                    sb.append(criteria[i]);
                    if (partialMatch)
                        sb.append('%');
                    sb.append("' AND ");
                }
            }
        }

        MtpTableModel model =(MtpTableModel)table.getModel();
        model.executeQuery(sb.toString());
    }


    private void doClickAdd() {
        String[] values = {mtpField.getText(), contField.getText(), popField.getText()};
        insertQuery(values);
        addMode = true;
        searchQuery(values);
    }

    private void insertQuery(String[] values) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO metropolises VALUES (");
        for (int i = 0; i < values.length; i++) {
            if (i != 2)
                sb.append("'");
            sb.append(values[i]);
            if (i != 2)
                sb.append("'");
            if (i != values.length - 1)
                sb.append(", ");
        }
        sb.append(")");
        MtpTableModel model =(MtpTableModel)table.getModel();
        model.executeUpdate(sb.toString());
    }

    private void addComponents() {
        addFields();
        addButtons();
        addTable();
        addSearchOptions();
    }

    private void addSearchOptions() {
        JPanel panel = new JPanel();
        String[] popOptions = { "Population greater than", "Population less than", "Population equal to"};
        popBox = new JComboBox<>(popOptions);

        String[] matchOptions = {"Exact match", "Partial match"};
        matchBox = new JComboBox<>(matchOptions);
        panel.add(popBox);
        panel.add(matchBox);
        panel.setBorder(new TitledBorder("Search Options"));
        getContentPane().add(panel, BorderLayout.SOUTH);
    }

    private void addTable() {
        table = new JTable(new MtpTableModel());
        table.getTableHeader().setBackground(Color.LIGHT_GRAY);
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setPreferredSize(new Dimension(500,400));
        scrollPane.setViewportView(table);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
    }

    private void addButtons() {
        MtpActionListener listener = new MtpActionListener();
        JPanel buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.PAGE_AXIS));
        addBtn = new JButton("Add");
        addBtn.addActionListener(listener);
        searchBtn = new JButton("Search");
        searchBtn.addActionListener(listener);
        buttons.add(addBtn);
        buttons.add(searchBtn);
        getContentPane().add(buttons, BorderLayout.EAST);
    }

    private void addFields() {
        JPanel fields = new JPanel();
        JLabel mtpLabel = new JLabel("Metropolis:");
        mtpField = new JTextField(10);
        JLabel contLabel = new JLabel("Continent:");
        contField = new JTextField(10);
        JLabel popLabel = new JLabel("Population:");
        popField = new JTextField(10);
        fields.add(mtpLabel);
        fields.add(mtpField);
        fields.add(contLabel);
        fields.add(contField);
        fields.add(popLabel);
        fields.add(popField);
        getContentPane().add(fields, BorderLayout.NORTH);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) { }

        MtpFrame frame = new MtpFrame();
    }
}
