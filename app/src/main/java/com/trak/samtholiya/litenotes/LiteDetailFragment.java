package com.trak.samtholiya.litenotes;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.trak.samtholiya.litenotes.dummy.MainContent;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LiteDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LiteDetailFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private MainContent.MainItem mParam1;
    private TextView detailTitle,detailContent;
    private OnFragmentInteractionListener mListener;
    private View view;
    private SharedPreferences pref;

    public LiteDetailFragment() {
        // Required empty public constructor
    }
    public boolean saveCurrentData(){
        MainContent.MainItem item=mParam1;
        item.title =detailTitle.getText().toString();
        item.details=detailContent.getText().toString();
        if(item.id=="0"||item.id==null) {
            boolean flag=MainActivity.dummyContent.insertData(item);
            if(flag){
                mParam1=MainActivity.dummyContent.getLastItem();
                return true;
            }
            else
               return false;
        }
        else
        {
            return MainActivity.dummyContent.updateData(item);
        }
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment LiteDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LiteDetailFragment newInstance(MainContent.MainItem param1) {
        LiteDetailFragment fragment = new LiteDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = (MainContent.MainItem) getArguments().getSerializable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_lite_details, container, false);
        detailTitle=(EditText)view.findViewById(R.id.detail_title);
        detailContent=(EditText)view.findViewById(R.id.detail_content);
        pref= PreferenceManager.getDefaultSharedPreferences(view.getContext());
        if (Build.VERSION.SDK_INT >= 23) {
            detailTitle.setTextAppearance(Integer.parseInt(pref.getString(getResources().getString(R.string.liteTitleFont),"7f09016e").substring(2),16));
            detailContent.setTextAppearance(Integer.parseInt(pref.getString(getResources().getString(R.string.liteDetailFont),"7f09016e").substring(2),16));

        } else {
            detailTitle.setTextAppearance(view.getContext(),Integer.parseInt(pref.getString(getResources().getString(R.string.liteTitleFont),"7f09016e").substring(2),16));
            detailContent.setTextAppearance(view.getContext(),Integer.parseInt(pref.getString(getResources().getString(R.string.liteDetailFont),"7f09016e").substring(2),16));
        }
        detailTitle.setTypeface(detailTitle.getTypeface(),Typeface.BOLD);

        if(mParam1!=null){
            detailTitle.setText(mParam1.title);
            detailContent.setText(mParam1.details);
        }
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

}
