package com.example.moviesapp.api

import com.google.gson.annotations.SerializedName


data class ExampleJson2KtKotlin (

  @SerializedName("data"     ) var data     : ArrayList<Data> = arrayListOf(),
  @SerializedName("metadata" ) var metadata : Metadata?       = Metadata()

)