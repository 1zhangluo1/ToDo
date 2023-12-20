package com.example.needtodo
import com.google.gson.annotations.SerializedName


data class OpenAiRequest(
    @SerializedName("messages")
    var messages: List<ResponseMessage>,
    @SerializedName("model")
    var model: String,
    @SerializedName("stream")
    var stream: Boolean
)

data class Message(
    @SerializedName("content")
    var content: String,
    @SerializedName("role")
    var role: String
)