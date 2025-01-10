package fr.yousolution.backendspringboot.service;

import fr.yousolution.backendspringboot.model.Contact;
import fr.yousolution.backendspringboot.repository.ContactRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ContactServiceImplTest {
    @Mock
    private ContactRepository contactRepository;

    @Mock
    private EmailServiceImpl emailService;

    @InjectMocks
    private ContactServiceImpl contactService;

    @Captor
    private ArgumentCaptor<Contact> contactCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveContact() {
        // Arrange: Créer un objet contact en entrée
        Contact inputContact = new Contact();
        inputContact.setName("John Doe");
        inputContact.setEmail("john.doe@example.com");
        inputContact.setMessage("Hello, this is a test.");

        // Créer un objet contact simulant la sauvegarde en base de données
        Contact savedContact = new Contact();
        savedContact.setId(UUID.randomUUID()); // Assurez-vous que l'ID est généré
        savedContact.setName(inputContact.getName());
        savedContact.setEmail(inputContact.getEmail());
        savedContact.setMessage(inputContact.getMessage());

        // Mock: Simuler la sauvegarde du contact
        when(contactRepository.save(any(Contact.class))).thenReturn(savedContact);

        // Act: Appeler la méthode saveContact
        Contact result = contactService.saveContact(inputContact);

        // Assert: Vérifier que le contact retourné est bien celui attendu
        assertNotNull(result);
        assertEquals(savedContact.getId(), result.getId());
        assertEquals("John Doe", result.getName());
        assertEquals("john.doe@example.com", result.getEmail());
        assertEquals("Hello, this is a test.", result.getMessage());

        // Vérifier que contactRepository.save a été appelé avec les bons paramètres
        verify(contactRepository).save(contactCaptor.capture());
        Contact capturedContact = contactCaptor.getValue();
        assertEquals("John Doe", capturedContact.getName());
        assertEquals("john.doe@example.com", capturedContact.getEmail());
        assertEquals("Hello, this is a test.", capturedContact.getMessage());

        // Vérifier que le service d'email a été appelé avec les bons paramètres
        verify(emailService, times(1)).sendContactNotification(
                eq("John Doe"),
                eq("john.doe@example.com"),
                eq("Hello, this is a test.")
        );
    }


}
