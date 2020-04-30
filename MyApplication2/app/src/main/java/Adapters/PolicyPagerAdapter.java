package Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.notez.com.myapplication.ClaimEstimation;
import com.notez.com.myapplication.FragmentDriverDetails;
import com.notez.com.myapplication.MySpace;
import com.notez.com.myapplication.Policy;
import com.notez.com.myapplication.RaiseClaim;
import com.notez.com.myapplication.ShowClaims;

public class PolicyPagerAdapter extends FragmentStatePagerAdapter {
    int NumberOfTabs;
    public PolicyPagerAdapter(FragmentManager fm, int NumberofTabs) {
        super(fm);
        this.NumberOfTabs=NumberofTabs;
    }

    @Override
    public Fragment getItem(int i) {
        switch ( i){
            case 0:
                Policy tab0 =new Policy();
                Log.e("SLIDER","TAB 0");
                return  tab0;
            case 1:
                FragmentDriverDetails tab1=new FragmentDriverDetails();
                Log.e("SLIDER","TAB 1");
                return  tab1;


            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NumberOfTabs;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
