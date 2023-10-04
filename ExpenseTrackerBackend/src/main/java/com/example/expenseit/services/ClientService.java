package com.example.expenseit.services;

import com.example.expenseit.errors.*;
import com.example.expenseit.errors.authentication.AuthException;
import com.example.expenseit.errors.authentication.EmailInUseException;
import com.example.expenseit.errors.authentication.InvalidEmailException;
import com.example.expenseit.errors.authentication.LoginFailedException;
import com.example.expenseit.models.Category;
import com.example.expenseit.models.Client;
import com.example.expenseit.models.dto.ClientDTO;
import com.example.expenseit.repositories.CategoryRepository;
import com.example.expenseit.repositories.ClientRepository;
import com.example.expenseit.util.SHA256PasswordEncoder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class ClientService {

    private ClientRepository clientRepository;
    private CategoryRepository categoryRepository;

    private static final String EMAIL_VERIFICATION_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";

    public Client logInClient(String email, String password) throws AuthException {

        Client client = clientRepository.findByEmailAndPassword(email,SHA256PasswordEncoder.getSHA(password));
        if(client == null){
            throw new LoginFailedException("Invalid email or password");
        }
        return client;
    }

    public Client registerNewClient(ClientDTO clientDTO) throws AuthException{

        boolean isEmailValid = validateEmail(clientDTO.getEmail());
        boolean isUserExisting = clientRepository.existsByEmail(clientDTO.getEmail());

        if(isEmailValid) {
            if(!isUserExisting)
            {
                Client tempClient = populateClientData(clientDTO);
                return clientRepository.save(tempClient);
            }
            else throw new EmailInUseException("User is already existing !");
        }
        else throw new InvalidEmailException("Email is not valid !");
    }

    private Client populateClientData(ClientDTO clientDTO) {
        return Client.builder()
                .firstName(clientDTO.getFirstName())
                .secondName(clientDTO.getSecondName())
                .thirdName(clientDTO.getThirdName())
                .email(clientDTO.getEmail())
                .balance(clientDTO.getBalance())
                .incomeHistory(new ArrayList<>())
                .password(SHA256PasswordEncoder.getSHA(clientDTO.getPassword()))
                .build();
    }

    private boolean validateEmail(String email) throws AuthException {

        Pattern pattern = Pattern.compile(EMAIL_VERIFICATION_REGEX);
        if (email != null) {
            email = email.toLowerCase();
            if (!pattern.matcher(email).matches()) {
                throw new InvalidEmailException("Invalid email format!");
            }
            int clientCount = clientRepository.countByEmail(email);

            if (clientCount > 0) throw new EmailInUseException("Email is already in use");

            return true;
        }

        return false;
    }

}
