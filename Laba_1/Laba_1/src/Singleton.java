import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import com.google.gson.Gson;

public class Singleton {

    static List<Object> list = new ArrayList<>();
    static ComboBoxUpdateListener listener;

    public static void setUpdateListener(ComboBoxUpdateListener listener) {
        Singleton.listener = listener;
    }

    static void Generation() {
        int randomInt = (int) (Math.random() * 3);
        switch (randomInt) {
            case 0:
                list.add(new Circl());
                break;
            case 1:
                list.add(new Square());
                break;
            case 2:
                list.add(new Triangle());
                break;
            default:
                list.add(new Triangle());
                break;
        }

        if (listener != null) {
            listener.onComboBoxUpdateNeeded();
        }
    }

    static String ObjectToJSON(Object item) {
        Gson gson = new Gson();
        return item.getClass().getName() + "\n === \n" + gson.toJson(item);
    }

    static void removeEl(Object selectedObject) {
        synchronized (Singleton.list) {
            if (list.remove(selectedObject)) {
                if (listener != null) {
                    listener.onComboBoxUpdateNeeded();
                }
            }
        }
    }


    static void addEl(Object selectedObject) {
        synchronized (Singleton.list) {
            if (Singleton.list.add(selectedObject)) {
                if (listener != null) {
                    listener.onComboBoxUpdateNeeded();
                }
            }
        }
    }
}
