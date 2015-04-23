package ardhi.com.latihan;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ardhi.com.latihan.web.RequestMethod;
import ardhi.com.latihan.web.RestClient;

public class WisataActivity extends ActionBarActivity {
    ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wisata);

        listview = (ListView) findViewById(R.id.listView1);

        PostServices panggil = new PostServices();
        panggil.appContext = WisataActivity.this;
        panggil.execute();
    }

    class PostServices extends AsyncTask {
        private ProgressDialog dialog;
        private Context appContext;
        String datawisata_postServices;

        protected void onPreExecute() {
            dialog = new ProgressDialog(appContext);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setTitle("menunggu");
            dialog.setMessage("memposting");
            dialog.setMax(100);
            dialog.setProgress(0);
            dialog.show();
        }
        @Override
        protected Object doInBackground(Object... params) {
            // TODO Auto-generated method stub
            String link = "http://ardhi.000space.com/temanggung/index.php";
            RestClient client = new RestClient(link);
            try {
                client.execute(RequestMethod.GET);
                System.out.println(client.getResponse());
                datawisata_postServices = client.getResponse();
                client.getResponse();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Object result) {
            this.dialog.cancel();
            buatTampilListView(datawisata_postServices);
        }
    }

    private void buatTampilListView(String dataWisata){
        final ArrayList<JSONObject> alWisata = new ArrayList<JSONObject>();
        try {
            JSONArray wisata = new JSONArray(dataWisata);
            System.out.println("---> wisata length : "+wisata.length());
            for (int i=0; i<wisata.length(); i++){
                JSONObject wisata1 = wisata.getJSONObject(i);
                alWisata.add(wisata1);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        StableArrayAdapter adapter = new StableArrayAdapter(this, R.layout.wisata_row, alWisata);
        System.out.println("----------> Set Adapter");
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                Intent detail = new Intent(WisataActivity.this, ReviewWisataActivity.class);
                detail.putExtra("data-wisata", alWisata.get(position).toString()); //mengirim data ke activity ReviewWisataActivity
                startActivity(detail);
            }
        });
    }

    public class StableArrayAdapter extends ArrayAdapter<JSONObject>{
        Context myContext;
        ArrayList<JSONObject> alWisata;

        //HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();
        public StableArrayAdapter(Context context, int resource, ArrayList objects) {
            super(context, resource, objects);

            myContext = context;
            alWisata = objects;

            /*for (int i=0; i < objects.size(); i++){
                mIdMap.put(objects.get(i), i);
            }*/
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(WisataActivity.this);//(LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rootView = inflater.inflate(R.layout.wisata_row, null);//(R.layout.wisata_row, parent, false);

            TextView txtNamaWisata = (TextView) rootView.findViewById(R.id.txtNamaWisata);
            TextView txtInfoWisata = (TextView) rootView.findViewById(R.id.txtInfoWisata);

            try {
                txtNamaWisata.setText(alWisata.get(position).getString("nama_wisata"));
                System.out.println("----> TEST: " + alWisata.get(position).getString("nama_wisata"));
                txtInfoWisata.setText(alWisata.get(position).getString("info_wisata"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return rootView;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_wisata, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
