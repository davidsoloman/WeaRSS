package com.creativedrewy.wearss.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.wearable.activity.ConfirmationActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.creativedrewy.wearss.R;
import com.creativedrewy.wearss.model.FeedHeadline;
import com.creativedrewy.wearss.services.DataToPhoneService;

/**
 * Fragment that allows the use to open a particular article on back on their phone
 */
public class OpenOnPhoneFragment extends Fragment {
    public static String HEADLINE_INFO_EXTRA = "headlineInfoExtra";

    private ImageView mPhoneOpener;
    private DataToPhoneService _phoneService;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_open_on_phone, container, false);

        _phoneService = new DataToPhoneService(getActivity());
        final FeedHeadline headline = (FeedHeadline) getArguments().getSerializable(HEADLINE_INFO_EXTRA);

        mPhoneOpener = (ImageView) view.findViewById(R.id.actionBgImageView);
        mPhoneOpener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _phoneService.sendHeadlineToReadList(headline);

                Intent intent = new Intent(getActivity(), ConfirmationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra(ConfirmationActivity.EXTRA_ANIMATION_TYPE, ConfirmationActivity.OPEN_ON_PHONE_ANIMATION);
                intent.putExtra(ConfirmationActivity.EXTRA_MESSAGE, getActivity().getString(R.string.sent_to_device));
                startActivity(intent);
            }
        });

        return view;
    }

    /**
     * Return a new instance of this fragment
     */
    public static OpenOnPhoneFragment newInstance(FeedHeadline headline) {
        Bundle args = new Bundle();
        args.putSerializable(HEADLINE_INFO_EXTRA, headline);

        OpenOnPhoneFragment fragment = new OpenOnPhoneFragment();
        fragment.setArguments(args);

        return fragment;
    }
}
