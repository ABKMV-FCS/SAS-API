package com.sas.sasapi.repository;

import com.sas.sasapi.model.ODEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ODEventRepository extends JpaRepository<ODEvent, String> {
    Optional<ODEvent> findByOdEventId(Long eventID);

    Optional<ODEvent> findByEventName(String eventName);
}