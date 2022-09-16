package com.example.demandmanagement.model

class DemandEntity(
    val userId: String,
    val demandId: String,
    val dmDesgn: String,
    val yoe: Int,
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