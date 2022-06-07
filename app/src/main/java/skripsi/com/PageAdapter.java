package skripsi.com;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import skripsi.com.Fragmant_Keruh.BulananFragment;
import skripsi.com.Fragmant_Keruh.HarianFragment;
import skripsi.com.Fragmant_Keruh.MingguanFragment;

public class PageAdapter extends FragmentPagerAdapter {
    private int numbertab;
    public PageAdapter(FragmentManager fm, int numoftab){
        super(fm);
        this.numbertab=numoftab;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
             case 0:
                return new HarianFragment();
             case 1:
                return new MingguanFragment();
                case 2:
                    return new BulananFragment();
            default:
                return null;
        }

        //return null;
    }

    @Override
    public int getCount() {
        return numbertab;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}
