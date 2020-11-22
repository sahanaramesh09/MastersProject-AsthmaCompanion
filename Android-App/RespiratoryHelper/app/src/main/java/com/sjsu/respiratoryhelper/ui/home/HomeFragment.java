package com.sjsu.respiratoryhelper.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.sjsu.respiratoryhelper.DataSourceApi;
import com.sjsu.respiratoryhelper.R;
import com.sjsu.respiratoryhelper.appconfig.BaseHelper;
import com.sjsu.respiratoryhelper.model.ResponseModel;
import com.sjsu.respiratoryhelper.model.ResultItem;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    public static int GAP_PERIOD = 60000;
    private static int INITIAL_DELAY = 1000;

    Retrofit retrofit;
    Disposable disposable;
    DataSourceApi dataSourceApi;
    String asthmaRiskData;
    LottieAnimationView weatherAnimation;
    Animation animationLeft, animationRight;
    CardView cvRiskScore, cvFirstDisplay, cvSecondDisplay;
    private List<ResultItem> resultItems;
    private TextView temperature, humidity, co, no2, o3, asthmaRisk;
    private RelativeLayout rlRiskScore;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        temperature = view.findViewById(R.id.tvTemp);
        humidity = view.findViewById(R.id.tvHumid);
        co = view.findViewById(R.id.tvCO);
        no2 = view.findViewById(R.id.tvNO);
        o3 = view.findViewById(R.id.tvO);

        asthmaRisk = view.findViewById(R.id.tvAsthmaRisk);
        rlRiskScore = view.findViewById(R.id.rlRiskScore);
        cvRiskScore = view.findViewById(R.id.cvRiskScore);
        cvFirstDisplay = view.findViewById(R.id.cvFirstDisplay);
        cvSecondDisplay = view.findViewById(R.id.cvSecondDisplay);

        weatherAnimation = view.findViewById(R.id.laWeatherAnimationView);

        animationLeft = AnimationUtils.loadAnimation(getContext(), R.anim.swing_up_left);
        animationRight = AnimationUtils.loadAnimation(getContext(), R.anim.swing_up_right);

        cvRiskScore.startAnimation(animationLeft);
        cvFirstDisplay.setAnimation(animationRight);
        cvSecondDisplay.setAnimation(animationRight);

        //Call the api
        retrofit = new Retrofit.Builder()
                .baseUrl(BaseHelper.SERVER_PREFIX)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        dataSourceApi = retrofit.create(DataSourceApi.class);
        //Call it at repeated interval
        disposable = Observable.interval(INITIAL_DELAY, GAP_PERIOD,
                TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::callAsthmaRisk, this::onError);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // In case stopped resume it
        if (disposable.isDisposed()) {
            disposable = Observable.interval(INITIAL_DELAY, GAP_PERIOD,
                    TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::callAsthmaRisk, this::onError);
        }
    }

    private void onError(Throwable throwable) {
        Toast.makeText(getContext(), "OnError in Observable Timer", Toast.LENGTH_LONG).show();
    }

    private void callAsthmaRisk(Long aLong) {
        Observable<ResponseModel> observable = dataSourceApi.getResults();
        Disposable subscribe = observable.subscribeOn(Schedulers.newThread()).
                observeOn(AndroidSchedulers.mainThread())
                .map(result -> result.resultItems)
                .subscribe(this::handleResults, this::handleError);
    }

    /**
     * Handle incoming json data from backend
     *
     * @param resultItems
     */
    private void handleResults(List<ResultItem> resultItems) {
        if (!resultItems.isEmpty()) {
            for (int i = 0; i < resultItems.size(); i++) {
                Log.d(TAG, "Asthma Risk value : " + resultItems.get(i).getAsthmaRisk());

                temperature.setText(" Temperature : " + resultItems.get(i).getTemperature() + ((char) 0x00B0) + "c");
                humidity.setText(" Humidity : " + resultItems.get(i).getHumidity() + " % ");
                weatherAnimation.setVisibility(View.VISIBLE);
                co.setText(" CO : " + resultItems.get(i).getCO());
                no2.setText(" NO2 : " + resultItems.get(i).getNO2());
                o3.setText(" O3 : " + resultItems.get(i).getO3());

                asthmaRiskData = resultItems.get(i).getAsthmaRisk();
                if (asthmaRiskData.equalsIgnoreCase("1")) {
                    rlRiskScore.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.material_red));
                    Log.d(TAG, "Asthma Risk data is 1 which is high");
                    this.asthmaRisk.setText(" Asthma Risk :  HIGH");
                    // We can show alert or something
                } else {
                    rlRiskScore.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.material_green));
                    this.asthmaRisk.setText(" Asthma Risk :  LOW");
                }
            }
        } else {
            Toast.makeText(getContext(), "NO RESULTS FOUND", Toast.LENGTH_LONG).show();
        }
    }

    private void handleError(Throwable t) {
        Log.e(TAG, "@@@@ handleError " + t.toString());
        Toast.makeText(getContext(), "handleError in handleResults ", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPause() {
        super.onPause();
        disposable.dispose();
    }
}