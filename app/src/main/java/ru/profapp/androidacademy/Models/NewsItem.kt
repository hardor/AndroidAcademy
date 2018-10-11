package ru.profapp.androidacademy.Models

import android.os.Parcel
import android.os.Parcelable
import java.util.*

class NewsItem(val title: String, val imageUrl: String, val category: Category, val publishDate: Date, val previewText: String, val fullText: String) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            Category(parcel.readInt(), parcel.readString()),
            Date(parcel.readLong()),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(imageUrl)

        //Is it good method to write Category ?
        parcel.writeInt(category.id)
        parcel.writeString(category.name)


        parcel.writeLong(publishDate.time)
        parcel.writeString(previewText)
        parcel.writeString(fullText)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NewsItem> {
        override fun createFromParcel(parcel: Parcel): NewsItem {
            return NewsItem(parcel)
        }

        override fun newArray(size: Int): Array<NewsItem?> {
            return arrayOfNulls(size)
        }
    }
}

