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

    public void readFromCard(Intent intent) {
//        resetValue();
        //取出封装在intent中的TAG
        Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        for (String tech : tagFromIntent.getTechList()) {
            System.out.println(tech);
        }
        //读取TAG
        final MifareClassic mfc = MifareClassic.get(tagFromIntent);
        try {
            //Enable I/O operations to the tag from this TagTechnology object.
            mfc.connect();
            final byte[] UID = mfc.getTag().getId();
//            valueID.setText(Utils.bytesToHexString(UID).toUpperCase());
            resetValue();
            byte[] key = new byte[6];
            key[0] = (byte) 0xA0;
            key[1] = (byte) 0xB1;
            key[2] = (byte) 0xC2;
            key[3] = (byte) 0xD3;
            key[4] = (byte) 0xE4;
            key[5] = (byte) 0xF5;
            final byte[][] data10 = new byte[4][];
            if (!readSector(mfc, key, 10, data10)) {
//                textView.setText("非校园卡，请重新放置...");
//                textView.setTextColor(Color.rgb(41, 128, 185));
                setStatus(1);
                return;
            }
//            textView.setText("正在加载，请稍后...");
//            textView.setTextColor(Color.rgb(39, 174, 96));
            final RSAEncrypt rsaEncrypt = new RSAEncrypt();
            rsaEncrypt.loadPrivateKey(RSAEncrypt.DEFAULT_PRIVATE_KEY);
            rsaEncrypt.loadPublicKey(RSAEncrypt.DEFAULT_PUBLIC_KEY);
//            if (data10[0].length == 16) {
//                data10[0][15] = (byte) 0;
//            }
//            if (data10[1].length == 16) {
//                CitizenID = Utils.bytesToHexString(Utils.subBytes(data10[1], 10, 6));
//            }
            setStatus(3);
            if (data10[0].length == 16) {
                data10[0][15] = (byte) 0;
            }
            try {
                name.setText(new String(data10[0], "GBK"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (data10[2].length == 16) {
                cardID.setText(new String(Utils.subBytes(data10[2], 3, 13), "GBK"));
            }

            byte[] data = rsaEncrypt.encrypt(rsaEncrypt.getPublicKey(), UID);
            Utils.POST("http://1.1i.hk/getKey3.php", data, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            textView.setText("网络连接失败");
//                            textView.setTextColor(Color.rgb(192, 57, 43));
                        }
                    });
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                loading.setVisibility(View.GONE);
                                byte[] Key3 = null;
                                Key3 = rsaEncrypt.decrypt(rsaEncrypt.getPrivateKey(), response.body().bytes());
                                String CitizenID = null;
//                                textView.setText("加载成功");
//                                textView.setTextColor(Color.rgb(22, 160, 133));
                                if (data10[0].length == 16) {
                                    data10[0][15] = (byte) 0;
                                }
                                if (data10[1].length == 16) {
                                    CitizenID = Utils.bytesToHexString(Utils.subBytes(data10[1], 10, 6));
                                }
//                                try {
////                                    valueName.setText(new String(data10[0], "GBK"));
//                                } catch (UnsupportedEncodingException e) {
//                                    e.printStackTrace();
//                                }
                                if (data10[2].length == 16) {

//                                    valueCardID.setText(new String(Utils.subBytes(data10[2], 3, 13), "GBK"));
                                    if (CitizenID != null) {
                                        CitizenID += Utils.bytesToHexString(Utils.subBytes(data10[2], 0, 3));
                                        textID.setText(CitizenID.replace('a', 'X'));
                                    }
                                }
                                byte[][] data3 = new byte[4][];
                                if (!readSector(mfc, Key3, 3, data3)) {
//                                    textView.setText("解密错误，请重新放置...");
//                                    textView.setTextColor(Color.rgb(41, 128, 185));
                                    setStatus(1);
                                    return;
                                }
                                textMoney.setText(String.format(Locale.CHINA, "￥%.2f", (double) Utils.bytes3ToInt(data3[0], 0) / 100));

                                textLastTime.setText(String.format(Locale.CHINA, "%s-%s %s:%s:%s",
                                        Utils.bytesToHexString(Utils.subBytes(data3[2], 0, 1)),
                                        Utils.bytesToHexString(Utils.subBytes(data3[2], 1, 1)),
                                        Utils.bytesToHexString(Utils.subBytes(data3[2], 2, 1)),
                                        Utils.bytesToHexString(Utils.subBytes(data3[2], 3, 1)),
                                        Utils.bytesToHexString(Utils.subBytes(data3[2], 4, 1))
                                ));
                                textTodayCost.setText(String.format(Locale.CHINA, "￥%.2f", (double) Utils.bytes3ToInt(data3[2], 6) / 100));
                                textLastCost.setText(String.format(Locale.CHINA, "￥%.2f", (double) Utils.bytes3ToInt(data3[2], 9) / 100));
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private boolean readSector(MifareClassic mfc, byte[] key, int Sector, byte[][] data) throws IOException {
        boolean auth = false;
        auth = mfc.authenticateSectorWithKeyA(Sector, key);
        int bCount;
        int bIndex;
        if (auth) {
            bCount = mfc.getBlockCountInSector(Sector);
            bIndex = mfc.sectorToBlock(Sector);
            for (int i = 0; i < bCount; i++) {
                data[i] = mfc.readBlock(bIndex);
                bIndex++;
            }
            return true;
        } else {
            return false;
        }
    }

}