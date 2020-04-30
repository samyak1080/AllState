package com.notez.com.myapplication;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import Adapters.DependentDetailAdapter;
import Adapters.DriverDetailsAdapter;
import Models.DependentModel;
import Models.DriverModel;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentUserProfile.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentUserProfile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentUserProfile extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private List<DependentModel> mDependents;
    public static RecyclerView mRecyclerView;
    private DependentDetailAdapter mAdapter;
    public static  TextView login_username,customer_id,customer_name,customer_dob,policy_id,customer_address;
    public ImageView account_settings;



    private OnFragmentInteractionListener mListener;

    public FragmentUserProfile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentUserProfile.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentUserProfile newInstance(String param1, String param2) {
        FragmentUserProfile fragment = new FragmentUserProfile();
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
        View rootView=inflater.inflate(R.layout.fragment_user_profile, container, false);

        mRecyclerView = rootView.findViewById(R.id.dependents);
        customer_name = rootView.findViewById(R.id.customer_name);
        policy_id = rootView.findViewById(R.id.policy_id);
        customer_id = rootView.findViewById(R.id.customer_id);

        login_username = rootView.findViewById(R.id.login_username);
        customer_address = rootView.findViewById(R.id.address);
        customer_dob = rootView.findViewById(R.id.dob);
        mRecyclerView.setHasFixedSize(false);
        account_settings = rootView.findViewById(R.id.account_settings);


        account_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final android.app.Fragment mp=new FragmentAccountSetting();

                final FragmentManager fragmentManager = getFragmentManager();
                final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.home_screen,mp);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        MainActivity.actionBar.show();

        mDependents= new ArrayList<>();



        new GetDependentDetails(MainActivity.policy_id).execute();
        new GetCustomerDetails(MainActivity.policy_id).execute();
        return rootView;

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

    public class GetDependentDetails extends AsyncTask<Void,Void, JSONArray> {
        DriverModel current;
        String policy_id;

        public GetDependentDetails(String policy_id){
            this.policy_id=policy_id;
        }


        @Override
        protected JSONArray doInBackground(Void... voids) {

            try {
                OkHttpClient client = new OkHttpClient();


                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("policy_id", MainActivity.policy_id)
                        .build();


                Request request = new Request.Builder()
                        .url(getContext().getResources().getString(R.string.api_server)+"get_dependent_details")
                        .post(requestBody)
                        .build();
                Log.e("REQ_GET_DRIVER_DETAILS","sent");

                Response response = client.newCall(request).execute();
                InputStream inputStream = response.body().byteStream();
                InputStreamReader isReader = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(isReader);
                StringBuffer sb = new StringBuffer();
                String str;
                while((str = reader.readLine())!= null){
                    sb.append(str);
                }
                Log.e("Response",sb.toString());

                JSONArray myresponse= new JSONArray(sb.toString());

                Log.e("Request",response.message());
                return myresponse;


            } catch (IOException e) {
                Log.e("IO",e.getMessage());
            } catch (JSONException e) {

                Log.e("JE",e.getMessage());
            }

            return null;
        }


        @Override
        protected void onPostExecute(JSONArray all_claims) {
            try {

                if (all_claims==null)
                    Toast.makeText(getContext(),"No new Claims",Toast.LENGTH_LONG).show();
                else{
                    mDependents.clear();
                    for (int i = 0; i < all_claims.length(); i++) {
                        JSONObject jsonobject = null;
                        jsonobject = all_claims.getJSONObject(i);
                        String dependent_id = jsonobject.getString("dependent_id");
                        String driver_name = jsonobject.getString("dependent_name");
                        String dob = jsonobject.getString("dob");
                        String policy_id = jsonobject.getString("policy_id");
                        String relation_to_primary = jsonobject.getString("relation_to_primary");



                        DependentModel temp = new DependentModel();
                        temp.setDependentID(dependent_id);
                        temp.setName(driver_name);
                        temp.setDob(dob);
                        temp.setPolicyID(policy_id);
                        temp.setRelationship(relation_to_primary);
                        mDependents.add(temp);

                    }
                }
                mAdapter = new DependentDetailAdapter(getActivity(), mDependents);
                mRecyclerView.setAdapter(mAdapter);

            }catch (JSONException e) {
                e.printStackTrace();
            }
            catch (Exception e){
                Log.e("Exception",e.getMessage());
            }


            super.onPostExecute(all_claims);

        }
    }

    public class GetCustomerDetails extends AsyncTask<Void,Void, JSONArray> {

        String policy_id;

        public GetCustomerDetails(String policy_id){
            this.policy_id=policy_id;
        }


        @Override
        protected JSONArray doInBackground(Void... voids) {

            try {
                OkHttpClient client = new OkHttpClient();


                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("policy_id", MainActivity.policy_id)
                        .build();


                Request request = new Request.Builder()
                        .url(getContext().getResources().getString(R.string.api_server)+"get_customer_details")
                        .post(requestBody)
                        .build();
                Log.e("REQ_GET_CUST_DETAILS","sent");

                Response response = client.newCall(request).execute();
                InputStream inputStream = response.body().byteStream();
                InputStreamReader isReader = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(isReader);
                StringBuffer sb = new StringBuffer();
                String str;
                while((str = reader.readLine())!= null){
                    sb.append(str);
                }
                Log.e("Response",sb.toString());

                JSONArray myresponse= new JSONArray(sb.toString());

                Log.e("Request",response.message());
                return myresponse;


            } catch (IOException e) {
                Log.e("IO",e.getMessage());
            } catch (JSONException e) {

                Log.e("JE",e.getMessage());
            }

            return null;
        }


        @Override
        protected void onPostExecute(JSONArray all_claims) {
            try {

                if (all_claims==null)
                    Toast.makeText(getContext(),"Can't Fetch Customer Details",Toast.LENGTH_LONG).show();
                else{

                    for (int i = 0; i < all_claims.length(); i++) {
                        JSONObject jsonobject = null;
                        jsonobject = all_claims.getJSONObject(i);
                        customer_id.setText(jsonobject.getString("customer_id"));
                        customer_name.setText(jsonobject.getString("customer_name"));
                        customer_dob.setText( jsonobject.getString("dob"));
                        FragmentUserProfile.policy_id.setText( jsonobject.getString("policy_id"));
                        login_username.setText(jsonobject.getString("login_username"));

                        customer_address.setText(jsonobject.getString("mailing_address_line_1")+", "
                                +jsonobject.getString("mailing_address_line_2")+", "
                                +jsonobject.getString("mailing_city")+", "
                                +jsonobject.getString("mailing_state")+", "
                                +jsonobject.getString("mailing_country")+", "
                        );

                    }
                }


            }catch (JSONException e) {
                e.printStackTrace();
            }
            catch (Exception e){
                Log.e("Exception",e.getMessage());
            }


            super.onPostExecute(all_claims);

        }
    }

}
