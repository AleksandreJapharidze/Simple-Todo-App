public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            TodoAppGUI app = new TodoAppGUI();
            app.setVisible(true);
        });
    }
}
