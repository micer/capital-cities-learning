package eu.micer.capitalcitieslearning.repository.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import eu.micer.capitalcitieslearning.model.db.Country;

@Entity(tableName = "countries")
public class CountryEntity implements Country {

    @PrimaryKey
    private int id;

    private String cca2Code;

    private String name;

    private String capital;

    private String region;

    private String subregion;

    private double area;

    @Ignore
    public CountryEntity() {
    }

    public CountryEntity(int id, String cca2Code, String name, String capital, String region, String subregion, double area) {
        this.id = id;
        this.cca2Code = cca2Code;
        this.name = name;
        this.capital = capital;
        this.region = region;
        this.subregion = subregion;
        this.area = area;
    }

    public CountryEntity(Country country) {
        this.id = country.getId();
        this.cca2Code = country.getCca2Code();
        this.name = country.getName();
        this.capital = country.getCapital();
        this.region = country.getRegion();
        this.subregion = country.getSubregion();
        this.area = country.getArea();
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getCca2Code() {
        return cca2Code;
    }

    public void setCca2Code(String cca2Code) {
        this.cca2Code = cca2Code;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    @Override
    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    @Override
    public String getSubregion() {
        return subregion;
    }

    public void setSubregion(String subregion) {
        this.subregion = subregion;
    }

    @Override
    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }
}
