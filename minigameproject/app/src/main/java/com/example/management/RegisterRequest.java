﻿package com.example.management;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {

    final static  private String URL = "http://서버주소/UserRegister.php";
    private Map<String, String> parameters;

    public  RegisterRequest(String userId, String userPassword, String userName, int userAge, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("userId",userId);
        parameters.put("userPassword",userPassword);
        parameters.put("userName",userName);
        parameters.put("userAge",userAge + "");//userAge는 int 형이기때문에 + ""를 해줘야함
    }

    @Override
    public Map<String, String> getParams(){
        return parameters;
    }
}
