package ru.ldi.monitoring_service.usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.ldi.monitoring_service.domain.User;
import java.util.HashMap;
import java.util.Map;
import static org.mockito.Mockito.*;

public class UserAuthenticationUseCaseTest {
    private MonitoringService monitoringService;
    private UserAuthenticationUseCase userAuthenticationUseCase;

    @BeforeEach
    public void setup() {
        monitoringService = Mockito.mock(MonitoringService.class);
        userAuthenticationUseCase = new UserAuthenticationUseCase(monitoringService);
    }

    @Test
    public void testLoginUser() {
        User user = new User("username", "password");
        Map<String, User> users = new HashMap<>();
        users.put("username", user);
        when(monitoringService.getUsers()).thenReturn(users);

        userAuthenticationUseCase.loginUser("username", "password");

        verify(monitoringService, times(1)).setCurrentUser(user);
    }

    @Test
    public void testLoginUserFail() {
        Map<String, User> users = new HashMap<>();
        when(monitoringService.getUsers()).thenReturn(users);

        userAuthenticationUseCase.loginUser("username", "wrong_password");

        verify(monitoringService, times(0)).setCurrentUser(any());
    }

    @Test
    public void testLogoffUser() {
        User user = new User("username", "password");
        when(monitoringService.getCurrentUser()).thenReturn(user);

        userAuthenticationUseCase.logoffUser();

        verify(monitoringService, times(1)).setCurrentUser(null);
    }

    @Test
    public void testLogoffUserFail() {
        when(monitoringService.getCurrentUser()).thenReturn(null);

        userAuthenticationUseCase.logoffUser();

        verify(monitoringService, times(0)).setCurrentUser(any());
    }
}
