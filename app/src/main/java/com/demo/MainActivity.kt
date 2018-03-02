package com.demo
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MyDB(this)
        showData()
        Count()


        search.setOnClickListener({
            val cn = MyDB(this).getContact(Contact(roll.text.toString().toInt(),"",0,""))

            val data = StringBuilder()
            val li = ArrayList<String>()
                data.append(" ${cn.ROLL_NO} - ${cn.NAME} - ${cn.AGE} - ${cn.MOB} ")
                li.add(data.toString())

            val adp = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, li)
            list.adapter = adp


        })

        btn.setOnClickListener({
            val insert = MyDB(this).AddData(Contact(roll.text.toString().toInt(), name.text.toString(), age.text.toString().toInt(), mob.text.toString()))
            Toast.makeText(baseContext, insert, Toast.LENGTH_SHORT).show()
            showData()
            Count()

        })

        delete.setOnClickListener({
            val delete = MyDB(this).deleteContact(Contact(roll.text.toString().toInt(), "", 0, ""))
            Toast.makeText(baseContext, delete, Toast.LENGTH_SHORT).show()
            showData()
            Count()

        })

        update.setOnClickListener({
            val update = MyDB(this).updateContact(Contact(roll.text.toString().toInt(), name.text.toString(), age.text.toString().toInt(), ""))
            Toast.makeText(baseContext, update, Toast.LENGTH_SHORT).show()
            showData()
            Count()

        })


    }

    private fun Count() {
        val count = MyDB(this).getContactsCount()
        Toast.makeText(baseContext, count.toString(), Toast.LENGTH_SHORT).show()
    }

    private fun showData() {

        val contacts = MyDB(this).getAllContacts()
        val data = StringBuilder()
        val li = ArrayList<String>()
        for (cn in contacts) {
            data.append(" ${cn.ROLL_NO} - ${cn.NAME} - ${cn.AGE} - ${cn.MOB} ")
            li.add(data.toString())
        }
        val adp = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, li)
        list.adapter = adp

    }

    val isStoragePermissionGranted: Boolean
        get() {
            if (Build.VERSION.SDK_INT >= 23) {
                if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Log.v(TAG, "Permission is granted")

                    return true
                } else {

                    Log.v(TAG, "Permission is revoked")
                    ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 234)
                    return false
                }
            } else {
                Log.v(TAG, "Permission is granted")
                return true
            }
        }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {


        if (requestCode == 234) {

            var i = 0
            val len = permissions.size
            while (i < len) {
                val permission = permissions[i]
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    Log.e(TAG, "user rejected the permission: " + permissions[0] + "was " + grantResults[0])
                    // user rejected the permission

                    var showRationale = false
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        showRationale = shouldShowRequestPermissionRationale(permission)
                    }
                    if (!showRationale) {
                        Log.e(TAG, "user also CHECKED \"never ask again\" " + permissions[0] + "was " + grantResults[0])

                        /*   showMessageOKCancel("Without WRITE_EXTERNAL_STORAGE permission the app is unable to auto-generate folder when storeing files to your device. Are you sure you want to deny this permission?",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // click ok button
                                        SplashActivity.this.finish();
                                    }
                                },
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //click Cancel button
                                        isStoragePermissionGranted();
                                    }
                                });*/

                    } else if (android.Manifest.permission.WRITE_EXTERNAL_STORAGE == permission) {

                        showMessageOKCancel("This permission is necessary for this app.Are you sure you want to deny this permission?",
                                DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() },
                                DialogInterface.OnClickListener { dialog, which -> isStoragePermissionGranted })
                    }
                } else {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Log.e(TAG, "Permission: " + permissions[0] + "was " + grantResults[0])
                        //resume tasks needing this permission


                    }
                }
                i++
            }
        }

    }

    private fun showMessageOKCancel(message: String, onClickListener: DialogInterface.OnClickListener, onClickCancel: DialogInterface.OnClickListener) {
        AlertDialog.Builder(applicationContext, AlertDialog.THEME_HOLO_LIGHT)
                .setMessage(message)
                .setPositiveButton("OK", onClickListener)
                .setCancelable(false)
                .setNegativeButton("Cancel", onClickCancel).create().show()

    }

    companion object {

        internal val TAG = "MainActivity"
    }
}
