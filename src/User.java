public class User {

    private String name;
    private String id;
    private String password;
    private int coupon;

    public User(String name, String id, String password, int coupon){

        this.name = name;
        this.id = id;
        this.password = password;
        this.coupon = coupon;

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public int getCoupon() {
        return coupon;
    }

    public void setCoupon(int coupon) {
        this.coupon = coupon;
    }
}
