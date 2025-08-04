package com.railse.hiring.workforcemgmt.model;

import com.railse.hiring.workforcemgmt.model.enums.ActivityType;
import lombok.Data;

@Data
public class Activity {
    private Long id;
    private Long taskId;
    private ActivityType activityType;
    private String description;
    private Long userId; // The user who performed the action
    private Long timestamp;
    private String oldValue; // For tracking changes (e.g., old status)
    private String newValue; // For tracking changes (e.g., new status)
}