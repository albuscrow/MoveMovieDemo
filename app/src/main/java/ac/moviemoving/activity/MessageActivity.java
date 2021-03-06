package ac.moviemoving.activity;

import ac.moviemoving.R;
import ac.moviemoving.data.DataProvider;
import ac.moviemoving.data.MyMessage;
import android.app.*;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.*;

import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MessageActivity extends BaseActivity {

    private Random random;
    private MessageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        random = new Random(19987);
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
            View rootView = LayoutInflater.from(MessageActivity.this).inflate(R.layout.include_send_message, null);
            dialog.setContentView(rootView);
            final MultiAutoCompleteTextView receiverEditText = (MultiAutoCompleteTextView) rootView.findViewById(R.id.receiver_text);

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
                if (receiverEditText.getText().toString().length() == 0) {
                    receiverEditText.setText(name);
                } else {
                    receiverEditText.setText(receiverEditText.getText().toString() + "," + name);
                }

            });

            EditText contentEditText = (EditText) rootView.findViewById(R.id.content);

            final View sendButton = rootView.findViewById(R.id.send_button);
            sendButton.setOnClickListener(view1 -> {
                boolean cancel = false;
                View focusView = null;

                String receiver = receiverEditText.getText().toString();
                String content = contentEditText.getText().toString();

                // Check for a valid password, if the user entered one.
                if (TextUtils.isEmpty(receiver)) {
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
                    DataProvider.sendMessage(receiver, content, ((Spinner) rootView.findViewById(R.id.alert_after)).getSelectedItem().toString());
                    MessageActivity.this.adapter.refresh();
                    Timer t = new Timer();
                    t.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            Intent phoneCallIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "17816861269"));
                            PendingIntent pendingPhoneCallIntent = PendingIntent.getActivity(MessageActivity.this, random.nextInt(), phoneCallIntent, 0);
                            Intent intent = new Intent(MessageActivity.this, MessageActivity.class);
                            String who = receiver.split(",")[0];
                            System.out.println(who);
                            intent.putExtra("who", who);
                            PendingIntent pendingOpenMessageIntent = PendingIntent.getActivity(MessageActivity.this, random.nextInt(), intent, 0);
                            NotificationCompat.Builder mBuilder =
                                    new NotificationCompat.Builder(MessageActivity.this)
                                            .setSmallIcon(R.drawable.ic_launcher)
                                            .setContentText(who + " hasn't seen the message")
                                            .setContentTitle("MovieMoving")
                                            .setAutoCancel(true)
                                            .setContentIntent(pendingOpenMessageIntent)
                                            .addAction(android.R.drawable.ic_menu_call, "make a call", pendingPhoneCallIntent);

                            // Sets an ID for the notification
                            int mNotificationId = random.nextInt();
                            // Gets an instance of the NotificationManager service
                            NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                            // Builds the notification and issues it.
                            final Notification build = mBuilder.build();
                            build.defaults |= Notification.DEFAULT_SOUND;
                            mNotifyMgr.notify(mNotificationId, build);
                        }
                    }, 5000);
                    dialog.dismiss();
                }
            });
            Spinner timeSpinner = (Spinner) rootView.findViewById(R.id.alert_after);
            timeSpinner.setAdapter(new MyAdapter(
                    MessageActivity.this,
                    new String[]{
                            "5 min",
                            "15 min",
                            "30 min",
                            "1 hour",
                            "3 hour",
                            "never"
                    }));

            timeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    //todo change room
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
            timeSpinner.setSelection(2);

            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            dialog.show();
            dialog.getWindow().setLayout(1200, 1100);
            sendButton.requestFocus();
        });

        ListView listview = (ListView) findViewById(R.id.messages_listView);
        adapter = new MessageAdapter();
        listview.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.refresh();
    }

    class MessageAdapter extends BaseAdapter {
        private List<MyMessage> data;

        public MessageAdapter() {
            this.data = DataProvider.getMessage();
        }

        public void refresh() {
            this.data = DataProvider.getMessage();
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return this.data.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = LayoutInflater.from(MessageActivity.this).inflate(R.layout.item_message, viewGroup, false);
            }
            MyMessage mm = data.get(i);
            ((TextView) view.findViewById(R.id.send_time)).setText("send time: " + mm.getSendTime());
            ((TextView) view.findViewById(R.id.receiver)).setText("receiver: " + mm.getReceiversStr());
            final String unReceiversStr = mm.getUnReceiversStr();
            if (unReceiversStr.equals("All Read")) {
                ((TextView) view.findViewById(R.id.unreceiver)).setText(unReceiversStr);
                ((TextView) view.findViewById(R.id.unreceiver)).setTextColor(0xffaaffaa);
                view.setBackgroundColor(0xffeeffee);

                ((Button) view.findViewById(R.id.call_button)).setVisibility(View.GONE);
                ((Button) view.findViewById(R.id.mark_as_read)).setVisibility(View.GONE);
            } else {
                ((TextView) view.findViewById(R.id.unreceiver)).setText("Who has't seen: " + unReceiversStr);
                ((TextView) view.findViewById(R.id.unreceiver)).setTextColor(0xffff5555);
                view.setBackgroundColor(0xffffdddd);
                ((Button) view.findViewById(R.id.call_button)).setVisibility(View.VISIBLE);
                ((Button) view.findViewById(R.id.mark_as_read)).setVisibility(View.VISIBLE);

                ((Button) view.findViewById(R.id.call_button)).setOnClickListener(view1 -> {
                    Intent phoneCallIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "17816861269"));
                    //noinspection ResourceType
                    startActivity(phoneCallIntent);
                });
                ((Button) view.findViewById(R.id.mark_as_read)).setOnClickListener(view1 -> {
                    DataProvider.allMessageReaded();
                    adapter.refresh();
                });
            }
            ((TextView) view.findViewById(R.id.content)).setText("content:" + mm.getContent());
            return view;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        System.out.println("MessageActivity.onNewIntent");

        final String who = intent.getStringExtra("who");
        System.out.println(who);
        if (who != null && !who.isEmpty()) {
            new AlertDialog.Builder(this)
                    .setMessage("Have you called to " + who + "?")
                    .setNegativeButton("No", null)
                    .setPositiveButton("Yes", (dialogInterface, i) -> {
                        DataProvider.allMessageReaded();
                        adapter.refresh();
                    })
                    .create().show();
        }
    }
}
