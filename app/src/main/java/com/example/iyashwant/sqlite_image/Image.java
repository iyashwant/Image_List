package com.example.iyashwant.sqlite_image;

/**
 * Created by iyashwant on 17/6/17.
 */

public class Image {

    // private variables
    int _id;
    String _name;
    byte[] _image;

    // Empty constructor
    public Image() {

    }

    // constructor
    public Image(int keyId, String name, byte[] image) {
        this._id = keyId;
        this._name = name;
        this._image = image;

    }

    public Image(int keyId) {
        this._id = keyId;

    }


    public int getID() {
        return this._id;
    }

    public void setID(int keyId) {
        this._id = keyId;
    }


    public String getName() {
        return this._name;
    }

    public void setName(String name) {
        this._name = name;
    }


    public byte[] getImage() {
        return this._image;
    }

    public void setImage(byte[] image) {
        this._image = image;
    }
}



