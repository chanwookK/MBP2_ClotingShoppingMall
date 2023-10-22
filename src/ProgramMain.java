import java.util.InputMismatchException;
import java.util.Scanner;

public class ProgramMain {

    public ProgramMain() {
        showProgramMain();
    }

    void showProgramMain(){
        int selNum = 0;

        Scanner scan = new Scanner(System.in);

        System.out.println("A쇼핑몰에 오신 것을 환영합니다!\n");
        System.out.println("이용할 서비스의 번호를 입력하세요");
        System.out.println("[회원가입 및 로그인]");
        System.out.println("1. 회원가입");
        System.out.println("2. 로그인");
        System.out.println("3. 종료");
        while(true){
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
                    break;
                }else{
                    System.out.println("!오류 : 메뉴번호를 잘못 입력했습니다. 다시 입력해주세요.");
                }
            }catch (InputMismatchException e) {
                scan.nextLine();
                System.out.println("!오류 : 메뉴번호를 잘못 입력했습니다. 다시 입력해주세요.");

            }
        }

    }

    void signUp(){
        System.out.println("회원가입");
    }

    void logIn(){
        System.out.println("로그인");
    }
}
