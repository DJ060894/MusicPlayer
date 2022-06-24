package cr.ac.musicplayer

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.documentfile.provider.DocumentFile
import cr.ac.musicplayer.entity.Cancion

class MainActivity : AppCompatActivity() {

    var arrayAdapter: ArrayAdapter<*>? = null
    lateinit var rootTree: DocumentFile
    private lateinit var buttonPlay : Button
    private lateinit var buttonStop : Button
    private lateinit var buttonPrev : Button
    private lateinit var buttonNext : Button
    private lateinit var textName : TextView
    val m_metaRetriever = android.media.MediaMetadataRetriever()

    lateinit var mediaPlayer : MediaPlayer

    private var files : MutableList<Cancion> = mutableListOf()
    var handler: Handler = Handler(Looper.getMainLooper())
    private var index : Int = 0
    companion object{
        var OPEN_DIRECTORY_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        startActivityForResult(intent, OPEN_DIRECTORY_REQUEST_CODE)

        val datos= findViewById<ListView>(R.id.listCanciones)
        arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1,files!!)
        datos.adapter = arrayAdapter

        buttonPlay = findViewById(R.id.buttonPlay)
        buttonStop = findViewById(R.id.buttonStop)
        buttonNext = findViewById(R.id.buttonNext)
        buttonPrev = findViewById(R.id.buttonPrev)

        setOnClickListeners(this)
    }





    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == OPEN_DIRECTORY_REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK){
                var directoryUri = data?.data ?: return
                Log.e("Directorio", directoryUri.toString())
                rootTree = DocumentFile.fromTreeUri(this, directoryUri)!!
                for(file in rootTree!!.listFiles()){
                    try {
                        file.name?.let {
                            Log.e("Archivo", it)
                            m_metaRetriever.setDataSource(this, file.uri)
                            handler.post {
                                files?.add(Cancion(
                                    m_metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE).toString(),
                                    m_metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST).toString(),
                                    m_metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM).toString()))
                            }
                        }
                    }catch(e: Exception){
                        Log.e("Error", "No pude ejecutar el archivo " + file.uri)
                    }
                }

            }
        }
    }


    private fun setOnClickListeners(context: Context) {

    }
}

