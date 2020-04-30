package com.notez.com.myapplication;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import Adapters.PagerAdapter;
import Adapters.PolicyPagerAdapter;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SlidingPolicyDetails.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SlidingPolicyDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SlidingPolicyDetails extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public  static ViewPager policyViewPager;
    TabLayout policyTabLayout;
    //TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public SlidingPolicyDetails() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SlidingPolicyDetails.
     */
    // TODO: Rename and change types and number of parameters
    public static SlidingPolicyDetails newInstance(String param1, String param2) {
        SlidingPolicyDetails fragment = new SlidingPolicyDetails();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view= inflater.inflate(R.layout.fragment_sliding_policy_details, container, false);
        policyTabLayout=(TabLayout) view.findViewById(R.id.policyTabLayout);
        policyViewPager=(ViewPager) view.findViewById(R.id.policy_view_pager);
        policyTabLayout.setVisibility(View.VISIBLE);
        policyTabLayout.addTab(policyTabLayout.newTab().setText("Policy Details"));//.setIcon(getContext().getDrawable(R.drawable.ic_insurance_policy)));
        policyTabLayout.addTab(policyTabLayout.newTab().setText("Driver Details"));//.setIcon(getContext().getDrawable(R.drawable.ic_driver)));

        policyTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final PolicyPagerAdapter adapter = new PolicyPagerAdapter(((MainActivity)getActivity()).getSupportFragmentManager(), policyTabLayout.getTabCount());

        policyViewPager.setAdapter(adapter);



        policyViewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(policyTabLayout));
        //new GetNearbyEvents().execute();
        policyTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                policyViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
