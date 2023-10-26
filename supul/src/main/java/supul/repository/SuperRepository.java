package supul.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import supul.model.Super;

public interface SuperRepository extends JpaRepository<Super, Long> {
    Super findBySuperId(String superId);
}
