package me.msfjarvis.halogenota;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.afollestad.bridge.Bridge;
import com.afollestad.bridge.BridgeException;
import com.crashlytics.android.Crashlytics;
import com.baoyz.widget.PullRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import me.msfjarvis.halogenota.adapters.BBAdapter;
import me.msfjarvis.halogenota.data.BasketBuildFile;
import me.msfjarvis.halogenota.utils.Constants;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends Activity {

    private PullRefreshLayout pullRefreshLayout;
    private Context context = this;
    private JSONObject fileJson = null;
    private ListView listView;
    private SimpleDateFormat sdf;
    private List<BasketBuildFile> filesD = new ArrayList<>();
    private BBAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fabric.with(context, new Crashlytics());
        sdf = new SimpleDateFormat("yyyy/MM/dd \nHH:mm", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getDefault());
        pullRefreshLayout = (PullRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        listView = (ListView) findViewById(R.id.listView);
        adapter = new BBAdapter(context,R.layout.bb_file,filesD);
        loadData();
    }

    private void loadData() {
        pullRefreshLayout.setRefreshing(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    fileJson = Bridge
                            .get(Constants.BB_API, Constants.DEV_NAME, Constants.FOLDER_NAME)
                            .asJsonObject();
                } catch (BridgeException exc) {
                    // Ignore this for now
                }

            }
        }).start();
    }

    public void parse() {
        JSONObject data = null;
        try {
            Object dataObj = fileJson.get("files");
            if (!(dataObj instanceof JSONObject))
                return;
            data = (JSONObject) dataObj;
            JSONArray files = null;
            if (!data.isNull("files"))
                files = data.getJSONArray("files");
            if (files != null) {
                for (int i = 0; i < files.length(); i++) {
                    JSONObject file = files.getJSONObject(i);
                    String name = file.getString("file");
                    String url = file.getString("filelink");
                    String upload_date = file.getString("fileTimestamp");
                    String file_size = file.getString("filesize");
                    String hDate = sdf.format(new Date(Integer.parseInt(upload_date) * 1000L));
                    filesD.add(new BasketBuildFile(name,url,file_size,hDate));
                    adapter.notifyDataSetChanged();
                }
            } else {
                Toast.makeText(this, "No JSON Object could be parsed", Toast.LENGTH_LONG).show();
            }
        } catch (JSONException exc) {
            Toast.makeText(context, exc.toString(), Toast.LENGTH_LONG).show();
        }

    }
}
