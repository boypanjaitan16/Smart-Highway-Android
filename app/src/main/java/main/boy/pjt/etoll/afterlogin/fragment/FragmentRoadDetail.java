package main.boy.pjt.etoll.afterlogin.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.concurrent.Callable;

import main.boy.pjt.etoll.R;
import main.boy.pjt.etoll.afterlogin.CoreActivity;
import main.boy.pjt.etoll.helper.MyAlert;
import main.boy.pjt.etoll.helper.MyConstant;
import main.boy.pjt.etoll.helper.MyRetrofit;
import main.boy.pjt.etoll.helper.MyRetrofitInterface;
import main.boy.pjt.etoll.helper.MySession;
import main.boy.pjt.etoll.response.ResponseBalance;
import main.boy.pjt.etoll.response.ResponseDefault;
import main.boy.pjt.etoll.response.ResponseRoad;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Boy Panjaitan on 10/06/2018.
 */

public class FragmentRoadDetail extends Fragment {

    ResponseRoad.Values roadValue;
    ResponseRoad.OutGate gateValue;
    TextView namaJalan;
    TextView namaGate;
    TextView panjangJalan;
    TextView tarif;
    LinearLayout btnLanjutkan;
    MyAlert alert;
    MySession session;
    MyRetrofitInterface retrofitInterface;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View mainView   = inflater.inflate(R.layout.fragment_detail_road, container, false);

        roadValue   = (ResponseRoad.Values) getArguments().getSerializable("value");
        gateValue   = (ResponseRoad.OutGate) getArguments().getSerializable("gate");
        namaJalan   = mainView.findViewById(R.id.nama_jalan);
        namaGate    = mainView.findViewById(R.id.nama_gate);
        panjangJalan    = mainView.findViewById(R.id.panjang_jalan);
        tarif  = mainView.findViewById(R.id.tarif);
        btnLanjutkan    = mainView.findViewById(R.id.lanjutkan);
        alert       = new MyAlert(getContext(), false);
        session     = new MySession(getContext());
        retrofitInterface   = MyRetrofit.getClient().create(MyRetrofitInterface.class);

        namaJalan.setText(roadValue.getName());
        namaGate.setText(gateValue.getName());
        panjangJalan.setText(gateValue.getDistance()+" KM");
        tarif.setText("Rp. "+gateValue.getPrice());

        btnLanjutkan.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        //out of tol
                        ((CoreActivity)getActivity()).moveGate(2, roadValue.getId(), gateValue, new Callable<Void>() {
                            @Override
                            public Void call() throws Exception {
                                Toast.makeText(getContext(), "Selamat Jalan. Terimakasih", Toast.LENGTH_LONG).show();
                                ((CoreActivity)getActivity()).startFragment(new FragmentRoadList());
                                return null;
                            }
                        });

                    }
                }
        );

        return mainView;
    }
}
