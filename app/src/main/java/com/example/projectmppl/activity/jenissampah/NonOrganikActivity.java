package com.example.projectmppl.activity.jenissampah;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.projectmppl.R;
import com.example.projectmppl.activity.KondisiActivity;
import com.example.projectmppl.model.Kantong;
import com.example.projectmppl.model.KantongNonOrganik;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NonOrganikActivity extends AppCompatActivity {
    private CheckBox chkPlastik,chkKertas,chkBotol,chkBesi,chkAluminium;

    @BindView(R.id.btn_request)
    Button btnRequest;
    @BindView(R.id.totalberat)
    EditText totalBerat;
    private ArrayList<String>list;
    private DatabaseReference getReference;
    int total = 0;
    int jumlahPoint = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_non_organik);
        ButterKnife.bind(this);

        initView();

        btnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (list == null){
                    Toast.makeText(NonOrganikActivity.this,"Silahkan memilih sampah terlebih dahulu",Toast.LENGTH_LONG).show();
                }if (TextUtils.isEmpty(totalBerat.getText().toString().trim())){
                    Toast.makeText(NonOrganikActivity.this,"Silahkan memasukkan berat yang diinginkan",Toast.LENGTH_LONG).show();
                }if (list == null && TextUtils.isEmpty(totalBerat.getText().toString().trim())){
                    Toast.makeText(NonOrganikActivity.this,"Silahkan mengisi data sampah",Toast.LENGTH_LONG).show();
                }else{
                    total = Integer.parseInt(String.valueOf(totalBerat.getText().toString()));
                    jumlahPoint = total*100;
                    list = getListSampah();
                    addKantong(list,total,jumlahPoint);
                }

            }
        });


        getReference = FirebaseDatabase.getInstance().getReference("kantong");


    }

    public void initView(){
        chkPlastik = (CheckBox)findViewById(R.id.plastik);
        chkKertas=(CheckBox)findViewById(R.id.kertas);
        chkBotol = (CheckBox)findViewById(R.id.botol);
        chkAluminium = (CheckBox)findViewById(R.id.aluminium);
        chkBesi = (CheckBox)findViewById(R.id.besi);
    }

    public void addKantong(ArrayList<String>listSampah, int totalSampah, int jumlahPoint){
        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getEmail().replaceAll("\\.", "_");
        KantongNonOrganik kantongNonOrganik = new KantongNonOrganik(currentUser,listSampah,"NonOrganik",totalSampah,jumlahPoint);
        getReference
                .child(currentUser)
                .child("data")
                .child("nonOrganik")
                .push()
                .setValue(kantongNonOrganik);
        chkAluminium.setChecked(false);
        chkPlastik.setChecked(false);
        chkKertas.setChecked(false);
        chkBotol.setChecked(false);
        chkBesi.setChecked(false);


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(NonOrganikActivity.this);
        alertDialogBuilder.setTitle("Permintaan");
        alertDialogBuilder.setMessage("Sampah Anda telah dimasukkan kedalam Kantong");

        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                totalBerat.setText("0");
                finish();

            }
        });
        alertDialogBuilder.show();
    }

    public ArrayList<String>getListSampah(){
        ArrayList<String> listSampah = new ArrayList<>();
        if (chkPlastik.isChecked()){
            listSampah.add(chkPlastik.getText().toString());
        }if (chkKertas.isChecked()){
            listSampah.add(chkKertas.getText().toString());
        }if (chkBotol.isChecked()){
            listSampah.add(chkBotol.getText().toString());
        }if (chkBesi.isChecked()){
            listSampah.add(chkBesi.getText().toString());
        }if (chkAluminium.isChecked()){
            listSampah.add(chkAluminium.getText().toString());
        }
        return listSampah;
    }
}
