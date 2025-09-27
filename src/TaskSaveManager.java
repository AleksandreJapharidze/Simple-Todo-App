import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.file.Files;

public class TaskSaveManager {
    private static final String DATA_DIRECTORY = "todoapp_data";
    private static final String SAVE_FILE = "tasks.json";

    private static File getSaveFile() {
        File dataDir = new File(DATA_DIRECTORY);
        if (!dataDir.exists()) {
            dataDir.mkdirs(); // Create the directory
        }
        return new File(dataDir, SAVE_FILE);
    }

    public static void saveTasks(JPanel taskComponentPanel) {
        try {
            JSONArray tasksArray = new JSONArray();
            Component[] components = taskComponentPanel.getComponents();
            for (Component component : components) {
                if (component instanceof TaskComponent) {
                    TaskComponent taskComponent = (TaskComponent) component;

                    JSONObject taskObject = new JSONObject();
                    taskObject.put("text", taskComponent.getTaskField().getText());
                    taskObject.put("completed", (taskComponent.getTaskCheckBox()).isSelected());

                    tasksArray.put(taskObject);
                }
            }

            File saveFile = getSaveFile();
            Files.write(saveFile.toPath(), tasksArray.toString(4).getBytes());
            System.out.println("Tasks saved to " + saveFile.getAbsolutePath() + " successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error saving tasks: " + e.getMessage());
        }
    }

    public static void loadTasks(JPanel taskComponentPanel) {
        try {
            File saveFile = getSaveFile();
            if (!saveFile.exists()) {
                return;
            }

            String content = new String(Files.readAllBytes(saveFile.toPath()));
            JSONArray tasksArray = new JSONArray(content);

            taskComponentPanel.removeAll();

            for (int i = 0; i < tasksArray.length(); i++) {
                JSONObject taskObject = tasksArray.getJSONObject(i);

                TaskComponent taskComponent = new TaskComponent(taskComponentPanel);
                taskComponentPanel.add(taskComponent);
                taskComponent.getTaskField().setText(taskObject.getString("text"));

                JCheckBox checkBox = taskComponent.getTaskCheckBox();
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
