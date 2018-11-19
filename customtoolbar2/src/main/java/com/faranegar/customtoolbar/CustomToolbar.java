package com.faranegar.customtoolbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.faranegar.customtoolbar2.R;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Created by shahab on 11/18/2018...
 */

public class CustomToolbar extends RelativeLayout implements DelayHandler.OnDelayCompletedListener {

    private boolean isSearchSupported;
    public CustomToolbarContract.NormalContract normalContract;
    public CustomToolbarContract.SearchSupportContract searchSupportContract;
    private TextView toolbarTitle;
    private ImageView searchImageView;
    private ImageView backImageView;
    private View normalToolbarLayout;
    private View searchToolbarLayout;
    private EditText searchEditText;
    private DelayHandler delayHandler;

    private static final String TAG = "CustomToolbar";
    private int normalToolbarBackGroundResId;
    private int searchToolbarBackGroundResId;
    private int hintTextResId;
    private int toolbarTitleResId;
    private int hintTextColorResId;
    private int toolbarTitleColorResId;

    public CustomToolbar(Context context) {
        super(context);
    }

    public CustomToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context,
                      AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.CustomToolbar);
        isSearchSupported = typedArray.getBoolean(R.styleable.CustomToolbar_isSearchSupported,
                false);
        setAttrs(typedArray);
        normalLayoutInflater(context);
    }

    private void setAttrs(TypedArray typedArray) {
        normalToolbarBackGroundResId = typedArray.getInt(R.styleable.CustomToolbar_normalToolbarBackGround,
                0);
        searchToolbarBackGroundResId = typedArray
                .getInt(R.styleable.CustomToolbar_searchToolbarBackGround,
                        0);
        hintTextResId = typedArray.getInt(R.styleable.CustomToolbar_hintText, 0);
        toolbarTitleResId = typedArray.getInt(R.styleable.CustomToolbar_toolbarTextTile,
                0);
        hintTextColorResId = typedArray.getInt(R.styleable.CustomToolbar_hintTextColor,
                0);
        toolbarTitleColorResId = typedArray.getInt(R.styleable.CustomToolbar_toolbarTextColor, 0);
    }


    public void setNormalToolbarBackGround(int drawableResId) {
        this.normalToolbarLayout.setBackgroundResource(drawableResId);
    }

    public void setSearchToolbarBackGround(int drawableResId) {
        this.searchToolbarLayout.setBackgroundResource(drawableResId);
    }

    public void setSearchHintText(String hintText) {
        this.searchEditText.setHint(hintText);
    }

    public void setSearchQueryTextColor(int colorResId) {
        this.searchEditText.setTextColor(colorResId);
    }

    public void setToolbarTextColor(int colorResId) {
        this.toolbarTitle.setTextColor(colorResId);
    }

    private void normalLayoutInflater(Context context) {
        removeAllViews();
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        normalToolbarLayout = layoutInflater.inflate(R.layout.custom_toolbar,
                this);
        normalToolbarLayout.setAnimation(AnimationUtils.loadAnimation(getContext(),
                android.R.anim.fade_in));
        toolbarTitle = normalToolbarLayout.findViewById(R.id.txtToolbarTitle);
        searchImageView = normalToolbarLayout.findViewById(R.id.imgSearch);
        backImageView = normalToolbarLayout.findViewById(R.id.imgBack);
        setAttrsToInflatedViews();
        setBackClickListener();
        setSearchImageViewVisibilityStatus();
    }

    private void setAttrsToInflatedViews() {
        setToolbarTitleResId();
        setToolbarTitleColorResId();
        setHintTextResId();
        setHintTextColorResId();
        setNormalBackGroundResId();
        setSearchBackGroundResId();
    }

    private void setSearchBackGroundResId() {
        if( searchToolbarBackGroundResId == 0 ){
            searchToolbarLayout.setBackgroundResource(R.drawable.search_toolbar_layout);
        } else{
            searchToolbarLayout.setBackgroundResource(searchToolbarBackGroundResId);
        }
    }

    private void setNormalBackGroundResId() {
        if( normalToolbarBackGroundResId == 0 ){
            normalToolbarLayout.setBackgroundResource(R.drawable.normal_toolbar_layout);
        } else{
            normalToolbarLayout.setBackgroundResource(normalToolbarBackGroundResId);
        }
    }

    private void setHintTextColorResId() {
        if( hintTextColorResId == 0 ){
            searchEditText.setHintTextColor(getContext().getResources()
                    .getColor(android.R.color.black));
        } else{
            searchEditText.setHintTextColor(hintTextColorResId);
        }
    }

    private void setHintTextResId() {
        if( hintTextResId == 0 ){
            searchEditText.setHint(getContext().getResources()
            .getString(R.string.search));
        } else {
            searchEditText.setHint(hintTextResId);
        }
    }

    private void setToolbarTitleColorResId() {
        if(toolbarTitleColorResId == 0){
            toolbarTitle.setTextColor(getContext().getResources()
                    .getColor(android.R.color.white));
        } else{
            toolbarTitle.setTextColor(getContext().getResources()
                    .getColor(toolbarTitleColorResId));
        }
    }

    private void setToolbarTitleResId() {
        if(toolbarTitleResId == 0){
            toolbarTitle.setText("---");
        } else{
            toolbarTitle.setText(toolbarTitleResId);
        }
    }


    private void setSearchImageViewVisibilityStatus() {
        if (isSearchSupported) {
            searchImageView.setVisibility(View.VISIBLE);
            setSearchClickListener();
        } else {
            searchImageView.setVisibility(View.GONE);
        }
    }

    private void setSearchClickListener() {
        searchImageView.setOnClickListener(view -> {
            if (searchSupportContract != null) {
                searchLayoutHandler();
            }
        });
    }

    private void searchLayoutHandler() {
        removeAllViews();
        LayoutInflater layoutInflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        searchToolbarLayout = layoutInflater
                .inflate(R.layout.search_toolbar_layout, CustomToolbar.this);
        searchToolbarLayout.setAnimation(AnimationUtils.loadAnimation(getContext(),
                android.R.anim.fade_in));
        searchEditText = searchToolbarLayout.findViewById(R.id.edtSearchQuery);
        searchEditText.addTextChangedListener(new SearchQueryChangedListener());
        ImageView closeSearchImageView =
                searchToolbarLayout.findViewById(R.id.imgClose);
        closeSearchImageView.setOnClickListener(view -> normalLayoutInflater(getContext()));
        delayHandler = new DelayHandler();
        setAttrsToInflatedViews();
        delayHandler.setOnDelayCompletedListener(this);
    }

    private void setBackClickListener() {
        backImageView.setOnClickListener(view -> {
            if (isSearchSupported) {
                if (searchSupportContract != null)
                    searchSupportContract.onBackClicked();
            } else {
                if (normalContract != null)
                    normalContract.onBackClicked();
            }
        });
    }

    public void setToolbarTitleText(String toolbarTitleText) {
        this.toolbarTitle.setText(toolbarTitleText);
    }

    public void setToolbarTitleTypeFace(Typeface typeFace) {
        this.toolbarTitle.setTypeface(typeFace);
    }

    public void setNormalContract(CustomToolbarContract.NormalContract normalContract) {
        this.normalContract = checkNotNull(normalContract,
                getErrorMessage(CustomToolbarContract.NormalContract.class.getName()));
    }

    public void setSearchSupportContract(CustomToolbarContract.SearchSupportContract
                                                 searchSupportContract) {
        this.searchSupportContract = checkNotNull(searchSupportContract,
                getErrorMessage(CustomToolbarContract.SearchSupportContract.class.getSimpleName()));
    }

    private String getErrorMessage(String className) {
        return className + " " + getContext().getString(R.string.can_not_be_null);
    }

    @Override
    public void onDelayCompleted(String queryString) {
        if (searchSupportContract != null) {
            searchSupportContract.onSearchQuery(queryString);
        }
    }

    private class SearchQueryChangedListener implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence,
                                  int i,
                                  int i1,
                                  int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (delayHandler != null) delayHandler
                    .startDelay(editable.toString());
        }
    }
}
