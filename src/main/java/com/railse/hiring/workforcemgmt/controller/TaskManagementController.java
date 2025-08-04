package com.railse.hiring.workforcemgmt.controller;


import com.railse.hiring.workforcemgmt.common.model.response.Response;
import com.railse.hiring.workforcemgmt.dto.*;
import com.railse.hiring.workforcemgmt.mapper.ITaskManagementMapper;
import com.railse.hiring.workforcemgmt.model.TaskManagement;
import com.railse.hiring.workforcemgmt.model.enums.Priority;
import com.railse.hiring.workforcemgmt.model.enums.TaskStatus;
import com.railse.hiring.workforcemgmt.repository.TaskRepository;
import com.railse.hiring.workforcemgmt.service.TaskManagementService;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/task-mgmt")
public class TaskManagementController {

    private final TaskManagementService taskManagementService;
    private final TaskRepository taskRepository;
    private final ITaskManagementMapper taskMapper;


    public TaskManagementController(TaskManagementService taskManagementService,
                                    TaskRepository taskRepository,
                                    ITaskManagementMapper taskMapper) {
        this.taskManagementService = taskManagementService;
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }


    @GetMapping("/{id}")
    public Response<TaskManagementDto> getTaskById(@PathVariable Long id) {
        return new Response<>(taskManagementService.findTaskById(id));
    }


    @PostMapping("/create")
    public Response<List<TaskManagementDto>> createTasks(@RequestBody TaskCreateRequest request) {
        return new Response<>(taskManagementService.createTasks(request));
    }


    @PostMapping("/update")
    public Response<List<TaskManagementDto>> updateTasks(@RequestBody UpdateTaskRequest request) {
        return new Response<>(taskManagementService.updateTasks(request));
    }


    @PostMapping("/assign-by-ref")
    public Response<String> assignByReference(@RequestBody AssignByReferenceRequest request) {
        return new Response<>(taskManagementService.assignByReference(request));
    }

    @PostMapping("/fetch-by-date/v2")
    public Response<List<TaskManagementDto>> fetchByDate(@RequestBody TaskFetchByDateRequest request) {
        return new Response<>(taskManagementService.fetchTasksByDate(request));
    }

    //feature 2 - a
    @PostMapping("/update-priority")
    public Response<TaskManagementDto> updatePriority(@RequestBody UpdatePriorityRequest request) {
        return new Response<>(taskManagementService.updateTaskPriority(request));
    }

    //feature 2 - b
    @GetMapping("/priority/{priority}")
    public Response<List<TaskManagementDto>> getTasksByPriority(@PathVariable Priority priority) {
        List<TaskManagement> filtered = taskRepository.findAll().stream()
                .filter(task -> task.getPriority() == priority && task.getStatus() != TaskStatus.CANCELLED)
                .collect(Collectors.toList());

        return new Response<>(taskMapper.modelListToDtoList(filtered));
    }

    @PostMapping("/add-comment")
    public Response<TaskManagementDto> addComment(@RequestBody AddCommentRequest request) {
        return new Response<>(taskManagementService.addComment(request));
    }

    @GetMapping("/all")
    public Response<List<TaskManagementDto>> getAllTasks() {
        List<TaskManagement> tasks = taskRepository.findAll();
        return new Response<>(taskMapper.modelListToDtoList(tasks));
    }


}