package com.example.needtodo;

import android.os.Parcel;
import android.os.Parcelable;

public class ToDoList implements Parcelable {
    private String thing;
    private int imageID;
    private String deadline;
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDeadline() {
        return deadline;
    }

    public ToDoList(String thing, int imageID, String deadline, long id) {
        this.thing = thing;
        this.imageID = imageID;
        this.deadline = deadline;
        this.id = id;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    protected ToDoList(Parcel in) {
        thing = in.readString();
        imageID = in.readInt();
        deadline = in.readString();
        id = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(thing);
        dest.writeInt(imageID);
        dest.writeString(deadline);
        dest.writeLong(id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ToDoList> CREATOR = new Creator<ToDoList>() {
        @Override
        public ToDoList createFromParcel(Parcel in) {
            return new ToDoList(in);
        }

        @Override
        public ToDoList[] newArray(int size) {
            return new ToDoList[size];
        }
    };

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

    public String getThing() {
        return thing;
    }

    public void setThing(String thing) {
        this.thing = thing;
    }

    @Override
    public String toString() {
        return "ToDoList{" +
                "thing='" + thing + '\'' +
                ", imageID=" + imageID +
                ", deadline='" + deadline + '\'' +
                ", id=" + id +
                '}';
    }
}
