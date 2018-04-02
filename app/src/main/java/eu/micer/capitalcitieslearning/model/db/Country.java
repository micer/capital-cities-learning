package eu.micer.capitalcitieslearning.model.db;

public interface Country {
    int getId();

    String getCca2Code();

    String getName();

    String getCapital();

    String getRegion();

    String getSubregion();

    double getArea();
}
