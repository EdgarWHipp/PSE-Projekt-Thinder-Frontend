package com.hfad.thinder.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.hfad.thinder.R;
import com.hfad.thinder.viewmodels.ThesisCardItem;
import java.util.ArrayList;

public class ThesisCardAdapter
    extends RecyclerView.Adapter<ThesisCardAdapter.ThesisManagerViewHolder> implements Filterable {

  private final ArrayList<ThesisCardItem> elementsFull;
  private ArrayList<ThesisCardItem> elements;
  private OnItemClickListener listener;
  private final Filter elementsFilter = new Filter() {
    @Override
    protected FilterResults performFiltering(CharSequence charSequence) {
      ArrayList<ThesisCardItem> filteredList = new ArrayList<>();

      if (charSequence == null || charSequence.length() == 0) {
        filteredList.addAll(elementsFull);
      } else {
        String filterPattern = charSequence.toString().toLowerCase().trim();

        for (ThesisCardItem item : elementsFull) {
          if (item.getTitle().toLowerCase().contains(filterPattern)) {
            filteredList.add(item);
          }
        }
      }

      FilterResults results = new FilterResults();
      results.values = filteredList;
      return results;
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
      elements.clear();
      elements.addAll((ArrayList) filterResults.values);
      notifyDataSetChanged();
    }
  };

  public ThesisCardAdapter(ArrayList<ThesisCardItem> elements) {
    if (elements != null && !(elements.isEmpty())) {
      this.elements = new ArrayList<>(elements);
      elementsFull = new ArrayList<>(elements);
    } else {
      this.elements = new ArrayList<>();
      elementsFull = new ArrayList<>();
    }

  }

  public void setOnItemClickListener(OnItemClickListener listener) {
    this.listener = listener;
  }

  @NonNull
  @Override
  public ThesisCardAdapter.ThesisManagerViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                      int viewType) {
    View v = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.item_thesis_manager, parent, false);
    ThesisCardAdapter.ThesisManagerViewHolder coursesOfStudyViewHolder =
        new ThesisCardAdapter.ThesisManagerViewHolder(v, listener);
    return coursesOfStudyViewHolder;
  }

  @Override
  public void onBindViewHolder(@NonNull ThesisCardAdapter.ThesisManagerViewHolder holder,
                               int position) {
    ThesisCardItem current = elements.get(position);
    holder.title.setText(current.getTitle());
    holder.description.setText(current.getTask());
    holder.image.setImageBitmap(current.getImage());
  }

  @Override
  public int getItemCount() {
    return elements.size();
  }

  @Override
  public Filter getFilter() {
    return elementsFilter;
  }

  public void setElements(ArrayList<ThesisCardItem> elements) {
    this.elements = elements;
    notifyDataSetChanged();
  }

  public interface OnItemClickListener {
    void onItemClick(int position);
  }

  public static class ThesisManagerViewHolder extends RecyclerView.ViewHolder {
    public TextView title;
    public TextView description;
    public ImageView image;

    public ThesisManagerViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
      super(itemView);
      title = itemView.findViewById(R.id.tvTitle);
      description = itemView.findViewById(R.id.tvDescription);
      image = itemView.findViewById(R.id.ivThesisManagerItem);

      itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          if (listener != null) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
              listener.onItemClick(position);
            }
          }
        }
      });
    }
  }

}
