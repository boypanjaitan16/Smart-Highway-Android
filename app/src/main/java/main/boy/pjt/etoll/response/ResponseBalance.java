package main.boy.pjt.etoll.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Boy Panjaitan on 08/06/2018.
 */

public class ResponseBalance {
    public static class RetrofitResponse{
        @SerializedName("status")
        @Expose
        private String status;

        @SerializedName("message")
        @Expose
        private String message;

        @SerializedName("data")
        @Expose
        private Values data;

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

        public Values getData() {
            return data;
        }

        public void setData(Values data) {
            this.data = data;
        }
    }

    public static  class Values implements Serializable{
        @SerializedName("username")
        @Expose
        private String username;

        @SerializedName("balance")
        @Expose
        private int balance;

        @SerializedName("roda")
        @Expose
        private int roda;

        public int getRoda() {
            return roda;
        }

        public void setRoda(int roda) {
            this.roda = roda;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public int getBalance() {
            return balance;
        }

        public void setBalance(int balance) {
            this.balance = balance;
        }
    }
}
