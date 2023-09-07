package com.exam.nisum.user.api.service;

import com.exam.nisum.user.api.dto.response.UserResponseDTO;
import com.exam.nisum.user.api.entity.UserEntity;
import com.exam.nisum.user.api.model.Phone;
import com.exam.nisum.user.api.model.User;
import com.exam.nisum.user.api.repository.PhoneRepository;
import com.exam.nisum.user.api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository mockedUserRepository;

    @Mock
    private PhoneRepository mockedPhoneRepository;

    @Mock
    private PasswordEncoder mockedPasswordEncoder;

    @Mock
    private User mockedUser;

    @Mock
    private Phone mockedPhone;

    @Mock
    private UserEntity mockedUserEntity;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        mockedUser = new User();
        mockedUser.setId("bb5d5031-4d64-4ff7-8164-1654002f7611");
        mockedUser.setName("Test User Nisum");
        mockedUser.setEmail("test@nisum.cl");
        mockedUser.setPassword("123Acb144*");
        mockedUser.setPhones(new ArrayList<>());
        mockedUser.getPhones().add(mockedPhone);

        mockedUserEntity = new UserEntity();
        mockedUserEntity.setId("bb5d5031-4d64-4ff7-8164-1654002f7611");
        mockedUserEntity.setName("Test User Nisum");
        mockedUserEntity.setEmail("test@nisum.cl");
    }

    @Test
    void getUsers_returnsAllUsers() {
        //given
        List<UserEntity> userList = Collections.singletonList(mockedUserEntity);
        when(mockedUserRepository.findAll()).thenReturn(userList);

        //when
        List<UserResponseDTO> users = userService.getUsers();

        //then
        assertEquals(users.size(), 1);
    }

    @Test
    void saveUser_returnUserResponseDTO() {
        // Mockear dependencias externas
        when(mockedPasswordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(mockedUserRepository.save(any(UserEntity.class))).thenAnswer(invocation -> {
            UserEntity saveUser = invocation.getArgument(0);
            saveUser.setId("bb5d5031-4d64-4ff7-8164-1654002f7611");
            return saveUser;
        });

        // Ejecutar método a probar
        UserResponseDTO result = userService.saveUser(mockedUser);

        // Verificar resultado
        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getToken());
        assertEquals(result.getName(), mockedUser.getName());
        assertEquals(result.getEmail(), mockedUser.getEmail());
        assertEquals(result.getPhones().size(), mockedUser.getPhones().size());
        verify(mockedUserRepository, times(1)).save(any(UserEntity.class));
    }
}
