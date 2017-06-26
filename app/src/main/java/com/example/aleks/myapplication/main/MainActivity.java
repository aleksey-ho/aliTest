package com.example.aleks.myapplication.main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aleks.myapplication.BasePresenterActivity;
import com.example.aleks.myapplication.PresenterFactory;
import com.example.aleks.myapplication.R;
import com.example.aleks.myapplication.utils.CustomTextWatcher;
import com.example.aleks.myapplication.utils.URLUnshortener;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.example.aleks.myapplication.utils.AppUtils.alert;

public class MainActivity extends BasePresenterActivity<MainPresenter, MainContract.View>
        implements MainContract.View {
    private final static String TAG = MainActivity.class.getSimpleName();
    private final static String INPUT_URL = "INPUT_URL";

    private String sharedText;
    MainPresenter presenter;

    ProgressDialog progressDialog;

    @BindView(R.id.textInputLayoutInputUrl) TextInputLayout textInputLayoutInputUrl;
    @BindView(R.id.editTextInputUrl) EditText editTextInputUrl;

    @BindView(R.id.progressBarStoreDetails) ProgressBar progressBarStoreDetails;

    //Item details
    @BindView(R.id.relativeLayoutItemDetails) RelativeLayout relativeLayoutItemDetails;
    @BindView(R.id.textViewItemTitle) TextView textViewItemTitle;
    @BindView(R.id.textViewItemPrice) TextView textViewItemPrice;
    @BindView(R.id.imageViewItemImage) ImageView imageViewItemImage;

    //Store details
    @BindView(R.id.relativeLayoutStoreDetails) RelativeLayout relativeLayoutStoreDetails;
    //Seller Summary
    @BindView(R.id.textViewSellerTitle) TextView textViewSellerTitle;
    @BindView(R.id.textViewSellerFeedbackScore) TextView textViewSellerFeedbackScore;
    @BindView(R.id.textViewSellerSince) TextView textViewSellerSince;
    //Detailed seller ratings
    @BindView(R.id.textViewSellerItemAsDescribed) TextView textViewSellerItemAsDescribed;
    @BindView(R.id.textViewSellerCommunication) TextView textViewSellerCommunication;
    @BindView(R.id.textViewSellerShippingSpeed) TextView textViewSellerShippingSpeed;
    //Detailed seller ratings
    @BindView(R.id.textViewSellerPositive3m) TextView textViewSellerPositive3m;
    @BindView(R.id.textViewSellerNeutral3m) TextView textViewSellerNeutral3m;
    @BindView(R.id.textViewSellerNegative3m) TextView textViewSellerNegative3m;
    @BindView(R.id.textViewSellerPositiveFeedbackRate) TextView textViewSellerPositiveFeedbackRate;

    @OnClick(R.id.buttonLoadData)
    void loadData() {
        presenter.setShortUrl(editTextInputUrl.getText().toString());
        presenter.loadData();
    }

    //TODO move to the presenter
    @OnClick(R.id.buttonShowLongUrl)
    void showLongUrl() {
        if (Objects.equals(editTextInputUrl.getText().toString(), "")) {
            textInputLayoutInputUrl.setError("Введите ссылку на товар");
            requestFocus(textInputLayoutInputUrl);
            return;
        }
        progressDialog.show();

        URLUnshortener urlUnshortener = new URLUnshortener();
        try {
            URL url = new URL(editTextInputUrl.getText().toString());
            urlUnshortener.expand(url)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(expanded -> {
                        progressDialog.dismiss();
                        alert(this, "Полная ссылка", expanded.toString());
                    }, throwable -> {
                        progressDialog.dismiss();
                        throwable.printStackTrace();
                        alert(this, null, "Не удалось получить полную ссылку");
                    });
        } catch (MalformedURLException e) {
            progressDialog.dismiss();
            alert(this, null, "Ссылка введена в некорректном формате");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Загрузка...");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);

        editTextInputUrl.addTextChangedListener(new CustomTextWatcher(textInputLayoutInputUrl));

        if (savedInstanceState != null) {
            editTextInputUrl.setText(savedInstanceState.getString(INPUT_URL));
        }

        Intent intent = getIntent();
        if (intent != null && intent.getClipData() != null && intent.getClipData().getItemAt(0) != null) {
            sharedText = intent.getClipData().getItemAt(0).getText().toString();
            intent.setClipData(null);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (sharedText != null) {
            presenter.handleSharedText(sharedText);
            sharedText = null;
            loadData();
        }
    }

    @NonNull
    @Override
    protected PresenterFactory<MainPresenter> getPresenterFactory() {
        return new MainPresenterFactory();
    }

    @Override
    protected void onPresenterCreatedOrRestored(@NonNull MainPresenter presenter) {
        this.presenter = presenter;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(INPUT_URL, editTextInputUrl.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void setItemTitle(String itemTitle) {
        textViewItemTitle.setText(itemTitle);
    }

    @Override
    public void setItemPrice(String itemPrice) {
        textViewItemPrice.setText(itemPrice);
    }

    @Override
    public void setItemImage(Bitmap itemBitmap) {
        imageViewItemImage.setImageBitmap(itemBitmap);
    }

    @Override
    public void showProgressDialog() {
        progressDialog.show();
    }

    @Override
    public void dismissProgressDialog() {
        progressDialog.dismiss();
    }

    @Override
    public void showItemDetails() {
        relativeLayoutItemDetails.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideItemDetails() {
        relativeLayoutItemDetails.setVisibility(View.GONE);
    }

    @Override
    public void showStoreDetails() {
        relativeLayoutStoreDetails.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideStoreDetails() {
        relativeLayoutStoreDetails.setVisibility(View.GONE);
    }

    @Override
    public void showStoreDetailsProgressBar() {
        progressBarStoreDetails.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideStoreDetailsProgressBar() {
        progressBarStoreDetails.setVisibility(View.GONE);
    }

    @Override
    public void showInputUrlError() {
        textInputLayoutInputUrl.setError("Введите ссылку на товар");
        requestFocus(textInputLayoutInputUrl);
    }

    @Override
    public void setSellerTitle(String sellerTitle) {
        textViewSellerTitle.setText(sellerTitle);
    }

    @Override
    public void setSellerFeedbackScore(String sellerFeedbackScore) {
        textViewSellerFeedbackScore.setText(sellerFeedbackScore);
    }

    @Override
    public void setSellerSince(String sellerSince) {
        textViewSellerSince.setText(sellerSince);
    }

    @Override
    public void setSellerItemAsDescribed(String itemAsDescribed) {
        textViewSellerItemAsDescribed.setText(itemAsDescribed);
    }

    @Override
    public void setSellerCommunication(String communication) {
        textViewSellerCommunication.setText(communication);
    }

    @Override
    public void setSellerShippingSpeed(String shippingSpeed) {
        textViewSellerShippingSpeed.setText(shippingSpeed);
    }

    @Override
    public void setSellerPositive3m(String positiveFeedbacks3m) {
        textViewSellerPositive3m.setText(positiveFeedbacks3m);
    }

    @Override
    public void setSellerNeutral3m(String neutralFeedbacks3m) {
        textViewSellerNeutral3m.setText(neutralFeedbacks3m);
    }

    @Override
    public void setSellerNegative3m(String negativeFeedbacks3m) {
        textViewSellerNegative3m.setText(negativeFeedbacks3m);
    }

    @Override
    public void setSellerPositiveFeedbackRate(String positiveFeedbackRate3m) {
        textViewSellerPositiveFeedbackRate.setText(positiveFeedbackRate3m);
    }

    @Override
    public void setShortUrl(String shortUrl) {
        editTextInputUrl.setText(shortUrl);
    }

    @Override
    public void showSimpleLoadingError() {
        Toast.makeText(this, "Не удалось загрузить данные", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
