import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.Calendar;
import java.util.Formatter;

public class ClipboardTool {
    public static void main (String[] args) throws Exception {
        Calendar calendar;
        Formatter format = new Formatter();
        int time;

        StringSelection stringSelection;
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        while (true) {
            calendar = Calendar.getInstance();
            format.format("%tM", calendar);
            time = Integer.parseInt(format.toString());
            stringSelection = new StringSelection(((2 * time) + 3) + "");
            System.out.println("Clipboard <- " + (2 * time + 3));
            clipboard.setContents(stringSelection, stringSelection);
            Thread.sleep(500);
            format = new Formatter();
        }
    }
    
}
