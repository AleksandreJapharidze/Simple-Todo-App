import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TaskSaveManager {
    private static final String SAVE_FILE = "tasks.json";

    public static void saveTasks(JPanel taskComponentPanel) {
        try {
            JSONArray tasksArray = new JSONArray();
            Component[] components = taskComponentPanel.getComponents();
            for (Component component : components) {
                if (component instanceof TaskComponent) {
                    TaskComponent taskComponent = (TaskComponent) component;

                    JSONObject taskObject = new JSONObject();
                    taskObject.put("text", taskComponent.getTaskField().getText());
                    taskObject.put("completed", ((JCheckBox) taskComponent.getComponents()[1]).isSelected());

                    tasksArray.put(taskObject);
                }
            }

            Files.write(Paths.get(SAVE_FILE), tasksArray.toString(4).getBytes());
            System.out.println("Tasks saved successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error saving tasks: " + e.getMessage());
        }
    }

    public static void loadTasks(JPanel taskComponentPanel) {
        try {
            if (!Files.exists(Paths.get(SAVE_FILE))) {
                return;
            }

            String content = new String(Files.readAllBytes(Paths.get(SAVE_FILE)));
            JSONArray tasksArray = new JSONArray(content);

            taskComponentPanel.removeAll();

            for (int i = 0; i < tasksArray.length(); i++) {
                JSONObject taskObject = tasksArray.getJSONObject(i);

                TaskComponent taskComponent = new TaskComponent(taskComponentPanel);
                taskComponentPanel.add(taskComponent);
                taskComponent.getTaskField().setText(taskObject.getString("text"));

                JCheckBox checkBox = (JCheckBox) taskComponent.getComponents()[1];
                checkBox.setSelected(taskObject.getBoolean("completed"));
                if (checkBox.isSelected()) {
                    String taskText = taskComponent.getTaskField().getText().replaceAll("<[^>]*>", "");
                    taskComponent.getTaskField().setText("<strike>" + taskText + "</strike>");
                }
            }

            taskComponentPanel.revalidate();
            taskComponentPanel.repaint();
            System.out.println("Tasks loaded successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error loading tasks: " + e.getMessage());
        }
    }
}
