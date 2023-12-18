package net.rishiz.contactnest

import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import net.rishiz.contactnest.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    companion object {
        private val TAG = MainActivity::class.java.canonicalName
    }

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private lateinit var adapter: ContactAdapter
    private var contact = ArrayList<ContactDetails>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        Log.d(TAG, "ok")
        syncContacts()

        binding.apply {
            contactBtn.setOnClickListener {
                Log.d(TAG, "ok")
                syncContacts()
                contactBtn.visibility = View.GONE
                recyclerview.visibility = View.VISIBLE
                recyclerview.adapter = adapter
            }
        }

    }

    private fun syncContacts() {
        if (checkSelfPermission(android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                arrayOf(android.Manifest.permission.READ_CONTACTS), 1
            )
        } else {
            fetchContacts()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                syncContacts()
            } else {
                Snackbar.make(
                    this, binding.root, "Permission Not Granted", Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun fetchContacts() {
        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
        )
        val contentResolver = contentResolver
        contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, null, null, null
        )?.use { cursor ->
            val nameColumn =
                cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val numberColumn = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            while (cursor.moveToNext()) {
                val name = cursor.getString(nameColumn)
                val number = cursor.getString(numberColumn)
                contact.add(ContactDetails(name, number))

            }
            cursor.close()
        }
        adapter = ContactAdapter(contact, this@MainActivity)
    }

}