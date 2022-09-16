package com.example.demandmanagement.model

class NewDemand(
    var design:String,
    var exp:String,
    var skills:String,
    var desc:String,
    var location:String,
    var priority : String,
    var shift : String,
    var dueDate : String,
    var nReq : Int,
    var receipients : String? = null
) {
}