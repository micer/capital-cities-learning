package eu.micer.capitalcitieslearning.binding;

import android.databinding.BindingAdapter;
import android.widget.Button;

import eu.micer.capitalcitieslearning.R;
import eu.micer.capitalcitieslearning.model.AnswerOption;

public class BindingAdapters {

    @BindingAdapter({"bind:answered", "bind:answerOption"})
    public static void setBackground(Button button, boolean answered, AnswerOption answerOption) {
        if (answered) {
            if (!answerOption.isCorrect() && answerOption.isSelectedAsAnswer()) {
                button.setBackground(button.getResources().getDrawable(R.drawable.btn_default_red));
            }
            if (answerOption.isCorrect()) {
                button.setBackground(button.getResources().getDrawable(R.drawable.btn_default_green));
            }
        } else {
            button.setBackground(button.getResources().getDrawable(R.drawable.btn_default));
        }
    }
}
