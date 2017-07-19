package ma.dradeb.hiddenfounders.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Youness on 10/07/2017.
 */

public class PhotosFragmentsAdapter extends FragmentStatePagerAdapter {

    List<Fragment> listFragments = new ArrayList<>();


    public PhotosFragmentsAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.listFragments = fragments ;
    }

    @Override
    public Fragment getItem(int position) {
        return listFragments.get(position);
    }

    @Override
    public int getCount() {
        return listFragments.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
