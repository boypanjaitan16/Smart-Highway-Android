package main.boy.pjt.etoll.values;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Boy Panjaitan on 04/03/2018.
 */

public class ValueProfile {
    public static class RetrofitResponse{
        @SerializedName("status")
        @Expose
        private String status;

        @SerializedName("message")
        @Expose
        private String message;

        @SerializedName("data")
        @Expose
        private ValueProfile.Values data;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public ValueProfile.Values getData() {
            return data;
        }

        public void setData(ValueProfile.Values data) {
            this.data = data;
        }
    }

    public static class Values implements Serializable{
        @SerializedName("username")
        @Expose
        private String username;

        @SerializedName("name")
        @Expose
        private String name;

        @SerializedName("password")
        @Expose
        private String password;

        @SerializedName("phone")
        @Expose
        private String phone;

        @SerializedName("email")
        @Expose
        private String email;

        @SerializedName("address")
        @Expose
        private String address;

        @SerializedName("token")
        @Expose
        private String token;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        @Override
        public String toString() {
            return "{username:"+getUsername()+",name:"+getName()+",email:"+getEmail()+",phone:"+getPhone()+",address:"+getAddress()+",password:"+getPassword()+",token:"+getToken()+"}";
        }
    }
}
