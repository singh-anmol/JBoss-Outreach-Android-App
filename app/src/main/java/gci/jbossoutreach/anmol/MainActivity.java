package gci.jbossoutreach.anmol;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import gci.jbossoutreach.anmol.adapter.RepoAdapter;
import gci.jbossoutreach.anmol.model.ListItem;

public class MainActivity extends AppCompatActivity {

	private static final String API_URL = "https://api.github.com/users/JBossOutreach/repos";
	private long exitTime = 0;
	private LinearLayout NoInternet;
	private RelativeLayout FetchingData;
	private RecyclerView recyclerView;
	private RecyclerView.Adapter adapter;
	private List<ListItem> listItems;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		if (getSupportActionBar() != null) {
			getSupportActionBar().setLogo(R.mipmap.ic_launcher);

		}

		recyclerView = findViewById(R.id.repo_recycler_view);
		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));

		NoInternet = findViewById(R.id.layout_no_internet);
		ImageView img = findViewById(R.id.btn_retry);
		img.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				loadRecyclerViewData(true);
			}
		});
		FetchingData = findViewById(R.id.fetching_data);

		listItems = new ArrayList<>();
		loadRecyclerViewData(false);

	}

	private void loadRecyclerViewData(Boolean refresh) {

		if (refresh) {
			FetchingData.setVisibility(View.VISIBLE);
			recyclerView.setVisibility(View.GONE);
			NoInternet.setVisibility(View.GONE);
		}

		StringRequest stringRequest = new StringRequest(Request.Method.GET, API_URL, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				setTitle("GitHub Repos");
				try {
					JSONArray jsonArray = new JSONArray(response);
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						ListItem item = new ListItem(
								jsonObject.getString("name"),
								jsonObject.getString("description")
						);
						listItems.add(item);
					}

					adapter = new RepoAdapter(listItems, getApplicationContext());
					recyclerView.setAdapter(adapter);
					FetchingData.setVisibility(View.GONE);
					recyclerView.setVisibility(View.VISIBLE);
					recyclerView.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));

				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				String message = null;
				if (error instanceof NetworkError) {
					message = "Cannot connect to Internet... Please check your connection!";
				} else if (error instanceof ServerError) {
					message = "The server could not be found. Please try again after some time!";
				} else if (error instanceof AuthFailureError) {
					message = "Cannot connect to Internet... Please check your connection!";
				} else if (error instanceof ParseError) {
					message = "Parsing error! Please try again after some time!!";
				} else if (error instanceof NoConnectionError) {
					message = "Cannot connect to Internet... Please check your connection!";
				} else if (error instanceof TimeoutError) {
					message = "Connection TimeOut! Please check your internet connection.";
				}
				FetchingData.setVisibility(View.GONE);
				NoInternet();
				Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
			}
		});

		RequestQueue requestQueue = Volley.newRequestQueue(this);
		requestQueue.add(stringRequest);
	}

	public void NoInternet() {
		FetchingData.setVisibility(View.GONE);
		recyclerView.setVisibility(View.GONE);
		NoInternet.setVisibility(View.VISIBLE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_about) {
			Intent launchURL = new Intent(MainActivity.this, AboutActivity.class);
			startActivity(launchURL);
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		doExitApp();
	}

	public void doExitApp() {
		if ((System.currentTimeMillis() - exitTime) > 2000) {
			Toast.makeText(this, R.string.press_again_exit_app, Toast.LENGTH_SHORT).show();
			exitTime = System.currentTimeMillis();
		} else {
			finish();
		}
	}
}
