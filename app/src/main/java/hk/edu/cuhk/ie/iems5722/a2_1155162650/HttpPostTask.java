package hk.edu.cuhk.ie.iems5722.a2_1155162650;

import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;

import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;


import java.net.URL;

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
            DefaultHttpClient client = new DefaultHttpClient();

            HttpPost post = new HttpPost(url);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//            post.setHeader("Content-type", "multipart/form-data");
//            post.setHeader("Accept", "application/json");
//
//            JSONObject param_data = new JSONObject();
//            param_data.put("chatroom_id", params[1]);
//            param_data.put("user_id", params[2]);
//            param_data.put("name", params[3]);
//            param_data.put("message", params[4]);
//            post.setEntity(new StringEntity(param_data.toString()));
            builder.addTextBody("chatroom_id", params[1], ContentType.TEXT_PLAIN);
            builder.addTextBody("user_id", params[2], ContentType.TEXT_PLAIN);
            builder.addTextBody("name", params[3], ContentType.TEXT_PLAIN);
            builder.addTextBody("message", params[4], ContentType.TEXT_PLAIN);
            HttpEntity multipart = builder.build();
            post.setEntity(multipart);

            HttpResponse response = client.execute(post);
            int status = response.getStatusLine().getStatusCode();

            if (status == 200) {
                HttpEntity entity = response.getEntity();
                String data = EntityUtils.toString(entity);
                setResponse(new JSONObject(data));
                return true;
            }


        }catch (Exception e) {
            e.printStackTrace();
        }

        return false;
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


