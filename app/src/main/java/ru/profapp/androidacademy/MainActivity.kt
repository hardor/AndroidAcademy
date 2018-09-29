package ru.profapp.androidacademy

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var tvSendMessage: TextView
    private lateinit var message: EditText
    private lateinit var ivVK: ImageView
    private lateinit var ivLinkedIn: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = getString(R.string.full_name)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        message = findViewById(R.id.eT)
        tvSendMessage = findViewById(R.id.tV_sendMessage)
        tvSendMessage.setOnClickListener { v ->
            val emailIntent = Intent(Intent.ACTION_SENDTO).setData(Uri.parse("mailto:test@gmail.com"))
                    .putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject))
                    .putExtra(Intent.EXTRA_TEXT, message.text)
            if (emailIntent.resolveActivity(this.packageManager) != null)
                startActivity(emailIntent)
            else
                Toast.makeText(this, R.string.email_not_found, Toast.LENGTH_SHORT).show()
        }

        ivVK = findViewById(R.id.iV_vk)
        ivVK.setOnClickListener { v ->
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://vk.com/"))
            if (browserIntent.resolveActivity(this.packageManager) != null)
                startActivity(browserIntent)
            else
                Toast.makeText(this, R.string.browser_not_found, Toast.LENGTH_SHORT).show()
        }

        ivLinkedIn = findViewById(R.id.iV_li)
        ivLinkedIn.setOnClickListener { v ->
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://linkedIn"))
            if (browserIntent.resolveActivity(this.packageManager) != null)
                startActivity(browserIntent)
            else
                Toast.makeText(this, R.string.browser_not_found, Toast.LENGTH_SHORT).show()
        }
    }
}
