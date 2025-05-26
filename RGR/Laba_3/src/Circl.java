public class Circl {
    String color;
    int pos_x, pos_y, Radius;

    Circl() {
        pos_x = (int) (Math.random() * 100);
        pos_y = (int) (Math.random() * 100);
        Radius = (int) (Math.random() * 50);

        int TypeColor = (int) (Math.random() * 4);

        switch (TypeColor) {
            case 0:
                color = "RED";
                break;
            case 1:
                color = "YELLOW";
                break;
            case 2:
                color = "GREEN";
                break;
            case 3:
                color = "BLUE";
                break;
            default:
                break;
        }
    }
}
