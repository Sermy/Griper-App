package com.griper.griperapp.homescreen.activities

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.view.MenuItem
import android.view.ViewTreeObserver
import butterknife.ButterKnife
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.griper.griperapp.R
import com.griper.griperapp.dbmodels.UserPreferencesData
import com.griper.griperapp.homescreen.interfaces.ShowGripeDetailsScreenContract
import com.griper.griperapp.utils.CloudinaryImageUrl
import com.griper.griperapp.utils.Utils
import com.griper.griperapp.widgets.SwipeBackActivity
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import timber.log.Timber
import kotlinx.android.synthetic.main.activity_show_gripe_details.*
import java.util.*


class ShowGripeDetailsActivity : SwipeBackActivity(), ShowGripeDetailsScreenContract.View, OnMapReadyCallback {

    companion object {
        private val EXTRA_GRIPE_DATA = "gripe_data"
        private val EXTRA_GRIPE_TITLE = "gripe_title"
        private val EXTRA_GRIPE_IMAGE = "gripe_image"
        private val EXTRA_GRIPE_ADDRESS = "gripe_address"
        private val EXTRA_GRIPE_LAT = "gripe_lat"
        private val EXTRA_GRIPE_LON = "gripe_lon"
        private val EXTRA_GRIPE_DESCRIPTION = "gripe_description"
    }

    private var gripeTitle: String? = null
    private var gripeAddress: String? = null
    private var gripeDescription: String? = null
    private var gripeLocation: ArrayList<Double> = ArrayList()
    private var gripeImage: ArrayList<String> = ArrayList()

    override fun init() {
        setSupportActionBar(toolbarGripe)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(false)
        val mapFragment = fragmentManager.findFragmentById(R.id.map) as MapFragment
        mapFragment.getMapAsync(this)

        if (intent.extras != null) {
            gripeTitle = intent.extras.getString(EXTRA_GRIPE_TITLE)
            gripeAddress = intent.extras.getString(EXTRA_GRIPE_ADDRESS)
            gripeLocation.add(intent.extras.getDouble(EXTRA_GRIPE_LAT))
            gripeLocation.add(intent.extras.getDouble(EXTRA_GRIPE_LON))
            gripeImage = intent.extras.getStringArrayList(EXTRA_GRIPE_IMAGE)
            gripeDescription = intent.extras.getString(EXTRA_GRIPE_DESCRIPTION)
            textToolbarGripe.setText(gripeTitle)
            tvGripeLocation.setText(gripeAddress)
            tvGripeDescription.setText(gripeDescription)
            setGripeImage()
        }
    }

    override fun setGripeImage() {
        val preferencesData = UserPreferencesData.getUserPreferencesData()
        if (preferencesData.gripeLargeImageHeight == 0 && preferencesData.gripeLargeImageWidth == 0) {
            imageDetailGripe.viewTreeObserver.addOnGlobalLayoutListener(
                    object : ViewTreeObserver.OnGlobalLayoutListener {
                        override fun onGlobalLayout() {
                            val availableHeight = imageDetailGripe.measuredHeight
                            val availableWidth = imageDetailGripe.measuredWidth
                            // Saving to local database
                            preferencesData.gripeLargeImageHeight = availableHeight
                            preferencesData.gripeLargeImageWidth = availableWidth
                            Timber.i(availableHeight.toString().plus(" ").plus(availableWidth.toString()))
                            if (availableHeight > 0 && availableWidth > 0) {
                                imageDetailGripe.viewTreeObserver.removeOnGlobalLayoutListener(this)
                                val imageUrl = CloudinaryImageUrl.Builder(gripeImage.get(0), gripeImage.get(1),
                                        availableWidth, availableHeight, gripeImage.get(2)).build()

                                if (imageUrl != null) {
                                    val transformedImageUrl = imageUrl.transformedImageUrl
                                    if (transformedImageUrl != null) {
                                        loadImageDetailGripe.smoothToShow()
                                        Picasso.with(baseContext).load(transformedImageUrl)
                                                .into(imageDetailGripe, object : Callback {
                                                    override fun onSuccess() {
                                                        loadImageDetailGripe.smoothToHide()
                                                    }

                                                    override fun onError() {

                                                    }
                                                })
                                    }
                                }
                            }
                        }
                    }
            )
        } else {
            val imageUrl = CloudinaryImageUrl.Builder(gripeImage.get(0), gripeImage.get(1),
                    preferencesData.gripeLargeImageWidth, preferencesData.gripeLargeImageHeight, gripeImage.get(2)).build()

            if (imageUrl != null) {
                val transformedImageUrl = imageUrl.transformedImageUrl
                if (transformedImageUrl != null) {
                    loadImageDetailGripe.smoothToShow()
                    Picasso.with(baseContext).load(transformedImageUrl)
                            .into(imageDetailGripe, object : Callback {
                                override fun onSuccess() {
                                    loadImageDetailGripe.smoothToHide()
                                }

                                override fun onError() {

                                }
                            })
                }
            }
        }
    }


    override fun onMapReady(p0: GoogleMap?) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Utils.showToast(this, getString(R.string.string_show_location))
            return
        }
        p0?.setMyLocationEnabled(true)
        val uiSettings = p0?.getUiSettings()
        uiSettings?.setMapToolbarEnabled(true)
        uiSettings?.setMyLocationButtonEnabled(false)
        uiSettings?.setCompassEnabled(false)
        p0?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(gripeLocation[0],
                gripeLocation[1]), 16f))
        p0?.addMarker(MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.map_pin_red)).
                position(LatLng(gripeLocation[0], gripeLocation[1])))
    }


    override fun onBackPressed() {
        finish()
        overridePendingTransition(R.anim.slide_out_right, 0)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.getItemId()) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_gripe_details)
        ButterKnife.bind(this)
        init()
    }

}
