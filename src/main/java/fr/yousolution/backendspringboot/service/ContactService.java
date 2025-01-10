package fr.yousolution.backendspringboot.service;

import fr.yousolution.backendspringboot.model.Contact;
import org.springframework.transaction.annotation.Transactional;

public interface ContactService {
    @Transactional
    Contact saveContact(Contact contact);
}
