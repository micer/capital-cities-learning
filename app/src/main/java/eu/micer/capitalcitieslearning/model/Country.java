
package eu.micer.capitalcitieslearning.model;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Country implements Parcelable
{

    @SerializedName("name")
    @Expose
    private Name name;
    @SerializedName("cca2")
    @Expose
    private String cca2;
    @SerializedName("independent")
    @Expose
    private boolean independent;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("currency")
    @Expose
    private List<String> currency = null;
    @SerializedName("capital")
    @Expose
    private List<String> capital = null;
    @SerializedName("region")
    @Expose
    private String region;
    @SerializedName("subregion")
    @Expose
    private String subregion;
    @SerializedName("area")
    @Expose
    private double area;
    public final static Parcelable.Creator<Country> CREATOR = new Creator<Country>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Country createFromParcel(Parcel in) {
            return new Country(in);
        }

        public Country[] newArray(int size) {
            return (new Country[size]);
        }

    }
    ;

    protected Country(Parcel in) {
        this.name = ((Name) in.readValue((Name.class.getClassLoader())));
        this.cca2 = ((String) in.readValue((String.class.getClassLoader())));
        this.independent = ((boolean) in.readValue((boolean.class.getClassLoader())));
        this.status = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.currency, (java.lang.String.class.getClassLoader()));
        in.readList(this.capital, (java.lang.String.class.getClassLoader()));
        this.region = ((String) in.readValue((String.class.getClassLoader())));
        this.subregion = ((String) in.readValue((String.class.getClassLoader())));
        this.area = ((int) in.readValue((int.class.getClassLoader())));
    }

    public Country() {
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public String getCca2() {
        return cca2;
    }

    public void setCca2(String cca2) {
        this.cca2 = cca2;
    }

    public boolean isIndependent() {
        return independent;
    }

    public void setIndependent(boolean independent) {
        this.independent = independent;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getCurrency() {
        return currency;
    }

    public void setCurrency(List<String> currency) {
        this.currency = currency;
    }

    public List<String> getCapital() {
        return capital;
    }

    public void setCapital(List<String> capital) {
        this.capital = capital;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getSubregion() {
        return subregion;
    }

    public void setSubregion(String subregion) {
        this.subregion = subregion;
    }

    public double getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(name);
        dest.writeValue(cca2);
        dest.writeValue(independent);
        dest.writeValue(status);
        dest.writeList(currency);
        dest.writeList(capital);
        dest.writeValue(region);
        dest.writeValue(subregion);
        dest.writeValue(area);
    }

    public int describeContents() {
        return  0;
    }

}
