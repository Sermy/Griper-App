package com.griper.griperapp.homescreen.interfaces

/**
 * Created by Sarthak on 19-03-2017
 */
interface ShowGripeDetailsScreenContract {

    interface View {
        fun init()

        fun setUpRecyclerViewFirebase();

        fun setGripeImage()

        fun showLeftAction(visibility: Boolean)

        fun showRightAction(visibility: Boolean)
    }

    interface Presenter {

    }
}