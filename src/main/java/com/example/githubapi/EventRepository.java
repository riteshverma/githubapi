package com.example.githubapi;

import java.util.List;
import java.util.Optional;

public interface EventRepository {
    Event save(Event event);
    List<Event> findAll();
    Optional<Event> findById(Integer id);
    List<Event> findByRepoId(Integer repoId);
    List<Event> findByActorId(Integer actorId);
    void deleteAll();
}