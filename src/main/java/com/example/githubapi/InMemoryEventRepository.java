package com.example.githubapi;

import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class InMemoryEventRepository implements EventRepository {

    private final List<Event> events = new ArrayList<>();
    private final AtomicInteger idCounter = new AtomicInteger(1);

    /** Persist a new Event, assigning it a unique ID */
    public Event save(Event event) {
        event.setId(Integer.valueOf(idCounter.getAndIncrement()));
        events.add(event);
        return event;
    }

    /** Return all events in ID order */
    public List<Event> findAll() {
        return events.stream()
                .sorted(Comparator.comparing(Event::getId))
                .toList();
    }

    /** Look up a single event by its ID */
    public Optional<Event> findById(Integer id) {
        return events.stream()
                .filter(e -> e.getId().equals(id))
                .findFirst();
    }

    /** All events for a given repository ID, in ID order */
    public List<Event> findByRepoId(Integer repoId) {
        return events.stream()
                .filter(e -> Objects.equals(e.getRepoId(), repoId))
                .sorted(Comparator.comparing(Event::getId))
                .toList();
    }

    /** All events for a given actor (user) ID, in ID order */
    public List<Event> findByActorId(Integer actorId) {
        return events.stream()
                .filter(e -> Objects.equals(e.getActorId(), actorId))
                .sorted(Comparator.comparing(Event::getId))
                .toList();
    }

    /** Remove all stored events (useful for tests or resets) */
    public void deleteAll() {
        events.clear();
        idCounter.set(1);
    }
}
