import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TaskComponent extends JPanel implements ActionListener {
    private JCheckBox taskCheckBox;
    private JTextPane taskField;
    private JButton deleteButton;

    public JTextPane getTaskField() {
        return taskField;
    }

    private JPanel parentPanel;

    public TaskComponent(JPanel parentPanel) {
        this.parentPanel = parentPanel;

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

        add(taskField);
        add(taskCheckBox);
        add(deleteButton);
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
