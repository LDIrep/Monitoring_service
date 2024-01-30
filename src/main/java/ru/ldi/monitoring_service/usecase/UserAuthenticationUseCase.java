package ru.ldi.monitoring_service.usecase;

import ru.ldi.monitoring_service.domain.User;

/**
 * Класс, отвечающий за процессы аутентификации пользователей в системе мониторинга.
 */
public class UserAuthenticationUseCase {
    private MonitoringService monitoringService;

    public UserAuthenticationUseCase(MonitoringService monitoringService) {

        this.monitoringService = monitoringService;
    }
    /**
     * Метод для входа пользователя в систему.
     *
     * @param username Имя пользователя.
     * @param password Пароль пользователя.
     */
    public void loginUser(String username, String password) {
        User user = monitoringService.getUsers().get(username);
        if (user != null && user.getPassword().equals(password)) {
            monitoringService.setCurrentUser(user);
            logAuditEvent("Пользователь вошел в систему: " + username + ". C правами Администратора: " + user.isAdmin());

        } else {
            logAuditEvent("Ошибка входа. Проверьте имя пользователя и пароль: " + username);
        }
    }
    /**
     * Метод для выхода пользователя из системы.
     */
    public void logoffUser() {
        if (monitoringService.getCurrentUser() != null) {
            logAuditEvent("Пользователь вышел из системы: " + monitoringService.getCurrentUser().getUsername());
            monitoringService.setCurrentUser(null);
        } else {
            logAuditEvent("Ошибка: Никто не вошел в систему.");
        }
    }

    private void logAuditEvent(String event) {
        monitoringService.getAuditLog().add(event);
        System.out.println("Запись в аудит: " + event);
    }
}
