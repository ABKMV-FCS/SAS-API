package com.sas.sasapi.repository;

import com.sas.sasapi.model.ODEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface ODEventRepository extends JpaRepository<ODEvent, String> {
    public Optional<ODEvent> findByEventId(Long eventID);
}