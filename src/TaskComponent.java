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

    private Point mouseOffset;
    private int originalIndex;
    private boolean isDragging = false;

    public JTextPane getTaskField() {
        return taskField;
    }

    public JCheckBox getTaskCheckBox() {
        return taskCheckBox;
    }

    public JComboBox<String> getDueComboBox() {
        return dueComboBox;
    }

    public JLabel getDueTrackerLabel() {
        return dueTrackerLabel;
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
        deleteButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
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

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mouseOffset = e.getPoint();
                originalIndex = parentPanel.getComponentZOrder(TaskComponent.this);
                isDragging = true;
                setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                isDragging = false;
                setBorder(null);
                revalidate();
                repaint();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (!isDragging) return;

                // Calculate the center point of the dragged component
                int mouseY = SwingUtilities.convertPoint(TaskComponent.this, e.getPoint(), parentPanel).y;

                // Find the new index based on the Y position
                int newY = getTargetIndex(mouseY);

                if (newY >= 0 && newY < parentPanel.getComponentCount()
                        && newY != getComponentIndex()) {
                    // Move component to new position
                    parentPanel.remove(TaskComponent.this);
                    parentPanel.add(TaskComponent.this, newY);
                    parentPanel.revalidate();
                    parentPanel.repaint();
                }
            }
        });

        taskField.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                e.consume(); // Prevent dragging when clicking inside the text field
            }
        });

        setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
    }

    private int getComponentIndex() {
        Container parent = getParent();
        for (int i = 0; i < parent.getComponentCount(); i++) {
            if (parent.getComponent(i) == this) {
                return i;
            }
        }
        return -1;
    }

    private int getTargetIndex(int y) {
        Container parent = getParent();
        int count = parent.getComponentCount();

        for (int i = 0; i < count; i++) {
            Component comp = parent.getComponent(i);
            int compMiddle = comp.getY() + comp.getHeight() / 2;
            if (y < compMiddle) {
                return i;
            }
        }
        return count - 1;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (taskCheckBox.isSelected()) {
            String taskText = taskField.getText().replaceAll("<[^>]*>", "");
            taskField.setText("<strike>" + taskText + "</strike>");
            dueTrackerLabel.setText("Completed!");
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
            if (!taskCheckBox.isSelected()) {
                taskField.setBackground(null);
                dueTrackerLabel.setText("");
            } else {
                taskField.setBackground(null);
                dueTrackerLabel.setText("Completed!");
            }
            return;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime currentTime = LocalTime.now();
        if (selectedDueTime != null) {
            LocalTime selectedTime = LocalTime.parse(selectedDueTime, formatter);
            if (selectedTime.isBefore(currentTime) && !taskCheckBox.isSelected()) {
                taskField.setBackground(Color.PINK);
                dueTrackerLabel.setText("Overdue!");
            } else if (selectedTime.isAfter(currentTime) && !taskCheckBox.isSelected()) {
                taskField.setBackground(null);
                dueTrackerLabel.setText("Due at " + selectedDueTime);
            }

            if (taskCheckBox.isSelected()) {
                taskField.setBackground(null);
                dueTrackerLabel.setText("Completed!");
            }
        }
    }
}
