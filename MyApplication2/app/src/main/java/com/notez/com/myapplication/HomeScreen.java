package com.notez.com.myapplication;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.os.CountDownTimer;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.SwipeDismissBehavior;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

import Models.ClaimsModel;
import Models.GeoEvents;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

import static android.provider.MediaStore.Files.FileColumns.MIME_TYPE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeScreen.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeScreen#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeScreen extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static ImageView my_space_bell,alert_noti;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public TextView clientName,sign_out;
    private List<ClaimsModel> mClaims;


    public RelativeLayout event_1,event_2;
    public ImageView event_1_icon,event_2_icon;
    public ImageView dismiss_event_alert;
    public TextView event_1_title,event_1_category,event_1_message,event_2_title,event_2_category,event_2_message;



    public CardView profile, policy, payment,contactUs,showClaims,roadSideAssist,event_alerts;
    private OnFragmentInteractionListener mListener;
    public static List<GeoEvents> mgeoEvents=new ArrayList<>();


    public HomeScreen() {
        // Required empty public constructorog
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeScreen.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeScreen newInstance(String param1, String param2) {
        HomeScreen fragment = new HomeScreen();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public void checkEvents(){

        new GetNearbyEvents().execute();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        final View rootView=inflater.inflate(R.layout.fragment_home_screen, container, false);
        profile =rootView.findViewById(R.id.profile);
        my_space_bell =rootView.findViewById(R.id.my_space_bell);
        clientName=rootView.findViewById(R.id.agent_name);
        roadSideAssist=rootView.findViewById(R.id.road_side_assistance);
        policy =rootView.findViewById(R.id.policy);
        sign_out=rootView.findViewById(R.id.sign_out);
        payment =rootView.findViewById(R.id.payment);
        contactUs=rootView.findViewById(R.id.contact_us);
        showClaims=rootView.findViewById(R.id.claims);

        mClaims= new ArrayList<>();
        alert_noti=rootView.findViewById(R.id.alert_noti_icon);

        (( MainActivity)getActivity()).hideClaimPager();
        if(MainActivity.total_events==0)
            alert_noti.setVisibility(View.GONE);
        else
            alert_noti.setVisibility(View.VISIBLE);

        event_alerts = (CardView) rootView.findViewById(R.id.event_alerts);

        dismiss_event_alert =  rootView.findViewById(R.id.cd_ok);

        dismiss_event_alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.exit_to_right);
                animation.setStartOffset(0);

                event_alerts.startAnimation( animation);


                new Handler().postDelayed(new Runnable() {

                    public void run() {

                       event_alerts.setVisibility(View.GONE);
                    }

                }, animation.getDuration());

            }
        });

        event_1=rootView.findViewById(R.id.event_1);
        event_2=rootView.findViewById(R.id.event_2);
        event_1_icon=rootView.findViewById(R.id.event_1_icon);
        event_2_icon=rootView.findViewById(R.id.event_2_icon);
        event_1_message=rootView.findViewById(R.id.event_1_message);
        event_2_message=rootView.findViewById(R.id.event_2_message);
        event_1_category=rootView.findViewById(R.id.event_1_category);
        event_2_category=rootView.findViewById(R.id.event_2_category);
        event_1_title=rootView.findViewById(R.id.event_1_title);
        event_2_title=rootView.findViewById(R.id.event_2_title);


        implementSwipeDismiss();
        //new RetrieveAll().execute();
        MainActivity.actionBar.hide();
        Bundle args = getArguments();
        String agent_name = args.getString("user_name", "Invalid Singh");
        clientName.setText(agent_name);

        sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                MainActivity.call_once=0;
                getActivity().finish();
            }
        });

        alert_noti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayEvents();
                implementSwipeDismiss();
                event_alerts.requestLayout();
                alert_noti.clearAnimation();


                event_alerts.setVisibility(View.VISIBLE);
                Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.enter_from_right);
                animation.setStartOffset(0);

                event_alerts.startAnimation( animation);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final android.app.Fragment mp=new FragmentUserProfile();

                final FragmentManager fragmentManager = getFragmentManager();
                final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.home_screen,mp);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        showClaims.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new RetrieveAll().execute();
             /*   final android.app.Fragment maClaimHomePage=new ClaimHomePage();
                getActivity().getFragmentManager().popBackStack();
                final FragmentManager fragmentManager = getFragmentManager();
                final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.home_screen,maClaimHomePage);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
*/
            /*    MainActivity.viewPager.setCurrentItem(0);

                MainActivity.home_screen.setVisibility(View.GONE);
                MainActivity.actionBar.show();*/
            }
        });

        policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final android.app.Fragment mp=new SlidingPolicyDetails();

                final FragmentManager fragmentManager = getFragmentManager();
                final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.home_screen,mp);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Fragment not Added yet",Toast.LENGTH_LONG).show();
            }
        });
        contactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Fragment not Added yet",Toast.LENGTH_LONG).show();
            }
        });
        roadSideAssist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final android.app.Fragment me=new RoadsideAssistance();

                final FragmentManager fragmentManager = getFragmentManager();
                final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.home_screen,me);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });


        if (MainActivity.call_once==0)
        {
            checkEvents();
            MainActivity.call_once=-1;
        }


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
    public  class  GetNearbyEvents extends AsyncTask<Void,Void, JSONArray> {
        GeoEvents current;

        public GetNearbyEvents(){
            Log.e("Request_EVENTS","Created");
        }


        @Override
        protected JSONArray doInBackground(Void... voids) {

            try {
                OkHttpClient client = new OkHttpClient();

                Log.e("Request_EVENTS","Initiated");
                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("policy_id", MainActivity.policy_id)
                        .build();


                Request request = new Request.Builder()
                        .url(getResources().getString(R.string.api_server)+"nearby_events")
                        .post(requestBody)
                        .build();
                Log.e("Request_EVENTS","sent");

                Response response = client.newCall(request).execute();
                InputStream inputStream = response.body().byteStream();
                InputStreamReader isReader = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(isReader);
                StringBuffer sb = new StringBuffer();
                String str;
                while((str = reader.readLine())!= null){
                    sb.append(str);
                }
                Log.e("Response_EVENTS",sb.toString());

                JSONArray myresponse= new JSONArray(sb.toString());

                Log.e("Request_EVENTS",myresponse.toString());
                return myresponse;


            } catch (IOException e) {
                Log.e("IO",e.getMessage());
            } catch (JSONException e) {

                Log.e("JE",e.getMessage());
            }

            return null;
        }


        @Override
        protected void onPostExecute(JSONArray all_geo_events) {
            try {


                if (all_geo_events==null) {
                    Toast.makeText(getContext(), "No nearby Events", Toast.LENGTH_LONG).show();

                }
                else{
                    int t=0;
                    mgeoEvents.clear();





                    for (int i = 0; i < all_geo_events.length(); i++) {
                        JSONObject jsonobject = null;
                        jsonobject = all_geo_events.getJSONObject(i);
                        t++;
                        String latitude = jsonobject.getString("lat");
                        String longitude = jsonobject.getString("long");
                        String title = jsonobject.getString("title");
                        String category = jsonobject.getString("category");
                        String distance = jsonobject.getString("distance");
                        GeoEvents temp = new GeoEvents();
                        temp.setLatitude(latitude);
                        temp.setLongitude(longitude);
                        temp.setTitle(title);
                        temp.setDistance(distance);
                        temp.setCategory(category);
                        mgeoEvents.add(temp);

                    }

                    MainActivity.total_events=t;
                   if(t>0)
                    {
                        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.shake);

                        animation.setStartOffset(0);
                        animation.setRepeatCount(Animation.INFINITE);
                        alert_noti.setVisibility(View.VISIBLE);
                        alert_noti.startAnimation(animation);
                        //displayEvents();
                    }
                }

            }catch (JSONException e) {
                Log.e("Exception JSON NEA",e.getMessage());
            }catch (Exception e){
                Log.e("Exception NEARBYEVENTS",e.getMessage());
            }

            super.onPostExecute(all_geo_events);

        }
    }


    private void implementSwipeDismiss() {
        SwipeDismissBehavior swipeDismissBehavior = new SwipeDismissBehavior();

        swipeDismissBehavior.setSwipeDirection(SwipeDismissBehavior.SWIPE_DIRECTION_ANY);
        final CoordinatorLayout.LayoutParams layoutParams =
                (CoordinatorLayout.LayoutParams) event_alerts.getLayoutParams();
        swipeDismissBehavior.setListener(new SwipeDismissBehavior.OnDismissListener() {
            @Override
            public void onDismiss(View view) {
                layoutParams.setMargins(0, 0, 0, 0);
                event_alerts.requestLayout();
                event_alerts.setAlpha(1.0f);
                event_alerts.setVisibility(View.GONE);

            }

            @Override
            public void onDragStateChanged(int state) {
            }
        });
        //Swipe direction i.e any direction, here you can put any direction LEFT or RIGHT


        layoutParams.setBehavior(swipeDismissBehavior);//set swipe behaviour to Coordinator layout
    }
    public void displayEvents(){
        event_1.setVisibility(View.GONE);
        event_2.setVisibility(View.GONE);
        int t=MainActivity.total_events;
        if (t>=1) {

            event_1.setVisibility(View.VISIBLE);
            event_2.setVisibility(View.GONE);
            event_1_title.setText(mgeoEvents.get(0).getTitle());
            event_1_category.setText(mgeoEvents.get(0).getCategory().toUpperCase());
            if (mgeoEvents.get(0).getCategory().toLowerCase().contains("weather") ||
                    mgeoEvents.get(0).getCategory().toLowerCase().contains("storm") ||
                    mgeoEvents.get(0).getCategory().toLowerCase().contains("winter")) {
                event_1_icon.setBackground(getResources().getDrawable(R.drawable.ic_storm));

                //event_1_message.setText("There was a severe weather warning in the locality of the user that might had been the cause of Incident.");
                event_1_message.setText("Severe weather - " + mgeoEvents.get(0).getTitle() + ", take caution while driving");
            } else {
                if (mgeoEvents.get(0).getCategory().equals("concerts")) {
                    event_1_icon.setBackground(getResources().getDrawable(R.drawable.ic_concert));
                    //event_1_message.setText("There was a concert happening "+mgeoEvents.get(0).getDistance()+" km away from the incident loction that might had been the cause of Incident.");
                }
                if (mgeoEvents.get(0).getCategory().equals("sports")) {
                    event_1_icon.setBackground(getResources().getDrawable(R.drawable.ic_trophy));
                    //event_1_message.setText("There was a sports event happening "+mgeoEvents.get(0).getDistance()+" km away from the incident loction that might had been the cause of Incident.");
                }

                if (mgeoEvents.get(0).getCategory().equals("festivals")) {
                    event_1_icon.setBackground(getResources().getDrawable(R.drawable.ic_festival));
                    //event_1_message.setText("There was a festival happening "+mgeoEvents.get(0).getDistance()+" km away from the incident loction that might had been the cause of Incident.");
                }
                event_1_message.setText(mgeoEvents.get(0).getTitle() + " " + mgeoEvents.get(0).getCategory() + " is taking place at Stevens Avenue, you might expect heavy traffic.");
            }
        }
        if (t==2){
            event_2.setVisibility(View.VISIBLE);
            event_2_title.setText(mgeoEvents.get(1).getTitle());
            event_2_category.setText(mgeoEvents.get(1).getCategory().toUpperCase());

            if(mgeoEvents.get(1).getCategory().toLowerCase().contains("weather") ||
                    mgeoEvents.get(1).getCategory().toLowerCase().contains("storm") ||
                    mgeoEvents.get(1).getCategory().toLowerCase().contains("winter")){
                event_2_icon.setBackground(getResources().getDrawable(R.drawable.ic_storm));

                //event_1_message.setText("There was a severe weather warning in the locality of the user that might had been the cause of Incident.");
                event_2_message.setText("Severe weather - " + mgeoEvents.get(1).getTitle() + ", take caution while driving");
            } else {
//            if(mgeoEvents.get(1).getCategory().equals("severe-weather")){
//                event_2_icon.setBackground(getResources().getDrawable(R.drawable.ic_storm));
//
//                event_2_message.setText("There was a severe weather warning in the locality of the user that might had been the cause of Incident.");
//            }
                if (mgeoEvents.get(1).getCategory().equals("concerts")) {
                    event_2_icon.setBackground(getResources().getDrawable(R.drawable.ic_concert));
                    //event_2_message.setText("There was a concert happening "+mgeoEvents.get(1).getDistance()+" km away from the incident loction that might had been the cause of Incident.");
                }
                if (mgeoEvents.get(1).getCategory().equals("sports")) {
                    event_2_icon.setBackground(getResources().getDrawable(R.drawable.ic_trophy));
                    //event_2_message.setText("There was a sports event happening "+mgeoEvents.get(1).getDistance()+" km away from the incident loction that might had been the cause of Incident.");
                }

                if (mgeoEvents.get(1).getCategory().equals("festivals")) {
                    event_2_icon.setBackground(getResources().getDrawable(R.drawable.ic_festival));
                    //event_2_message.setText("There was a festival happening "+mgeoEvents.get(1).getDistance()+" km away from the incident loction that might had been the cause of Incident.");
                }
                event_2_message.setText(mgeoEvents.get(1).getTitle() + " " + mgeoEvents.get(1).getCategory() + " is taking place at Stevens Avenue, you might expect heavy traffic.");
            }
        }
    }


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
                    if(should_show!=1)
                    {

                        /* final TextView auto_dismissal_timer,help;
                         ImageView dismiss_accident_alert;


                        final Dialog dialog = new Dialog(getContext());
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.accident_detected_alert_dialog);
                        dialog.setTitle("");
                        auto_dismissal_timer=dialog.findViewById(R.id.auto_dismisal_timer);

                        dismiss_accident_alert=dialog.findViewById(R.id.dismiss);
                        help=dialog.findViewById(R.id.help);

                        new CountDownTimer(60000, 1000) {
                            public void onTick(long millisUntilFinished) {
                                auto_dismissal_timer.setText( millisUntilFinished / 1000 +" sec ");
                            }

                            public void onFinish() {
                               dialog.dismiss();
                            }
                        }.start();

                        help.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                               */ TextView help_yes, help_no;
                                ImageView dismiss;
                                final Dialog childDialog = new Dialog(getContext());
                                childDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                childDialog.setContentView(R.layout.help_confirmation_dialog);
                                childDialog.setTitle("");
                                help_no = (TextView) childDialog.findViewById(R.id.help_no);

                                help_yes = (TextView) childDialog.findViewById(R.id.help_yes);
                                dismiss =  childDialog.findViewById(R.id.dismiss);
                                help_no.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        childDialog.dismiss();
                                        (( MainActivity)getActivity()).showClaimsPager();
                                        MainActivity.viewPager.setCurrentItem(1);
                                        getActivity().getFragmentManager().popBackStack();
                                        MainActivity.home_screen.setVisibility(View.GONE);
                                        MainActivity.actionBar.show();
                                    }
                                });
                                dismiss.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        childDialog.dismiss();

                                    }
                                });
                                help_yes.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        childDialog.dismiss();
                                        (( MainActivity)getActivity()).showClaimsPager();
                                        MainActivity.viewPager.setCurrentItem(0);
                                        getActivity().getFragmentManager().popBackStack();
                                        MainActivity.home_screen.setVisibility(View.GONE);
                                        MainActivity.actionBar.show();

                                    }
                                });

                                childDialog.show();
                      /*      }
                        });

                        dismiss_accident_alert.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                              dialog.dismiss();
                            }
                        });
                        dialog.show();*/
                    }
                    else{
                        (( MainActivity)getActivity()).showClaimsPager();
                        MainActivity.viewPager.setCurrentItem(1);
                        getActivity().getFragmentManager().popBackStack();
                        MainActivity.home_screen.setVisibility(View.GONE);
                        MainActivity.actionBar.show();
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
