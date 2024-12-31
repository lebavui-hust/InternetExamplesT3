package vn.edu.hust.internetexamples

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import vn.edu.hust.internetexamples.databinding.ActivityMainBinding
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

  val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()
  val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl("https://jsonplaceholder.typicode.com/")
    .build()
  val service = retrofit.create(PostService::class.java)

  private lateinit var binding: ActivityMainBinding
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.main)

    binding.buttonGet.setOnClickListener {
      lifecycleScope.launch(Dispatchers.IO) {
        val url = URL("https://hust.edu.vn")
        val conn = url.openConnection() as HttpURLConnection

        val responseCode = conn.responseCode
        Log.v("TAG", "Response code: $responseCode")

        val reader = conn.inputStream.reader()
        val content = reader.readText()
        reader.close()

        Log.v("TAG", "$content")
      }
    }

    binding.buttonDownload.setOnClickListener {
      lifecycleScope.launch(Dispatchers.IO) {
        val url = URL("https://lebavui.github.io/videos/ecard.mp4")
        val conn = url.openConnection() as HttpURLConnection

        val responseCode = conn.responseCode
        Log.v("TAG", "Response code: $responseCode")
        val total = conn.contentLength

        val reader = conn.inputStream
        val writer = openFileOutput("download.mp4", MODE_PRIVATE)

        val buffer = ByteArray(2048)
        var downloaded = 0

        while (true) {
          val len = reader.read(buffer)
          if (len <= 0)
            break
          writer.write(buffer, 0, len)
          downloaded += len

          withContext(Dispatchers.Main) {
            binding.textProgress.text = "$downloaded / $total"
            binding.progressBar.max = total
            binding.progressBar.progress = downloaded
          }
        }

        Log.v("TAG", "Done.")

        writer.close()
        reader.close()
      }
    }

    binding.buttonPost.setOnClickListener {
      lifecycleScope.launch(Dispatchers.IO) {
        val url = URL("https://httpbin.org/post")
        val conn = url.openConnection() as HttpURLConnection

        conn.requestMethod = "POST"
        conn.doOutput = true

        val writer = conn.outputStream.writer()
        writer.write("param1=value1&param2=value2")
        writer.close()


        val responseCode = conn.responseCode
        Log.v("TAG", "Response code: $responseCode")

        val reader = conn.inputStream.reader()
        val content = reader.readText()
        reader.close()

        Log.v("TAG", "$content")
      }
    }

    val jsonString = "[\n" +
            "  {\n" +
            "    \"userId\": 1,\n" +
            "    \"id\": 1,\n" +
            "    \"title\": \"sunt aut facere repellat provident occaecati excepturi optio reprehenderit\",\n" +
            "    \"body\": \"quia et suscipit\\nsuscipit recusandae consequuntur expedita et cum\\nreprehenderit molestiae ut ut quas totam\\nnostrum rerum est autem sunt rem eveniet architecto\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"userId\": 1,\n" +
            "    \"id\": 2,\n" +
            "    \"title\": \"qui est esse\",\n" +
            "    \"body\": \"est rerum tempore vitae\\nsequi sint nihil reprehenderit dolor beatae ea dolores neque\\nfugiat blanditiis voluptate porro vel nihil molestiae ut reiciendis\\nqui aperiam non debitis possimus qui neque nisi nulla\"\n" +
            "  }]"
    try {
      val jArr = JSONArray(jsonString)
      val count = jArr.length()
      for (i in 0..count - 1) {
        val jObj = jArr.getJSONObject(i)
        val id = jObj.getInt("id")
        val title = jObj.getString("title")
        Log.v("TAG", "$id - $title")
      }

    } catch (ex: Exception) {
      ex.printStackTrace()
    }

    try {
      val jObj = JSONObject()
      jObj.put("hoten", "Student 1")
      jObj.put("mssv", "20211234")
      val jsonString = jObj.toString()
      Log.v("TAG", "$jsonString")

      val jArr  = JSONArray()
      for (i in 1..10) {
        val jObj = JSONObject()
        jObj.put("hoten", "Student $i")
        jObj.put("mssv", "$i")
        jArr.put(jObj)
      }
      Log.v("TAG", "${jArr.toString()}")

    } catch (ex: Exception) {
      ex.printStackTrace()
    }

    lifecycleScope.launch {
//      val posts = service.getAllPosts()
//      for (post in posts)
//        Log.v("TAG", "$post")

      val post = service.getPost(1)
      Log.v("TAG", "$post")
    }

    Glide.with(binding.imageView)
      .load("https://lebavui.github.io/walls/wall1000.jpg")
      .apply(RequestOptions()
        .placeholder(R.drawable.baseline_downloading_24)
        .error(R.drawable.baseline_error_outline_24))
      .into(binding.imageView)
  }
}