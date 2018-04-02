package eu.micer.capitalcitieslearning.model;

import eu.micer.capitalcitieslearning.repository.db.entity.CountryEntity;

public class QuestionData {

    private CountryEntity country;
    private AnswerOption option1;
    private AnswerOption option2;
    private AnswerOption option3;
    private AnswerOption option4;
    private boolean answered;

    public QuestionData(CountryEntity country, AnswerOption option1, AnswerOption option2, AnswerOption option3, AnswerOption option4) {
        this.country = country;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
    }

    public AnswerOption getOption1() {
        return option1;
    }

    public AnswerOption getOption2() {
        return option2;
    }

    public AnswerOption getOption3() {
        return option3;
    }

    public AnswerOption getOption4() {
        return option4;
    }

    public CountryEntity getCountry() {
        return country;
    }

    public boolean isAnswered() {
        return answered;
    }

    public void setAnswered(boolean answered) {
        this.answered = answered;
    }
}
