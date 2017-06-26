package com.example.aleks.myapplication.main;

import android.graphics.Bitmap;

/**
 * Created by aleks on 25.06.2017.
 */

public interface MainContract {

    interface View {

        void setItemTitle(String itemTitle);

        void setItemPrice(String itemPrice);

        void setItemImage(Bitmap itemBitmap);

        void showProgressDialog();

        void dismissProgressDialog();

        void showItemDetails();

        void hideItemDetails();

        void showStoreDetails();

        void hideStoreDetails();

        void showStoreDetailsProgressBar();

        void hideStoreDetailsProgressBar();

        void showInputUrlError();

        void setSellerTitle(String sellerTitle);

        void setSellerFeedbackScore(String sellerFeedbackScore);

        void setSellerSince(String sellerSince);

        void setSellerItemAsDescribed(String itemAsDescribed);

        void setSellerCommunication(String communication);

        void setSellerShippingSpeed(String shippingSpeed);

        void setSellerPositive3m(String positiveFeedbacks3m);

        void setSellerNeutral3m(String neutralFeedbacks3m);

        void setSellerNegative3m(String negativeFeedbacks3m);

        void setSellerPositiveFeedbackRate(String positiveFeedbackRate3m);

        void setShortUrl(String shortUrl);

        void showSimpleLoadingError();
    }

    interface Presenter {

        void loadData();

        void handleSharedText(String sharedText);

        void setShortUrl(String s);
    }

}
