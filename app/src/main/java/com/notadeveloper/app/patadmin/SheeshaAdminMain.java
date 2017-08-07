package com.notadeveloper.app.patadmin;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.widget.TextView;

public class SheeshaAdminMain extends AppCompatActivity
        implements EditFragment.OnFragmentInteractionListener,
        OrderFragment.OnFragmentInteractionListener {


    private ViewPager viewPager;
    private TabLayout tabLayout;

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html) {
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sheesha_admin_main);

      TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Orders"));
        tabLayout.addTab(tabLayout.newTab().setText("Edit Sheesha"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

      Toolbar toolbarTop = findViewById(R.id.my_toolbar);
      TextView mTitle = toolbarTop.findViewById(R.id.toolbar_title);

      final ViewPager viewPager = findViewById(R.id.pager);
      final com.notadeveloper.app.patadmin.PagerAdapter adapter =
          new com.notadeveloper.app.patadmin.PagerAdapter(getSupportFragmentManager(),
              tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }
    @Override
    public void onBackPressed() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(SheeshaAdminMain.this, R.style.pop);
        builder.setMessage("Are You Sure you want to exit?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
        //  super.onBackPressed();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
