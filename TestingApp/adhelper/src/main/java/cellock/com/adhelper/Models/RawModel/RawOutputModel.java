package cellock.com.adhelper.Models.RawModel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by AntonisSt on 4/6/2017.
 */

public class RawOutputModel {

    @SerializedName("email")
    protected String email;

    @SerializedName("gender")
    protected String gender;

    @SerializedName("Nationality")
    protected String nationality;

    @SerializedName("DateOfBirth")
    protected String dateOfBirth;

    @SerializedName("udid")
    protected String udid;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getUdid() {
        return udid;
    }

    public void setUdid(String udid) {
        this.udid = udid;
    }
}
