package com.feliii.alpvp.model

data class UserResponse(
    val data: UserModel
)

//cz only take username and token
data class UserModel (
    val username: String,
    val token: String?
)