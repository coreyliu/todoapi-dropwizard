package org.zetrahytes.todoapi.entity;

public class Note {

    private String id;
    private String content;

    public Note() {
    }

    public Note(String uuid, String content) {
        this.id = uuid;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}