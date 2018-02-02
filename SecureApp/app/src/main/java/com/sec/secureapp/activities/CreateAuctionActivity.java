package com.sec.secureapp.activities;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.sec.secureapp.R;
import com.sec.secureapp.databinding.ActivityCreateAuctionBinding;
import com.sec.secureapp.general.AuctionInfo;
import com.sec.secureapp.general.InfoMessage;
import com.sec.secureapp.general.T;
import com.sec.secureapp.security.Hashing;

import java.text.NumberFormat;
import java.util.Calendar;

public class CreateAuctionActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener, TextWatcher {

    ActivityCreateAuctionBinding binding;

    //create a list of items for the spinner.
    String[] type = new String[]{"English Auction"};

    // variables to store choices
    private String auctionType = "english";
    private String objectName = "";
    private String auctioneerId = T.USER_ID;
    private String auctionDuration = "";
    private String initialPrice = "";
    private String participants = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_auction);

        getSupportActionBar().setTitle("Create Auction");

        //create a list of items for the spinner.
        setSpinner(type, binding.createType);

        binding.createAuctioneer.setText(auctioneerId);

        binding.createChooseObject.setOnClickListener(this);
        binding.createSubmit.setOnClickListener(this);
        binding.createDuration.setOnClickListener(this);
        binding.createPrice.addTextChangedListener(this);
    }

    private void setSpinner(String[] spinner_items, Spinner spinner) {
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_list_style, spinner_items);
        //set the spinners adapter to the previously created one.
        spinner.setAdapter(adapter);
        if (spinner == binding.createType)
            spinner.setOnItemSelectedListener(new TypeSpinner());
    }

    @Override
    public void onBackPressed() {
        Intent mIntent = new Intent(CreateAuctionActivity.this, MainActivity.class);
        startActivity(mIntent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.create_choose_object:
                dialog();
                break;
            case R.id.create_submit:
                if (everythingGood()) {
                    new InfoMessage(CreateAuctionActivity.this, T.CREATE_AUCTION, new AuctionInfo(auctionType, auctioneerId, objectName, Double.parseDouble(cleanPrice(initialPrice))/100)).start();
                }
                break;
            case R.id.create_duration:
                durationPicker();
                break;
            default:
                break;
        }
    }

    // check for edittext that cannot be empty
    private boolean everythingGood() {
        initialPrice = binding.createPrice.getText().toString();
        participants = binding.createParticipants.getText().toString();
        if (objectName.matches("")) {
            T.VIEW_TOAST(getApplicationContext(), "Please Choose Object", Toast.LENGTH_SHORT);
            return false;
        } else if (cleanPrice(initialPrice).matches("")) {
            T.VIEW_TOAST(getApplicationContext(), "Please Choose Initial Price", Toast.LENGTH_SHORT);
            return false;
        }
        return true;
    }

    private String cleanPrice(String price) {
        String replaceable = String.format("[%s,.\\s]", NumberFormat.getCurrencyInstance().getCurrency().getSymbol());
        String cleanString = price.replaceAll(replaceable, "");

        return cleanString;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    private String current = "";

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        System.out.println("****" + charSequence);
        if (!charSequence.toString().equals(current)) {
            binding.createPrice.removeTextChangedListener(this);

            String replaceable = String.format("[%s,.\\s]", NumberFormat.getCurrencyInstance().getCurrency().getSymbol());
            String cleanString = charSequence.toString().replaceAll(replaceable, "");


            double parsed = Double.parseDouble(cleanPrice(charSequence.toString()));
            String formatted = NumberFormat.getCurrencyInstance().format((parsed / 100));

            current = formatted;
            binding.createPrice.setText(formatted);
            binding.createPrice.setSelection(formatted.length());

            binding.createPrice.addTextChangedListener(this);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    class TypeSpinner implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
            Toast.makeText(v.getContext(), "Your choose :" + type[position], Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i) {
            case 0:
                auctionType = "english";
                T.VIEW_TOAST(getApplicationContext(), auctionType, Toast.LENGTH_SHORT);
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private String dialog() {
        final EditText txtObject = new EditText(this);

        new AlertDialog.Builder(this)
                .setTitle("Choose Object")
                .setMessage("Write the object you want to put on auction.")
                .setView(txtObject)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        objectName = txtObject.getText().toString();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                })
                .show();

        return "";
    }

    private void durationPicker() {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(CreateAuctionActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                auctionDuration = selectedHour + ":" + selectedMinute;
                T.VIEW_TOAST(getApplicationContext(), selectedHour + ":" + selectedMinute, Toast.LENGTH_SHORT);
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }
}
