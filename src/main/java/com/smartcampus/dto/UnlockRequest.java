package com.smartcampus.dto;

public class UnlockRequest {
    private String tagUid;
    private String readerId;

    public String getTagUid() { return tagUid; }
    public void setTagUid(String tagUid) { this.tagUid = tagUid; }

    public String getReaderId() { return readerId; }
    public void setReaderId(String readerId) { this.readerId = readerId; }
}