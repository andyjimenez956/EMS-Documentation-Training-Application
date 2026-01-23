package edu.wgu.d.emsbackend.common;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @GeneratedValue
    protected UUID id;

    @Column(nullable = false)
    protected LocalDateTime created = LocalDateTime.now();

    public UUID getId() { return id; }
    public LocalDateTime getCreated() { return created; }
}

