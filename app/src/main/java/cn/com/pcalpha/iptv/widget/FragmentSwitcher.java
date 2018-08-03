package cn.com.pcalpha.iptv.widget;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.annotation.IdRes;

import cn.com.pcalpha.iptv.R;

/**
 * Created by caiyida on 2018/7/23.
 */

public class FragmentSwitcher {
    private FragmentManager fragmentManager;
    private Fragment currentFragment;
    private int containerViewId;

    public FragmentSwitcher(@IdRes int containerViewId, FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
        this.containerViewId = containerViewId;
    }

    public void addFragment(Fragment targetFragment, String tag) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (!targetFragment.isAdded()) {
            if (null != currentFragment) {
                transaction.hide(currentFragment);
            }
            transaction
                    .add(containerViewId, targetFragment, tag)
                    .hide(targetFragment)
                    .commit();
        }
    }

    public void showFragment(Fragment targetFragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (!targetFragment.isAdded()) {
            if (null != currentFragment) {
                transaction.hide(currentFragment);
            }
            transaction
                    .add(containerViewId, targetFragment, targetFragment.getTag())
                    .commit();
        } else {
            if (null != currentFragment) {
                transaction.hide(currentFragment);
            }
            transaction
                    .show(targetFragment)
                    .commit();
        }
        currentFragment = targetFragment;
    }

    public Fragment getFragment(String tag) {
        return fragmentManager.findFragmentByTag(tag);
    }
}
