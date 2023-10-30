import java.util.Map;

public class User {

    private String name;
    private String id;
    private String password;
    private Map<String, Integer> coupon;

    public User(String name, String id, String password, Map<String, Integer> coupon){

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

    public Map<String, Integer> getCoupon() {
        return coupon;
    }

    public void setCoupon(Map<String, Integer> coupon) {
        this.coupon = coupon;
    }
}
