package com.hellocare.fragment;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidGridAdapter;

public class CaldroidSampleCustomFragment extends CaldroidFragment {

	@Override
	public CaldroidGridAdapter getNewDatesGridAdapter(int month, int year) {

		return new CaldroidSampleCustomAdapter(getActivity(), month, year,
				getCaldroidData(), getExtraData());
	}

}
