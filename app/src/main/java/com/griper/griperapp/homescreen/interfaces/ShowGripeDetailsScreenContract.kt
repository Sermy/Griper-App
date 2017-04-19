package com.griper.griperapp.homescreen.interfaces

/**
 * Created by Sarthak on 19-03-2017
 */
interface ShowGripeDetailsScreenContract {

    interface View {
        fun init()

        fun setUpRecyclerViewFirebase();

        fun setGripeImage()
    }

    interface Presenter {

    }
}