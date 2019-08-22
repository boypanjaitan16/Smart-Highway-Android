package main.boy.pjt.etoll.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RequestLogin implements Serializable {

    public RequestLogin(){
        super();
    }

    public RequestLogin(String id, String password){
        this.id = id;
        this.password   = password;
    }

    @Expose
    @SerializedName("id")
    private String id;

    @Expose
    @SerializedName("password")
    private String password;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
