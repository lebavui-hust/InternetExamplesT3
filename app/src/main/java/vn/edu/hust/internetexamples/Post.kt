package vn.edu.hust.internetexamples

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Post(
  val id: Int,
  val userId: Int,
  val title: String,
  val body: String
)
