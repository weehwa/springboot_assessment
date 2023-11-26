package com.example.demo.services;

import com.example.demo.models.Task;
import com.example.demo.repositories.TaskRepository;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    public Task createNewTask(Task task) {
        return taskRepository.save(task);
    }

    public List<Task> getAllTask() {
        List<Task> result;
        result = new ArrayList<>();
        taskRepository.findAll().forEach(result::add);
        return result;
    }
    public Optional<Task> findTaskById(Long id) {
        return taskRepository.findById(id);
    }
    public List<Task> findAllCompletedTask() {
        return taskRepository.findByCompletedTrue();
    }

    public List<Task> findAllInCompleteTask() {
        return taskRepository.findByCompletedFalse();
    }

    public Optional<Task> deleteTask(Long id) {
        Optional<Task> taskToDelete = taskRepository.findById(id);
        if (taskToDelete.isPresent()) {
            taskRepository.deleteById(id);
        }
        return taskToDelete;
    }

    public Optional<Task> updateTask(Long id, Task task) {
        // check if task is more than 2 letter
        if (task.getTask() == null || task.getTask().length() < 2) {
            throw new ConstraintViolationException("Task should have at least 2 characters", null);
        }

        Optional<Task> result = taskRepository.findById(id);
        return result.map(existingTask -> {
            existingTask.setTask(task.getTask());
            existingTask.setCompleted(task.isCompleted());
            return taskRepository.save(existingTask);
        });
    }
}