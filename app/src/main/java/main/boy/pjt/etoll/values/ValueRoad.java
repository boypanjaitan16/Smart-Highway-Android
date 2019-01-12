package main.boy.pjt.etoll.values;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Boy Panjaitan on 08/06/2018.
 */

public class ValueRoad {
    public static class RetrofitResponse{
        @SerializedName("status")
        @Expose
        private String status;

        @SerializedName("message")
        @Expose
        private String message;

        @SerializedName("data")
        @Expose
        private List<Values> data;

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

        public List<ValueRoad.Values> getData() {
            return data;
        }

        public void setData(List<ValueRoad.Values> data) {
            this.data = data;
        }
    }

    public static class Values implements Serializable{
        @SerializedName("id")
        @Expose
        private String id;

        @SerializedName("name")
        @Expose
        private String name;

        @SerializedName("distance")
        @Expose
        private int distance;

        @SerializedName("roda4")
        @Expose
        private int price4;

        @SerializedName("roda6")
        @Expose
        private int price6;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getDistance() {
            return distance;
        }

        public void setDistance(int distance) {
            this.distance = distance;
        }

        public int getPrice4() {
            return price4;
        }

        public void setPrice4(int price4) {
            this.price4 = price4;
        }

        public int getPrice6() {
            return price6;
        }

        public void setPrice6(int price6) {
            this.price6 = price6;
        }
    }
}
