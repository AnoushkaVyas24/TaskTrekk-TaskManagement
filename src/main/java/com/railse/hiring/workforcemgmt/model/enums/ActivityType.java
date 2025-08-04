package com.railse.hiring.workforcemgmt.model.enums;

public enum ActivityType {
    TASK_CREATED("Task created"),
    TASK_ASSIGNED("Task assigned"),
    TASK_REASSIGNED("Task reassigned"),
    TASK_STATUS_CHANGED("Task status changed"),
    TASK_PRIORITY_CHANGED("Task priority changed"),
    TASK_CANCELLED("Task cancelled"),
    COMMENT_ADDED("Comment added");

    private final String description;

    ActivityType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

