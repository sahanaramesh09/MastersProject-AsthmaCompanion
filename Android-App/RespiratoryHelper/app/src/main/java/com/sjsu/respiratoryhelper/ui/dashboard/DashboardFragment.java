package com.sjsu.respiratoryhelper.ui.dashboard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerUtils;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.sjsu.respiratoryhelper.R;
import com.sjsu.respiratoryhelper.appconfig.BaseHelper;

import org.jetbrains.annotations.NotNull;

import static android.Manifest.permission.CALL_PHONE;
import static android.content.Context.MODE_PRIVATE;

public class DashboardFragment extends Fragment {
    private static final String TAG = "DashboardFragment";

    String videoId = "";
    private YouTubePlayerView youTubePlayerView;
    private YouTubePlayer initializedYouTubePlayer;
    private TextView asthmaInfoDetails;

    private CardView cvHotline, cvEmergency;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        youTubePlayerView = view.findViewById(R.id.ypYoutubePlayer);
        videoId = "CzpvD_5TWv0";
        initYouTubePlayerView();

        asthmaInfoDetails = view.findViewById(R.id.tvAsthmaInfo);
        asthmaInfoDetails.setText(R.string.asthma_info_details);

        TextView tvHotLine = view.findViewById(R.id.hotLineCall);
        tvHotLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "@@@ alert clicked ");
                showHotLineDialog();
            }
        });

        TextView tvEmergency = view.findViewById(R.id.tvEmergency);
        if (!isEmergencyNoPresent()) {
            showEnterEmergencyNoDialog();
        }

        tvEmergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emergencyNo = getEmergencyNo();
                Log.d(TAG, "Emergency No : " + emergencyNo);

                Intent i = new Intent(Intent.ACTION_CALL);
                i.setData(Uri.parse("tel:" + emergencyNo));
                if (ContextCompat.checkSelfPermission(getActivity(), CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    startActivity(i);
                } else {
                    requestPermissions(new String[]{CALL_PHONE}, 1);
                }
            }
        });
        return view;
    }

    /**
     * Get the emergency no
     */
    private String getEmergencyNo() {
        SharedPreferences mSharedPreferences = getActivity().getSharedPreferences(BaseHelper.APP_SHARED_PREF, MODE_PRIVATE);
        return mSharedPreferences.getString(BaseHelper.EMERGENCY_PHN_NO, "1234567890");
    }

    /**
     * Check if emergency no is already present in shared pref
     */
    private boolean isEmergencyNoPresent() {
        SharedPreferences mSharedPreferences = getActivity().getSharedPreferences(BaseHelper.APP_SHARED_PREF, MODE_PRIVATE);
        return mSharedPreferences.getBoolean(BaseHelper.EMERGENCY_NO_FLAG, false);
    }

    /**
     * Show emergency no entry pop up
     */
    private void showEnterEmergencyNoDialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext(), R.style.Theme_AppCompat_DayNight_Dialog);
        View mView = getLayoutInflater().inflate(R.layout.dialog_add_emergency, null);
        EditText etPhnNo = mView.findViewById(R.id.etEnterPhn);
        Button mAgree = mView.findViewById(R.id.btAgree);

        mBuilder.setTitle("Add Emergency No ");
        mBuilder.setMessage("Please add a phone no whom you can call in case of an emergency :");
        mBuilder.setView(mView);

        final AlertDialog mDialog = mBuilder.create();
        mDialog.show();

        mAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phnNoEntered = String.valueOf(etPhnNo.getText());

                SharedPreferences mSharedPreferences = getActivity().getSharedPreferences(BaseHelper.APP_SHARED_PREF, MODE_PRIVATE);
                SharedPreferences.Editor mEditor = mSharedPreferences.edit();
                mEditor.putString(BaseHelper.EMERGENCY_PHN_NO, phnNoEntered);
                mEditor.putBoolean(BaseHelper.EMERGENCY_NO_FLAG, true);
                mEditor.apply();

                mDialog.dismiss();
            }
        });
    }

    /**
     * Show hotLine Call popup
     */
    private void showHotLineDialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity(), R.style.Theme_AppCompat_DayNight_Dialog);
        View mView = getLayoutInflater().inflate(R.layout.dialog_hot_line_info, null);
        TextView mCall = mView.findViewById(R.id.tvCall);
        TextView mCancel = mView.findViewById(R.id.tvCancel);

        mBuilder.setTitle("Call Asthma and Allergy Foundation of America");
        mBuilder.setMessage(getString(R.string.hot_line_details));
        mBuilder.setView(mView);

        final AlertDialog mDialog = mBuilder.create();
        mDialog.show();

        mCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_CALL);
                i.setData(Uri.parse("tel:6692268385"));
                if (ContextCompat.checkSelfPermission(getActivity(), CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    startActivity(i);
                } else {
                    requestPermissions(new String[]{CALL_PHONE}, 1);
                }
                mDialog.dismiss();
            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
    }

    /**
     * Set up small youtube player
     */
    private void initYouTubePlayerView() {
        // If you don't add YouTubePlayerView as a lifecycle observer, you will have to release it manually.
        getLifecycle().addObserver(youTubePlayerView);

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                initializedYouTubePlayer = youTubePlayer;
                YouTubePlayerUtils.loadOrCueVideo(
                        initializedYouTubePlayer,
                        getLifecycle(),
                        videoId,
                        0f
                );
                initializedYouTubePlayer.pause();
            }

            @Override
            public void onApiChange(@NotNull YouTubePlayer youTubePlayer) {
                super.onApiChange(youTubePlayer);
            }

            @Override
            public void onCurrentSecond(@NotNull YouTubePlayer youTubePlayer, float second) {
            }

            @Override
            public void onError(@NotNull YouTubePlayer youTubePlayer, @NotNull PlayerConstants.PlayerError error) {
                super.onError(youTubePlayer, error);
            }

            @Override
            public void onPlaybackQualityChange(@NotNull YouTubePlayer youTubePlayer, @NotNull PlayerConstants.PlaybackQuality playbackQuality) {
                super.onPlaybackQualityChange(youTubePlayer, playbackQuality);
            }

            @Override
            public void onPlaybackRateChange(@NotNull YouTubePlayer youTubePlayer, @NotNull PlayerConstants.PlaybackRate playbackRate) {
                super.onPlaybackRateChange(youTubePlayer, playbackRate);
            }

            @Override
            public void onStateChange(@NotNull YouTubePlayer youTubePlayer, @NotNull PlayerConstants.PlayerState state) {
                super.onStateChange(youTubePlayer, state);
            }

            @Override
            public void onVideoDuration(@NotNull YouTubePlayer youTubePlayer, float duration) {
                super.onVideoDuration(youTubePlayer, duration);
            }

            @Override
            public void onVideoId(@NotNull YouTubePlayer youTubePlayer, @NotNull String videoId) {
                super.onVideoId(youTubePlayer, videoId);
            }

            @Override
            public void onVideoLoadedFraction(@NotNull YouTubePlayer youTubePlayer, float loadedFraction) {
                super.onVideoLoadedFraction(youTubePlayer, loadedFraction);
            }
        });
    }

}