package com.example.picleaf

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import android.widget.Toolbar
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.app_bar_main.*
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var mCategorization: Categorization
    private lateinit var mBitmap: Bitmap
    private val mCameraRequestCode = 0
    private val mGalleryRequestCode = 2

    private val mInputSize = 224
    private val mModelPath = "plant_disease_model.tflite"
    private val mLabelPath = "plant_labels.txt"

    private val mSamplePath = "automn.jpg"
    lateinit var toolbar: Toolbar
    // lateinit var drawer: AdvanceDrawerLayout //
    lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigationView = findViewById<View>(R.id.nav_views) as NavigationView
        toolbar = findViewById<Toolbar>(R.id.toolbar)
        drawer = findViewById<View>(R.id.drawer_layout) as AdvanceDrawerLayout
        drawer.setViewScale(GravityCompat.START, 0.9f)

        drawer.setDrawerElevation(GravityCompat.START,28f)
        drawer.setContrastThreshold(3f)
        drawer.serRadius(GravityCompat.START,25f)
        drawer.useCustomBehavior(GravityCompat.START)
        drawer.useCustomBehavior(GravityCompat.END)
        val headerView = navigationView.getHeaderView(0)

        mCategorization = Categorization(assets,mModelPath,mLabelPath)

        resources.assets.open(mSamplePath).use{
            mBitmap = BitmapFactory.decodeStream(it)
            mBitmap = Bitmap.createScaledBitmap(mInputSize,mInputSizetrue)
            mPhotoImageView.setImageBitmap(mBitmap)
        }
        bottom_nav.setActiveItem(0)
        val bottomnav: SmoothBottomBar= findViewById<SmoothBottomBar>(R.id.bottom_nav)
        bottomnav.setOnClickListener ( object: OnItemSelectedListener{
            override fun onItemSelect(pos: Int): Boolean {
               when(pos){
                   1->{
                       val intent = Intent(this@MainActivity.CommonRemedies::class.java)
                       startActivity(intent)
                       return true
                   }
                   2->{
                       val intent = Intent(this@MainActivity.AboutUs::class.java)
                       startActivity(intent)
                       return true
                   }
               }
                return false
            }
        })}

        mCameraButton.SetOnClickListener{
            val callcameraintent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(callcameraintent.mCameraRequestCode)
         }
        mGalleryButton.setOnClickListener{
            val callGalleryIntent = Intent(Intent.ACTION_PICK)
            callGalleryIntent.type = "image/*"
            startActivityForResult(callGalleryIntent,mGalleryRequestCode)
         }

         mDetectButton.setOnClickListener{
            val progressDialog = ProgressDialog(this@MainActivity)
            progressDialog.setTitle("Please Wait")
            progressDialog.setMessage("Wait There While I do something...")
            progressDialog.show()
            val handler = Handler()
           handler.postDelayed(Runnable { porgressDialog.dismiss()
               val results = mCategorization.recognizeImage(mBitmap).firstOrNull()
            mResultTextView.text = results?.title+ "\n Confidence:"+results?.confidence
             }, 2000)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == mCameraRequestCode) {
            if (resultCode == Activity.RESULT_OK && data != null)
                mBitmap = data.extras!!.get("data") as Bitmap
            mBitmap = scaleImage(mBitmap)
            mPhotoImageView.SetImageBitmap(mBitmap)
            mResultTextView.text = ("Your photo image set now")
        } else {
            Toast.makeText(this, "Camera cancel...", Toast.LENGTH_LONG).show()
        }
    }
    else if(reqquestCode == mGalleryRequestCode){
    if (data != null) {
        val uri = data.data
        try {
            mBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        mBitmap = scaleImage(mBitmap)
        mPhotoImageView.setImageeitmap(mBitmap)
    }
}
}
        private fun scaleImage(mBitmap: Bitmap): Bitmap {
            val originalWidth = bitmap!!.width
            val originalHeight = bitmap.height
            val scaleWidth = mInputSize.toFloat()
            val scaleHeight = mInputSize.toFloat()
            val matrix = Matrix()
            matrix.postScale(scaleWidth.scaleHeight)
            return Bitmap.createBitmap(bitmap,0,0,originalWidth,originalHeight,matrix,true)

        }
        override fun onNavigationItemSelected(item: MenuItem): Boolean {
            val drawer =
                findViewById<View>(R.id.drawer_layout) as AdvanceDrawerLayout

            when (item.itemId) {
                R.id.about -> {
                    val intent = Intent(this@MainActivity, AboutUs::class.java)
                    startActivity(intent)
                }

                R.id.ready -> {
                    val intent = Intent(this@MainActivity, CommonRemedies::class.java)
                    startActivity(intent)
                }
            }
            drawer.closeDrawer(GravityCompat.START)
            return true

        }

}