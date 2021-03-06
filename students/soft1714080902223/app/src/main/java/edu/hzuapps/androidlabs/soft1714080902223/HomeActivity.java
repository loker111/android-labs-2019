package edu.hzuapps.androidlabs.soft1714080902223;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import edu.hzuapps.androidlabs.model.Task;
import edu.hzuapps.androidlabs.presenter.JsonService;
import edu.hzuapps.androidlabs.presenter.TaskService;

public class HomeActivity extends AppCompatActivity {

    private ImageButton mDetailBtn;
    private ListView lv_1;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mDetailBtn = findViewById(R.id.home_ibtn);
        mDetailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, EditActivity.class);
                startActivity(intent);

            }
        });

        lv_1 = findViewById(R.id.home_list);
        lv_1.setAdapter(new TaskService(this).allListAdapter());
        //为每个list_Item设置点击事件
        lv_1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(HomeActivity.this, DetailActivity.class);
                Task task = (Task)parent.getItemAtPosition(position);
                long taskId = task.getId();
                intent.putExtra("id", taskId);
                System.out.println(id);
                startActivity(intent);
//                Toast.makeText(HomeActivity.this, "您单击了" + position, Toast.LENGTH_SHORT).show();
            }
        });


        //创建一个新线程异步获取json数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject jsonObject = new JsonService().getHomeJson();
                tv = findViewById(R.id.bottom_text);
                try {
                    String text = jsonObject.getString("author") + "  "
                            + jsonObject.getString("code");
                    tv.setText(text);
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }).start();

    }


}

