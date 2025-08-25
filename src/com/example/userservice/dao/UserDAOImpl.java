package com.example.userservice.dao;

import com.example.userservice.entity.User;
import com.example.userservice.util.HibernateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class UserDAOImpl implements com.example.userservice.dao.UserDAO {
    private static final Logger logger = LogManager.getLogger(UserDAOImpl.class);

    @Override
    public void saveUser(User user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();
            logger.info("Пользователь сохранён: {}", user.getEmail());
        } catch (HibernateException e) {
            if (transaction != null) transaction.rollback();
            logger.error("Ошибка при сохранении пользователя: {}", e.getMessage());
            throw new RuntimeException("Ошибка при сохранении пользователя", e);
        }
    }

    @Override
    public User getUserById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            User user = session.get(User.class, id);
            if (user != null) {
                logger.info("Найден пользователь: {}", user.getEmail());
            } else {
                logger.warn("Пользователь с ID {} не найден", id);
            }
            return user;
        } catch (HibernateException e) {
            logger.error("Ошибка при получении пользователя по ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Ошибка при получении пользователя", e);
        }
    }

    @Override
    public List<User> getAllUsers() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<User> users = session.createQuery("FROM User", User.class).list();
            logger.info("Получено {} пользователей из базы", users.size());
            return users;
        } catch (HibernateException e) {
            logger.error("Ошибка при получении списка пользователей: {}", e.getMessage());
            throw new RuntimeException("Ошибка при получении списка пользователей", e);
        }
    }

    @Override
    public void updateUser(User user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(user);
            transaction.commit();
            logger.info("Пользователь обновлён: {}", user.getEmail());
        } catch (HibernateException e) {
            if (transaction != null) transaction.rollback();
            logger.error("Ошибка при обновлении пользователя: {}", e.getMessage());
            throw new RuntimeException("Ошибка при обновлении пользователя", e);
        }
    }

    @Override
    public void deleteUser(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) {
                session.remove(user);
                transaction.commit();
                logger.info("Пользователь удалён: {}", user.getEmail());
            } else {
                logger.warn("Попытка удалить несуществующего пользователя с ID {}", id);
            }
        } catch (HibernateException e) {
            if (transaction != null) transaction.rollback();
            logger.error("Ошибка при удалении пользователя с ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Ошибка при удалении пользователя", e);
        }
    }
}