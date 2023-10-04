package com.example.expenseit.services;

import com.example.expenseit.errors.*;
import com.example.expenseit.errors.authentication.*;
import com.example.expenseit.models.Client;
import com.example.expenseit.models.dto.ClientDTO;
import com.example.expenseit.repositories.CategoryRepository;
import com.example.expenseit.repositories.ClientRepository;
import com.example.expenseit.util.SHA256PasswordEncoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClientServiceTest {

    @InjectMocks
    private ClientService clientService;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void logInClient_Valid() throws AuthException {
        String email = "test@example.com";
        String password = "password";
        Client client = new Client();
        when(clientRepository.findByEmailAndPassword(email, SHA256PasswordEncoder.getSHA(password))).thenReturn(client);

        Client result = clientService.logInClient(email, password);

        assertEquals(client, result);
    }

    @Test
    void logInClient_Invalid() {
        String email = "test@example.com";
        String password = "password";
        when(clientRepository.findByEmailAndPassword(email, SHA256PasswordEncoder.getSHA(password))).thenReturn(null);

        assertThrows(LoginFailedException.class, () -> clientService.logInClient(email, password));
    }

    @Test
    void registerNewClient_Valid() throws AuthException {
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setEmail("test@example.com");
        clientDTO.setPassword("password");
        when(clientRepository.existsByEmail(clientDTO.getEmail())).thenReturn(false);

        Client client = new Client();
        when(clientRepository.save(any(Client.class))).thenReturn(client);

        Client result = clientService.registerNewClient(clientDTO);

        assertEquals(client, result);
    }


    @Test
    void registerNewClient_InvalidEmail() {
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setEmail("invalidEmail");

        assertThrows(InvalidEmailException.class, () -> clientService.registerNewClient(clientDTO));
    }

    @Test
    void registerNewClient_EmailInUse() {
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setEmail("test@example.com");
        when(clientRepository.existsByEmail(clientDTO.getEmail())).thenReturn(true);

        assertThrows(EmailInUseException.class, () -> clientService.registerNewClient(clientDTO));
    }

    @Test
    void registerNewClient_Success() throws AuthException {
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setEmail("test@example.com");
        clientDTO.setPassword("password");
        when(clientRepository.existsByEmail(clientDTO.getEmail())).thenReturn(false);

        Client client = new Client();
        when(clientRepository.save(any(Client.class))).thenReturn(client);

        Client result = clientService.registerNewClient(clientDTO);

        assertEquals(client, result);
    }
}
