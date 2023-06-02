package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;
import org.hibernate.mapping.Property;

import org.hibernate.cfg.Configuration;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Util {
    private static final String URL = "jdbc:postgresql://localhost:5432/mydatabase";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "lisa2011";

    public static Connection getConnection() {
        Connection connection = null;
        try {
            // Регистрируем драйвер JDBC
            Class.forName("org.postgresql.Driver");

            // Устанавливаем соединение с БД
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static void main(String[] args) {
        Connection connection = Util.getConnection();
        if (connection != null) {
            System.out.println("Успешное подключение к базе данных!");
        } else {
            System.out.println("Ошибка при подключении к базе данных!");
        }
    }


    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(getHibernateProperties())
                        .build();

                MetadataSources metadataSources = new MetadataSources(standardRegistry)
                        .addAnnotatedClass(User.class);

                sessionFactory = metadataSources.buildMetadata()
                        .buildSessionFactory();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }
    private static Properties getHibernateProperties() {
        Properties properties = new Properties();
        properties.put(Environment.DRIVER, "org.postgresql.Driver");
        properties.put(Environment.URL, "jdbc:postgresql://localhost:5432/postgres");
        properties.put(Environment.USER, "postgres");
        properties.put(Environment.PASS, "lisa2011");
        properties.put(Environment.SHOW_SQL, "true");
        properties.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
        return properties;
    }

    public static void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}



