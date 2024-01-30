package ru.ldi.monitoring_service.usecase;

import ru.ldi.monitoring_service.domain.User;

/**
 * �����, ���������� �� �������� �������������� ������������� � ������� �����������.
 */
public class UserAuthenticationUseCase {
    private MonitoringService monitoringService;

    public UserAuthenticationUseCase(MonitoringService monitoringService) {

        this.monitoringService = monitoringService;
    }
    /**
     * ����� ��� ����� ������������ � �������.
     *
     * @param username ��� ������������.
     * @param password ������ ������������.
     */
    public void loginUser(String username, String password) {
        User user = monitoringService.getUsers().get(username);
        if (user != null && user.getPassword().equals(password)) {
            monitoringService.setCurrentUser(user);
            logAuditEvent("������������ ����� � �������: " + username + ". C ������� ��������������: " + user.isAdmin());

        } else {
            logAuditEvent("������ �����. ��������� ��� ������������ � ������: " + username);
        }
    }
    /**
     * ����� ��� ������ ������������ �� �������.
     */
    public void logoffUser() {
        if (monitoringService.getCurrentUser() != null) {
            logAuditEvent("������������ ����� �� �������: " + monitoringService.getCurrentUser().getUsername());
            monitoringService.setCurrentUser(null);
        } else {
            logAuditEvent("������: ����� �� ����� � �������.");
        }
    }

    private void logAuditEvent(String event) {
        monitoringService.getAuditLog().add(event);
        System.out.println("������ � �����: " + event);
    }
}
