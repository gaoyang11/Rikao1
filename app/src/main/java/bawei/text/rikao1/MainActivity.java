package bawei.text.rikao1;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.gson.Gson;
import com.limxing.xlistview.view.XListView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity implements XListView.IXListViewListener{
    private XListView xlv;
    private int page=1;
    private boolean flag;
    private List<Bean.ResultBean.DataBean> list;
    private MyBaseadapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        xlv = (XListView) findViewById(R.id.xlv);
        xlv.setPullLoadEnable(true);
        xlv.setXListViewListener(this);
        data();
        //askjdhviashvd
    }
    public void data(){
        jiexi("http://apis.juhe.cn/cook/query?key=900eb2e99f3c7b21f3914aefa914a327&menu=%E8%A5%BF%E7%BA%A2%E6%9F%BF&rn=10&pn="+page+"");
    }
    public void jiexi(String path){
        new AsyncTask<String,Void,String>(){
            @Override
            protected void onPostExecute(String s) {
                if (s!=null){
                    Gson gson=new Gson();
                    Bean bean = gson.fromJson(s, Bean.class);
                    list = bean.getResult().getData();
                    if (adapter==null) {
                        adapter = new MyBaseadapter(MainActivity.this, list);
                        xlv.setAdapter(adapter);
                    }else{
                        adapter.sss(list,flag);
                    }
                }
            }

            @Override
            protected String doInBackground(String... params) {
                try {
                    URL url=new URL(params[0]);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setReadTimeout(3000);
                    connection.setConnectTimeout(3000);
                    int code = connection.getResponseCode();
                    if (code==200){
                        InputStream is = connection.getInputStream();
                        return StreamTools.readFromNetWork(is);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute(path);
    }

    @Override
    public void onRefresh() {
        flag=false;
        ++page;
        data();
        xlv.stopRefresh(true);
    }

    @Override
    public void onLoadMore() {
        flag=true;
        ++page;
        data();
        xlv.stopLoadMore();
    }
    }

