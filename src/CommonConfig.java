import java.awt.*;

public class CommonConfig {
    public static final Dimension GUI_SIZE = new Dimension(500, 800);
    public static final Dimension BANNER_SIZE = new Dimension(GUI_SIZE.width, 50);
    public static final Dimension TASKPANEL_SIZE = new Dimension(GUI_SIZE.width - 30, GUI_SIZE.height - 240);
    public static final Dimension ADDTASK_BUTTON_SIZE = new Dimension(GUI_SIZE.width, 50);
    public static final Dimension TASKFIELD_SIZE = new Dimension((int) (TASKPANEL_SIZE.width * 0.80), 50);
    public static final Dimension CHECKBOX_SIZE = new Dimension((int) (TASKFIELD_SIZE.width * 0.05), 50);
    public static final Dimension DELETEBUTTON_SIZE = new Dimension((int) (TASKFIELD_SIZE.width * 0.12), 50);
    public static final Dimension DUE_COMBOBOX_SIZE = new Dimension((int) (TASKFIELD_SIZE.width * 0.20), 25);
    public static final Dimension SAVEBUTTON_SIZE = new Dimension(GUI_SIZE.width, 50);
}
