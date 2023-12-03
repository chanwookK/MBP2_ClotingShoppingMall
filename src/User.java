import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {

    private String name;
    private String id;
    private String password;
    private Map<String, Integer> coupon;

    private Map<String, List<String>> expirationMap;
    private String todayDate;

    public User(String name, String id, String password, Map<String, Integer> coupon, Map<String, List<String>> expirationMap){

        this.name = name;
        this.id = id;
        this.password = password;
        this.coupon = coupon;
        this.expirationMap = expirationMap;

        String filePath = "src/date.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            // 첫 번째 줄 읽기
            br.readLine();

            // 두 번째 줄 읽기
            String secondLine = br.readLine();

            //todayDate 셋팅
            this.todayDate = secondLine;

        } catch (IOException e) {
            e.printStackTrace();
        }


        String mapPath = "src/User/"+this.id+".txt";

        try (BufferedReader read = new BufferedReader(new FileReader(mapPath))) {
            //이전 줄 읽기
            for(int i=0; i<3; i++) {
                read.readLine();
            }
            String line = read.readLine();
            String[] parts2 = line.split("/");
            if(Integer.parseInt(parts2[1]) == 0) {
                List<String> list = new ArrayList<>();
                this.expirationMap.put("5000", list);
            } else {
                //5번째 줄 읽기
                String fifthLine = read.readLine();


                //map초기화
                //key값은 첫번째 //value값을 ,기준으로 하나씩 넣기
                Map<String, List<String>> settingMap = new HashMap<>();
                List<String> date = new ArrayList<>();

                String[] parts = fifthLine.split("/");
                String key = parts[0];

                String[] dataSet = parts[1].split(",");
                for(int j=0; j< dataSet.length; j++) {
                    date.add(dataSet[j]);
                }
                settingMap.put(key, date);
                this.expirationMap = settingMap;
            }



        } catch (IOException e) {
            e.printStackTrace();
        }



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

    public Map<String, List<String>> getExpirationMap() {
        return expirationMap;
    }

    public void setExpirationMap(Map<String, List<String>> expirationMap) {
        this.expirationMap = expirationMap;
    }

    public String getTodayDate() {
        return todayDate;
    }

    public void setTodayDate(String todayDate) {
        this.todayDate = todayDate;
    }
}
