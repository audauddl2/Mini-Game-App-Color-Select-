package com.example.management;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginRequest extends StringRequest {

    final static  private String URL = "http://서버주소/UserLogin.php";
    private Map<String, String> parameters;

    public  LoginRequest(String userId, String userPassword,Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("userId",userId);
        parameters.put("userPassword",userPassword);
    }

    @Override
    public Map<String, String> getParams(){
        return parameters;
    }

}
