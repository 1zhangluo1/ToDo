package com.example.needtodo
import com.google.gson.annotations.SerializedName


data class ChatResponse(
    @SerializedName("choices")
    var choices: List<Choice>,
    @SerializedName("created")
    var created: Int,
    @SerializedName("id")
    var id: String,
    @SerializedName("model")
    var model: String,
    @SerializedName("object")
    var objectX: String,
    @SerializedName("system_fingerprint")
    var systemFingerprint: String,
    @SerializedName("usage")
    var usage: Usage
)

data class Choice(
    @SerializedName("finish_reason")
    var finishReason: String,
    @SerializedName("index")
    var index: Int,
    @SerializedName("message")
    var message: ResponseMessage
)

data class Usage(
    @SerializedName("completion_tokens")
    var completionTokens: Int,
    @SerializedName("prompt_tokens")
    var promptTokens: Int,
    @SerializedName("total_tokens")
    var totalTokens: Int
)

data class ResponseMessage(
    @SerializedName("content")
    var content: String,
    @SerializedName("role")
    var role: String
)