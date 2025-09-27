import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TaskComponent extends JPanel implements ActionListener {
    private JCheckBox taskCheckBox;
    private JTextPane taskField;
    private JButton deleteButton;
    private JComboBox<String> dueComboBox;

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

        String[] dueTime = new String[96];
        for (int i = 0; i < 24; i++) {
            for (int j = 0; j < 60; j += 15) {
                String time = String.format("%02d:%02d", i, j);
                dueTime[i * 4 + j / 15] = time;
            }
        }

        dueComboBox = new JComboBox<>(dueTime);
        dueComboBox.setPreferredSize(CommonConfig.DUE_COMBOBOX_SIZE);
        dueComboBox.setSelectedIndex(-1);
        dueComboBox.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        topPanel.add(taskField);
        topPanel.add(taskCheckBox);
        topPanel.add(deleteButton);

        bottomPanel.add(dueComboBox);

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
    }
}
