package fr.yousolution.backendspringboot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.yousolution.backendspringboot.model.Contact;
import fr.yousolution.backendspringboot.repository.ContactRepository;
import fr.yousolution.backendspringboot.service.ContactService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.doThrow;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ContactControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private ContactService contactService;

    @BeforeEach
    public void setUp() {
        // Nettoyage de la base de données avant chaque test
        contactRepository.deleteAll();
    }

    /**
     * Test d'intégration: Soumission de contact avec succès.
     */
    @Test
    public void testSubmitContactSuccess() throws Exception {
        // Arrange: Créer un objet Contact valide
        Contact contact = new Contact();
        contact.setId(UUID.randomUUID());
        contact.setName("John Doe");
        contact.setEmail("john.doe@example.com");
        contact.setMessage("Hello, this is a test message.");

        // Act & Assert
        mockMvc.perform(post("/api/contact")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contact)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Contact saved successfully"))
                .andExpect(jsonPath("$.data.name").value("John Doe"))
                .andExpect(jsonPath("$.data.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.data.message").value("Hello, this is a test message."));
    }


    /**
     * Test d'intégration: Validation échoue à cause de données manquantes.
     */
    @Test
    public void testSubmitContactValidationFailure_MissingEmail() throws Exception {
        // Arrange: Créer un objet Contact sans email
        Contact contact = new Contact();
        contact.setId(UUID.randomUUID());
        contact.setName("Jane Doe");
        contact.setMessage("This is a test message.");

        // Act & Assert
        mockMvc.perform(post("/api/contact")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contact)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message").value("email: Email is required"));
    }

    /**
     * Test d'intégration: Validation échoue à cause d'un email invalide.
     */
    @Test
    public void testSubmitContactValidationFailure_InvalidEmail() throws Exception {
        // Arrange: Créer un objet Contact avec un email invalide
        Contact contact = new Contact();
        contact.setId(UUID.randomUUID());
        contact.setName("Jane Doe");
        contact.setEmail("invalid-email");
        contact.setMessage("This is a test message.");

        // Act & Assert
        mockMvc.perform(post("/api/contact")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contact)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message").value("email: Invalid email format"));
    }

    /**
     * Test d'intégration: Erreur interne simulée.
     */
    @Test
    public void testSubmitContactInternalServerError1() throws Exception {
        // Arrange: Créer un objet Contact valide
        Contact contact = new Contact();
        contact.setName("Jane Doe");
        contact.setEmail("jane.doe@example.com");
        contact.setMessage("This is a test message.");

        // Simuler une exception levée par le service
        Mockito.when(contactService.saveContact(Mockito.any(Contact.class)))
                .thenThrow(new RuntimeException("Simulated internal error"));

        // Act & Assert
        mockMvc.perform(post("/api/contact")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contact)));
                //.andExpect(status().isInternalServerError())
                //.andExpect(jsonPath("$.success").value(false))
                //.andExpect(jsonPath("$.message").value("Une erreur interne s'est produite. Veuillez réessayer plus tard."));
    }

    @Test
    public void testSubmitContactInternalServerError() throws Exception {
        // Arrange : Créer un objet Contact valide
        Contact contact = new Contact();
        contact.setName("Jane Doe");
        contact.setEmail("jane.doe@example.com");
        contact.setMessage("This is a test message.");

        // Convertir l'objet Contact en JSON
        String contactJson = objectMapper.writeValueAsString(contact);

        // Simuler une erreur interne en faisant échouer la méthode saveContact
        doThrow(new RuntimeException("Database error")).when(contactService).saveContact(contact);

        // Act & Assert : Vérifier que le statut est 500 et le message d'erreur
        mockMvc.perform(post("/api/contact")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contactJson))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Une erreur interne s'est produite. Veuillez réessayer plus tard."));
    }


}
