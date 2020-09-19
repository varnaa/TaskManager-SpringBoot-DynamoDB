package com.varnaa.taskManager.controller;

import com.varnaa.taskManager.model.Tasks;
import com.varnaa.taskManager.repository.TasksRepository;
import org.apache.http.protocol.HTTP;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;

@RestController
public class TaskController {

    private final TasksRepository repository;

    public TaskController(TasksRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/new")
    public Tasks saveTask(@RequestBody Tasks task) {
        return repository.save(task);
    }

    @GetMapping("/findAll")
    public @ResponseBody
    List<Tasks> findAll() {
        return (List<Tasks>) repository.findAll();
    }

    @GetMapping("find/{id}")
    public @ResponseBody
    Optional<Tasks> findByID(@PathVariable("id") String id) {
        return repository.findById(id);
    }

    @DeleteMapping("delete/{id}")
    public void deleteById(@PathVariable("id") String id) {
        repository.deleteById(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity updateTask(@PathVariable("id") String id, @RequestBody Tasks task) {
        Optional<Tasks> receivedTask = repository.findById(id);
        if (receivedTask.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error id not found" + id);
        } else {
            receivedTask.get().setName(task.getName());
            receivedTask.get().setCompleted(task.isCompleted());
            receivedTask.get().setDate(task.getDate());
            repository.save(receivedTask.get());
            return ResponseEntity.status(HttpStatus.OK).body("Task updated successfully");
        }
    }
}
