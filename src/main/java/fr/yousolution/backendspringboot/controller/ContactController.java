package fr.yousolution.backendspringboot.controller;

import fr.yousolution.backendspringboot.dto.ApiResponseDTO;
import fr.yousolution.backendspringboot.model.Contact;
import fr.yousolution.backendspringboot.service.ContactService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class ContactController {
    private final ContactService contactService;

    @PostMapping("/contact")
    public ResponseEntity<ApiResponseDTO<Contact>> submitContact(@Valid @RequestBody Contact contact) {
        try {
            Contact savedContact = contactService.saveContact(contact);

            ApiResponseDTO<Contact> response = new ApiResponseDTO<>(
                    true,
                    savedContact,
                    "Contact saved successfully"
            );
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            ApiResponseDTO<Contact> response = new ApiResponseDTO<>(
                    false,
                    null,
                    "Une erreur interne s'est produite. Veuillez réessayer plus tard."
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
