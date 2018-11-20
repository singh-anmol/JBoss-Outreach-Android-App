package gci.jbossoutreach.anmol.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import gci.jbossoutreach.anmol.R;
import gci.jbossoutreach.anmol.model.ContributorItem;

public class ContributorAdapter extends RecyclerView.Adapter<ContributorAdapter.ViewHolder> {

	private List<ContributorItem> contributorItems;
	private Context context;

	public ContributorAdapter(List<ContributorItem> listItems, Context context) {
		this.contributorItems = listItems;
		this.context = context;
	}


	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
		View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.contributor_item, viewGroup, false);
		return new ViewHolder(v);
	}

	@Override
	public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
		final ContributorItem listItem = contributorItems.get(i);
		viewHolder.contributor_name.setText(listItem.getName());
		viewHolder.contribution_count.setText("Contributions : ".concat(listItem.getContributions()));

		viewHolder.contributor_avatar.setVisibility(View.VISIBLE);
		Picasso.get()
				.load(listItem.getAvatar())
				.placeholder(R.drawable.ic_avatar)
				.error(R.drawable.ic_avatar)
				.into(viewHolder.contributor_avatar);

		viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String url = listItem.getUrl();
				Intent launchURL = new Intent(Intent.ACTION_VIEW);
				if (!url.startsWith("http://") && !url.startsWith("https://")) {
					url = "http://" + url;
				}
				launchURL.setData(Uri.parse(url));
				context.startActivity(launchURL);
			}
		});

	}

	@Override
	public int getItemCount() {
		return contributorItems.size();
	}

	public class ViewHolder extends RecyclerView.ViewHolder {

		public TextView contributor_name;
		public TextView contribution_count;
		public ImageView contributor_avatar;
		CardView cardView;

		ViewHolder(@NonNull View itemView) {
			super(itemView);
			cardView = itemView.findViewById(R.id.contributor);
			contributor_name = itemView.findViewById(R.id.contributor_name);
			contribution_count = itemView.findViewById(R.id.contribution_count);
			contributor_avatar = itemView.findViewById(R.id.contributor_avatar);
		}
	}

}
