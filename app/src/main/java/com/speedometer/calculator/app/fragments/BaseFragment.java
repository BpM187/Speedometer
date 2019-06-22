package com.speedometer.calculator.app.fragments;

import android.support.v4.app.Fragment;
import android.view.View;

import com.speedometer.calculator.app.activities.BaseActivity;

public class BaseFragment extends Fragment {

    View baseView;

    public void setTitle(String newTitle) {
        if (getActivity() != null) {
            getActivity().setTitle(newTitle);
        }
    }

    public void addFragment(Fragment fragment, String name) {
        if (getActivity() != null && !getActivity().isFinishing()) {
            ((BaseActivity) getActivity()).addFragment(fragment, name);
        }
    }

    public void replaceFragment(Fragment fragment, String name) {
        if (getActivity() != null && !getActivity().isFinishing()) {
            ((BaseActivity) getActivity()).replaceFragment(fragment, name);
        }
    }

    public void replaceFragmentWithRightAnimation(Fragment fragment, String name) {
        if (getActivity() != null && !getActivity().isFinishing()) {
            ((BaseActivity) getActivity()).replaceFragmentWithRightAnimation(fragment, name);
        }
    }
    public void replaceFragmentWithBottomAnimation(Fragment fragment, String name) {
        if (getActivity() != null && !getActivity().isFinishing()) {
            ((BaseActivity) getActivity()).replaceFragmentWithBottomAnimation(fragment, name);
        }
    }

    public void addFragmentWithRightAnimation(Fragment fragment, String name) {
        if (getActivity() != null && !getActivity().isFinishing()) {
            ((BaseActivity) getActivity()).addFragmentWithRightAnimation(fragment, name);
        }
    }

    public void updateStatusBarColor(int color) {
        if (getActivity() != null && !getActivity().isFinishing()) {
            ((BaseActivity) getActivity()).updateStatusBarColor(color);
        }
    }

    public void hideKeyboard() {
        if (getActivity() != null && !getActivity().isFinishing()) {
            ((BaseActivity) getActivity()).hideKeyboard();
        }
    }

    public void showToast(String text)
    {
        if (getActivity() != null && !getActivity().isFinishing()) {
            ((BaseActivity) getActivity()).showToast(text);
        }
    }
}
