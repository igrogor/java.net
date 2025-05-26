import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;

public class Singleton {
  static List<Object> list = new ArrayList<>();
  static List<String> listPerson = new ArrayList<>(); // Теперь список строковых идентификаторов
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
    synchronized (list) {
      if (list.remove(selectedObject)) {
        if (listener != null) {
          listener.onComboBoxUpdateNeeded();
        }
      }
    }
  }

  static void addEl(Object selectedObject) {
    synchronized (list) {
      if (list.add(selectedObject)) {
        if (listener != null) {
          listener.onComboBoxUpdateNeeded();
        }
      }
    }
  }

  static void removeAllPerson() {
    synchronized (listPerson) {
      listPerson.clear();
      if (listener != null) {
        listener.onComboBoxUpdateNeeded();
      }
    }
  }

  // Singleton.java
  static void addPerson(String id) {
    synchronized (listPerson) {
      if (!listPerson.contains(id)) { // Проверка на дубликаты
        listPerson.add(id);
        if (listener != null) {
          listener.onComboBoxUpdateNeeded();
        }
      }
    }
  }
}