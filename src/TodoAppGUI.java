import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TodoAppGUI extends JFrame implements ActionListener {
    private JPanel taskPanel, taskComponentPanel;

    public TodoAppGUI() {
        super("Todo-List Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(CommonConfig.GUI_SIZE);
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(null);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveTasksBeforeExit();
                System.exit(0);
            }
        });

        addGUIComponents();
        loadTasksOnStart();
    }

    private void addGUIComponents() {
        JLabel bannerLabel = new JLabel("Todo-List");
        bannerLabel.setBounds((CommonConfig.GUI_SIZE.width - bannerLabel.getPreferredSize().width) / 2, 15,
                CommonConfig.BANNER_SIZE.width, CommonConfig.BANNER_SIZE.height);

        taskPanel = new JPanel();

        taskComponentPanel = new JPanel();
        taskComponentPanel.setLayout(new BoxLayout(taskComponentPanel, BoxLayout.Y_AXIS));
        taskPanel.add(taskComponentPanel);

        JScrollPane scrollPane = new JScrollPane(taskPanel);
        scrollPane.setBounds(8, 70, CommonConfig.TASKPANEL_SIZE.width, CommonConfig.TASKPANEL_SIZE.height);
        scrollPane.setBorder(BorderFactory.createLoweredBevelBorder());
        scrollPane.setMaximumSize(CommonConfig.TASKPANEL_SIZE);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setUnitIncrement(20);

        JButton addTaskButton = new JButton("Add Task");
        addTaskButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        addTaskButton.setBounds(-5, CommonConfig.GUI_SIZE.height - 150,
                CommonConfig.ADDTASK_BUTTON_SIZE.width, CommonConfig.ADDTASK_BUTTON_SIZE.height);
        addTaskButton.addActionListener(this);

        JButton saveButton = new JButton("Save Tasks");
        saveButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        saveButton.setBounds(-5, CommonConfig.GUI_SIZE.height - 88,
                CommonConfig.SAVEBUTTON_SIZE.width, CommonConfig.SAVEBUTTON_SIZE.height);
        saveButton.addActionListener(this);

        this.getContentPane().add(bannerLabel);
        this.getContentPane().add(scrollPane);
        this.getContentPane().add(addTaskButton);
        this.getContentPane().add(saveButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equalsIgnoreCase("Add Task")) {
            TaskComponent taskComponent = new TaskComponent(taskComponentPanel);
            taskComponentPanel.add(taskComponent);
            taskComponent.getTaskField().requestFocus();
            repaint();
            revalidate();
        } else if (command.equalsIgnoreCase("Save Tasks")) {
            TaskSaveManager.saveTasks(taskComponentPanel);
        }
    }

    private void loadTasksOnStart() {
        TaskSaveManager.loadTasks(taskComponentPanel);
    }

    private void saveTasksBeforeExit() {
        int result = JOptionPane.showConfirmDialog(
                this,
                "Would you like to save your tasks before exiting?",
                "Save Tasks",
                JOptionPane.YES_NO_CANCEL_OPTION
        );

        if (result == JOptionPane.YES_OPTION) {
            TaskSaveManager.saveTasks(taskComponentPanel);
        } else if (result == JOptionPane.CANCEL_OPTION) {
            setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        }

        System.exit(0);
    }
}
