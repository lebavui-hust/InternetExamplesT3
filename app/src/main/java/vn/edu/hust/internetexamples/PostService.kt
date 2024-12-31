package vn.edu.hust.internetexamples

import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface PostService {
  @GET("posts")
  suspend fun getAllPosts(): List<Post>

  @GET("posts/{postId}")
  suspend fun getPost(@Path("postId") postId: Int): Post

  @POST("posts")
  suspend fun createPost(post: Post)

  @PUT("posts/{postId}")
  suspend fun updatePost(@Path("postId") postId: Int, post: Post)

  @DELETE("posts/{postId}")
  suspend fun deletePost(@Path("postId") postId: Int)
}