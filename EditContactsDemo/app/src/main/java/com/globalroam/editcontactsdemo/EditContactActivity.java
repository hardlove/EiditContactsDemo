package com.globalroam.editcontactsdemo;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

public class EditContactActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView lastName;
    private  TextView firstName;
    private LinearLayout content;
    private TextView addPhone;
    private PopupWindow popWindow;
    private TextView backBtn;
    private TextView saveBtn;
    private String contactID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);
        initView();


//       String contactID = getIntent().getStringExtra("contact_id");
//        if (!TextUtils.isEmpty(contactID)) {
//
//        }
        contactID = ContactUtils.getContactId(this);

        if (contactID != null) {
            loadDatas(contactID);
        }



    }

    private void loadDatas(String contactID) {

        String name = ContactUtils.getContactNameById(this, contactID);
        firstName.setText(name);
        Contact.Phone[] phones = ContactUtils.getPhonesByContacaId(this, contactID);
        for (int i = 0; i < phones.length; i++) {
            addPhoneItem(phones[i].getType(),phones[i].getNumber());
        }


    }

    private void initView() {
        firstName = (TextView) findViewById(R.id.first_name);
        lastName = (TextView) findViewById(R.id.last_name);
        content = (LinearLayout) findViewById(R.id.phone_content);
        addPhone = (TextView) findViewById(R.id.add_phone_btn);
        backBtn = (TextView) findViewById(R.id.back_btn);
        saveBtn = (TextView) findViewById(R.id.save_btn);

        addPhone.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add_phone_btn) {
            if (popWindow==null||!popWindow.isShowing()) {
                showPopWindow();
            }

        }else if (v.getId() == R.id.save_btn) {
            saveData();
        }else if (v.getId() == R.id.back_btn) {
            finish();
        } else {
            addPhoneItem(v.getId());
            popWindow.dismiss();
        }

    }

    private void saveData() {
        String firsName = this.firstName.getText().toString();
        String lastName = this.lastName.getText().toString();
        String name = firsName+" "+lastName;
        int count = content.getChildCount();
        String[] numbers = new String[count];
        int[] types = new int[count];
        for (int i = 0; i < count; i++) {
            String number = ((EditText) content.getChildAt(i).findViewById(R.id.phone_number)).getText().toString();
            String type = ((Spinner)  content.getChildAt(i).findViewById(R.id.type_spinner)).getSelectedItem().toString();
            types[i] =  getPhoneType(type);
            numbers[i] = number;
        }

        if (contactID == null) {
            ContactUtils.addContact(this, name, numbers, types);
        } else {
            ContactUtils.updateContact(this,contactID,name,numbers, types);
        }

        finish();

    }

    private int getPhoneType(String type) {
        int phoneType;
        if ("Mobile".equalsIgnoreCase(type)) {
            phoneType = ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE;
        }else if ("Home".equalsIgnoreCase(type)) {
            phoneType = ContactsContract.CommonDataKinds.Phone.TYPE_HOME;
        }else if ("Work".equalsIgnoreCase(type)) {
            phoneType = ContactsContract.CommonDataKinds.Phone.TYPE_WORK;
        }else {
            phoneType = ContactsContract.CommonDataKinds.Phone.TYPE_OTHER;
        }
        return phoneType;
    }

    private void showPopWindow() {
        View view = LayoutInflater.from(EditContactActivity.this).inflate(R.layout.popwindow_layout,null);
        popWindow = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        popWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        ColorDrawable drawable = new ColorDrawable(0xb0000000);
        popWindow.setBackgroundDrawable(drawable);
        popWindow.showAtLocation(this.findViewById(R.id.add_phone_btn), Gravity.BOTTOM, 0, 0);
        popWindow.setFocusable(true);



        view.findViewById(R.id.type_mobile).setOnClickListener(this);
        view.findViewById(R.id.type_home).setOnClickListener(this);
        view.findViewById(R.id.type_work).setOnClickListener(this);
        view.findViewById(R.id.type_other).setOnClickListener(this);




    }

    private void addPhoneItem(int phoneType, String number) {

        {
            final LinearLayout child = (LinearLayout) LayoutInflater.from(EditContactActivity.this).inflate(R.layout.phone_item, null);
            content.addView(child);

            Spinner spinner = (Spinner) child.findViewById(R.id.type_spinner);
            String[] types = new String[]{"Mobile","Home","Work","Other"};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>( this , android.R.layout.simple_spinner_item , types );
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);

            int i = 0;
            switch (phoneType) {
                case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                    i=0;break;
                case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                    i=1;
                    break;
                case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
                    i=2;
                    break;
                case ContactsContract.CommonDataKinds.Phone.TYPE_OTHER:
                    i=3;
                    break;
            }
            spinner.setSelection(i, true);

            child.findViewById(R.id.delete_phone_item).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LinearLayout phoneParent = (LinearLayout) child.findViewById(R.id.delete_phone_item).getParent().getParent();
                    phoneParent.removeView(child);
                }
            });

            ((EditText) child.findViewById(R.id.phone_number)).setText(number);

        }

    }

    private void addPhoneItem(int viewId) {
        final LinearLayout child = (LinearLayout) LayoutInflater.from(EditContactActivity.this).inflate(R.layout.phone_item, null);
        content.addView(child);

        Spinner spinner = (Spinner) child.findViewById(R.id.type_spinner);
        String[] types = new String[]{"Mobile","Home","Work","Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>( this , android.R.layout.simple_spinner_item , types );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        int i = 0;
        switch (viewId) {
            case R.id.type_home:
                i=1;break;
            case R.id.type_mobile:
                i=0;
                break;
            case R.id.type_work:
                i=2;
                break;
            case R.id.type_other:
                i=3;
                break;
        }
        spinner.setSelection(i, true);

        child.findViewById(R.id.delete_phone_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout phoneParent = (LinearLayout) child.findViewById(R.id.delete_phone_item).getParent().getParent();
                phoneParent.removeView(child);
            }
        });
        child.findViewById(R.id.phone_number).requestFocus();

        popWindow.dismiss();
    }
}
