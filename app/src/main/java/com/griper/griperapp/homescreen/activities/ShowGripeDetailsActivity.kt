package com.griper.griperapp.homescreen.activities

import android.Manifest
import android.app.PendingIntent.getActivity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import android.view.View
import android.view.ViewTreeObserver
import butterknife.ButterKnife
import butterknife.OnClick
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.*
import com.griper.griperapp.R
import com.griper.griperapp.dbmodels.UserPreferencesData
import com.griper.griperapp.firebase.adapters.CommentViewHolder
import com.griper.griperapp.firebase.adapters.CommentsAdapter
import com.griper.griperapp.firebase.model.CommentDataModel
import com.griper.griperapp.homescreen.adapters.ImagesShowPagerAdapter
import com.griper.griperapp.homescreen.interfaces.ShowGripeDetailsScreenContract
import com.griper.griperapp.utils.CloudinaryImageUrl
import com.griper.griperapp.utils.Utils
import com.griper.griperapp.widgets.SwipeBackActivity
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import timber.log.Timber
import kotlinx.android.synthetic.main.activity_show_gripe_details.*
import kotlinx.android.synthetic.main.layout_progress_bar_full_screen.*
import java.util.*


class ShowGripeDetailsActivity : SwipeBackActivity(), ShowGripeDetailsScreenContract.View, OnMapReadyCallback {

    override fun showRightAction(visibility: Boolean) {
        if (visibility) {
            imageViewRight.visibility = View.VISIBLE
        } else {
            imageViewRight.visibility = View.GONE
        }

    }

    override fun showLeftAction(visibility: Boolean) {
        if (visibility) {
            imageViewLeft.setVisibility(View.VISIBLE)
        } else {
            imageViewLeft.setVisibility(View.GONE)
        }
    }

    companion object {
        private val EXTRA_GRIPE_DATA = "gripe_data"
        private val EXTRA_GRIPE_TITLE = "gripe_title"
        private val EXTRA_GRIPE_IMAGE = "gripe_image"
        private val EXTRA_GRIPE_ADDRESS = "gripe_address"
        private val EXTRA_GRIPE_LAT = "gripe_lat"
        private val EXTRA_GRIPE_LON = "gripe_lon"
        private val EXTRA_GRIPE_DESCRIPTION = "gripe_description"
        private val EXTRA_GRIPE_ID = "gripe_id"
        private val EXTRA_GRIPE_IMAGE_COUNT = "gripe_image_count"
    }

    private var gripeId: String? = null
    private var gripeTitle: String? = null
    private var gripeAddress: String? = null
    private var gripeDescription: String? = null
    private var gripeLocation: ArrayList<Double> = ArrayList()
    private var gripeImage: ArrayList<String> = ArrayList()
    private var gripeImage2: ArrayList<String> = ArrayList()
    private var gripeImage3: ArrayList<String> = ArrayList()
    private var transformedUrlList: ArrayList<String> = ArrayList()
    private var imageCount: Int? = 0
    private var showPhotoGalleryPagerAdapter: ImagesShowPagerAdapter? = null
    private var mFirebaseDatabaseReference: DatabaseReference? = null
    private var firebaseAdapter: FirebaseRecyclerAdapter<CommentDataModel, CommentViewHolder>? = null
    internal var linearLayoutManager: LinearLayoutManager? = null
    internal var queryRef: Query? = null


    override fun init() {
        setSupportActionBar(toolbarGripe)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(false)
        val mapFragment = fragmentManager.findFragmentById(R.id.map) as MapFragment
        mapFragment.getMapAsync(this)
        ButterKnife.bind(this)
        if (intent.extras != null) {
            gripeTitle = intent.extras.getString(EXTRA_GRIPE_TITLE)
            gripeAddress = intent.extras.getString(EXTRA_GRIPE_ADDRESS)
            gripeLocation.add(intent.extras.getDouble(EXTRA_GRIPE_LAT))
            gripeLocation.add(intent.extras.getDouble(EXTRA_GRIPE_LON))
            imageCount = intent.extras.getInt(EXTRA_GRIPE_IMAGE_COUNT)
            for (i in 0..(imageCount!! - 1)) {
                if (i == 0) {
                    gripeImage = intent.extras.getStringArrayList(EXTRA_GRIPE_IMAGE + "[0]")
                } else if (i == 1) {
                    gripeImage2 = intent.extras.getStringArrayList(EXTRA_GRIPE_IMAGE + "[1]")
                } else {
                    gripeImage3 = intent.extras.getStringArrayList(EXTRA_GRIPE_IMAGE + "[2]")
                }
            }
            gripeId = intent.extras.getString(EXTRA_GRIPE_ID)
            gripeDescription = intent.extras.getString(EXTRA_GRIPE_DESCRIPTION)
            textToolbarGripe.setText(gripeTitle)
            tvGripeLocation.setText(gripeAddress)
            tvGripeDescription.setText(gripeDescription)
            setGripeImage()
            setUpRecyclerViewFirebase()
        }
    }

    fun setUpViewPager() {
        loadImageDetailGripe.smoothToHide()
        showPhotoGalleryPagerAdapter = ImagesShowPagerAdapter(this, transformedUrlList)
        viewPagerGallery.adapter = showPhotoGalleryPagerAdapter
        if (transformedUrlList.size == 1) {
            showLeftAction(false)
            showRightAction(false)
        } else {
            showLeftAction(false)
            showRightAction(true)
        }
        showPhotoGalleryPagerAdapter!!.notifyDataSetChanged()

        viewPagerGallery.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {

                if (transformedUrlList.size > 1)
                    for (i in transformedUrlList.indices)
                        if (position == 0) {
                            showLeftAction(false)
                            showRightAction(true)
                        } else if (position == transformedUrlList.size - 1) {
                            showRightAction(false)
                            showLeftAction(true)
                        } else {
                            showLeftAction(true)
                            showRightAction(true)
                        }
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })

        imageViewLeft.setOnClickListener(View.OnClickListener {
            viewPagerGallery.currentItem = viewPagerGallery.currentItem - 1
        })

        imageViewRight.setOnClickListener(View.OnClickListener {
            viewPagerGallery.currentItem = viewPagerGallery.currentItem + 1
        })

    }

    override fun setGripeImage() {
        val preferencesData = UserPreferencesData.getUserPreferencesData()
        if (preferencesData.gripeLargeImageHeight == 0 && preferencesData.gripeLargeImageWidth == 0) {
            viewPagerGallery.viewTreeObserver.addOnGlobalLayoutListener(
                    object : ViewTreeObserver.OnGlobalLayoutListener {
                        override fun onGlobalLayout() {
                            val availableHeight = viewPagerGallery.measuredHeight
                            val availableWidth = viewPagerGallery.measuredWidth
                            // Saving to local database
                            preferencesData.gripeLargeImageHeight = availableHeight
                            preferencesData.gripeLargeImageWidth = availableWidth
                            Timber.i(availableHeight.toString().plus(" ").plus(availableWidth.toString()))
                            if (availableHeight > 0 && availableWidth > 0) {
                                viewPagerGallery.viewTreeObserver.removeOnGlobalLayoutListener(this)
                                val imageUrl = CloudinaryImageUrl.Builder(gripeImage.get(0), gripeImage.get(1),
                                        availableWidth, availableHeight, gripeImage.get(2)).build()

                                if (imageUrl != null) {
                                    val transformedImageUrl = imageUrl.transformedImageUrl
                                    if (transformedImageUrl != null) {
                                        loadImageDetailGripe.smoothToShow()
                                        transformedUrlList.add(transformedImageUrl)
                                        for (i in 0..(imageCount!! - 1)) {
                                            if (i == 0) {
                                                continue
                                            } else if (i == 1) {
                                                val imgUrl = CloudinaryImageUrl.Builder(gripeImage2.get(0), gripeImage2.get(1),
                                                        availableWidth, availableHeight, gripeImage2.get(2)).build()
                                                if (imgUrl != null) {
                                                    val transformedImgUrl = imgUrl.transformedImageUrl
                                                    transformedUrlList.add(transformedImgUrl)
                                                }
                                            } else {
                                                val img_url = CloudinaryImageUrl.Builder(gripeImage3.get(0), gripeImage3.get(1),
                                                        availableWidth, availableHeight, gripeImage3.get(2)).build()
                                                if (img_url != null) {
                                                    val transformedImgUrl = img_url.transformedImageUrl
                                                    transformedUrlList.add(transformedImgUrl)
                                                }
                                            }
                                        }
//                                        Picasso.with(baseContext).load(transformedImageUrl)
//                                                .into(imageDetailGripe, object : Callback {
//                                                    override fun onSuccess() {
//                                                        loadImageDetailGripe.smoothToHide()
//                                                    }
//
//                                                    override fun onError() {
//
//                                                    }
//                                                })
                                        setUpViewPager()
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
                    transformedUrlList.add(transformedImageUrl)
                    for (i in 0..(imageCount!! - 1)) {
                        if (i == 0) {
                            continue
                        } else if (i == 1) {
                            val imgUrl = CloudinaryImageUrl.Builder(gripeImage2.get(0), gripeImage2.get(1),
                                    preferencesData.gripeLargeImageWidth, preferencesData.gripeLargeImageHeight, gripeImage2.get(2)).build()
                            if (imgUrl != null) {
                                val transformedImgUrl = imgUrl.transformedImageUrl
                                transformedUrlList.add(transformedImgUrl)
                            }
                        } else {
                            val imgUrl = CloudinaryImageUrl.Builder(gripeImage3.get(0), gripeImage3.get(1),
                                    preferencesData.gripeLargeImageWidth, preferencesData.gripeLargeImageHeight, gripeImage3.get(2)).build()
                            if (imgUrl != null) {
                                val transformedImgUrl = imgUrl.transformedImageUrl
                                transformedUrlList.add(transformedImgUrl)
                            }
                        }
                    }
                }
            }
            setUpViewPager()
        }

    }

    override fun setUpRecyclerViewFirebase() {

        linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager!!.stackFromEnd = true
        rvHintComments.layoutManager = linearLayoutManager
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().reference
        queryRef = mFirebaseDatabaseReference!!.child(CommentsActivity.GRIPES_CHILD).child(gripeId!!).child(CommentsActivity.COMMENTS_CHILD).limitToLast(2)
        firebaseAdapter = CommentsAdapter(this, layoutProgressBar, CommentDataModel::class.java, R.layout.item_show_gripes_comment, CommentViewHolder::class.java, queryRef)
        rvHintComments.adapter = firebaseAdapter
        firebaseAdapter!!.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
            }
        })

        queryRef!!.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.childrenCount > 0) {
                    appTvComments.setText("View all Comments")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })

        layoutComments.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, CommentsActivity::class.java)
            intent.putExtra(CommentsActivity.GRIPE_ID, gripeId)
            startActivity(intent)
            overridePendingTransition(0, 0)
        })
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
