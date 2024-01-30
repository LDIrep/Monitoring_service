package ru.ldi.monitoring_service.usecase;

import ru.ldi.monitoring_service.domain.User;
import ru.ldi.monitoring_service.in.ConsoleInput;
import ru.ldi.monitoring_service.out.ConsoleOutput;
import ru.ldi.monitoring_service.presentation.ConsoleUI;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MonitoringService {
    private Map<String, User> users;
    private List<String> auditLog;
    private ConsoleOutput consoleOutput;
    private ConsoleInput consoleInput;
    private UserAuthenticationUseCase userAuthenticationUseCase;
    private UserActionsUseCase userActionsUseCase;
    private UserRegistrationUseCase userRegistrationUseCase;
    private User currentUser;


    public static void main(String[] args) {
        MonitoringService monitoringService = new MonitoringService();
        monitoringService.start();
    }

    private void start() {
        currentUser = null;
        users = new HashMap<>();
        User admin = new User("admin","123");
        admin.setAdmin(true);
        users.put("admin",admin);
        auditLog = new ArrayList<>();
        consoleOutput = new ConsoleOutput();
        consoleInput = new ConsoleInput();
        userAuthenticationUseCase = new UserAuthenticationUseCase(this);
        userActionsUseCase = new UserActionsUseCase(this);
        userRegistrationUseCase = new UserRegistrationUseCase(this);
        ConsoleUI consoleUI = new ConsoleUI(this);
        consoleUI.start();
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public Map<String, User> getUsers() {
        return users;
    }

    public void setUsers(Map<String, User> users) {
        this.users = users;
    }

    public List<String> getAuditLog() {
        return auditLog;
    }

    public void setAuditLog(List<String> auditLog) {
        this.auditLog = auditLog;
    }

    public ConsoleOutput getConsoleOutput() {
        return consoleOutput;
    }

    public void setConsoleOutput(ConsoleOutput consoleOutput) {
        this.consoleOutput = consoleOutput;
    }

    public ConsoleInput getConsoleInput() {
        return consoleInput;
    }

    public void setConsoleInput(ConsoleInput consoleInput) {
        this.consoleInput = consoleInput;
    }

    public UserAuthenticationUseCase getUserAuthenticationUseCase() {
        return userAuthenticationUseCase;
    }

    public void setUserAuthenticationUseCase(UserAuthenticationUseCase userAuthenticationUseCase) {
        this.userAuthenticationUseCase = userAuthenticationUseCase;
    }

    public UserActionsUseCase getUserActionsUseCase() {
        return userActionsUseCase;
    }

    public void setUserActionsUseCase(UserActionsUseCase userActionsUseCase) {
        this.userActionsUseCase = userActionsUseCase;
    }

    public UserRegistrationUseCase getUserRegistrationUseCase() {
        return userRegistrationUseCase;
    }

    public void setUserRegistrationUseCase(UserRegistrationUseCase userRegistrationUseCase) {
        this.userRegistrationUseCase = userRegistrationUseCase;
    }
}

