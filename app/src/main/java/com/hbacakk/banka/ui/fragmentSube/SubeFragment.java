package com.hbacakk.banka.ui.fragmentSube;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.hbacakk.banka.R;
import com.hbacakk.banka.data.models.Sube;
import com.hbacakk.banka.databinding.FragmentSubeBinding;
import com.hbacakk.banka.viewmodels.MainViewModel;

public class SubeFragment extends Fragment implements SubeListener {

    FragmentSubeBinding subeBinding;
    MainViewModel mainViewModel;

    SubeAdapter subeAdapter;
    static String TAG = "SubeFragment";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        subeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_sube, container, false);

        initialize();

        return subeBinding.getRoot();
    }


    private void initialize() {
        //region: ViewModelProvider
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        //endregion
        //region: RecyclerView Adapter oluşturma
        subeAdapter = new SubeAdapter();
        subeAdapter.setSubeListener(this);
        subeBinding.recyclerView.setAdapter(subeAdapter);
        //endregion
        //region: SearchBar
        subeBinding.searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                subeAdapter.search(s);
                return false;
            }
        });
        //endregion
        //region: SwipeRefresh Layout
        subeBinding.swipeRefreshLayout.setOnRefreshListener(() -> {
            subeBinding.swipeRefreshLayout.setRefreshing(false);
            getBankaData();
        });
        //endregion


    }

    @Override
    public void onResume() {
        getBankaData();
        super.onResume();
    }

    private void getBankaData() {
        subeBinding.setLoading(true);
        mainViewModel.getBankaSubeleri().observe(getActivity(), response -> {
            Log.d(TAG, "getBankaData: " + response.size());
            subeBinding.setLoading(false);
            subeAdapter.setSubeArrayList(response);
            subeBinding.setListofEmpty(response.size() < 1);
        });
    }

    @Override
    public void SelectSube(Sube sube) {
        SubeFragmentDirections.ActionSubeFragmentToSubeDetayFragment action = SubeFragmentDirections.actionSubeFragmentToSubeDetayFragment();
        action.setSube(sube);
        Navigation.findNavController(subeBinding.getRoot()).navigate((NavDirections) action);
    }


}