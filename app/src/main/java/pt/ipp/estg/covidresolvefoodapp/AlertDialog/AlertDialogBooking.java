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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import pt.ipp.estg.covidresolvefoodapp.R;

public class AlertDialogBooking extends AppCompatDialogFragment {

    private CalendarView mCalendarView;
    private RadioGroup mRadioGroup;

    private DialogBookingListener mListener;
    private long date = -1;
    private long dateCurrent = -1;
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

                int mounth = i1 + 1;

                contentDay = String.valueOf(i2);
                contentMonth = String.valueOf(mounth);

                if (i2 < 10) {
                    contentDay = "0" + i2;
                }

                if (mounth < 10) {
                    contentMonth = "0" + mounth;
                }

                contentDate = contentDay + "/" + contentMonth + "/" + i;
            }
        });

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

                        break;

                    case R.id.jantar_choice:
                        eatChoice = "Jantar (19:00-22:00)";
                        tStart = Time.valueOf("19:00:00");
                        tEnd = Time.valueOf("22:00:00");

                        timeStart = tStart.getTime();
                        timeEnd = tEnd.getTime();
                        break;
                }
            }
        });

        builder.setView(view)
                .setTitle("Marcação")
                .setPositiveButton("Marcação", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        long moment = System.currentTimeMillis();

                        Date dateLong = new Date();
                        Date dateFormatCurrent = new Date(moment);

                        String contentDateCurrent = String.valueOf(dateFormatCurrent.getDate());
                        String contentMonthCurrent = String.valueOf(dateFormatCurrent.getMonth());
                        String contentYearCurrent = String.valueOf(dateFormatCurrent.getYear() + 1900);

                        if (dateFormatCurrent.getDate() < 10) {
                            contentDateCurrent = "0" + dateFormatCurrent.getDate();
                        }

                        if ((dateFormatCurrent.getMonth() + 1) < 10) {
                            contentMonthCurrent = "0" + (dateFormatCurrent.getMonth() + 1);
                        }

                        String currentDateFormat = contentDateCurrent + "/" + contentMonthCurrent + "/" + contentYearCurrent;

                        System.out.println("CurrentDateFormat: " + currentDateFormat);
                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                            dateLong = sdf.parse(contentDate);
                            dateFormatCurrent = sdf.parse(currentDateFormat);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        date = dateLong.getTime();
                        dateCurrent = dateFormatCurrent.getTime();

                        System.out.println("Convert CurrentTime: " + dateCurrent);

                        System.out.println("DATE: " + date);
                        System.out.println("Moment: " + moment);

                        //Ver a data actual dd/mm/yyyy é superior a data escolhido dd/mm/yyyy e impedir de escolher datas anteriores do cá de hoje
                        if (dateCurrent > date) {
                            date = dateCurrent;

                            if (dateFormatCurrent.getTime() <= 50400000) {
                                timeStart = 43200000; //12:00
                                timeEnd = 50400000; //14:00
                            } else {
                                timeStart = 68400000; //19:00
                                timeEnd = 79200000; //22:00
                            }

                            mListener.bookingReview(date, timeStart, timeEnd, eatChoice);
                        } else {
                            //Data + Almoço 14:00
                            long dateAlmocoJantarCompare = date + timeEnd;

                            System.out.println("SUM-DATE-TIME: " + dateAlmocoJantarCompare);

                            //02/09/2020 - 23:30 > 02/09/2020 - 14:00
                            if (moment >= dateAlmocoJantarCompare) {
                                timeStart = 68400000; //19:00
                                timeEnd = 79200000; //22:00
                            }

                            //Data + Jantar 22:00
                            dateAlmocoJantarCompare = date + timeEnd;

                            //02/09/2020 - 23:30 > 02/09/2020 - 22:00
                            if (moment >= dateAlmocoJantarCompare) {
                                date += TimeUnit.DAYS.toMillis(1);
                                timeStart = 43200000; //12:00
                                timeEnd = 50400000; //14:00
                            }

                            mListener.bookingReview(date, timeStart, timeEnd, eatChoice);
                        }

                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
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
