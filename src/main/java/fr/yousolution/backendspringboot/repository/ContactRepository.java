package fr.yousolution.backendspringboot.repository;

import fr.yousolution.backendspringboot.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ContactRepository extends JpaRepository<Contact, UUID> {
}
