package com.example.dao;

import com.example.domain.User;
import com.example.util.HibernateUtil;
import com.example.util.LoggerUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.mindrot.jbcrypt.BCrypt;

public class UserDAO {


    public boolean registerUser(String username, String hashedPassword) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Verifica se l'utente esiste già
            Query<User> query = session.createQuery("FROM User WHERE username = :username", User.class);
            query.setParameter("username", username);
            if (!query.list().isEmpty()) {
                return false; // Utente già esistente
            }

            // Crea e salva un nuovo utente
            transaction = session.beginTransaction();
            User user = new User(username, hashedPassword);
            session.save(user);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LoggerUtil.error("Errore durante la registrazione dell'utente", e);
            return false;
        }
    }

    public boolean authenticateUser(String username, String password) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<User> query = session.createQuery("FROM User WHERE username = :username", User.class);
            query.setParameter("username", username);
            User user = query.uniqueResult();
            if (user != null) {
                // Verifica la password usando BCrypt
                return BCrypt.checkpw(password, user.getPassword());
            }
            return false;
        } catch (Exception e) {
            LoggerUtil.error("Errore durante l'autenticazione dell'utente", e);
            return false;
        }
    }
}

