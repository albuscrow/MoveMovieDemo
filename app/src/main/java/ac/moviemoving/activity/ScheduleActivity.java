package ac.moviemoving.activity;

import ac.moviemoving.R;
import ac.moviemoving.data.DataProvider;
import ac.moviemoving.model.RoomSchedule;
import ac.moviemoving.model.Show;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.AdapterView.OnItemSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;


public class ScheduleActivity extends BaseActivity {

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

        adapter = new ShowAdapter();

        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // When the given dropdown item is selected, show its contents in the
                // container view.
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, ShowScheduleFragment.newInstance(position + 1, adapter))
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
            dialog.setCancelable(true);
            View rootView = LayoutInflater.from(ScheduleActivity.this).inflate(R.layout.dialog_oneclick_to_arrange, null);
            rootView.findViewById(R.id.begin_arrange).setOnClickListener(view1 -> {
                adapter.afterArrange();
                dialog.dismiss();
            });
            dialog.setContentView(rootView);
            dialog.show();
        });

    }

    public void chooseTime(View v) {
        TextView tt = (TextView) v;
        Calendar calendar = new GregorianCalendar();
        DatePickerDialog dpd = new DatePickerDialog(
                this, (datePicker, i, i1, i2) -> {
            System.out.println(i);
            System.out.println(i1);
            System.out.println(i2);
            tt.setText(i + "-" + (i1 + 1) + "-" + i2);
        },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dpd.show();
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public ShowAdapter adapter;
    public static class ShowScheduleFragment extends Fragment {
        private final ShowAdapter adapter;
        private List<RoomSchedule> roomSchedules;

        public ShowScheduleFragment(ShowAdapter sa) {
            this.adapter = sa;
        }

        private static final String ARG_SECTION_NUMBER = "section_number";

        public static ShowScheduleFragment newInstance(int sectionNumber, ShowAdapter sa) {
            ShowScheduleFragment fragment = new ShowScheduleFragment(sa);
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
            timeListView.setAdapter(adapter);

            return rootView;
        }

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

    class ShowAdapter extends BaseAdapter {
        List<Show> shows = new ArrayList<>();

        public void afterArrange() {
            Show.initDate();
            for (int i = 0; i < 32; ++i) {
                shows.add(Show.randomShow());
            }

            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return shows.size();
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
                view = LayoutInflater.from(ScheduleActivity.this).inflate(R.layout.item_time, viewGroup, false);
            }
            ((TextView) view.findViewById(R.id.time_start)).setText(shows.get(i).getStartTime());
            ((TextView) view.findViewById(R.id.time_end)).setText(shows.get(i).getEndTime());
            return view;
        }

        @Override
        public boolean isEnabled(int position) {
            return false;
        }

    }

}
