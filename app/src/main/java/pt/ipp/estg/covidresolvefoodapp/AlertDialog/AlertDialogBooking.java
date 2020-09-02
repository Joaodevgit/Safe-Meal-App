package pt.ipp.estg.covidresolvefoodapp.AlertDialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CalendarView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import pt.ipp.estg.covidresolvefoodapp.R;

public class AlertDialogBooking extends AppCompatDialogFragment {

    private CalendarView mCalendarView;
    private RadioGroup mRadioGroup;

    private DialogBookingListener mListener;
    private long date = -1;
    private long timeStart = -1;
    private long timeEnd = -1;

    private String eatChoice;

    private String contentDate = "";
    private String contentDay = "";
    private String contentMonth = "";

    private Time tStart;
    private Time tEnd;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_booking_review, null);

        this.mCalendarView = view.findViewById(R.id.calender_booking);

        this.mRadioGroup = view.findViewById(R.id.radio_group_horas);

        //Caso que o utilizador não escolher outras opções
        Date dateParserString = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        this.contentDate = formatter.format(dateParserString);

        this.date = this.mCalendarView.getDate();

        eatChoice = "Almoçar (12:00-14:00)";
        tStart = Time.valueOf("12:00:00");
        tEnd = Time.valueOf("14:00:00");

        timeStart = tStart.getTime();
        timeEnd = tEnd.getTime();

        this.mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {

                contentDay = String.valueOf(i2);
                contentMonth = String.valueOf(i1);

                if (i2 < 10) {
                    contentDay = "0" + i2;
                }

                if (i1 < 10) {
                    contentMonth = "0" + i1;
                }

                contentDate = contentDay + "/" + contentMonth + "/" + i;
                Toast.makeText(getContext(), "Data: " + contentDate, Toast.LENGTH_SHORT).show();
            }
        });

        //TODO: Ver como posso ter em long essas cenas....
        this.mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.almocar_choice:
                        eatChoice = "Almoçar (12:00-14:00)";
                        tStart = Time.valueOf("12:00:00");
                        tEnd = Time.valueOf("14:00:00");

                        timeStart = tStart.getTime();
                        timeEnd = tEnd.getTime();

                        Toast.makeText(getContext(), "TimeST: " + timeStart + " TimeEnd: " + timeEnd, Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.jantar_choice:
                        eatChoice = "Jantar (19:00-22:00)";
                        tStart = Time.valueOf("19:00:00");
                        tEnd = Time.valueOf("22:00:00");

                        timeStart = tStart.getTime();
                        timeEnd = tEnd.getTime();
                        Toast.makeText(getContext(), "TimeST: " + timeStart + " TimeEnd: " + timeEnd, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        builder.setView(view)
                .setTitle("Marcação")
                .setPositiveButton("Marcação", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Date dateLong = new Date();

                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                            dateLong = sdf.parse(contentDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        date = dateLong.getTime();

                        mListener.bookingReview(date, timeStart, timeEnd, eatChoice);
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getContext(), "Aconteceu um erro", Toast.LENGTH_SHORT).show();
                    }
                });

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            mListener = (DialogBookingListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement ExampleDialogListener");
        }
    }

    public interface DialogBookingListener {
        void bookingReview(long date, long timeStart, long timeEnd, String description);
    }
}
