package com.example.springbootmongodb.controllers;

import com.example.springbootmongodb.model.TodoDTO;
import com.example.springbootmongodb.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RequestMapping("/todos")
@RestController
public class TodoController {
    @Autowired
    private TodoRepository todoRepo;

    @GetMapping
    public ResponseEntity<?> getAllTodos() {
        List<TodoDTO> todos = todoRepo.findAll();
        return !CollectionUtils.isEmpty(todos) ? ResponseEntity.ok(todos) :
                new ResponseEntity<>("No todos available", HttpStatus.NOT_FOUND);
    }


    @PostMapping
    public ResponseEntity<?> createTodo(@RequestBody TodoDTO todo) {
        try {
            todo.setCreatedAt(new Date(System.currentTimeMillis()));
            todoRepo.save(todo);
            return new ResponseEntity<>(todo, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTodo(@PathVariable("id") String id) {
        return todoRepo.findById(id).map(ResponseEntity::ok)
                .orElse(new ResponseEntity("Does not exist " + id, HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTodoById(@PathVariable("id") String id, @RequestBody TodoDTO todo) {
        Optional<TodoDTO> todoOptional = todoRepo.findById(id);
        if (todoOptional.isPresent()) {
            TodoDTO t = todoOptional.get();
            t.setCompleted(todo.getCompleted() != null ? todo.getCompleted() : t.getCompleted());
            t.setTodo(todo.getTodo() != null ? todo.getTodo() : t.getTodo());
            t.setDescription(todo.getDescription() != null ? todo.getDescription() : t.getDescription());
            t.setUpdatedAt(new Date(System.currentTimeMillis()));
            todoRepo.save(t);
            return new ResponseEntity<>(t, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Does not exist " + id, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTodoById(@PathVariable("id") String id) {
        Optional<TodoDTO> todoOptional = todoRepo.findById(id);
        try {
            todoRepo.deleteById(id);
            return new ResponseEntity<>("Deleted Successfully " + id, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}




