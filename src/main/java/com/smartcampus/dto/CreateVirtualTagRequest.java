package com.smartcampus.dto;

public class CreateVirtualTagRequest {
    private String tagUid;
    private String name;

    public String getTagUid() { return tagUid; }
    public void setTagUid(String tagUid) { this.tagUid = tagUid; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}