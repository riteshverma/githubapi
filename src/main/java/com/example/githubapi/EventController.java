package com.example.githubapi;//package com.example.githubapi;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
//@RequestMapping("/events")
public class EventController {

    private static final List<String> VALID_TYPES =
            Arrays.asList("PushEvent", "ReleaseEvent", "WatchEvent");

    private final InMemoryEventRepository repository;

    public EventController(InMemoryEventRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/events")
    public ResponseEntity<?> createEvent(@RequestBody Event event) {
        ResponseEntity<?> result;
        if (!VALID_TYPES.contains(event.getType())) {
            result = ResponseEntity
                    .badRequest()
                    .body("Invalid event type: " + event.getType());
        } else {
            Event saved = repository.save(event);
            result = ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(saved);
        }
        return result;
    }

    @GetMapping("/events")
    public List<Event> getAllEvents() {
        return repository.findAll();
    }

    @GetMapping("/events/{eventId}")
    public ResponseEntity<Event> getEventById(@PathVariable Integer eventId) {
        return repository.findById(eventId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/repos/{repoId}/events")
    public ResponseEntity<List<Event>> getEventsByRepo(@PathVariable Integer repoId) {
        return findByCriteria(() -> repository.findByRepoId(repoId));
    }

    @GetMapping("/users/{userId}/events")
    public ResponseEntity<List<Event>> getEventsByUser(@PathVariable Integer userId) {
        return findByCriteria(() -> repository.findByActorId(userId));
    }

    private ResponseEntity<List<Event>> findByCriteria(Supplier<List<Event>> finder) {
        List<Event> list = finder.get();
        return list.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(list);
    }

    @FunctionalInterface
    private interface Supplier<T> { T get(); }
}
