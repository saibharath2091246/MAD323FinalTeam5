package com.team5.seeshop.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.team5.seeshop.firebase.MySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class SeeShopUtility {

    final private static String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final public static String serverKey = "key=" + "AAAAncm3ufk:APA91bEKJ_VMRrbI17rbycyvj1nJ28wV8SOh2JyJbIy29NwsDVRQ6yt_UTb1wNrKjQNbRMeRGLN3pOzXRMCqg0e0XucoO1oF68OcmPGPXsFYv13FBs-r-9Lhe3SU5nTsUjdB1f5JTHJH";
    final public static String contentType = "application/json";

    public static void startActivity(Context context,Class<?> cls )
    {

        Intent intent = new Intent(context, cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);


    }


    public static void showToast(Activity activity, String message) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }

    public static void hideKeyboard(Activity context)
    {
        try {
            InputMethodManager imm = (InputMethodManager)context.getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static void showKeyboard(Activity context){
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }


    public static String getCurrentDate()
    {
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
        String formattedDate = df.format(c);

        return formattedDate;
    }

    public static String getCurrentTime()
    {
        return new SimpleDateFormat("hh:mm:ss a", Locale.getDefault()).format(new Date());
    }

    public static String createOrderId() throws Exception{
        return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase().substring(15);
    }





    public static String getRandomNumber() {

        Random rnd = new Random();
        int number = rnd.nextInt(999999);

        // this will convert any number sequence into 6 character.
        return String.format("%06d", number);
    }

    public static Bitmap convertStringToBitmap(String base64Str) throws IllegalArgumentException
    {
        byte[] decodedBytes = Base64.decode(
                base64Str.substring(base64Str.indexOf(",")  + 1),
                Base64.DEFAULT
        );

        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public static String convertBitmapToString(Bitmap bitmap)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    }

    public static void notificationSetUp(Context context,String title,String message,String token)
    {
        JSONObject notification = new JSONObject();
        JSONObject notifcationBody = new JSONObject();
        try {
            notifcationBody.put("title", title);
            notifcationBody.put("message", message);

            notification.put("to", token);
            notification.put("data", notifcationBody);
        } catch (JSONException e) {
            Log.e("TAG", "onCreate: " + e.getMessage() );
        }
        sendNotification(context,notification);
    }

    public static void sendNotification(Context context,final JSONObject notification) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("TAG", "onResponse: " + response.toString());

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Request error", Toast.LENGTH_LONG).show();
                        Log.i("TAG", "onErrorResponse: Didn't work");
                    }
                })
        {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }
}
