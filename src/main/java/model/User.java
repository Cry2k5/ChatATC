package model;

import javafx.beans.property.SimpleStringProperty;

public class User {
    public SimpleStringProperty name;
    public SimpleStringProperty image;

    public User(){}
    public User(String name, String image){
        this.name = new SimpleStringProperty(name);
        this.image = new SimpleStringProperty(image);
    }

    public String getname() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setname(String name) {
        if (this.name == null) {
            this.name = new SimpleStringProperty();
        }
        this.name.set(name);
    }
    public String getimage() {
        return image.get();
    }

    public SimpleStringProperty imageProperty() {
        return image;
    }

    public void setimage(String image) {
        if (this.image == null) {
            this.image = new SimpleStringProperty();
        }
        this.name.set(image);
    }
}
