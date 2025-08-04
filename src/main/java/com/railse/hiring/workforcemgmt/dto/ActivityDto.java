package com.railse.hiring.workforcemgmt.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.railse.hiring.workforcemgmt.model.enums.ActivityType;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ActivityDto {
    private Long id;
    private Long taskId;
    private ActivityType activityType;
    private String description;
    private Long userId;
    private Long timestamp;
    private String oldValue;
    private String newValue;
}