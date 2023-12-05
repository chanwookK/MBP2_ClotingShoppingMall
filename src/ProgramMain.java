import java.io.*;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;
import java.lang.Integer;

import static java.lang.Integer.parseInt;

public class ProgramMain {

    public ProgramMain() {
        showProgramMain();
    }

    void showProgramMain(){
        int selNum = 0;

        Scanner scan = new Scanner(System.in);

        boolean exit=false;

        while(!exit){
            System.out.println("A쇼핑몰에 오신 것을 환영합니다!\n");
            System.out.println("이용할 서비스의 번호를 입력하세요");
            System.out.println("[회원가입 및 로그인]");
            System.out.println("1. 회원가입");
            System.out.println("2. 로그인");
            System.out.println("3. 종료");
            while(true){

                //Scanner scan = new Scanner(System.in);

                System.out.print("AShoppingMall > ");
                try{
                    selNum = scan.nextInt();
                    if(selNum == 1){
                        signUp();
                        break;
                    }else if(selNum == 2){
                        logIn();
                        break;
                    }else if(selNum == 3){
                        System.out.println("\n의류 쇼핑몰 프로그램을 종료합니다.");
                        exit=true;
                        break;
                    }else{
                        System.out.println("!오류 : 메뉴번호를 잘못 입력했습니다. 다시 입력해주세요.");
                    }
                }catch (InputMismatchException | IOException e) {
                    scan.nextLine();
                    System.out.println("!오류 : 메뉴번호를 잘못 입력했습니다. 다시 입력해주세요.");

                }
            }
        }

    }
    void checkDate(){

        Scanner scan = new Scanner(System.in);
        String date;

        System.out.println("[날짜 입력]\n" + "오늘 날짜를 입력해주세요 (예: 231221)");
        while (true) {
            System.out.print("AShoppingMall >");
            date = scan.nextLine();

            if (date.length() == 6 && date.matches(("\\d+"))) {
                //유효한 날짜인지 확인
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
                try {
                    LocalDate inputDate = LocalDate.parse(date, formatter);
                    int day = parseInt(date.substring(4,6));
                    LocalDate validDate = LocalDate.of(inputDate.getYear(), inputDate.getMonthValue(), day);

                    //여기까지 왔다면 존재하는 날짜
                    //마지막 주문 날짜 이후 날짜인지 확인
                    File file = new File("src/date.txt");
                    BufferedReader reader = new BufferedReader((new FileReader(file)));
                    String lastDate = reader.readLine();
                    int result = lastDate.compareTo(date);

                    if(result<= 0){
                        System.out.println("\n오늘 날짜 : "+date.substring(0,2)+"년 "+date.substring(2,4)+"월 "+date.substring(4,6)+"일");
                        System.out.println();

                        FileWriter fw = new FileWriter(file,false);
                        // 문자열 출력
                        fw.write(lastDate+"\n"+date);

                        fw.close();
                        reader.close(); //BufferedReader 닫기
                        break;
                    }else{
                        System.out.println("!오류: " + lastDate + " 이후 날짜를 다시 입력해주세요.");
                    }

                } catch (DateTimeException e) {
                    System.out.println("!오류: 존재하지 않는 날짜입니다. 다시 입력해주세요.");
                } catch (IOException e) {
                    //throw new RuntimeException(e);
                    System.out.println("파일 존재 X");
                }
            } else {
                System.out.println("!오류: 잘못된 입력입니다. 다시 입력해주세요.");
            }
        }
    }

    void signUp(){

        String name; String id; String password;

        Scanner scan = new Scanner(System.in);
        System.out.println("\n회원가입을 시작합니다.\n");

        //이름 입력받기
        System.out.println("이름을 입력하세요.");
        while (true){
            System.out.print("AShoppingMall > ");
            name = scan.nextLine();

            if(name.isBlank()||name.trim()!=name||(!Pattern.matches("^[a-zA-z가-힣]*$",name.replaceAll("\\s","")))){
                System.out.println("!오류 : 이름은 길이가 1 이상인 한글, 영문 대/소문자, 비개행공백열만으로 구성된 문자열이어야 " +
                        "합니다. 또한 첫 문자와 끝 문자는 비개행공백열이 아니어야 합니다.\n" +
                        "다시 입력해주세요.");
            }else{
                break;
            }
        }

        //아이디 입력받기
        System.out.println("아이디를 입력하세요.");
        while (true){
            System.out.print("AShoppingMall > ");
            id = scan.nextLine();

            //의미규칙 확인하기
            boolean c = true; //의미규칙이 맞다면 true, 맞지 않으면 false;
            File file = new File("./src/User/");
            String[] fileList = file.list();
            String newId = id+".txt";
            for(String f :fileList) {
                if(f.equals(newId)){
                    c = false;
                    break;
                }
            }

            if(!(Pattern.matches("^[0-9a-z]*$",id))||id.length()<6||id.length()>10){ //문법규칙 확인
                System.out.println("!오류 : 아이디는 영문 소문자와 숫자로만 이루어진 길이가 6 이상 10 이하인 문자열이어야합니다.\n" +
                        "다시 입력해주세요.");
            }else if(!c) {
                System.out.println("!오류 : 이미 존재하는 아이디입니다. 다시 입력해주세요.");
            }
            else {
                break;
            }
        }

        //비밀번호 입력받기
        System.out.println("비밀번호를 입력하세요.");
        while (true){
            System.out.print("AShoppingMall > ");
            password = scan.nextLine();

            if(!(Pattern.matches("^[0-9a-zA-z]*$",password))||password.length()<8||password.length()>20){
                System.out.println("!오류 : 비밀번호는 영문 대/소문자와 숫자로만 이루어진 길이가 8이상 20이하인 문자열이어야합니다.\n" +
                        "다시 입력해주세요.");
            }else{
                break;
            }

        }

        //회원 파일 생성하기
        File newFile = new File("./src/User/"+id+".txt");
        try {
            FileWriter writer = new FileWriter(newFile);
            writer.write(name+"\n");
            writer.write(password+"\n");
            writer.write("0/0\n");
            writer.write("5000/0\n");
            writer.write("5000/0\n");
            writer.close();
        }catch (IOException e){
            e.printStackTrace();
        }


        System.out.println("\n회원가입이 완료되었습니다.\n");
        return;

    }

    public void logIn() throws IOException {

        final String managerName = "관리자";
        final String managerId = "adminadminadmin";
        final String managerPassword = "password00";

        String name;
        String id;
        String password;

        int coupon=0;

        String filepath = "";
        boolean exitOuterLoop = false;

        Scanner scanner = new Scanner(System.in);

        // ID 입력받기
        System.out.println("\n[로그인]");
        System.out.println("아이디를 입력하세요.");
        while (true) {
            System.out.print("AShoppingMall > ");
            id = scanner.nextLine();
            filepath= "src/User/"+id+".txt";

            // ID가 관리자인 경우
            if (id.equals(managerId)) {
                break;
            }

            // ID 입력 조건
            if (id.contains(" ") || id.length() < 6 || 10 < id.length() || !id.matches("^[a-z0-9]*$")) {
                System.out.println("!오류 : 등록되지 않은 아이디입니다. 다시 입력해주세요.");
            }
            else {
                // ID가 user 폴더에 저장된 userId.txt 데이터에 존재하는 경우
                File file = new File(filepath);
                if (file.exists()) {
                    break;
                }
                else {
                    System.out.println("!오류 : 등록되지 않은 아이디입니다. 다시 입력해주세요.");
                }
            }

        }

        // PW 입력받기
        System.out.println("비밀번호를 입력하세요.");
        while (true) {
            if (exitOuterLoop) {
                break;
            }
            System.out.print("AShoppingmall > ");
            password = scanner.nextLine();

            // manager ID와 PW일경우
            if (id.equals(managerId) && password.equals(managerPassword)) {

                Map<String, Integer> managerCoupon = new HashMap<>();
                managerCoupon.put("0", 0);
                Map<String, List<String>> managerExpiration = new HashMap<>();
                managerExpiration.put("0", null);

                User user = new User(managerName, id, password,managerCoupon, managerExpiration);
                System.out.println();
                ManagerMain managerMain = new ManagerMain(user);
                break;
            }

            // PW 입력조건
            if (password.length() < 8 || 20 < password.length() || !password.matches("^[a-zA-Z0-9]*$")) {
                System.out.println("!오류 : 틀린 비밀번호입니다. 다시 입력해주세요.");
            }

            else { // 입력조건 부합

                File file = new File(filepath);
                BufferedReader reader = new BufferedReader((new FileReader(file)));
                name = reader.readLine(); //이름 가져오기
                String truePassword = reader.readLine(); //PW 가져오기

                if(!truePassword.equals(password)){ //PW 잘못 입력한 경우
                    System.out.println("!오류 : 틀린 비밀번호입니다. 다시 입력해주세요.");
                }
                else{ //ID에 부합하는 PW 입력한 경우

                    String line;
                    int lineNumber = 2;

                    Map<String, Integer> couponMap = new HashMap<>();
                    Map<String, List<String>> expirationMap = new HashMap<>();
                    List<String> expirationList = new ArrayList<>();

                    while ((line = reader.readLine()) != null) {

                        lineNumber++;
                        // 사용자.txt에서 coupon 정보 가져온다

                        if (lineNumber == 4) {
                            String[] couponPairs = line.split(",");
                            for (String pair : couponPairs) {
                                String[] parts = pair.split("/");
                                if (parts.length == 2) {
                                    String discount_amount = parts[0];
                                    int coupon_amount = Integer.parseInt(parts[1]);
                                    couponMap.put(discount_amount, coupon_amount);
                                }
                            }

                            // 쿠폰 할인 가격들을 불러와 합치기
                            for (Map.Entry<String, Integer> entry : couponMap.entrySet()) {
                                coupon += entry.getValue();
                            }
                        }

                        if (lineNumber == 5) { // expirationMap 가져오기
                            String[] parts2=line.split("/");
                            String couponPrice=parts2[0];
                            if(parts2[1].equals("0")){
                                //빈 리스트 상태로 넣기
                                expirationMap.put(couponPrice, expirationList);
                            }else{
                                String[] expirationPairs = parts2[1].split(",");
                                for (String expPairs : expirationPairs) {
                                    expirationList.add(expPairs);

                                    expirationMap.put(couponPrice, expirationList);
                                }
                            }
                        }

                    }
                    // 위에서 입력한 ID에 부합하는 PW가 입력된 경우
                    System.out.println("\n로그인 완료!\n");

                    reader.close();

                    checkDate(); //날짜 입력 받기

                    // 등록된 일반 유저의 ID와 PW일경우
                    User user = new User(name, id, password, couponMap, expirationMap);
//                    System.out.printf(name + " " + id + " " + password + " " + couponMap + " ");
                    UserMain usermain = new UserMain(user);
                    exitOuterLoop = true;
                    break;

                }

            }
        }
    }
}
