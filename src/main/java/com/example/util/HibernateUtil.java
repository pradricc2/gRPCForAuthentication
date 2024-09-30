package com.example.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private HibernateUtil() {
    }

    private static SessionFactory buildSessionFactory() {
        try {
            // Crea la SessionFactory da hibernate.cfg.xml
            return new Configuration().configure().buildSessionFactory();
        } catch (RuntimeException ex) {
            // Stampa l'eccezione
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        // Chiude la SessionFactory
        getSessionFactory().close();
    }
}

