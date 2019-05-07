package com.example.management;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ValidateRequest extends StringRequest {

    final static  private String URL = "http://서버주소/UserValidate.php";
    private Map<String, String> parameters;

    public ValidateRequest(String userId, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("userId",userId);
    }

    @Override
    public Map<String, String> getParams(){
        return parameters;
    }
}

