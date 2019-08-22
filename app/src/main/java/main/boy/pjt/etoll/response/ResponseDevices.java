package main.boy.pjt.etoll.response;

import java.io.Serializable;

/**
 * Created by Boy Panjaitan on 15/06/2018.
 */

public class ResponseDevices implements Serializable {
    private String name;

    private String address;

    public ResponseDevices(){

    }

    public ResponseDevices(String name, String address){
        setAddress(address);
        setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
