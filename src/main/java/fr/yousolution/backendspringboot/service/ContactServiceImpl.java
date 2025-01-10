package fr.yousolution.backendspringboot.service;

import fr.yousolution.backendspringboot.model.Contact;
import fr.yousolution.backendspringboot.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService{
    private final ContactRepository contactRepository;
    private final EmailServiceImpl emailService;

    @Override
    @Transactional
    public Contact saveContact(Contact contact){
        try {
            Contact savedContact = contactRepository.save(contact);

            emailService.sendContactNotification(
                    savedContact.getName(),
                    savedContact.getEmail(),
                    savedContact.getMessage()
            );

            return savedContact;
        } catch (Exception e) {
            throw new RuntimeException("Une erreur s'est produite lors de la sauvegarde du contact.", e);
        }


    }

}
