package com.hfad.thinder.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hfad.thinder.R;
import com.hfad.thinder.databinding.FragmentSwipeScreenTopBinding;
import com.hfad.thinder.viewmodels.student.SwipeScreenViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SwipeScreenTopFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SwipeScreenTopFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentSwipeScreenTopBinding binding;
    private View view;
    private SwipeScreenViewModel viewModel;
    private Boolean isCardOne = true;

    private TextView title;
    private TextView task;
    private ImageView image;

    public SwipeScreenTopFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SwipeScreenTopFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SwipeScreenTopFragment newInstance(String param1, String param2) {
        SwipeScreenTopFragment fragment = new SwipeScreenTopFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_swipe_screen_top, container, false);
        view = binding.getRoot();
        viewModel = new ViewModelProvider(requireActivity()).get(SwipeScreenViewModel.class);

        title = binding.tvTitleCardOne;
        image = binding.ivCardOne;
        task = binding.TaskContentCardOne;

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        Bundle bundle = this.getArguments();
        if(bundle!=null){
            boolean isCardOne = bundle.getBoolean("isCardOne");
            if(isCardOne){
                isCardOne = true;
                title.setText(viewModel.getCurrentCard().getTitle());
                if(viewModel.getCurrentHasImages().getValue())
                    image.setImageBitmap(viewModel.getCurrentCard().getImages().get(0));
                task.setText(viewModel.getCurrentCard().getTask());

            }else{
                isCardOne = false;
                title.setText(viewModel.getNextCard().getTitle());
                if(viewModel.getNextHasImages().getValue())
                    image.setImageBitmap(viewModel.getNextCard().getImages().get(0));
                task.setText(viewModel.getNextCard().getTask());
            }
        }else{
            title.setText(viewModel.getCurrentCard().getTitle());
            image.setImageBitmap(viewModel.getCurrentCard().getImages().get(0));
            task.setText(viewModel.getCurrentCard().getTask());
        }
    }
}