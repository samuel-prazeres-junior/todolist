package br.com.cursojava.todolist.task.controller;

import br.com.cursojava.todolist.task.TaskModel;
import br.com.cursojava.todolist.task.repository.ITaskRepository;
import br.com.cursojava.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private ITaskRepository repository;

    @PostMapping("/")
    @ResponseStatus(HttpStatus.OK)
    private TaskModel create(@RequestBody TaskModel taskModel, HttpServletRequest request){
        var currentData = LocalDateTime.now();
        if (currentData.isAfter(taskModel.getStartAt()) || currentData.isAfter(taskModel.getEndtAt())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        if (taskModel.getStartAt().isAfter(taskModel.getEndtAt())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data de inicio deve ser menor que a data de término");

        taskModel.setId((UUID) request.getAttribute("idUser"));
        return this.repository.save(taskModel);
    }
    @GetMapping("/")
    public List<TaskModel> list(HttpServletRequest request){
        return this.repository.findByIdUser((UUID) request.getAttribute("idUser"));
    }

    @PutMapping("/{id}")
    public TaskModel update(@RequestBody TaskModel taskModel, HttpServletRequest request, @PathVariable UUID id){
       var task = this.repository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.BAD_REQUEST));

       if (!taskModel.getIdUser().equals(request.getAttribute("idUser"))) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        Utils.copyNonNullProperties(taskModel, task);
        return this.repository.save(task);
    }
}
