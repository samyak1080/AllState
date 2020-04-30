package com.notez.com.myapplication;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;

import android.support.v7.widget.CardView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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

import Adapters.ShowClaimsAdapter;
import Models.ClaimsModel;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

import static android.provider.MediaStore.Files.FileColumns.MIME_TYPE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ClaimHomePage.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ClaimHomePage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClaimHomePage extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public TextView auto_dismissal_timer,help;
    public ImageView dismiss_accident_alert;
    public CardView accident_detected_alert;
    public CardView showClaims,roadSideAssist;
    private List<ClaimsModel> mClaims;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ClaimHomePage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ClaimHomePage.
     */
    // TODO: Rename and change types and number of parameters
    public static ClaimHomePage newInstance(String param1, String param2) {
        ClaimHomePage fragment = new ClaimHomePage();
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
        View rootView=inflater.inflate(R.layout.fragment_claim_home_page, container, false);
        auto_dismissal_timer=rootView.findViewById(R.id.auto_dismisal_timer);
        accident_detected_alert=rootView.findViewById(R.id.accident_detected_alert);
        dismiss_accident_alert=rootView.findViewById(R.id.dismiss);
        showClaims=rootView.findViewById(R.id.claims);
        help=rootView.findViewById(R.id.help);
        mClaims= new ArrayList<>();
        MainActivity.actionBar.show();
        accident_detected_alert.setVisibility(View.GONE);

        //MainActivity.actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimaryDark)));

        new RetrieveAll().execute();


        showClaims.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              MainActivity.viewPager.setCurrentItem(3);
                getActivity().getFragmentManager().popBackStack();
                MainActivity.home_screen.setVisibility(View.GONE);
                MainActivity.actionBar.show();
            }
        });

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView help_yes, help_no;
                ImageView dismiss;
                final Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.help_confirmation_dialog);
                dialog.setTitle("");
                help_no = (TextView) dialog.findViewById(R.id.help_no);

                help_yes = (TextView) dialog.findViewById(R.id.help_yes);
                dismiss =  dialog.findViewById(R.id.dismiss);
                accident_detected_alert.setVisibility(View.GONE);
                help_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dismiss.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                    }
                });
                help_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        MainActivity.viewPager.setCurrentItem(0);
                        getActivity().getFragmentManager().popBackStack();
                        MainActivity.home_screen.setVisibility(View.GONE);
                        MainActivity.actionBar.show();

                    }
                });

                dialog.show();
            }
        });

        dismiss_accident_alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accident_detected_alert.setVisibility(View.GONE);
            }
        });

        new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                auto_dismissal_timer.setText( millisUntilFinished / 1000 +" sec ");
            }

            public void onFinish() {
                accident_detected_alert.setVisibility(View.GONE);
            }
        }.start();
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

    public class  RetrieveAll extends AsyncTask<Void,Void, JSONArray> {
        ClaimsModel current;
        String imagename;

        public RetrieveAll(){
        }


        @Override
        protected JSONArray doInBackground(Void... voids) {

            try {
                OkHttpClient client = new OkHttpClient();


                final MediaType MEDIA_TYPE_PNG = MediaType.parse(MIME_TYPE);

                RequestBody requestBody = new RequestBody() {

                    @Override
                    public MediaType contentType() {
                        return null;
                    }

                    @Override
                    public void writeTo(BufferedSink sink) throws IOException {

                    }
                };




                Request request = new Request.Builder()
                        .url(getContext().getResources().getString(R.string.api_server)+"retrieve_all_claims")
                        .post(requestBody)
                        .build();
                Log.e("Request","sent");

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
                    mClaims.clear();
                    int should_show=0;
                    for (int i = 0; i < all_claims.length(); i++) {
                        JSONObject jsonobject = null;
                        jsonobject = all_claims.getJSONObject(i);

                        String claimid = jsonobject.getString("claimid");
                        String overview = jsonobject.getString("overview");
                        String path = jsonobject.getString("path");
                        String st = jsonobject.getString("status");
                        ClaimsModel temp = new ClaimsModel();
                        temp.setStatus(st);
                        if(st.equals("SUBMITTED")||st.equals("ONHOLD"))
                        {
                            should_show=1;
                            break;
                        }

                        temp.setClaimid(claimid);
                        temp.setImageurl(path);
                        temp.setOverView(overview);
                        mClaims.add(temp);

                    }
                    if(should_show==1)
                        accident_detected_alert.setVisibility(View.GONE);
                    else
                        accident_detected_alert.setVisibility(View.VISIBLE);
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
