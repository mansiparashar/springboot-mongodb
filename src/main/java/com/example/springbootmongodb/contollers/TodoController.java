package com.example.springbootmongodb.contollers;

import com.example.springbootmongodb.model.TodoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import com.example.springbootmongodb.repository.TodoRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class TodoController {
    @Autowired
    private TodoRepository todoRepo;

    @GetMapping("/todos")
    public ResponseEntity<?> getAllTodos() {
        List<TodoDTO> todos = todoRepo.findAll();
        if (todos.size() > 0) {
            return new ResponseEntity<List<TodoDTO>>(todos, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No todos available", HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping("/todos")
        public ResponseEntity<?> createTodo(@RequestBody TodoDTO todo){
            try{
                todo.setCreatedAt(new Date(System.currentTimeMillis()));
                todoRepo.save(todo);
                return new ResponseEntity<TodoDTO>(todo,HttpStatus.OK);
        }catch(Exception e){
                return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
            }
    }

    @GetMapping("/todos/{id}")
    public ResponseEntity<?> getTodo(@PathVariable("id") String id){
        Optional<TodoDTO> todoOptional = todoRepo.findById(id);
        if(todoOptional.isPresent()){
            return new ResponseEntity<>(todoOptional.get(),HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("Does not exist "+id,HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/todos/{id}")
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
        @DeleteMapping("/todos/{id}")

                public ResponseEntity<?> deleteTodoById(@PathVariable("id") String id){
            Optional<TodoDTO> todoOptional = todoRepo.findById(id);
            try{
                todoRepo.deleteById(id);
                return new ResponseEntity<>("Deleted Successfully "+id,HttpStatus.OK);
            }
            catch(Exception e){
                return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
            }
    }

    }




