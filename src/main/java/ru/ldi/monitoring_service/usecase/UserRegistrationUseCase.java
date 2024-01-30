package ru.ldi.monitoring_service.usecase;


import ru.ldi.monitoring_service.domain.User;

import java.util.Map;
/**
 * Класс, отвечающий за процесс регистрации новых пользователей в системе мониторинга.
 */
public class UserRegistrationUseCase {
    private MonitoringService monitoringService;

    public UserRegistrationUseCase(MonitoringService monitoringService) {
        this.monitoringService = monitoringService;
    }
    /**
     * Метод для регистрации нового пользователя в системе.
     *
     * @param username Имя пользователя.
     * @param password Пароль пользователя.
     */
    public void registerUser(String username, String password) {
        Map<String, User> users = monitoringService.getUsers();
        if (!users.containsKey(username)) {
            User user = new User(username, password);
            users.put(username, user);
            logAuditEvent("Пользователь зарегистрирован: " + username);
        } else {
            logAuditEvent("Ошибка регистрации. Пользователь с таким именем уже существует: " + username);
        }
    }

    private void logAuditEvent(String event) {
        monitoringService.getAuditLog().add(event);
        System.out.println("Запись в аудит: " + event);
    }
}
