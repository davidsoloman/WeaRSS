package com.creativedrewy.wearss.fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.creativedrewy.wearss.R;
import com.creativedrewy.wearss.adapters.DestinationAppAdapter;
import com.creativedrewy.wearss.models.DestinationAppInfo;
import com.creativedrewy.wearss.services.DeviceAppService;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Fragment that allows user to pick the app to specifically share to
 */
public class ChooseArticleOpenFragment extends DialogFragment {

    public interface IAppChoosing {
        void chooseDestinationApp(DestinationAppInfo info);
    }

    @InjectView(R.id.selectableAppsListview)
    ListView mAppsListView;

    private IAppChoosing mAppChoose;

    public IAppChoosing getAppChoose() { return mAppChoose; }
    public void setAppChoose(IAppChoosing mAppChoose) { this.mAppChoose = mAppChoose; }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_app_chooser, null, false);
        ButterKnife.inject(this, view);

        getDialog().setTitle(getActivity().getString(R.string.choose_app_dialog_title));

        DeviceAppService deviceAppService = new DeviceAppService();
        List<DestinationAppInfo> destinationApps = deviceAppService.getDestinationApps(getActivity());

        DestinationAppInfo[] items = destinationApps.toArray(new DestinationAppInfo[destinationApps.size()]);
        DestinationAppAdapter adapter = new DestinationAppAdapter(getActivity(), items);
        mAppsListView.setAdapter(adapter);

        mAppsListView.setOnItemClickListener((parent, view1, position, id) -> {
            if (mAppChoose != null) {
                DestinationAppInfo appInfo = adapter.getItem(position);
                mAppChoose.chooseDestinationApp(appInfo);
            }

            dismiss();
        });

        return view;
    }
}
