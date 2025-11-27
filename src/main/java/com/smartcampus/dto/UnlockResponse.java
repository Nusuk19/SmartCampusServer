package com.smartcampus.dto;

public class UnlockResponse {
    private String status;
    private String roomName;
    private String message;
    private long accessLogId;

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getRoomName() { return roomName; }
    public void setRoomName(String roomName) { this.roomName = roomName; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public long getAccessLogId() { return accessLogId; }
    public void setAccessLogId(long accessLogId) { this.accessLogId = accessLogId; }
}
