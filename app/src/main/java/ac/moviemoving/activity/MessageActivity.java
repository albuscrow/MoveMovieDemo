package ac.moviemoving.activity;

import ac.moviemoving.R;
import ac.moviemoving.data.DataProvider;
import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;

public class MessageActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.new_message);
        fab.setOnClickListener(view -> {
            final Dialog dialog = new Dialog(MessageActivity.this);
//            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setTitle("send new message");
            dialog.setCancelable(true);
            View v = LayoutInflater.from(MessageActivity.this).inflate(R.layout.include_send_message, null);
            dialog.setContentView(v);
            final MultiAutoCompleteTextView receiverEditText = (MultiAutoCompleteTextView) v.findViewById(R.id.receiver_text);

            ArrayAdapter<String> adapter =
                    new ArrayAdapter<>(MessageActivity.this,
                            android.R.layout.simple_dropdown_item_1line, new String[]{"Bob (Device Operator)", "Peter (Cleaner)", "Lily (Ticket Staff)", "Albus (Snack Seller)", "Mike (Cleaner)", "Lucy(Device Manager)", "Jam (Customer Service)"});
            receiverEditText.setAdapter(adapter);
            receiverEditText.setTokenizer(new MultiAutoCompleteTextView.Tokenizer() {

                @Override
                public CharSequence terminateToken(CharSequence arg0) {
                    return "";
                }

                @Override
                public int findTokenStart(CharSequence arg0, int arg1) {
                    return 0;
                }

                @Override
                public int findTokenEnd(CharSequence arg0, int arg1) {
                    return 0;
                }
            });
            receiverEditText.setOnItemClickListener((arg0, arg1, arg2, arg3) -> {
                String name = (String) arg0.getItemAtPosition(arg2);
                if (receiverEditText.getText().toString().length() == 0)
                    receiverEditText.setText(name);
                else
                    receiverEditText.setText(receiverEditText.getText().toString() + "," + name);

            });

            EditText contentEditText = (EditText) v.findViewById(R.id.content);

            findViewById(R.id.send_button).setOnClickListener(view1 -> {
                boolean cancel = false;
                View focusView = null;

                String receiver = receiverEditText.getText().toString();
                String content = contentEditText.getText().toString();

                // Check for a valid password, if the user entered one.
                if (!TextUtils.isEmpty(receiver)) {
                    receiverEditText.setError("Receiver can not be empty");
                    focusView = receiverEditText;
                    cancel = true;
                }

                // Check for a valid phone address.
                if (TextUtils.isEmpty(content)) {
                    contentEditText.setError("conetent can not be empty");
                    focusView = contentEditText;
                    cancel = true;
                }

                if (cancel) {
                    focusView.requestFocus();
                } else {
                    DataProvider.sendMessage(receiver, content);
                    dialog.dismiss();
                }
            });

            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            dialog.show();
            dialog.getWindow().setLayout(1200, 900);
        });
    }

}
