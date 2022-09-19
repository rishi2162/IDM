package com.example.demandmanagement.model

import java.util.*
import kotlin.collections.ArrayList

class DemandEntity(
    val userId: String,
    val demandId: String,
    val dmDesgn: String,
    val yoe: String,
    val requiredQty: Int,
    val skills: String,
    val desc: String,
    val location: String,
    val recipients: String,
    val date: String,
    val dueDate: String,
    val status: String,
    val fulfilledQty: Int,
    val shift: String,
    val priority: String,
    val comment: ArrayList<CommentEntity>

) {
}