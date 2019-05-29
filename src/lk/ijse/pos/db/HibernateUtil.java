package lk.ijse.pos.db;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class HibernateUtil {

    private static EntityManagerFactory emf = createEntityManagerFactory();

    private static EntityManagerFactory createEntityManagerFactory(){

        Properties properties = new Properties();

        try{
            File file = new File("modern-pos-hibernate/resources/application.properties");

            FileReader fileReader = null;

            fileReader = new FileReader(file);

            properties.load(fileReader);

        }catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Persistence.createEntityManagerFactory("unit1",properties);
    }

    public static EntityManagerFactory getEntityManagerFactory(){
        return emf;
    }
}
