package com.example.Clothesshoponline.repository;

import com.example.Clothesshoponline.model.SiteContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SiteContentRepository extends JpaRepository<SiteContent, Long> {
    Optional<SiteContent> findByContentKey(String contentKey);

    List<SiteContent> findByContentType(String contentType);
}
