import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class ManagerMain {
    final private User user;
    private Scanner scanner;
    final private String filepath="src/productlist.txt";


    //생성자
    public ManagerMain(User user){
        this.user=user;
        managerMainLoop();
    }


    //관리자 메뉴
    public void managerMainLoop(){
        int selNum;
        Scanner scanner = new Scanner(System.in);
        while(true){
            System.out.println("[관리자 메뉴] 실행할 메뉴를 선택해주세요");
            System.out.println("1. 재고 확인 및 수량 변경");
            System.out.println("2. 로그아웃");

            while(true){
                try{
                    System.out.print("AShoppingMall > ");
                    selNum=scanner.nextInt();
                    if(selNum!=1 && selNum!=2){ //1이나 2를 입력하지 않은 경우. 비정상 입력
                        System.out.println("!오류: 잘못된 입력입니다. 다시 입력해주세요.");
                    }else break;
                }catch (InputMismatchException e) {
                    System.out.println("!오류 : 메뉴번호를 잘못 입력했습니다. 다시 입력해주세요.");
                }
            }

            if(selNum==1) checkProduct();
            else if(selNum==2){
                System.out.println("\n관리자님, 로그아웃을 완료했습니다.");
                break;
            }
        }
        scanner.close();
    }

    //전체 상품 데이터 체크하는 함수
    public void checkProduct(){
        System.out.println();
        System.out.println("[재고확인]");

        //productlist.txt파일읽기
        try(Scanner filescan = new Scanner(new File(filepath))){

            while(filescan.hasNextLine()){
                String[] arr=filescan.nextLine().split("/");

                //35000 -> 35,000
                DecimalFormat decimalFormat= new DecimalFormat("#,###");
                String formattedNumber=decimalFormat.format(Integer.parseInt(arr[2]));

                System.out.printf("%s. %s\n",arr[0],arr[1]);
                System.out.printf("\t가격: %s\n",formattedNumber);
                System.out.printf("\t잔여수량: %s\n",arr[3]);

            }
            filescan.close();

        }catch(FileNotFoundException e){
            e.printStackTrace();
        }

        //productlist.txt파일읽기 완료
        System.out.println("\n수량변경을원하시면 'y'를, 관리자 메뉴 첫 화면으로 돌아가길 원하시면 'n'을 입력해주세요.");

        scanner = new Scanner(System.in);
        String select;
        while(true){
            System.out.print("AShoppingMall > ");
            select=scanner.nextLine();
            select=select.trim();
            if(!select.equals("y") && !select.equals("n")){
                System.out.println("!오류: 잘못된 입력입니다. 다시 입력해주세요.");
            }else break;
        }

        if(select.equals("y")) modifyProduct();
        else if(select.equals("n")){
            //checkProduct() 종료하고 다시 managerMainLooop로 돌아감.
            System.out.println();
        }
    }


    //상품 수량 변경하는 함수
    public void modifyProduct(){
        int pNum=0;
        String pName="";
        int pAmount=-1;

        scanner = new Scanner(System.in);

        //상품번호 예외처리
        while(true){
            try{
                System.out.println("원하시는 상품의 번호를 입력해주세요");
                System.out.print("AShoppingMall > ");
                //공백제거
                String num_input=scanner.nextLine().replaceAll("\\s","");

                //입력이 숫자로만 구성되고 길이가 1 이상인지 확인
                if(num_input.matches("[0-9]+") && num_input.length()>=1){
                    pNum=Integer.parseInt(num_input);
                    break;
                }else{
                    System.out.println("!오류: 문법 규칙에 맞는 입력이 아닙니다.\n");
                }
            }catch(Exception e) {
                System.out.println("예외 발생: "+e.getMessage()+"\n");
            }
        }

        //변경수량 예외처리 대비
        while(true){
            try{
                System.out.println("변경하실 수량을 입력해주세요");
                System.out.print("AShoppingMall > ");
                String check_amount=scanner.nextLine(); //앞뒤 공백 확인하는
                String amount_input=check_amount.trim();

                //상품 수량 입력에 앞뒤 공백이 있었던 경우
                if(check_amount.length()!=amount_input.length()){
                    System.out.println("!오류: 문법 규칙에 맞는 입력이 아닙니다.\n");
                }else if(!amount_input.matches("^[0-9]+$")){
                    System.out.println("!오류: 문법 규칙에 맞는 입력이 아닙니다.\n");
                }else{
                    pAmount=Integer.parseInt(amount_input);
                    if(pAmount>=0){ //0이상의 자연수면서, 공백이 없는 입력
                        break;
                    }else{
                        System.out.println("!오류: 수량은 0 이상의 숫자여야합니다.\n");
                    }
                }
            }catch(Exception e){
                System.out.println("예외 발생: "+e.getMessage()+"\n");
            }
        }


        //제품 데이터 읽기
        List<String> lines = new ArrayList<>();
        boolean find=false; //pNum과 일치하는 상품번호가 있는지 확인할 변수
        try(BufferedReader reader= new BufferedReader(new FileReader(filepath))){
            String line;
            while((line=reader.readLine())!=null){
                String[] arr=line.split("/");

                //상품번호 같은지 확인
                if(pNum==Integer.parseInt(arr[0])){
                    //상품번호가 위에서 입력한 pNum과 일치하면 수량변경
                    find=true;
                    //arr[3]=String.valueOf(pAmount);
                    pName=arr[1];

                    line=String.join("/",arr);
                }

                lines.add(line);
            }
        }catch(IOException e){
            System.out.println("예외 발생: "+e.getMessage()+"\n");
        }


        if(!find){//pNum이 존재하지 않는 상품번호일때
            System.out.println();
            System.out.println("입력하신 상품번호에 해당하는 상품이 없습니다.");
        }else{
            //파일에 수정된 데이터 저장
            try(BufferedWriter writer=new BufferedWriter(new FileWriter(filepath))){
                for(String line: lines){
                    writer.write(line);
                    writer.newLine();
                }
            }catch(IOException e){
                System.out.println("예외 발생: "+e.getMessage()+"\n");
            }

            //수정완료
            System.out.println();
            System.out.printf("%d. %s 잔여수량이 %d으로 변경되었습니다.\n",pNum,pName,pAmount);
        }

        //사용자 엔터키 입력 대기
        System.out.println();
        System.out.println("엔터키를 입력하시면 관리자 첫 메뉴로 돌아갑니다.");
        System.out.print("AShoppingMall > ");
        scanner.nextLine(); //엔터키 입력 대기

        System.out.println();

    }

}
