package com.inoi.todolistapps.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.transition.Slide;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.inoi.todolistapps.Helpers.Url;
import com.inoi.todolistapps.MainActivity;
import com.inoi.todolistapps.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    EditText etUname, etPass;

    TextView tvRegister;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        etUname = v.findViewById(R.id.et_username);
        etPass = v.findViewById(R.id.et_password);

        tvRegister = v.findViewById(R.id.tv_register);

        tvRegister.setOnClickListener(view -> {
            showFragment(new RegisterFragment(), "Regigster");
        });

        return v;
    }

    private void showFragment(Fragment fragment, String tag){
        try {
            fragment.setEnterTransition(new Slide(Gravity.END));
            fragment.setExitTransition(new Slide(Gravity.START));
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//            transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
            transaction.replace(R.id.container_main, fragment, tag);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void Login(){
        try {
            String uname = etUname.getText().toString();
            String pass = etPass.getText().toString();


            JSONObject jsonParam = new JSONObject();
            try {
                jsonParam.put("password", uname);
                jsonParam.put("username", pass);
            } catch (Exception e) {
                e.printStackTrace();
            }

            java.lang.System.out.println(jsonParam);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Url.LOGIN, jsonParam, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        String message = response.getString("message");
                        int status = response.getInt("statusCode");
                        if (status == 2110) {
                            getActivity().finish();
                            startActivity(new Intent(getActivity(), MainActivity.class));
                        } else {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    String message = "";
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        //This indicates that the reuest has either time out or there is no connection
                        message =  "Tidak Ada Koneksi atau Kehabisan Waktu \n"+error.getMessage();
                    } else if (error instanceof AuthFailureError) {
                        //Error indicating that there was an Authentication Failure while performing the request
                        message ="Kesalahan Otentikasi \n"+error.getMessage();
                    } else if (error instanceof ServerError) {
                        //Indicates that the server responded with a error response
                        message = "Terjadi Kesalahan \n"+error.getMessage();
                    } else if (error instanceof NetworkError) {
                        //Indicates that there was network error while performing the request
                        message ="Jaringan Anda Tidak Stabil \n"+error.getMessage();
                    } else if (error instanceof ParseError) {
                        // Indicates that the server response could not be parsed
                        message = "Server Bermaslah \n"+error.getMessage();
                    }
                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String,String> params = new HashMap<String, String>();
                    return params;
                }
            };

            request.setRetryPolicy(new DefaultRetryPolicy(
                    20000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.add(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}