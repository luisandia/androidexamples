package com.example.rickandmorty

import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.example.rickandmorty.databinding.ActivityMainBinding
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val nameTextView = findViewById<AppCompatTextView>(R.id.nameTextView)
        val headerImageView = findViewById<AppCompatImageView>(R.id.headerImageView)
        val genderImageView = findViewById<AppCompatImageView>(R.id.genderImageView)
        val aliveTextView = findViewById<AppCompatTextView>(R.id.aliveTextView)
        val originTextView = findViewById<AppCompatTextView>(R.id.originTextView)
        val speciesTextView = findViewById<AppCompatTextView>(R.id.speciesTextView)

        val moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://rickandmortyapi.com/api/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
        val rickAndMortyService: RickAndMortyService = retrofit.create(RickAndMortyService::class.java)

        rickAndMortyService.getCharacterById(10).enqueue(object : Callback<GetCharacterByIdResponse> {
            override fun onResponse(call: Call<GetCharacterByIdResponse>, response: Response<GetCharacterByIdResponse>) {
                Log.i("MainActivity", response.toString())

                if (!response.isSuccessful) {
                    Toast.makeText(
                        this@MainActivity,
                        "Unsuccessful network call!",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }

                val body = response.body()!!

                nameTextView.text = body.name
                aliveTextView.text = body.status
                speciesTextView.text = body.species
                originTextView.text = body.origin.name

                genderImageView.setImageResource(R.drawable.ic_female_24)

                if(body.gender.equals("male",true)){
                    genderImageView.setImageResource(R.drawable.ic_male_24)
                }

                Picasso.get().load(body.image).into(headerImageView)

            }

            override fun onFailure(call: Call<GetCharacterByIdResponse>, t: Throwable) {
                Log.i("MainActivity",t.message ?:"null message")
            }

        })


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}