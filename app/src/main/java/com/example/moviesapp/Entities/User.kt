package com.example.moviesapp.Entities


class User {
    var user_name:String?=null
    var email:String?=null
    constructor(user_name: String,email:String){
        this.user_name=user_name
        this.email=email
    }
}