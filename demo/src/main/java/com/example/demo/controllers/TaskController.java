package com.example.demo.controllers;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.models.Task;
import com.example.demo.services.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api/v1/tasks")
public class TaskController {

    @Autowired
    TaskService taskService;

    @GetMapping("/")
    public ResponseEntity<Object> getAllTasks(@RequestParam(required = false) Long id) {
        Collection<Task> result;

        if (id == null) {
            result = taskService.getAllTask();
        } else {
            Optional<Task> taskOptional = taskService.findTaskById(id);
            result = taskOptional.map(Collections::singletonList).orElse(Collections.emptyList());
        }

        if (!result.isEmpty()) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            throw new ResourceNotFoundException(id);
        }
    }

    @GetMapping("/completed")
    public ResponseEntity<Object> getAllCompletedTasks(@RequestParam(required = false) Long id) {
        List<Task> completedTasks = taskService.findAllCompletedTask();

        if (completedTasks != null && !completedTasks.isEmpty()) {
            return ResponseEntity.ok(completedTasks);
        } else {
            throw new ResourceNotFoundException(id);
        }
    }

    @GetMapping("/incomplete")
    public ResponseEntity<Object> getAllIncompleteTasks(@RequestParam(required = false) Long id) {
        List<Task> inCompletedTasks = taskService.findAllInCompleteTask();
        if (inCompletedTasks != null && !inCompletedTasks.isEmpty()) {
            return ResponseEntity.ok(inCompletedTasks);
        } else {
            throw new ResourceNotFoundException(id);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getSingleTask(@PathVariable("id") Long id) {
        Task result = taskService.findTaskById(id).orElseThrow(() -> new ResourceNotFoundException(id));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<Object> createTask(@Valid @RequestBody Task task) {
        Task result = taskService.createNewTask(task);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateTask(@PathVariable("id") Long id, @Valid @RequestBody Task task) {
        Task result = taskService.updateTask(id, task).orElseThrow(() -> new ResourceNotFoundException(id));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTaskById(@PathVariable("id") Long id) {
        Task result = taskService.deleteTask(id).orElseThrow(() -> new ResourceNotFoundException(id));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
