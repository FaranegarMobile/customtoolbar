package com.faranegar.customtoolbar;

/**
 * Created by shahab on 11/18/2018.
 */

public interface CustomToolbarContract {

    interface SearchSupportContract{
        void onSearchQuery(CharSequence query);
        void onBackClicked();
    }

    interface NormalContract{
        void onBackClicked();
    }
}
