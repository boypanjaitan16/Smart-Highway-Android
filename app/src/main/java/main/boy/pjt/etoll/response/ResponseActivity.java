package main.boy.pjt.etoll.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Boy Panjaitan on 15/06/2018.
 */

public class ResponseActivity {
    public static class RetrofitResponse{
        @SerializedName("status")
        @Expose
        private String status;

        @SerializedName("message")
        @Expose
        private String message;

        @SerializedName("data")
        @Expose
        private List<ResponseActivity.Values> data;

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

        public List<ResponseActivity.Values> getData() {
            return data;
        }

        public void setData(List<ResponseActivity.Values> data) {
            this.data = data;
        }
    }

    public static class Values implements Serializable{
        @SerializedName("date")
        @Expose
        private String date;

        @SerializedName("road")
        @Expose
        private ResponseRoad.Values road;

        @SerializedName("type")
        @Expose
        private String type;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public ResponseRoad.Values getRoad() {
            return road;
        }

        public void setRoad(ResponseRoad.Values road) {
            this.road = road;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
