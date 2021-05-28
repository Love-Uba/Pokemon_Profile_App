package com.example.weekseven

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.SurfaceControl
import androidx.fragment.app.FragmentManager
import com.example.weekseven.databinding.ActivityMainBinding
import com.example.weekseven.ui.PokeList

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)


        setContentView(binding.root)
    }

}