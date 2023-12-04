package com.example.needtodo;

import org.litepal.crud.LitePalSupport;

public class ThingsList extends LitePalSupport {
    private long id;
    private boolean setTop;
    private boolean isDone;
    private User user;
    private String things;
    private String headline;
    private String deadline;
    private boolean isOutDate;
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public boolean isOutDate() {
        return isOutDate;
    }

    public void setOutDate(boolean outDate) {
        isOutDate = outDate;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public boolean isSetTop() {
        return setTop;
    }

    public void setSetTop(boolean setTop) {
        this.setTop = setTop;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getThings() {
        return things;
    }

    public void setThings(String things) {
        this.things = things;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    @Override
    public String toString() {
        return "ThingsList{" +
                "id=" + id +
                ", user=" + user +
                ", things='" + things + '\'' +
                ", headline='" + headline + '\'' +
                ", deadline='" + deadline + '\'' +
                '}';
    }
}
