package ru.profapp.androidacademy.Data.network

enum class State {
    HasData,
    HasNoData,
    Loading,
    NetworkError,
    ServerError
}