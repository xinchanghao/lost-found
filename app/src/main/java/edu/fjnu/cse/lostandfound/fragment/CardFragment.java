package edu.fjnu.cse.lostandfound.fragment;
/**
 * homefragment
 */

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

import edu.fjnu.cse.lostandfound.R;
import edu.fjnu.cse.lostandfound.activity.MainActivity;
import edu.fjnu.cse.lostandfound.tools.RSAEncrypt;
import edu.fjnu.cse.lostandfound.tools.Utils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

//
public class CardFragment extends Fragment {
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    LinearLayout cardinfo, loading;
    TextView name, cardID, textTodayCost, textLastCost, textLastTime, textID, textMoney;
    RelativeLayout statusWait;
    RelativeLayout statusNoNFC;
    RelativeLayout statusError;


    public CardFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card, container, false);
        findView(view);
        return view;
    }

    private void findView(View view) {
        cardinfo = (LinearLayout) getActivity().findViewById(R.id.cardinfo);
        statusWait = (RelativeLayout) view.findViewById(R.id.statusWait);
        statusError = (RelativeLayout) view.findViewById(R.id.statusError);
        statusNoNFC = (RelativeLayout) view.findViewById(R.id.statusNoNFC);
        setStatus(0);
        textTodayCost = (TextView) view.findViewById(R.id.textTodayCost);
        textLastCost = (TextView) view.findViewById(R.id.textLastCost);
        textLastTime = (TextView) view.findViewById(R.id.textLastTime);
        textID = (TextView) view.findViewById(R.id.textID);
        textMoney = (TextView) view.findViewById(R.id.textMoney);
        loading = (LinearLayout) view.findViewById(R.id.loading);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (cardinfo == null) {
            cardinfo = (LinearLayout) getActivity().findViewById(R.id.cardinfo);
        }
        if (name == null) {
            name = (TextView) getActivity().findViewById(R.id.textView5);
        }
        if (cardID == null) {
            cardID = (TextView) getActivity().findViewById(R.id.textView4);
        }
    }

    public void resetValue() {
        loading.setVisibility(View.VISIBLE);
        textTodayCost.setText("...");
        textLastCost.setText("...");
        textLastTime.setText("...");
        textID.setText("...");
        textMoney.setText("...");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        cardinfo.setVisibility(View.GONE);
    }

    public void setStatus(int status) {
        statusWait.setVisibility(View.GONE);
        statusError.setVisibility(View.GONE);
        statusNoNFC.setVisibility(View.GONE);
        cardinfo.setVisibility(View.GONE);
        switch (status) {
            case 0:
                statusWait.setVisibility(View.VISIBLE);
                break;
            case 1:
                statusError.setVisibility(View.VISIBLE);
                break;
            case 2:
                statusNoNFC.setVisibility(View.VISIBLE);
                break;
            case 3:
                cardinfo.setVisibility(View.VISIBLE);
                break;
        }
    }


}