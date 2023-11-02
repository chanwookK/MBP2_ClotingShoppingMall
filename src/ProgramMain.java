import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Pattern;

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

            if(!(Pattern.matches("^[0-9a-zA-z]*$",id))||id.length()<6||id.length()>10){ //문법규칙 확인
                System.out.println("!오류 : 아이디는 영문 대/소문자와 숫자로만 이루어진 길이가 6 이상 10 이하인 문자열이어야합니다.\n" +
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
            writer.write("5000/0");
            writer.close();
        }catch (IOException e){
            e.printStackTrace();
        }


        System.out.println("\n회원가입이 완료되었습니다.\n");
        showProgramMain();

    }

    void logIn(){
        System.out.println("로그인");
        System.out.println("백업되었는지 확인");
    }
}
