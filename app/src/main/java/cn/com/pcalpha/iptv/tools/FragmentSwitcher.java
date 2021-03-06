package cn.com.pcalpha.iptv.tools;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by caiyida on 2018/7/23.
 */

public class FragmentSwitcher {
    private static Map<Integer,Fragment> currentMap = new HashMap<>();//全局缓存
    private FragmentManager fragmentManager;
    //private Fragment currentFragment;
    private int containerViewId;

    public FragmentSwitcher(@IdRes int containerViewId, FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
        this.containerViewId = containerViewId;
    }

    public void addFragment(Fragment targetFragment, String tag, boolean isBackStack) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (!targetFragment.isAdded()) {
            Fragment currentFragment = currentMap.get(containerViewId);
            if (null != currentFragment) {
                transaction.hide(currentFragment);
            }
            transaction
                    .add(containerViewId, targetFragment, tag);
            if(isBackStack){
                transaction.addToBackStack(targetFragment.getTag());
            }
            transaction
                    .hide(targetFragment)
                    .commit();
        }
    }

    public Fragment getFragment(String tag) {
        return fragmentManager.findFragmentByTag(tag);
    }

    public void showFragment(Fragment targetFragment) {
        fragmentManager.executePendingTransactions();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (!targetFragment.isAdded()) {
            Fragment currentFragment = currentMap.get(containerViewId);
            if (null != currentFragment) {
                transaction.hide(currentFragment);
            }
            transaction.add(containerViewId, targetFragment, targetFragment.getTag());

            transaction.show(targetFragment).commit();
        } else {
            Fragment currentFragment = currentMap.get(containerViewId);
            if (null != currentFragment) {
                transaction.hide(currentFragment);
            }
            transaction
                    .show(targetFragment)
                    .commit();
        }
        currentMap.put(containerViewId,targetFragment);
    }

}
