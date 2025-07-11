package com.example.githubapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EventController.class)
class EventControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private InMemoryEventRepository repo;

    private Event makeEvent() {
        Event e = new Event();
        e.setType("PushEvent");
        e.setIsPublic(true);
        e.setRepoId(100);
        e.setActorId(200);
        return e;
    }

    @Test
    void createEvent() {
    }

    @Test
    void getAllEvents() {
    }

    @Test
    void getEventById() {
    }

    @Test
    void getEventsByRepo() {
    }

    @Test
    void getEventsByUser() {
    }

    @Nested
    @DisplayName("POST /events")
    class CreateEvent {

        @Test
        @DisplayName("should create valid event")
        void createValid() throws Exception {
            Event input = makeEvent();
            when(repo.save(any(Event.class))).thenAnswer(inv -> {
                Event saved = inv.getArgument(0);
                saved.setId(1);
                return saved;
            });

            mvc.perform(post("/events")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(input)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.type").value("PushEvent"));
        }

        @Test
        @DisplayName("should reject invalid type")
        void createInvalid() throws Exception {
            Event bad = makeEvent();
            bad.setType("InvalidType");

            mvc.perform(post("/events")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(bad)))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("Invalid event type: InvalidType"));
        }
    }

    @Nested
    @DisplayName("GET /events")
    class GetAll {

        @Test
        @DisplayName("should return list of events")
        void getAll() throws Exception {
            Event e = makeEvent();
            e.setId(5);
            when(repo.findAll()).thenReturn(List.of(e));

            mvc.perform(get("/events"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].id").value(5));
        }
    }

    @Nested
    @DisplayName("GET /events/{id}")
    class GetById {

        @Test
        @DisplayName("found")
        void found() throws Exception {
            Event e = makeEvent();
            e.setId(10);
            when(repo.findById(10)).thenReturn(Optional.of(e));

            mvc.perform(get("/events/10"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(10));
        }

        @Test
        @DisplayName("not found")
        void notFound() throws Exception {
            when(repo.findById(99)).thenReturn(Optional.empty());

            mvc.perform(get("/events/99"))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("GET /repos/{repoId}/events")
    class GetByRepo {

        @Test
        @DisplayName("found")
        void found() throws Exception {
            Event e = makeEvent();
            e.setId(20);
            when(repo.findByRepoId(300)).thenReturn(List.of(e));

            mvc.perform(get("/events/repos/300/events"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].id").value(20));
        }

        @Test
        @DisplayName("not found")
        void notFound() throws Exception {
            when(repo.findByRepoId(301)).thenReturn(List.of());

            mvc.perform(get("/events/repos/301/events"))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("GET /users/{userId}/events")
    class GetByUser {

        @Test
        @DisplayName("found")
        void found() throws Exception {
            Event e = makeEvent();
            e.setId(30);
            when(repo.findByActorId(400)).thenReturn(List.of(e));

            mvc.perform(get("/events/users/400/events"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].id").value(30));
        }

        @Test
        @DisplayName("not found")
        void notFound() throws Exception {
            when(repo.findByActorId(401)).thenReturn(List.of());

            mvc.perform(get("/events/users/401/events"))
                    .andExpect(status().isNotFound());
        }
    }
}
