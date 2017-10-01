package cellock.com.adhelper.Models.SuperClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by AntonisSt on 2/10/2017.
 */

public class LoadUsersInput {

    @SerializedName("udid")
    @Expose
    protected String udid;
    @SerializedName("Nationality")
    @Expose
    protected String nationality;
    @SerializedName("gender")
    @Expose
    protected String gender;

    @SerializedName("DateOfBirth")
    @Expose
    protected String birth;
    @SerializedName("email")
    @Expose
    protected String email;

    public String getUdid() {
        return udid;
    }

    public void setUdid(String udid) {
        this.udid = udid;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
