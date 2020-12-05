package com.divaltor.notes.db;

import android.os.Parcel;
import android.os.Parcelable;

public class Note implements Parcelable {
    /**
     * Unique note id, for coloring purposes or to query from DB
     */
    public int id = -1;

    /**
     * Index to move them in the future, idk
     */
    public int index = -1;

    /**
     * Title(Upper line) of the note
     */
    public String title;

    /**
     * Text of the note
     */
    public String text;

    public Note() {}

    protected Note(Parcel parcel) {
        id = parcel.readInt();
        index = parcel.readInt();
        title = parcel.readString();
        text = parcel.readString();
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel parcel) {
            return new Note(parcel);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(index);
        dest.writeString(title);
        dest.writeString(text);
    }
}
