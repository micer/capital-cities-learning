
package eu.micer.capitalcitieslearning.model.gson;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Name implements Parcelable {

    @SerializedName("common")
    @Expose
    private String common;
    @SerializedName("official")
    @Expose
    private String official;
    public final static Parcelable.Creator<Name> CREATOR = new Creator<Name>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Name createFromParcel(Parcel in) {
            return new Name(in);
        }

        public Name[] newArray(int size) {
            return (new Name[size]);
        }

    };

    protected Name(Parcel in) {
        this.common = ((String) in.readValue((String.class.getClassLoader())));
        this.official = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Name() {
    }

    public String getCommon() {
        return common;
    }

    public void setCommon(String common) {
        this.common = common;
    }

    public String getOfficial() {
        return official;
    }

    public void setOfficial(String official) {
        this.official = official;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(common);
        dest.writeValue(official);
    }

    public int describeContents() {
        return 0;
    }

}
