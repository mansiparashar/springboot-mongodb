package com.example.springbootmongodb.controllers;

import com.example.springbootmongodb.model.TodoDTO;
import com.example.springbootmongodb.repository.TodoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WebMvcTest(controllers = TodoController.class)
class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoRepository todoRepo;

    private static final String PATH = "/todos";

    private static TodoDTO todoDTO;

    @BeforeAll
    static void setupBeforeClass() {
        todoDTO = TodoDTO.builder()
                .todo("make unit test")
                .completed(Boolean.TRUE)
                .createdAt(new Date())
                .description("difficult")
                .updatedAt(new Date())
                .id("abc-123")
                .build();
    }

    @AfterAll
    static void teardownAfterClass() {
        todoDTO = null;
    }

    @Test
    void testGetAllTodos() throws Exception {

        when(todoRepo.findAll()).thenReturn(Arrays.asList(todoDTO));

        mockMvc.perform(get(PATH))
                .andDo(print())
                .andExpect(status().isOk());
    }


    @Test
    void testGetAllTodosNotFound() throws Exception {

        when(todoRepo.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get(PATH))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetTodo() throws Exception {
        when(todoRepo.findById(anyString())).thenReturn(Optional.of(todoDTO));

        mockMvc.perform(get(PATH + "/{id}", "abc-123"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void testGetTodoNotFound() throws Exception {
        when(todoRepo.findById(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(get(PATH + "/{id}", "abc-123"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateTodo() throws Exception {

        when(todoRepo.save(any(TodoDTO.class))).thenReturn(todoDTO);

        mockMvc.perform(post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(todoDTO)))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
