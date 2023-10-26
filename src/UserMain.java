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

    public void productSearch(){

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

    public void showMyPage(){

    }

    public User getUser() {
        return user;
    }
}
