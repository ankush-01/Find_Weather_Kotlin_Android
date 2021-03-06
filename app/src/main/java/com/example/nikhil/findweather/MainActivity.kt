package com.example.nikhil.findweather

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    protected  fun getWeather(view:View){
        var city=edit_text_city.text.toString()
        val url="https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22"+city+"%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys"

         MyAsyncTask().execute(url)
    }
    inner  class MyAsyncTask:AsyncTask<String,String,String>(){
        override fun onPreExecute() {
        }
        override fun doInBackground(vararg p0: String?): String {

         try {
             var url= URL(p0[0])
             var urlConnect=url.openConnection() as HttpURLConnection
             urlConnect.connectTimeout=7000
            var inString=convert_input_stream_to_String(urlConnect.inputStream)
             publishProgress(inString)
         }catch (ex:Exception){}
            return  " "

        }

        override fun onProgressUpdate(vararg values: String?) {
           try {
               var json = JSONObject(values[0])
               val query = json.getJSONObject("query")
               val results = query.getJSONObject("results")
               val channel = results.getJSONObject("channel")
               val atmosphere = channel.getJSONObject("atmosphere")
               var humidity = atmosphere.getString("humidity")
               var visibility=atmosphere.getString("visibility")
               var pressure=atmosphere.getString("pressure")
               tv_humidity.text =  humidity+" %"
               tv_preassure.text=pressure+" in"
               tv_visibility.text=visibility+" km"
               var item=channel.getJSONObject("item")
               var condition=item.getJSONObject("condition")
               var temp_fahrenheit=condition.getString("temp")
               var temp_celcius=(temp_fahrenheit.toInt()-32)*(0.5555555555555556)
               tv_temp.text=temp_fahrenheit+"°F"+" , "+temp_celcius+"°C"
                Toast.makeText(this@MainActivity,"Weather For ${edit_text_city.text}",Toast.LENGTH_SHORT).show()

           }

           catch (ex:Exception){}


        }
        override fun onPostExecute(result: String?) {
        }



    }
    fun convert_input_stream_to_String(inputStream:InputStream):String{
    var bufferReader=BufferedReader(InputStreamReader(inputStream))
        var line:String
        var AllString:String=""
        try {
            do{

                line=bufferReader.readLine()
                if (line!=null){
                    AllString+=line
                }
            }while (line!=null)
            inputStream.close()

        }catch (ex:Exception){

        }
       return AllString

    }
}
//#@cker's Studio
//By Ankush Shrivastava
