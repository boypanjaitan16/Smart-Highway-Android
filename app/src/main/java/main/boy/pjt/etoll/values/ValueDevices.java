package main.boy.pjt.etoll.values;

import java.io.Serializable;

/**
 * Created by Boy Panjaitan on 15/06/2018.
 */

public class ValueDevices implements Serializable {
    private String name;

    private String address;

    public ValueDevices(){

    }

    public ValueDevices(String name, String address){
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
