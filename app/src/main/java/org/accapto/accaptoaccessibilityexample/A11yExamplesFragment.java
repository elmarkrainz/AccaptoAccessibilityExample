package org.accapto.accaptoaccessibilityexample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by eKrainz on 04/06/17.
 *
 * proper implementation can be found  in the xml layout file
 */

public class A11yExamplesFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
              return inflater.inflate(R.layout.fragment_a11y_examples, container, false);
    }

}

