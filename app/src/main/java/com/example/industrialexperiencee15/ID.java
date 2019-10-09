package com.example.industrialexperiencee15;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.text.ClipboardManager;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class ID extends AppCompatActivity {

    String userID;
    TextView ID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id);

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        ID = (TextView) findViewById(R.id.idCopy);

        ID.setText(userID);

        ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager cm = (ClipboardManager)ID.this.getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(ID.getText());
                Toast.makeText(ID.this, "Copied to clipboard", Toast.LENGTH_LONG).show();
            }
        });
    }
}
