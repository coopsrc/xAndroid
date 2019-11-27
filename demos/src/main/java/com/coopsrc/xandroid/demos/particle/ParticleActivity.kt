package com.coopsrc.xandroid.demos.particle

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.coopsrc.xandroid.demos.R
import com.coopsrc.xandroid.demos.particle.drop.DropParticleActivity

import kotlinx.android.synthetic.main.activity_particle.*

class ParticleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_particle)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    fun dropParticle(view: View) {
        val intent = Intent(this, DropParticleActivity::class.java)
        startActivity(intent)
    }

}
