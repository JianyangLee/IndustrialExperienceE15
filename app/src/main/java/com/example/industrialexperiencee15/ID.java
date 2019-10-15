package com.example.industrialexperiencee15;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;
import android.text.ClipboardManager;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class ID extends AppCompatActivity {

    String userID;
    TextView ID, link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id);

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        ID = (TextView) findViewById(R.id.idCopy);
        link = (TextView) findViewById(R.id.link);

        ID.setText(userID);


        link.setClickable(true);
        link.setMovementMethod(LinkMovementMethod.getInstance());
        String text = "<a href='yunlife21.ml'> Yun Life website </a>";
        link.setText(Html.fromHtml(text));

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
