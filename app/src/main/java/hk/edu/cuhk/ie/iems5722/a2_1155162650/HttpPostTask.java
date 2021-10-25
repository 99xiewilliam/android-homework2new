package hk.edu.cuhk.ie.iems5722.a2_1155162650;

import android.os.AsyncTask;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.IOException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public abstract class HttpPostTask extends AsyncTask<String, String, Boolean> implements SuccessFailed{
    private JSONObject response;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(String... params) {
        String url = params[0];
        try{

            return post(params);
//            DefaultHttpClient client = new DefaultHttpClient();
//
//            HttpPost post = new HttpPost(url);
//            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
////            post.setHeader("Content-type", "multipart/form-data");
////            post.setHeader("Accept", "application/json");
////
////            JSONObject param_data = new JSONObject();
////            param_data.put("chatroom_id", params[1]);
////            param_data.put("user_id", params[2]);
////            param_data.put("name", params[3]);
////            param_data.put("message", params[4]);
////            post.setEntity(new StringEntity(param_data.toString()));
//            builder.addTextBody("chatroom_id", params[1], ContentType.TEXT_PLAIN);
//            builder.addTextBody("user_id", params[2], ContentType.TEXT_PLAIN);
//            builder.addTextBody("name", params[3], ContentType.TEXT_PLAIN);
//            builder.addTextBody("message", params[4], ContentType.TEXT_PLAIN);
//            HttpEntity multipart = builder.build();
//            post.setEntity(multipart);
//
//            HttpResponse response = client.execute(post);
//            int status = response.getStatusLine().getStatusCode();
//
//            if (status == 200) {
//                HttpEntity entity = response.getEntity();
//                String data = EntityUtils.toString(entity);
//                setResponse(new JSONObject(data));
//                return true;
//            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public Boolean post(String... params) {
        OkHttpClient client = new OkHttpClient();
        FormBody body = new FormBody.Builder().add("chatroom_id", params[1])
                .add("user_id", params[2])
                .add("name", params[3])
                .add("message", params[4])
                .build();
        Request request = new Request.Builder()
                .url(params[0])
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String data = response.body().string();
                    try {
                        setResponse(new JSONObject(data));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        return true;
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        if (success) {
            success();
        }else {
            failed();
        }
    }

    public JSONObject getResponse() {
        return response;
    }

    public void setResponse(JSONObject response) {
        this.response = response;
    }



}


