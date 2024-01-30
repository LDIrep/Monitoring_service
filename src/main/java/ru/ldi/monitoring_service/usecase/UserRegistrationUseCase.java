package ru.ldi.monitoring_service.usecase;


import ru.ldi.monitoring_service.domain.User;

import java.util.Map;
/**
 * �����, ���������� �� ������� ����������� ����� ������������� � ������� �����������.
 */
public class UserRegistrationUseCase {
    private MonitoringService monitoringService;

    public UserRegistrationUseCase(MonitoringService monitoringService) {
        this.monitoringService = monitoringService;
    }
    /**
     * ����� ��� ����������� ������ ������������ � �������.
     *
     * @param username ��� ������������.
     * @param password ������ ������������.
     */
    public void registerUser(String username, String password) {
        Map<String, User> users = monitoringService.getUsers();
        if (!users.containsKey(username)) {
            User user = new User(username, password);
            users.put(username, user);
            logAuditEvent("������������ ���������������: " + username);
        } else {
            logAuditEvent("������ �����������. ������������ � ����� ������ ��� ����������: " + username);
        }
    }

    private void logAuditEvent(String event) {
        monitoringService.getAuditLog().add(event);
        System.out.println("������ � �����: " + event);
    }
}
