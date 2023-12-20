package com.example.needtodo;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.List;

public class Selection{
    private String title;
    private List<SingleSelection> selections;

    public Selection(String title, List<SingleSelection> selections) {
        this.title = title;
        this.selections = selections;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<SingleSelection> getSelections() {
        return selections;
    }

    public void setSelections(List<SingleSelection> selections) {
        this.selections = selections;
    }

    public static class SingleSelection implements Parcelable{
        private String headline;

        private long id;

        protected SingleSelection(Parcel in) {
            headline = in.readString();
            id = in.readLong();
        }

        public static final Creator<SingleSelection> CREATOR = new Creator<SingleSelection>() {
            @Override
            public SingleSelection createFromParcel(Parcel in) {
                return new SingleSelection(in);
            }

            @Override
            public SingleSelection[] newArray(int size) {
                return new SingleSelection[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(@NonNull Parcel dest, int flags) {
            dest.writeString(headline);
            dest.writeLong(id);
        }

        public SingleSelection(String headline, long id) {
            this.headline = headline;
            this.id = id;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public SingleSelection(String headline) {
            this.headline = headline;
        }

        public String getHeadline() {
            return headline;
        }

        public void setHeadline(String headline) {
            this.headline = headline;
        }

        @Override
        public String toString() {
            return "SingleSelection{" +
                    "headline='" + headline + '\'' +
                    ", id=" + id +
                    '}';
        }
    }
}
