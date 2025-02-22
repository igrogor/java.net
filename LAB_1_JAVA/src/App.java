import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import com.google.gson.Gson;

public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");
        Circle MixailKrug = new Circle();
        MixailKrug.setColor("green");
        MixailKrug.setRadius(10);


        SuperJet(1, MixailKrug);

    }

    public static void SuperJet(int a, Circle MixailKrug) throws IOException {
        if (a == 1) {
            System.out.println("Client");
            Socket Kuritsa = new Socket("localhost", 65432);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(Kuritsa.getOutputStream()));
            
            
            Gson gson = new Gson();
            String json = gson.toJson(MixailKrug);

            Class<?> cls = MixailKrug.getClass();
   
            out.write(cls.getName());
            out.write(json);

            out.flush();

            Kuritsa.close();

        } else if (a == 2) {

            System.out.println("Server");

            ServerSocket Petuh = new ServerSocket(65432);
            Socket Korova = Petuh.accept();

            BufferedReader in = new BufferedReader(new InputStreamReader(Korova.getInputStream()));
            String koza = in.readLine();

            String[] parts = koza.split("\\{", 2); // Разделяем на 2 части

            String part1 = parts[0];            // "Hello MAX!"
            String part2 = "{" + parts[1];      // "{\"color\":\"green\",\"radius\":10}"
            
            System.out.println(part1 + "\n");


            System.out.println(part2);

            // Десериализация JSON
            Gson gson = new Gson();
            Circle obj = gson.fromJson(part2, Circle.class);
            System.out.println("Deserialized object: " + obj);
    

            Petuh.close();
            Korova.close();
        }
    }
}
