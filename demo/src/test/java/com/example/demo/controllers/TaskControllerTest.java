package com.example.demo.controllers;

import com.example.demo.models.Task;
import com.example.demo.services.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.example.demo.models.Task;

@WebMvcTest
class TaskControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    private TaskService taskService;
    @InjectMocks
    private TaskController taskController;
    private Task task1;
    private Task task2;
    private Task updateTask;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        task1 = new Task(1L,"first",true);
        task2 = new Task(2L,"second",false);
        updateTask = new Task(1L,"third",true);
    }
    @Test
    public void getAllTasks() throws Exception{
        List<Task> list = new ArrayList<>();
        list.add(task1);
        list.add(task2);

        when(taskService.getAllTask()).thenReturn(list);

        this.mockMvc.perform(get("/api/v1/tasks/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()",is(list.size())));
    }

    @Test
    void getAllCompletedTasks() throws Exception {
        List<Task> completedTasks = new ArrayList<>();
        completedTasks.add(task2);

        when(taskService.findAllCompletedTask()).thenReturn(completedTasks);

        this.mockMvc.perform(get("/api/v1/tasks/completed"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(completedTasks.size())))
                .andExpect(jsonPath("$[0].id", is(task2.getId().intValue())))
                .andExpect(jsonPath("$[0].task", is(task2.getTask())))
                .andExpect(jsonPath("$[0].completed", is(task2.isCompleted())));
    }

    @Test
    void getAllIncompleteTasks() throws Exception {
        List<Task> inCompletedTasks = new ArrayList<>();
        inCompletedTasks.add(task1);

        when(taskService.findAllInCompleteTask()).thenReturn(inCompletedTasks);

        this.mockMvc.perform(get("/api/v1/tasks/incomplete"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(inCompletedTasks.size())))
                .andExpect(jsonPath("$[0].id", is(task1.getId().intValue())))
                .andExpect(jsonPath("$[0].task", is(task1.getTask())))
                .andExpect(jsonPath("$[0].completed", is(task1.isCompleted())));
    }

    @Test
    void getSingleTask() throws Exception {
        // Mock the getProduct() method to return a product when given its ID
        when(taskService.findTaskById(any())).thenReturn(Optional.of(task1));

        // Perform the GET request and check the response status and content
        this.mockMvc.perform(get("/api/v1/tasks/{id}", task1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(task1.getId().intValue())))
                .andExpect(jsonPath("$.task", is(task1.getTask())))
                .andExpect(jsonPath("$.completed", is(task1.isCompleted())));
    }

    @Test
    void createTask() throws Exception {
        when(taskService.createNewTask(any(Task.class))).thenReturn(task1);

        this.mockMvc.perform(post("/api/v1/tasks/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(task1.getId().intValue())))
                .andExpect(jsonPath("$.task", is(task1.getTask())))
                .andExpect(jsonPath("$.completed", is(task1.isCompleted())));
    }

    @Test
    void updateTask() throws Exception {
        when(taskService.updateTask(any(Long.class), any(Task.class)))
                .thenReturn(Optional.ofNullable(updateTask));

        this.mockMvc.perform(put("/api/v1/tasks/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateTask)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(updateTask.getId().intValue())))
                .andExpect(jsonPath("$.task", is(updateTask.getTask())))
                .andExpect(jsonPath("$.completed", is(updateTask.isCompleted())));
    }

    @Test
    void deleteTaskById() throws Exception {
        when(taskService.deleteTask(any(Long.class)))
                .thenReturn(Optional.ofNullable(task1));
        this.mockMvc.perform(delete("/api/v1/tasks/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(task1.getId().intValue())))
                .andExpect(jsonPath("$.task", is(task1.getTask())))
                .andExpect(jsonPath("$.completed", is(task1.isCompleted())));
    }
}