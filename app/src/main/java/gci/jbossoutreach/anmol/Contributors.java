package gci.jbossoutreach.anmol;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
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

import gci.jbossoutreach.anmol.adapter.ContributorAdapter;
import gci.jbossoutreach.anmol.model.ContributorItem;

public class Contributors extends AppCompatActivity {

	private static String repo_name = "lead-management-android";
	private long exitTime = 0;
	private LinearLayout NoInternet;
	private RelativeLayout FetchingData;
	TextView textView;
	private RecyclerView recyclerView;
	private RecyclerView.Adapter adapter;
	private List<ContributorItem> listItems;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contributors);

		if (getSupportActionBar() != null) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		}

		Intent i = getIntent();
		Bundle b = i.getExtras();

		if (b != null) {
			repo_name = (String) b.get("repo_name");
			setTitle("Contributors");
			textView = findViewById(R.id.viewed_repo_name);
			textView.setText(repo_name);

		}

		recyclerView = findViewById(R.id.collaborator_recycler_view);
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

		String API_URL = "https://api.github.com/repos/JBossOutreach/";
		StringRequest stringRequest = new StringRequest(Request.Method.GET, API_URL.concat(repo_name).concat("/contributors"), new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					JSONArray jsonArray = new JSONArray(response);
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						String login = jsonObject.getString("login");
						String avatar = jsonObject.getString("avatar_url");
						String url = jsonObject.getString("html_url");
						String contributions = jsonObject.getString("contributions");
						ContributorItem item = new ContributorItem(login, avatar, url, contributions);
						listItems.add(item);
					}

					adapter = new ContributorAdapter(listItems, getApplicationContext());
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
	public boolean onOptionsItemSelected(MenuItem item) {
		onBackPressed();
		return true;
	}

}
