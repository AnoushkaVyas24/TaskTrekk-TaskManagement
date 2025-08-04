package com.railse.hiring.workforcemgmt.service.impl;


import com.railse.hiring.workforcemgmt.common.exception.ResourceNotFoundException;
import com.railse.hiring.workforcemgmt.dto.*;
import com.railse.hiring.workforcemgmt.mapper.ITaskManagementMapper;
import com.railse.hiring.workforcemgmt.model.TaskManagement;
import com.railse.hiring.workforcemgmt.model.enums.Task;
import com.railse.hiring.workforcemgmt.model.enums.TaskStatus;
import com.railse.hiring.workforcemgmt.repository.TaskRepository;
import com.railse.hiring.workforcemgmt.service.TaskManagementService;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
public class TaskManagementServiceImpl implements TaskManagementService {

    private final TaskRepository taskRepository;
    private final ITaskManagementMapper taskMapper;

    public TaskManagementServiceImpl(TaskRepository taskRepository, ITaskManagementMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    @Override
    public TaskManagementDto findTaskById(Long id) {
        TaskManagement task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
        return taskMapper.modelToDto(task);
    }

    @Override
    public List<TaskManagementDto> createTasks(TaskCreateRequest createRequest) {

        List<TaskManagement> createdTasks = new ArrayList<>();
        for (TaskCreateRequest.RequestItem item : createRequest.getRequests()) {
            TaskManagement newTask = new TaskManagement();
            newTask.setReferenceId(item.getReferenceId());
            newTask.setReferenceType(item.getReferenceType());
            newTask.setTask(item.getTask());
            newTask.setAssigneeId(item.getAssigneeId());
            newTask.setPriority(item.getPriority());
            newTask.setTaskDeadlineTime(item.getTaskDeadlineTime());
            newTask.setStatus(TaskStatus.ASSIGNED);
            newTask.setDescription("New task created.");

            //Log Activity:
            newTask.setActivityHistory(new ArrayList<>());
            newTask.getActivityHistory().add("Task created with priority: " + newTask.getPriority());
            newTask.setComments(new ArrayList<>());

            createdTasks.add(taskRepository.save(newTask));
        }

        return taskMapper.modelListToDtoList(createdTasks);
    }



    @Override
    public List<TaskManagementDto> updateTasks(UpdateTaskRequest updateRequest) {
        List<TaskManagement> updatedTasks = new ArrayList<>();
        for (UpdateTaskRequest.RequestItem item : updateRequest.getRequests()) {
            TaskManagement task = taskRepository.findById(item.getTaskId())
                    .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + item.getTaskId()));


            if (item.getTaskStatus() != null) {
                task.setStatus(item.getTaskStatus());
                if (task.getActivityHistory() == null) task.setActivityHistory(new ArrayList<>());
                task.getActivityHistory().add("Task status updated to: " + item.getTaskStatus());
            }
            if (item.getDescription() != null) {
                task.setDescription(item.getDescription());
            }
            updatedTasks.add(taskRepository.save(task));
        }
        return taskMapper.modelListToDtoList(updatedTasks);
    }


    @Override
    public String assignByReference(AssignByReferenceRequest request) {
        List<Task> applicableTasks = Task.getTasksByReferenceType(request.getReferenceType());
        List<TaskManagement> existingTasks = taskRepository.findByReferenceIdAndReferenceType(request.getReferenceId(), request.getReferenceType());

        for (Task taskType : applicableTasks) {
            List<TaskManagement> tasksOfType = existingTasks.stream()
                    .filter(t -> t.getTask() == taskType && t.getStatus() != TaskStatus.COMPLETED)
                    .collect(Collectors.toList());

            boolean alreadyAssigned = false;

            for (TaskManagement task : tasksOfType) {
                if (Objects.equals(task.getAssigneeId(), request.getAssigneeId())) {
                    // Task already assigned to requested assignee — no need to reassign
                    alreadyAssigned = true;
                } else {
                    // Mark the previous task as CANCELLED
                    task.setStatus(TaskStatus.CANCELLED);
                    if (task.getActivityHistory() == null) task.setActivityHistory(new ArrayList<>());
                    task.getActivityHistory().add("Task cancelled during reassignment");
                    taskRepository.save(task);
                }
            }

            if (!alreadyAssigned) {
                // Create new task for new assignee
                TaskManagement newTask = new TaskManagement();
                newTask.setReferenceId(request.getReferenceId());
                newTask.setReferenceType(request.getReferenceType());
                newTask.setTask(taskType);
                newTask.setAssigneeId(request.getAssigneeId());
                newTask.setStatus(TaskStatus.ASSIGNED);

                //feature 3:
                newTask.setPriority(null);
                newTask.setTaskDeadlineTime(System.currentTimeMillis() + 86400000);
                newTask.setDescription("Task reassigned.");

                newTask.setActivityHistory(new ArrayList<>());
                newTask.getActivityHistory().add("Task assigned to user ID: " + request.getAssigneeId());
                newTask.setComments(new ArrayList<>());

                taskRepository.save(newTask);
            }
        }

        return "Tasks reassigned successfully for reference " + request.getReferenceId();
    }

    @Override
    public List<TaskManagementDto> fetchTasksByDate(TaskFetchByDateRequest request) {
        List<TaskManagement> tasks = taskRepository.findByAssigneeIdIn(request.getAssigneeIds());

        List<TaskManagement> filteredTasks = tasks.stream()
                .filter(task ->
                        task.getStatus() == TaskStatus.ASSIGNED &&
                                (
                                        (task.getTaskDeadlineTime() >= request.getStartDate() &&
                                                task.getTaskDeadlineTime() <= request.getEndDate())
                                                ||
                                                (task.getTaskDeadlineTime() < request.getStartDate())
                                )
                )
                .collect(Collectors.toList());

        return taskMapper.modelListToDtoList(filteredTasks);
    }


    @Override
    public TaskManagementDto updateTaskPriority(UpdatePriorityRequest request) {
        TaskManagement task = taskRepository.findById(request.getTaskId())
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + request.getTaskId()));

        task.setPriority(request.getPriority());
        if (task.getActivityHistory() == null) task.setActivityHistory(new ArrayList<>());
        task.getActivityHistory().add("Priority updated to: " + request.getPriority());

        return taskMapper.modelToDto(taskRepository.save(task));
    }

    @Override
    public TaskManagementDto addComment(AddCommentRequest request) {
        TaskManagement task = taskRepository.findById(request.getTaskId())
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + request.getTaskId()));

        if (task.getComments() == null) task.setComments(new ArrayList<>());
        if (task.getActivityHistory() == null) task.setActivityHistory(new ArrayList<>());

        task.getComments().add(request.getComment());
        task.getActivityHistory().add("Comment added: " + request.getComment());

        return taskMapper.modelToDto(taskRepository.save(task));
    }

}
