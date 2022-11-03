package com.example.demandmanagement.model

import java.time.LocalDateTime
import java.util.*

class CommentEntity(
    val commentId: String,
    val comment: String,
    val userId: String,
    val name: String,
    val fulfilledQtyCmt: ArrayList<FulfilEntity>?,
    val date: String,
    val active: Boolean
) {
}