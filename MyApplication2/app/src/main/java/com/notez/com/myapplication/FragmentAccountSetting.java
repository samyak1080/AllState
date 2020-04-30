package com.notez.com.myapplication;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentAccountSetting.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentAccountSetting#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentAccountSetting extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public static ImageView new_password_visible,old_password_visible,confirm_new_password_visible,edit_password;
    public static TextView change_password,username;
    public static boolean is_old_pass_vis=false,is_new_pass_vis=false,is_conf_new_pass_vis=false;
    public static EditText new_password,old_password,confirm_new_password;
    public static LinearLayout change_password_layout;
    private OnFragmentInteractionListener mListener;

    public FragmentAccountSetting() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentAccountSetting.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentAccountSetting newInstance(String param1, String param2) {
        FragmentAccountSetting fragment = new FragmentAccountSetting();
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
        View rootView=inflater.inflate(R.layout.fragment_account_setting, container, false);
        final String login_username,login_password;
        username = rootView.findViewById(R.id.username);
        old_password = rootView.findViewById(R.id.old_password);
        new_password = rootView.findViewById(R.id.new_password);
        confirm_new_password = rootView.findViewById(R.id.confirm_new_password);

        old_password_visible = rootView.findViewById(R.id.old_password_visible);
        new_password_visible = rootView.findViewById(R.id.new_password_visible);
        confirm_new_password_visible = rootView.findViewById(R.id.confirm_new_password_visible);

        edit_password = rootView.findViewById(R.id.edit_password);
        change_password = rootView.findViewById(R.id.change_password);

        change_password_layout = rootView.findViewById(R.id.change_password_layout);

        SharedPreferences prefs = getActivity().getSharedPreferences(
                "com.notez.com.myapplication", Context.MODE_PRIVATE);
        login_username=prefs.getString("login_username", "NA");
        login_password=prefs.getString("login_password", "NA");
        username.setText(login_username);
        old_password.setText(login_password);

        confirm_new_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if((!hasFocus)&&(!confirm_new_password.getText().toString().equals(new_password.getText().toString())))
                {
                    confirm_new_password.setError("Password Mismatch");
                }
            }
        });

        edit_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                old_password.setEnabled(true);
                old_password.setText("");
                change_password_layout.setVisibility(View.VISIBLE);
                edit_password.setVisibility(View.GONE);
            }
        });

        old_password_visible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                is_old_pass_vis=!is_old_pass_vis;

                if(is_old_pass_vis)
                {
                    old_password_visible.setImageDrawable(getContext().getDrawable(R.drawable.password_hidden));
                    old_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

                }
                else
                {

                    old_password_visible.setImageDrawable(getContext().getDrawable(R.drawable.password_visible));
                    old_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                old_password.setSelection(old_password.length());

            }
        });

        new_password_visible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                is_new_pass_vis=!is_new_pass_vis;

                if(is_new_pass_vis)
                {
                    new_password_visible.setImageDrawable(getContext().getDrawable(R.drawable.password_hidden));
                    new_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
                else
                {

                    new_password_visible.setImageDrawable(getContext().getDrawable(R.drawable.password_visible));
                    new_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                new_password.setSelection(new_password.length());

            }
        });
       confirm_new_password_visible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                is_conf_new_pass_vis=!is_conf_new_pass_vis;

                if(is_conf_new_pass_vis)
                {
                    confirm_new_password_visible.setImageDrawable(getContext().getDrawable(R.drawable.password_hidden));
                    confirm_new_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
                else
                {

                    confirm_new_password_visible.setImageDrawable(getContext().getDrawable(R.drawable.password_visible));
                    confirm_new_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }

                confirm_new_password.setSelection(confirm_new_password.length());

            }
        });

       change_password.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {


               new ChangePassword(login_username,confirm_new_password.getText().toString()).execute();
           }
       });

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


    public class ChangePassword extends AsyncTask<Void,Void, Boolean> {
        Boolean status=false;
        String username,new_password;

        public ChangePassword(String username,String new_password)
        {
            this.username=username;
            this.new_password=new_password;

        }


        @Override
        protected Boolean doInBackground(Void... voids) {

            try {
                OkHttpClient client = new OkHttpClient();


                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("username", username)
                        .addFormDataPart("new_password", new_password)
                        .build();


                Request request = new Request.Builder()
                        .url(getContext().getResources().getString(R.string.api_server)+"change_password")
                        .post(requestBody)
                        .build();
                Log.e("REQ_CHANGE_PASSWORD","sent");

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

                JSONObject myresponse= new JSONObject(sb.toString());
                if(myresponse.getString("password_changed").equals("true"))
                    status=true;

                Log.e("Request",response.message());
                return status;


            } catch (IOException e) {
                Log.e("IO",e.getMessage());
            } catch (JSONException e) {

                Log.e("JE",e.getMessage());
            }

            return status;
        }


        @Override
        protected void onPostExecute(Boolean status) {

            if(status)
                Toast.makeText(getContext(),"Password Updated Succesfull",Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getContext(),"Password Updation Failed",Toast.LENGTH_LONG).show();


            super.onPostExecute(status);

        }
    }

}
