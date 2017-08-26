package org.accapto.accaptoaccessibilityexample;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Created by EKRAINZ on 04/06/17.
 */

public class TalkBackFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_talkback, container, false);


        // on clicks

        Button btnTalkback = (Button) view.findViewById(R.id.btnOpenTalkback);

        btnTalkback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getActivity().startActivity(intent);

            }
        });


        TextView txtTalkBack = (TextView) view.findViewById(R.id.textMoreInfos);
        txtTalkBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://support.google.com/accessibility/android/answer/6283677?hl=en&ref_topic=3529932"));

                getActivity().startActivity(intent);
            }
        });

        return view;
    }







}
