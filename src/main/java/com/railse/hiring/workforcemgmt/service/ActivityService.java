package com.railse.hiring.workforcemgmt.service;

import com.railse.hiring.workforcemgmt.dto.ActivityDto;
import com.railse.hiring.workforcemgmt.dto.AddCommentRequest;
import com.railse.hiring.workforcemgmt.dto.CommentDto;
import com.railse.hiring.workforcemgmt.model.TaskManagement;
import com.railse.hiring.workforcemgmt.model.enums.ActivityType;

import java.util.List;

public interface ActivityService {
    void logActivity(Long taskId, ActivityType activityType, String description, Long userId, String oldValue, String newValue);
    CommentDto addComment(AddCommentRequest request);
    List<ActivityDto> getTaskActivities(Long taskId);
    List<CommentDto> getTaskComments(Long taskId);
    TaskManagement getTaskDetails(Long taskId);
}