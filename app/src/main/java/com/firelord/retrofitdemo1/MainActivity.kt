package com.firelord.retrofitdemo1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import com.firelord.retrofitdemo1.databinding.ActivityMainBinding
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var retService: AlbumService

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        retService = RetrofitInstance
            .getRetrofitInstance()
            .create(AlbumService::class.java)

        //getRequestWithQueryParameters()
        // getRequestWithPathParameter()
        uploadAlbum()

    }

    private fun getRequestWithQueryParameters(){
        val responseLiveData:LiveData<Response<Albums>> = liveData {
            val response = retService.getSortedAlbums(3)
            emit(response)
        }
        responseLiveData.observe(this, Observer {
            val albumsList = it.body()?.listIterator()
            if (albumsList!=null){
                while (albumsList.hasNext()){
                    val albumsItem = albumsList.next()
                    val result = " "+"Album Title : ${albumsItem.title}"+"\n"+
                            " "+"Album id : ${albumsItem.id}"+"\n"+
                            " "+"User id : ${albumsItem.userId}"+"\n\n\n"
                    binding.textView.append(result)
                }
            }
        })
    }

    private fun getRequestWithPathParameter(){
        //path parameter example
        val pathResponse : LiveData<Response<AlbumsItem>> = liveData {
            val response = retService.getAlbum(3)
            emit(response)
        }

        pathResponse.observe(this, Observer {
            val title = it.body()?.title
            Toast.makeText(applicationContext,title,Toast.LENGTH_LONG).show()

        })
    }

    private fun uploadAlbum(){
        val albums = AlbumsItem(0,"My title",3)
        val postResponse : LiveData<Response<AlbumsItem>> = liveData {
            val  response = retService.uploadAlbum(albums)
            emit(response)
        }
        postResponse.observe(this, Observer {
            val receviedAlbumItem = it.body()
            val result = " "+"Album Title : ${receviedAlbumItem?.title}"+"\n"+
                         " "+"Album id : ${receviedAlbumItem?.id}"+"\n"+
                         " "+"User id : ${receviedAlbumItem?.userId}"+"\n\n\n"
            binding.textView.text = result
        })
    }
}