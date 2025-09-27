import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TaskComponent extends JPanel implements ActionListener {
    private JCheckBox taskCheckBox;
    private JTextPane taskField;
    private JButton deleteButton;
    private JComboBox<String> dueComboBox;
    private JLabel dueTrackerLabel;

    public JTextPane getTaskField() {
        return taskField;
    }

    public JCheckBox getTaskCheckBox() {
        return taskCheckBox;
    }

    public JComboBox<String> getDueComboBox() {
        return dueComboBox;
    }

    private JPanel parentPanel;

    public TaskComponent(JPanel parentPanel) {
        this.parentPanel = parentPanel;
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        taskField = new JTextPane();
        taskField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        taskField.setFont(new Font("Arial", Font.BOLD, 30));
        taskField.setPreferredSize(CommonConfig.TASKFIELD_SIZE);
        taskField.setContentType("text/html");
        taskField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                taskField.setBackground(Color.WHITE);
            }

            @Override
            public void focusLost(FocusEvent e) {
                taskField.setBackground(null);
            }
        });

        taskCheckBox = new JCheckBox();
        taskCheckBox.setPreferredSize(CommonConfig.CHECKBOX_SIZE);
        taskCheckBox.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        taskCheckBox.addActionListener(this);

        deleteButton = new JButton("X");
        deleteButton.setPreferredSize(CommonConfig.DELETEBUTTON_SIZE);
        deleteButton.addActionListener(this);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        String[] dueTime = new String[97];
        dueTime[0] = "No Deadline";
        for (int i = 0; i < 24; i++) {
            for (int j = 0; j < 60; j += 15) {
                String time = String.format("%02d:%02d", i, j);
                dueTime[i * 4 + j / 15 + 1] = time;
            }
        }

        dueComboBox = new JComboBox<>(dueTime);
        dueComboBox.setPreferredSize(CommonConfig.DUE_COMBOBOX_SIZE);
        dueComboBox.setSelectedIndex(0);
        dueComboBox.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        dueComboBox.addActionListener(this);

        dueTrackerLabel = new JLabel();
        dueTrackerLabel.setPreferredSize(CommonConfig.DUE_COMBOBOX_SIZE);
        dueTrackerLabel.setFont(new Font("Arial", Font.BOLD, 16));

        topPanel.add(taskField);
        topPanel.add(taskCheckBox);
        topPanel.add(deleteButton);

        bottomPanel.add(dueComboBox);
        bottomPanel.add(dueTrackerLabel);

        add(topPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (taskCheckBox.isSelected()) {
            String taskText = taskField.getText().replaceAll("<[^>]*>", "");
            taskField.setText("<strike>" + taskText + "</strike>");
        } else if (!taskCheckBox.isSelected()) {
            String taskText = taskField.getText().replaceAll("<[^>]*>", "");
            taskField.setText(taskText);
        }

        if (e.getActionCommand().equalsIgnoreCase("X")) {
            parentPanel.remove(this);
            parentPanel.repaint();
            parentPanel.revalidate();
        }

        String selectedDueTime = (String) dueComboBox.getSelectedItem();
        if (selectedDueTime.equalsIgnoreCase("No Deadline")) {
            taskField.setBackground(null);
            dueTrackerLabel.setText("");
            return;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime currentTime = LocalTime.now();
        if (selectedDueTime != null) {
            LocalTime selectedTime = LocalTime.parse(selectedDueTime, formatter);
            if (selectedTime.isBefore(currentTime) && !taskCheckBox.isSelected()) {
                taskField.setBackground(Color.PINK);
                dueTrackerLabel.setText("Overdue!");
            } else {
                taskField.setBackground(null);
                dueTrackerLabel.setText("Due at " + selectedDueTime);
            }
        }
    }
}
