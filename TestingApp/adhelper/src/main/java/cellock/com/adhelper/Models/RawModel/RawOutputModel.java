package cellock.com.adhelper.Models.RawModel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by AntonisSt on 4/6/2017.
 */

public class RawOutputModel {
    @SerializedName("status")
    protected String status;

    @SerializedName("result")
    protected List<RawInputTemp> list;

    public void setList(List<RawInputTemp> list) {
        this.list = list;
    }

    public List<RawInputTemp> getList() {
        return list;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
