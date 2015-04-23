package ardhi.com.latihan;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;


public class WisataTabActivity extends ActionBarActivity {
    public static final String M_CURRENT_TAB = "M_CURRENT_TAB";
    private TabHost mTabHost;
    private String mCurrentTab;

    public static final String TAB_WISATA = "TAB_WISATA"; //tab yang akan dibentuk
    public static final String TAB_SEARCH = "TAB_SEARCH";

    //panggil fragment pembentuk tab
    Fragment wisataFragment = new WisataFragment();
    Fragment searchFragment = new SearchFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wisata_tab);

        //setup tabHost
        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup();

        if (savedInstanceState != null) {
            mCurrentTab = savedInstanceState.getString(M_CURRENT_TAB);
            initializeTabs();
            mTabHost.setCurrentTabByTag(mCurrentTab);
            /*
            when resume state it's important to set listener after initializeTabs
            */
            mTabHost.setOnTabChangedListener(listener);
        } else {
            mTabHost.setOnTabChangedListener(listener);
            initializeTabs();
        }
    }

    /*
    create 2 tabs with name and image
    and add it to TabHost
     */
    public void initializeTabs() {
        TabHost.TabSpec spec;

        spec = mTabHost.newTabSpec(TAB_WISATA);
        spec.setContent(new TabHost.TabContentFactory() {
            public View createTabContent(String tag) {
                return findViewById(R.id.realtabcontent);
            }
        });
        spec.setIndicator("wisata");
//        spec.setIndicator(createTabView(R.drawable.amarok, "wisata"));
        mTabHost.addTab(spec);

        spec = mTabHost.newTabSpec(TAB_SEARCH);
        spec.setContent(new TabHost.TabContentFactory() {
            public View createTabContent(String tag) {
                return findViewById(R.id.realtabcontent);
            }
        });
        spec.setIndicator("search");
//        spec.setIndicator(createTabView(R.drawable.amarok, "search"));
        mTabHost.addTab(spec);
    }

    /*
    first time listener will be trigered immediatelly after first: mTabHost.addTab(spec);
    for set correct Tab in setmTabHost.setCurrentTabByTag ignore first call of listener
    */
    TabHost.OnTabChangeListener listener = new TabHost.OnTabChangeListener() {
        public void onTabChanged(String tabId) {
            mCurrentTab = tabId;

            if (tabId.equals(TAB_WISATA)) {
                pushFragments(wisataFragment, false,
                        false, null);
            } else if (tabId.equals(TAB_SEARCH)) {
                pushFragments(searchFragment, false,
                        false, null);
            }
        }

    };

    private View createTabView(final int id, final String text) {
        View view = LayoutInflater.from(this).inflate(R.layout.tabs_icons, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.tab_icon);
        imageView.setImageDrawable(getResources().getDrawable(id));
        TextView textView = (TextView) view.findViewById(R.id.tab_text);
        textView.setText(text);
        return view;
    }

    public void pushFragments(android.support.v4.app.Fragment fragment,
                              boolean shouldAnimate, boolean shouldAdd, String tag) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        if (shouldAnimate) {
//            ft.setCustomAnimations(R.animator.fragment_slide_left_enter,
//                    R.animator.fragment_slide_left_exit,
//                    R.animator.fragment_slide_right_enter,
//                    R.animator.fragment_slide_right_exit);
        }
        ft.replace(R.id.realtabcontent, fragment, tag);

        if (shouldAdd) {
            /*
            here you can create named backstack for realize another logic.
            ft.addToBackStack("name of your backstack");
             */
            ft.addToBackStack(null);
        } else {
            /*
            and remove named backstack:
            manager.popBackStack("name of your backstack", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            or remove whole:
            manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
             */
            manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_wisata_tab, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
