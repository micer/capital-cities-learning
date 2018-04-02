package eu.micer.capitalcitieslearning.model;

public class AnswerOption {

    private String answer;
    private boolean isCorrect;
    private boolean selectedAsAnswer;

    public AnswerOption(String answer, boolean isCorrect) {
        this.answer = answer;
        this.isCorrect = isCorrect;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public boolean isSelectedAsAnswer() {
        return selectedAsAnswer;
    }

    public void setSelectedAsAnswer(boolean selectedAsAnswer) {
        this.selectedAsAnswer = selectedAsAnswer;
    }
}
