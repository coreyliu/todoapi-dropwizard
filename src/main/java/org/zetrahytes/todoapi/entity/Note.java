package org.zetrahytes.todoapi.entity;

import lombok.Data;

@Data
public class Note {
    private final String id;
    private final String content;
}
