package vikbur;

import java.io.*;
import java.util.Date;

//класс для проверки функционала
//реализует заполнение файла случайными данными
public class Loginer extends Thread{

    private File file;
    private static final String[] names = new String[] {"user1", "user2", "user3","user4","user5"};
    private static final String[] operations = new String[] {"login", "logout"};
    private boolean isActive;

    public Loginer(File file){
        this.file = file;
        isActive = true;
    }

    public void disable(){
        isActive = false;
    }

    @Override
    public void run() {

        //System.out.println("Loginer started");

        while (isActive) {
            try {

                BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));

                StringBuilder sb = new StringBuilder();

                int indexName = (int) (Math.random()*5);
                int indexOperation = (int) (Math.random()*2);

                sb.append(new Date()).append(" ").append(names[indexName]).append(" ").append(operations[indexOperation]);

                writer.write(sb.toString());
                writer.newLine();

                writer.close();

                Thread.sleep(2000);

            } catch (InterruptedException | IOException e) {
                System.out.println("Loginer stopped");
                break;
            }
        }

    }
}
