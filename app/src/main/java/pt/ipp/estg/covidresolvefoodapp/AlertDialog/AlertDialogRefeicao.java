package pt.ipp.estg.covidresolvefoodapp.AlertDialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import pt.ipp.estg.covidresolvefoodapp.R;

public class AlertDialogRefeicao extends AppCompatDialogFragment {

    private EditText mEditText;

    private DialogMealsListener mealsListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_meals, null);

        this.mEditText = view.findViewById(R.id.dialog_content_refeicao);

        builder.setView(view)
                .setTitle("Refeição")
                .setPositiveButton("Submeter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (isPadding())
                            mealsListener.addMeals(mEditText.getText().toString());
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
            mealsListener = (DialogMealsListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement ExampleDialogListener");
        }
    }

    public interface DialogMealsListener {
        void addMeals(String meals);
    }

    private boolean isPadding() {
        boolean isPadding = true;

        if (this.mEditText.getText().length() > 20) {
            isPadding = false;
        }

        return isPadding;
    }
}
