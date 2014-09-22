package org.piscatory.onebutton.view;



import android.app.Fragment;
import android.os.Bundle;
import android.preference.PreferenceFragment;

import org.piscatory.onebutton.R;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class SettingFragment extends PreferenceFragment {


    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_setting, container, false);
//    }


}
