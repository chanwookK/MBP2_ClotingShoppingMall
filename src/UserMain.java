import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
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
        String[] parts;

        int productNum;
        String inputString;
        System.out.println("[주문] 상품 번호를 입력해주세요.");
        inputString = in.nextLine();
        
        //상품번호 문법규칙에 부합하는지 체크
        Pattern pattern = Pattern.compile("[0-9]+");
        Matcher matcher = pattern.matcher(inputString);

        if(matcher.matches()){

            productNum = Integer.parseInt(inputString);
            String filePath = "productlist.txt";

            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    parts = line.split("/");
                    if(parts[0].equals(inputString)){

                        //상품수량이 0인 경우
                        if(parts[3].equals("0"))
                            break;

                        //모든 조건 만족 주문 ㄱㄱ
                        i = true;
                        break;
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }


        }else{
            //오류 발생(문법규칙 규합 X)
        }

        if(!i){
            //오류 발생(해당 상품번호 없음)
        }


        //주문 로직
        if(this.user.getCoupon() == 0){
            //구매 완료
            return;
        }

        //쿠폰 로직
        System.out.println(this.user.getName());


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

                try (BufferedReader reader = new BufferedReader(new FileReader("src/User/konkuk2023.txt"))) {
//                try (BufferedReader reader = new BufferedReader(new FileReader("src/User/"+this.user.getId()+".txt"))) {
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
                // 할인 쿠폰 보유량 확인 기능
                if(this.user.getCoupon() ==0) {
                    //할인 쿠폰 없는경우
                    System.out.println(user.getName()+"회원님 현재 쿠폰을 보유하고 있지 않습니다.");
                    System.out.println("메인 메뉴로 돌아가려면 엔터 키를 누르세요.");
                    in.nextLine(); //엔터 대기
                } else {
                    System.out.println(user.getName()+"회원님의 현재 쿠폰 보유량입니다.");
                    System.out.println("5,000원 할인 쿠폰 "+this.user.getCoupon()+"장");
                    System.out.println();
                    System.out.println("메인 메뉴로 돌아가려면 엔터 키를 누르세요.");
                    in.nextLine(); // 엔터 대기
                }
            } else if (selNum == 3) {
                userMainLoop();
            } else {
                System.out.println("!오류 : 메뉴번호를 잘못 입력했습니다. 다시 입력해주세요.");
            }
        }

    }

    public User getUser() {
        return user;
    }
}
