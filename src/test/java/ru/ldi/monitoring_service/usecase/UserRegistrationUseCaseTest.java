package ru.ldi.monitoring_service.usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.ldi.monitoring_service.domain.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class UserRegistrationUseCaseTest {
    private MonitoringService monitoringService;
    private UserRegistrationUseCase userRegistrationUseCase;

    @BeforeEach
    public void setup() {
        monitoringService = Mockito.mock(MonitoringService.class);
        userRegistrationUseCase = new UserRegistrationUseCase(monitoringService);
    }

    @Test
    public void testRegisterUser() {
        Map<String, User> users = new HashMap<>();
        when(monitoringService.getUsers()).thenReturn(users);

        userRegistrationUseCase.registerUser("username", "password");

        assertThat(users.containsKey("username")).isTrue();
        assertThat(users.get("username").getPassword()).isEqualTo("password");
    }

    @Test
    public void testRegisterUserFail() {
        User user = new User("username", "password");
        Map<String, User> users = new HashMap<>();
        users.put("username", user);
        when(monitoringService.getUsers()).thenReturn(users);

        List<String> auditLog = new ArrayList<>();
        when(monitoringService.getAuditLog()).thenReturn(auditLog);

        userRegistrationUseCase.registerUser("username", "password");

        assertThat(auditLog).containsExactly("Ошибка регистрации. Пользователь с таким именем уже существует: username");
    }
}
