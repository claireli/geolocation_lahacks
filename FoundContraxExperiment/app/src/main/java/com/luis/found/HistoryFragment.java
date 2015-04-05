
package com.luis.found;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import android.support.v4.app.Fragment;
import android.widget.Toast;


/**
 * Created by Luis Meraz on 4/4/2015. AND CLAIRE LI omg.
 */
public class HistoryFragment extends Fragment {

    Button btnAdd, btnViewAll, btnDelete, updateGPS, capture, coord, history, manual, account;
    Toast clairetoast;

    public HistoryFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.history_fragment, container, false);



        capture=(Button)rootView.findViewById(R.id.capture);
        capture.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ((MainActivity)getActivity()).getViewPager().setCurrentItem(1);
            }
        });

        coord=(Button)rootView.findViewById(R.id.coord);
        coord.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                  Intent myIntent = new Intent(getActivity(), Coord_Activity.class);
                  getActivity().startActivity(myIntent);

            }
        });

        manual=(Button)rootView.findViewById(R.id.manual);
        manual.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent myIntent = new Intent(getActivity(), ManualActivity.class);
                getActivity().startActivity(myIntent);
            }
        });

        history=(Button)rootView.findViewById(R.id.history);
        history.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                clairetoast("history of stuff");
            }
        });

        account=(Button)rootView.findViewById(R.id.account);
        account.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                clairetoast("account stuff");
            }
        });

        return rootView;
    }

    public void clairetoast(String message){


        clairetoast = Toast.makeText(getActivity().getApplicationContext(),
                message,
                Toast.LENGTH_SHORT);

        clairetoast.show();

    }

}
