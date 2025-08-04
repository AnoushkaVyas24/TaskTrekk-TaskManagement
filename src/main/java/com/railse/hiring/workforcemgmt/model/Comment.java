package com.railse.hiring.workforcemgmt.model;

import lombok.Data;

@Data
public class Comment {
    private Long id;
    private Long taskId;
    private String content;
    private Long userId; // The user who added the comment
    private Long timestamp;
}