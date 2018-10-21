package ru.profapp.androidacademy.Models

import android.os.Parcel
import android.os.Parcelable
import java.util.*

class NewsItem : Parcelable {
    val title: String?
    val imageUrl: String?
    val category: String?
    val publishDate: Date?
    val fullText: String?
    val url: String?

    constructor(title: String? = null, imageUrl: String? = null, category: String? = null, publishDate: Date? = null, fullText: String? = null, url: String? = null) {
        this.title = title
        this.imageUrl = imageUrl
        this.category = category
        this.publishDate = publishDate
        this.fullText = fullText
        this.url = url
    }

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()?.toLong()?.let { Date(it) },
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(imageUrl)
        parcel.writeString(category)
        parcel.writeString(publishDate?.time.toString())
        parcel.writeString(fullText)
        parcel.writeString(url)
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

