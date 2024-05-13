package com.example.hayakuchi

import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import androidx.appcompat.app.AppCompatActivity
import com.example.hayakuchi.databinding.ActivityMainBinding
import java.util.Locale

class MainActivity : AppCompatActivity(), OnClickListener, TextToSpeech.OnInitListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var tts: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tts = TextToSpeech(this@MainActivity, this@MainActivity)

        binding.btnFast.setOnClickListener(this)
        binding.btnNormal.setOnClickListener(this)
        binding.btnSlow.setOnClickListener(this)

        binding.btnFast.visibility = View.INVISIBLE
        binding.btnNormal.visibility = View.INVISIBLE
        binding.btnSlow.visibility = View.INVISIBLE
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            if (tts.isLanguageAvailable(Locale.JAPAN) >= TextToSpeech.LANG_AVAILABLE) {
                tts.language = Locale.JAPAN

                binding.btnFast.visibility = View.VISIBLE
                binding.btnNormal.visibility = View.VISIBLE
                binding.btnSlow.visibility = View.VISIBLE

            } else {
                Log.v("MY_LOG", "TextToSpeechの初期化成功。日本語が無効。")
            }
        } else {
            Log.v("MY_LOG", "TextToSpeechの初期化失敗。")
        }
    }

    override fun onClick(v: View) {
        tts.stop()

        val speakText = binding.editText.text.toString()

        val rate = when (v.id) {
            R.id.btnFast -> 2.0F
            R.id.btnSlow -> 0.5F
            else -> 1.0F
        }
        tts.setSpeechRate(rate)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts.speak(speakText, TextToSpeech.QUEUE_FLUSH, null, "utteranceId")
        } else {
            tts.speak(speakText, TextToSpeech.QUEUE_FLUSH, null)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tts.shutdown()
    }
}