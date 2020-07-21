package us.erlang.android_activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ContactPickerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_picker);

        Button btn = findViewById(R.id.lifecycle);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNewAcitity();
            }
        });
    }

    private void startNewAcitity() {
        Intent intent = new Intent(this, LifecycleActivity.class);
        startActivity(intent);
    }

    static final int REQUEST_SELECT_PHONE_NUMBER = 1;

    public void selectContact(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_SELECT_PHONE_NUMBER);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SELECT_PHONE_NUMBER && resultCode == RESULT_OK) {
            Uri contactUri = data.getData();
            String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.NUMBER};
            Cursor cursor = getContentResolver().query(contactUri, projection,
                    null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                String contactName = cursor.getString(nameIndex);
                String contactPhone = cursor.getString(numberIndex);
                Toast.makeText(this, contactName + " " + contactPhone, Toast.LENGTH_LONG).show();
                cursor.close();
            }
        }
    }
}