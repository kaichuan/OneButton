package org.piscatory.onebutton.view;


import android.app.Fragment;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.piscatory.onebutton.R;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class ExplainFragment extends Fragment {


    public ExplainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_explain, container, false);
        TextView textView = (TextView)rootView.findViewById(R.id.explain);
        textView.setMovementMethod(LinkMovementMethod.getInstance());

        textView.setText(getResources().getString(R.string.text_area_intro));
        // Inflate the layout for this fragment

        return rootView;
    }


}
