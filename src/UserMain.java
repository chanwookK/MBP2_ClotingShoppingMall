import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserMain {

    final private User user;
    private Scanner in = new Scanner(System.in);
    public UserMain(User user) throws IOException {

        this.user = user;
        userMainLoop();
        in.close();

    }

    public void userMainLoop() throws IOException {
        int selNum;

        while (true) {


            System.out.println("[메인 메뉴] 실행할 메뉴를 선택해주세요.");
            System.out.println("1. 상품검색");
            System.out.println("2. 주문");
            System.out.println("3. 마이페이지");
            System.out.println("4. 로그아웃");
            try {
                selNum = in.nextInt();
                in.nextLine();
                
            } catch(InputMismatchException e){
                //한글이나 영어를 입력했을 경우
                    continue;
            }

            switch (selNum) {
                case 1:
                    productSearch();
                    break;
                case 2:
                    productOrder();
                    break;
                case 3:
                    showMyPage();
                    break;
                case 4:
                    return;
                default:
            }
        }



    }

    public void productSearch() throws IOException{
        String userWantName;
        String searchedName;

        ArrayList<ArrayList<String>> productsList = new ArrayList<>();
        String parts[];
        boolean empty = true;

        // 사용자로부터 상품명 입력 받기
        System.out.println("[상품 검색]");
        System.out.println("검색할 상품명을 입력해주세요:");
        userWantName = in.nextLine();

        // 문법 규칙 검사
        Pattern pattern = Pattern.compile("^[가-힣]{1,30}$");
        Matcher matcher = pattern.matcher(userWantName);

        if (matcher.matches()) {
            // 의미 규칙 검사
            String filePath = "src/productlist.txt";

            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    parts = line.split("/");
                    ArrayList<String> product1 = new ArrayList<>();
                    product1.add(parts[1]); // Product Name
                    product1.add(parts[2]); // Product Price
                    productsList.add(product1);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            for (ArrayList<String> product : productsList) {
                int i=1;
                String productName = product.get(0); // 상품명
                String productPrice = product.get(1); // 상품가격

                // 사용자가 원하는 상품명과 일치하는 경우 출력
                if (productName.contains(userWantName)) {
                    empty = false;
                    System.out.println( i + ". " + productName + "\n  " + "가격: " +productPrice);
                    System.out.println();
                    i++;
                }
            }
            if(empty) {
                // 올바르지 않은 경우: 일치하는 상품 없음 오류 메시지 출력 후 재입력 요청
                System.out.println("!오류:'" + userWantName + "'와 일치하는 상품이 없습니다. 다시 입력해주세요.");
                productSearch(); // 재귀 호출을 통해 다시 상품명 검색
            }

            System.out.println("메인 메뉴로 돌아가려면 엔터 키를 누르세요.");
            in.nextLine(); // 엔터 대기
            userMainLoop();

        } else {
            // 올바르지 않은 경우: 문법 규칙 오류 메시지 출력 후 재입력 요청
            System.out.println("!오류 : 상품명 입력 형식이 맞지 않습니다. 다시 입력해주세요.");
            productSearch(); // 재귀 호출을 통해 다시 상품명 검색
        }
    }

    public void productOrder() throws IOException {

        boolean i = false;
        String[] parts = new String[4];

        int productNum;
        int targetLineNumber; // 수정하고자 하는 라인 번호
        int j = 0;
        String inputString;

        //상품번호 입력
        System.out.println("[주문] 상품 번호를 입력해주세요.");
        inputString = in.nextLine();
        
        //상품번호 문법규칙에 부합하는지 체크
        Pattern pattern = Pattern.compile("[0-9]+");
        Matcher matcher = pattern.matcher(inputString);

        //문법규칙에 부합한다면
        if(matcher.matches()){

            //파일커넥션
            productNum = Integer.parseInt(inputString);
            String filePath = "src/productlist.txt";

            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    j++;
                    parts = line.split("/");
                    if(parts[0].equals(inputString)){

                        //상품수량이 0인 경우// 재고가 없을 경우
                        if(parts[3].equals("0")) {
                            System.out.println("해당 상품은 재고가 없습니다.");
                            return;
                        }

                        //모든 조건 만족-> 주문 처리로 넘어감
                        i = true;
                        break;
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            targetLineNumber = j;


        }else{
            //오류 발생(문법규칙 규합 X)
            System.out.println("!오류: 잘못된 입력입니다. 다시 입력해주세요.");
            return;
        }

        if(!i){
            //오류 발생(해당 상품번호 없음)
            System.out.println("!오류: 존재하지 않는 상품 번호입니다.");
            return;
        }


        i = false;

        //주문 로직
        //보유한 쿠폰이 있는지 확인
        for (int couponNum :user.getCoupon().values()) {
            if(couponNum != 0){
                i = true;
                break;
            }
        }
        
        
        //유저 정보 받아오기
        List<String> userInfo = new ArrayList<>();
        String filePath = user.getId()+".txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                userInfo.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        int userAmount = 0; //변경 금액
        int userAmount2 = 0 ; //10만원 이하 누적 변경 금액
        int addCoupon = 0; //발급해야할 쿠폰 갯수
        
        //쿠폰 없는 주문처리
        if(!i){
            System.out.println(parts[0]+"."+parts[1]+" 구매가 완료되었습니다. 감사합니다.");
            String[] amount = userInfo.get(2).split("/");
            userAmount = Integer.parseInt(amount[0]) + Integer.parseInt(parts[2]);
            userAmount2 = Integer.parseInt(amount[1]) + Integer.parseInt(parts[2]);

            if(userAmount2 >= 100000){
                addCoupon = userAmount2 / 100000;
                userAmount2 = userAmount2 % 100000;
            }
            else{
                addCoupon = 0;
            }
        }
        else {
            //쿠폰이 적용된 주문처리
            int couponN;
            //쿠폰 종류
            String couponType;
            System.out.println(user.getName() + "회원님의 현재 쿠폰 보유량입니다.");
            for (String couponNum : user.getCoupon().keySet()) {

                System.out.println(couponNum + "원 할인 쿠폰 " + user.getCoupon().get(couponNum) + "장");
                System.out.println(parts[0] + "." + parts[1] + "\n  가격: " + parts[2]);
            }

            System.out.println();
            System.out.println();
            System.out.println("사용할 쿠폰의 수를 입력해주세요.(쿠폰 사용을 원하지 않을 시 '0'입력)");

            //비정상결과 판단
            while (true) {
                try {
                    //사용할 쿠폰 갯수 입력 받기
                    couponN = in.nextInt();
                    couponType = "5000";//임시로 작성

                } catch (InputMismatchException e) {
                    //잘못된 입력일 경우
                    System.out.println("!오류:잘못된 입력입니다. 다시 입력해주세요.");
                    continue;
                }
                //보유한 할인 쿠폰 수량보다 더 큰 숫자를 입력한 경우
                if (user.getCoupon().get(couponType) < couponN) {
                    System.out.println(user.getName() + "회원님의 현재 쿠폰 보유량은 " + user.getCoupon().get(couponType) + "입니다. 다시 입력해주세요");
                    continue;
                }
                //쿠폰 n장으로 구매 가능한 상품인데, n 초과 숫자를 입력하는 경우
                int outOfPrice = 0;
                outOfPrice = Integer.parseInt(parts[2]) - (Integer.parseInt(couponType) * couponN);
                int maxUseCouponNum = Integer.parseInt(parts[2]) / Integer.parseInt(couponType);

                if (outOfPrice < 0) {
                    System.out.println("해당 상품 구매 시 최대 사용 가능한 쿠폰은 " + maxUseCouponNum + "장입니다. 다시 입력해주세요.");
                    continue;
                }

                break;
            }

            int discountPrice = Integer.parseInt(couponType) * couponN;
            int price = Integer.parseInt(parts[2]) - discountPrice;
            
            //유저객체의 쿠폰 감소
            user.getCoupon().put(couponType, user.getCoupon().get(couponType) - couponN);
            
            System.out.println("지불할 금액은 " + price + "원 입니다.");


            System.out.println(parts[0] + "." + parts[1] + " 구매가 완료되었습니다. 감사합니다.");

            System.out.println(user.getName() + "회원님의 현재 쿠폰 보유량입니다.");
            for (String couponNum : user.getCoupon().keySet()) {

                System.out.println(couponNum + "원 할인 쿠폰 " + user.getCoupon().get(couponNum) + "장");
                System.out.println(parts[0] + "." + parts[1] + "\n  가격: " + parts[2]);
            }

            String[] amount = userInfo.get(2).split("/");

            userAmount = Integer.parseInt(amount[0]) + price;
            userAmount2 = Integer.parseInt(amount[1]) + price;

            if (userAmount2 >= 100000) {
                addCoupon = userAmount2 / 100000;
                userAmount2 = userAmount2 % 100000;
            } else {
                addCoupon = 0;
            }

        }


        //productlist 수정
        filePath = "productlist.txt";

        try {
            // 파일 읽기
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            int lineNumber = 0;

            while ((line = bufferedReader.readLine()) != null) {
                lineNumber++;

                if (lineNumber == targetLineNumber) {
                    String[] parts2 = line.split("/");

                    if (parts2.length >= 4) {
                        //상품 수량 1 감소
                        int newValue = Integer.parseInt(parts2[3]) - 1;
                        parts2[3] = String.valueOf(newValue);
                    }

                    // 수정된 라인을 StringBuilder에 추가
                    stringBuilder.append(String.join("/", parts2)).append("\n");
                } else {
                    // 수정할 라인이 아니면 그대로 추가
                    stringBuilder.append(line).append("\n");
                }
            }

            bufferedReader.close();

            // 파일 쓰기
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath));
            bufferedWriter.write(stringBuilder.toString());
            bufferedWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        //user 파일 수정
        filePath = user.getId() + ".txt";

        try {
            // 파일 읽기
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            int lineNumber = 0;

            while ((line = bufferedReader.readLine()) != null) {
                lineNumber++;

                if (lineNumber == 3) {
                    String[] parts2 = line.split("/");

                    if (parts2.length >= 0) {
                        //총 주문 금액 증가
                        int newValue = Integer.parseInt(parts2[0]) + userAmount;
                        parts2[0] = String.valueOf(newValue);
                    }
                    if (parts2.length >= 1) {
                        //10만원 이하 주문 금액 변경
                        int newValue = Integer.parseInt(parts2[1]) + userAmount2;
                        parts2[1] = String.valueOf(newValue);
                    }

                    // 수정된 라인을 StringBuilder에 추가
                    stringBuilder.append(String.join("/", parts2)).append("\n");

                } else if (lineNumber == 4) {
                    String[] parts2 = line.split("/");

                    if (parts2.length >= 1) {
                        //총 쿠폰 수 변경
                        int newValue = Integer.parseInt(parts2[1]) + addCoupon;
                        parts2[1] = String.valueOf(newValue);
                    }

                    // 수정된 라인을 StringBuilder에 추가
                    stringBuilder.append(String.join("/", parts2)).append("\n");

                } else {
                    // 수정할 라인이 아니면 그대로 추가
                    stringBuilder.append(line).append("\n");
                }


            }
            // 현재 날짜 가져오기
            LocalDate currentDate = LocalDate.now();

            // 출력을 원하는 형식으로 포맷 지정 ("yyMMdd" 형식)
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
            String formattedDate = currentDate.format(formatter);

            // 결과 출력
            System.out.println("현재 날짜: " + formattedDate);

            //마지막 행에 추가
            stringBuilder.append(parts[0]+"/"+parts[1]+"/"+parts[2]+"/"+ formattedDate).append("\n");

            bufferedReader.close();

            // 파일 쓰기
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath));
            bufferedWriter.write(stringBuilder.toString());
            bufferedWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }



    public void showMyPage() throws IOException {
        int selNum;
        String[] parts;


        while (true) {
            System.out.println("[마이페이지] 원하는 메뉴를 선택하세요.");
            System.out.println("1. 주문 내역 확인");
            System.out.println("2. 할인 쿠폰 보유량 확인");
            System.out.println("3. 뒤로가기");

            selNum = in.nextInt();
            in.nextLine();

            if (selNum == 1) {
                int k=1;
                // 주문 내역 확인 기능
                try (BufferedReader reader = new BufferedReader(new FileReader("src/User/"+this.user.getId()+".txt"))) {
                    String line;
                    boolean found = false;

                    for(int i=0; i<4; i++) {
                        reader.readLine();
                    }

                    // 해당 유저의 주문 내역 찾기
                    while ((line = reader.readLine()) != null) {
                        found = true;
                        parts = line.split("/");

                        System.out.println(k + ". " + parts[1] + "\n" +
                                "구매날짜: " + parts[3].substring(0,2)+"." + parts[3].substring(2,4)+"." + parts[3].substring(4)+ "\n" +
                                "가격: "+ parts[2]);
                        k++;
                    }

                    if (!found) {
                        System.out.println(this.user.getName()+"회원님 주문 내역이 없습니다. ");
                    }
                    System.out.println();
                    System.out.println("메인 메뉴로 돌아가려면 엔터 키를 누르세요.");
                    in.nextLine(); // 엔터 대기
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (selNum == 2) {
                boolean checkZero = true;
                for (String couponNum : user.getCoupon().keySet()) {
                    if(user.getCoupon().get(couponNum) != 0) {
                        checkZero = false;
                    }
                }

                // 할인 쿠폰 보유량 확인 기능
                if(checkZero) {
                    //할인 쿠폰 없는경우
                    System.out.println(user.getName()+"회원님 현재 쿠폰을 보유하고 있지 않습니다.");
                    System.out.println("메인 메뉴로 돌아가려면 엔터 키를 누르세요.");
                    in.nextLine(); //엔터 대기
                } else {
                    System.out.println(user.getName()+"회원님의 현재 쿠폰 보유량입니다.");
                    for (String couponNum : user.getCoupon().keySet()) {
                        System.out.println(couponNum + "원 할인 쿠폰 " + user.getCoupon().get(couponNum) + "장");
                    }
                    System.out.println();
                    System.out.println("메인 메뉴로 돌아가려면 엔터 키를 누르세요.");
                    in.nextLine(); // 엔터 대기
                }
            } else if (selNum == 3) {
//                userMainLoop();
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                reader.readLine();
                return;
            } else {
                System.out.println("!오류 : 메뉴번호를 잘못 입력했습니다. 다시 입력해주세요.");
            }
        }

    }

    public User getUser() {
        return user;
    }
}
