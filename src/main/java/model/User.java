package model;

public class User {
    private Integer id;
    private String name;
    private String email;
    private String password;
    private Integer verifyCode;

    public User(){

    }
    public User(Integer id, String name, String email, String password, Integer verifyCode) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.verifyCode = verifyCode;
    }

    public User(Integer id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Integer getVerifyCode() {
        return verifyCode;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setVerifyCode(Integer verifyCode) {
        this.verifyCode = verifyCode;
    }
}
