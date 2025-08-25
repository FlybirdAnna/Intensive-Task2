package com.example.userservice;

import com.example.model.HibernateUtil;
import com.example.userservice.dao.UserDAO;
import com.example.userservice.dao.UserDAOImpl;
import com.example.userservice.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);
    private static final UserDAO userDAO = new UserDAO() {
        @Override
        public void saveUser(User user) {

        }

        @Override
        public User getUserById(Long id) {
            return null;
        }

        @Override
        public List<User> getAllUsers() {
            return List.of();
        }

        @Override
        public void updateUser(User user) {

        }

        @Override
        public void deleteUser(Long id) {

        }
    };
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        logger.info("Запуск user-service...");

        boolean running = true;
        while (running) {
            showMenu();
            int choice = getIntInput();

            try {
                switch (choice) {
                    case 1 -> createUser();
                    case 2 -> readUser();
                    case 3 -> readAllUsers();
                    case 4 -> updateUser();
                    case 5 -> deleteUser();
                    case 0 -> {
                        running = false;
                        logger.info("Завершение работы...");
                    }
                    default -> System.out.println("Неверный выбор. Попробуйте снова.");
                }
            } catch (Exception e) {
                System.err.println("Ошибка: " + e.getMessage());
                logger.error("Необработанная ошибка: ", e);
            }
        }

        scanner.close();
        HibernateUtil.shutdown();
    }

    private static void showMenu() {
        System.out.println("\n=== User Service ===");
        System.out.println("1. Создать пользователя");
        System.out.println("2. Получить пользователя по ID");
        System.out.println("3. Показать всех пользователей");
        System.out.println("4. Обновить пользователя");
        System.out.println("5. Удалить пользователя");
        System.out.println("0. Выход");
        System.out.print("Выберите действие: ");
    }

    private static int getIntInput() {
        while (!scanner.hasNextInt()) {
            System.out.print("Введите число: ");
            scanner.next();
        }
        return scanner.nextInt();
    }

    private static void createUser() {
        scanner.nextLine(); // очистка буфера
        System.out.print("Имя: ");
        String name = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Возраст: ");
        int age = getIntInput();

        User user = new User(name, email, age);
        userDAO.saveUser(user);
        System.out.println("✅ Пользователь создан: " + user);
    }

    private static void readUser() {
        System.out.print("Введите ID: ");
        Long id = (long) getIntInput();
        User user = userDAO.getUserById(id);
        if (user != null) {
            System.out.println("Найден пользователь: " + user);
        } else {
            System.out.println("❌ Пользователь не найден.");
        }
    }

    private static void readAllUsers() {
        List<User> users = userDAO.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("Список пользователей пуст.");
        } else {
            System.out.println("\n--- Список пользователей ---");
            users.forEach(System.out::println);
        }
    }

    private static void updateUser() {
        System.out.print("Введите ID пользователя для обновления: ");
        Long id = (long) getIntInput();
        User user = userDAO.getUserById(id);
        if (user == null) {
            System.out.println("❌ Пользователь не найден.");
            return;
        }

        scanner.nextLine(); // очистка
        System.out.print("Новое имя (оставьте пустым для пропуска): ");
        String name = scanner.nextLine();
        if (!name.trim().isEmpty()) user.setName(name);

        System.out.print("Новый email (оставьте пустым для пропуска): ");
        String email = scanner.nextLine();
        if (!email.trim().isEmpty()) user.setEmail(email);

        System.out.print("Новый возраст (0 для пропуска): ");
        int age = getIntInput();
        if (age > 0) user.setAge(age);

        userDAO.updateUser(user);
        System.out.println("✅ Пользователь обновлён: " + user);
    }

    private static void deleteUser() {
        System.out.print("Введите ID пользователя для удаления: ");
        Long id = (long) getIntInput();
        userDAO.deleteUser(id);
        System.out.println("✅ Пользователь с ID " + id + " удалён (если существовал).");
    }
}