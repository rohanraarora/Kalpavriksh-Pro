package in.kvsc.kalpavrikshpro;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
public class ServiceListFragment extends android.support.v4.app.Fragment {

    public ServiceListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_service_list, container, false);
    }



}
