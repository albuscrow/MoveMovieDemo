package ac.moviemoving.activity;

import ac.CalUtil;
import ac.moviemoving.R;
import ac.moviemoving.data.DataProvider;
import ac.moviemoving.model.RoomSchedule;
import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.AdapterView.OnItemSelectedListener;

import java.util.ArrayList;
import java.util.List;


public class ScheduleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Setup spinner
        Spinner spinner = (Spinner) findViewById(R.id.cinema_spinner);
        spinner.setAdapter(new MyAdapter(
                toolbar.getContext(),
                new String[]{
                        "Cinema A(AAA Road)",
                        "Cinema B(BBB Road)",
                        "Cinema C(CCC Road)",
                }));

        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // When the given dropdown item is selected, show its contents in the
                // container view.
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, ShowScheduleFragment.newInstance(position + 1))
                        .commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinner = (Spinner) findViewById(R.id.room_spinner);
        spinner.setAdapter(new MyAdapter(
                toolbar.getContext(),
                new String[]{
                        "Room 1",
                        "Room 2",
                        "Room 3",
                }));

        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //todo change room
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            final Dialog dialog = new Dialog(ScheduleActivity.this);
//            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setTitle("One click to arrange schedule");
            dialog.setCancelable(true);

        });

    }

    public void chooseTime(View v) {
        //todo show date choose dialog
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class ShowScheduleFragment extends Fragment {
        private List<RoomSchedule> roomSchedules;

        public ShowScheduleFragment() {
        }

        private static final String ARG_SECTION_NUMBER = "section_number";

        public static ShowScheduleFragment newInstance(int sectionNumber) {
            ShowScheduleFragment fragment = new ShowScheduleFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_schedule, container, false);

            //todo should by async from internet
            roomSchedules = DataProvider.getRoomSchedule();

            //init time listview
            ListView timeListView = (ListView) rootView.findViewById(R.id.timeList);
            timeListView.setAdapter(new BaseAdapter() {
                @Override
                public int getCount() {
                    return 100;
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
                        view = LayoutInflater.from(getActivity()).inflate(R.layout.item_time, viewGroup, false);
                    }
                    CalUtil cal = new CalUtil();
                    String dateAndHour[] = cal.getTimeAfterNHours("MM-dd HH", i).split(" ");
                    if (dateAndHour[1].equals("00")) {
                        ((TextView) view.findViewById(R.id.time)).setText(dateAndHour[0] + "\n" + dateAndHour[1] + ":00");
                    } else {
                        ((TextView) view.findViewById(R.id.time)).setText(dateAndHour[1] + ":00");
                    }
                    return view;
                }

                @Override
                public boolean isEnabled(int position) {
                    return false;
                }
            });

            return rootView;
        }

        List<ListView> syncListViews = new ArrayList<>();


        private View touchedView = null;

//        private void addToSyncGroup(ListView lv) {
//            syncListViews.add(lv);
//            lv.setOnTouchListener((view, motionEvent) -> {
//                if (touchedView == null) {
//                    touchedView = view;
//                }
//                if (view == touchedView) {
//                    for (ListView other : syncListViews) {
//                        if (other != touchedView) {
//                            other.dispatchTouchEvent(motionEvent);
//                        }
//                    }
//                    if (motionEvent.getAction() == MotionEvent.ACTION_UP
//                            || motionEvent.getAction() == MotionEvent.ACTION_HOVER_EXIT) {
//                        touchedView = null;
//                    }
//                }
//                return false;
//            });
//        }

    }


}
