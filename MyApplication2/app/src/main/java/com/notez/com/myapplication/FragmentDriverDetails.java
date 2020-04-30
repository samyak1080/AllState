package com.notez.com.myapplication;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import Adapters.DriverDetailsAdapter;
import Models.DriverModel;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentDriverDetails.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentDriverDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentDriverDetails extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private List<DriverModel> mDrivers;
    private RecyclerView mRecyclerView;
    private DriverDetailsAdapter mAdapter;



    private OnFragmentInteractionListener mListener;

    public FragmentDriverDetails() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentDriverDetails.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentDriverDetails newInstance(String param1, String param2) {
        FragmentDriverDetails fragment = new FragmentDriverDetails();
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
        View rootView=inflater.inflate(R.layout.fragment_fragment_driver_details, container, false);

        mRecyclerView = rootView.findViewById(R.id.claims);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        MainActivity.actionBar.show();

        mDrivers= new ArrayList<>();



        new GetDriverDetails(MainActivity.policy_id).execute();
        return rootView; }

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


    public class  GetDriverDetails extends AsyncTask<Void,Void, JSONArray> {
        DriverModel current;
        String policy_id;

        public GetDriverDetails(String policy_id){
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
                        .url(getContext().getResources().getString(R.string.api_server)+"get_driver_details")
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
                    mDrivers.clear();
                    for (int i = 0; i < all_claims.length(); i++) {
                        JSONObject jsonobject = null;
                        jsonobject = all_claims.getJSONObject(i);
                        String driver_id = jsonobject.getString("driver_id");
                        String driver_name = jsonobject.getString("driver_name");
                        String dob = jsonobject.getString("dob");
                        String dl_no = jsonobject.getString("dl_no");
                        String address_line_1 = jsonobject.getString("address_line_1");
                        String address_line_2 = jsonobject.getString("address_line_2");
                        String country = jsonobject.getString("country");
                        String city = jsonobject.getString("city");
                        String state = jsonobject.getString("state");
                        String dl_expiry_date = jsonobject.getString("dl_expiry_date");
                        String dl_issue_date = jsonobject.getString("dl_issue_date");



                        DriverModel temp = new DriverModel();
                       temp.setAddress_line_1(address_line_1);
                       temp.setAddress_line_2(address_line_2);
                       temp.setCity(city);
                       temp.setCountry(country) ;
                       temp.setId(driver_id);
                       temp.setName(driver_name);
                       temp.setDob(dob);
                       temp.setDl_number(dl_no);
                       temp.setState(state);
                       temp.setDl_expiry_date(dl_expiry_date);
                       temp.setDl_issue_date(dl_issue_date);
                       mDrivers.add(temp);

                    }
                }
                mAdapter = new DriverDetailsAdapter(getActivity(), mDrivers);
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


}
