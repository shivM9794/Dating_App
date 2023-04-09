package com.karlo.binderstatic.RelateToFragment_OnBack;

import androidx.fragment.app.Fragment;

/**
 * Created by karlo on 3/30/2018.
 */

public class RootFragment extends Fragment implements OnBackPressListener {

    @Override
    public boolean onBackPressed() {
        return new BackPressImplimentation(this).onBackPressed();
    }
}