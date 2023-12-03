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
        this.scanner = new Scanner(System.in);
        while(true){
            System.out.println("[관리자 메뉴] 실행할 메뉴를 선택해주세요");
            System.out.println("1. 재고 확인 및 수량/가격 변경");
            System.out.println("2. 상품 추가 및 삭제");
            System.out.println("3. 로그아웃");

            while(true){
                try{
                    System.out.print("AShoppingMall > ");

                    String input=scanner.nextLine().trim();
                    if(input.matches("[1-9][0-9]*")&&input.length()>=1){
                        selNum=Integer.parseInt(input);
                        if(selNum!=1 && selNum!=2 &&selNum!=3){ //1,2 또는 3을 입력하지 않은 경우. 비정상 입력
                            System.out.println("!오류: 잘못된 입력입니다. 다시 입력해주세요.");
                        }else break;
                    }else {
                        System.out.println("!오류: 잘못된 입력입니다. 다시 입력해주세요.");
                    }
                }catch (NumberFormatException e) {
                    System.out.println("!오류: 잘못된 입력입니다. 다시 입력해주세요.");
                }
            }

            if(selNum==1){
                checkProduct();
            }else if(selNum==2){
                manageProduct();
            }else if(selNum==3){
                System.out.println("\n관리자님, 로그아웃을 완료했습니다.\n");
                break;
            }
        }
    }

    //전체 상품 데이터 체크하는 함수
    public void checkProduct(){
        System.out.println();
        System.out.println("[재고확인]");

        //productlist.txt파일읽기
        try(Scanner filescan = new Scanner(new File(filepath))){
            filescan.nextLine(); //맨 위에 "존재했던 상품번호 중 가장 큰 상품번호" 읽음
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
        System.out.println("\n수량/가격 변경을원하시면 'y'를, 관리자 메뉴 첫 화면으로 돌아가길 원하시면 'n'을 입력해주세요.");

        scanner = new Scanner(System.in);
        String select;
        while(true){
            System.out.print("AShoppingMall > ");
            select=scanner.nextLine();
            select=select.trim();
            if(!select.equals("y") && !select.equals("n")){
                System.out.println("!오류 : 잘못된 입력입니다. 다시 입력해주세요.");
            }else break;
        }

        if(select.equals("y")) modifyProduct();
        else if(select.equals("n")){
            System.out.println();
        }
    }


    //상품 수량/가격 변경하는 함수
    public void modifyProduct(){
        int pNum=0;
        String pName="";
        int pAmount=-1;
        int pPrice=-1;
        int selNum;

        scanner = new Scanner(System.in);

        //제품 데이터 읽기
        List<String> lines = new ArrayList<>();
        try(BufferedReader reader= new BufferedReader(new FileReader(filepath))){
            String line;
            while((line=reader.readLine())!=null){
                lines.add(line);
            }
        }catch(IOException e){
            System.out.println("예외 발생 : "+e.getMessage()+"\n");
        }

        int maxNumber=Integer.parseInt(lines.get(0));


        //상품번호 예외처리
        int checkIndex=0; //수정할 배열 번호
        String targetLine = ""; // pNum과 일치하는 행을 담을 변수
        System.out.println("원하시는 상품의 번호를 입력해주세요");
        while(true){
            try{
                System.out.print("AShoppingMall > ");

                String num_input = scanner.nextLine().trim(); // 앞뒤 공백은 허용!
                //입력에 공백이 포함되지 않고 길이가 1이상인지 확인
                if(!num_input.contains(" ")&& num_input.length()>=1){
                    //입력이 0을 포함 안하는 경우
                    if(num_input.matches("[1-9][0-9]*")){
                        pNum=Integer.parseInt(num_input);

                        if(maxNumber<pNum){
                            System.out.println("번호를 다시 입력해주세요.");
                            continue;
                        }
                        for (String line : lines) {
                            String[] arr = line.split("/");
                            if (Integer.parseInt(arr[0]) == pNum) { // arr[0]가 pNum과 같은지 확인
                                targetLine = line; // 일치하는 행을 변수에 저장
                                break; // 일치하는 행을 찾았으므로 반복문 종료
                            }
                        }
                        // targetLine을 가공하여 필요한 작업 수행
                        if (!targetLine.isEmpty()) {
                            checkIndex=lines.indexOf(targetLine);
                            break;
                        }
                        else{ //pNum에 해당하는 행을 찾지 못한 경우에 대한 처리
                            System.out.println("번호를 다시 입력해주세요.");
                        }
                    }else if(num_input.startsWith("0")){ //0선행
                        System.out.println("번호를 다시 입력해주세요.");
                    }
                }else{ //공백이 포함된 문법오류
                    System.out.println("번호를 다시 입력해주세요.");
                }


            }catch(Exception e) {
                System.out.println("예외 발생 : "+e.getMessage()+"\n");
            }
        }


        //수량변경을 할건지, 가격변경을 할건지 정하기
        System.out.println("수량변경을 원하시면 1, 가격변경을 원하시면 2를 입력해주세요.");
        while(true){
            try{
                System.out.print("AShoppingMall > ");

                String input=scanner.nextLine().trim();
                if(input.matches("[1-9][0-9]*")&&input.length()>=1){
                    selNum=Integer.parseInt(input);
                    if(selNum!=1 && selNum!=2){ //1이나 2를 입력하지 않은 경우. 비정상 입력
                        System.out.println("!오류: 잘못된 입력입니다. 다시 입력해주세요.");
                    }else break;
                }else {
                    System.out.println("!오류: 잘못된 입력입니다. 다시 입력해주세요.");
                }
            }catch (NumberFormatException e) {
                System.out.println("!오류: 잘못된 입력입니다. 다시 입력해주세요.");
            }
        }

        String[] change = targetLine.split("/");
        if(selNum==1) {
            //변경수량 예외처리
            System.out.println("변경하실 수량을 입력해주세요");
            while (true) {
                try {
                    System.out.print("AShoppingMall > ");
                    String check_amount = scanner.nextLine(); //앞뒤 공백 확인하는
                    String amount_input = check_amount.trim();

                    //상품 수량 입력에 앞뒤 공백이 있었던 경우
                    if (check_amount.length() != amount_input.length()) {
                        System.out.println("!오류 : 재고수량 입력 형식이 맞지 않습니다. 다시 입력해주세요.");
                    } else if (!amount_input.matches("^[0-9]+$")) {
                        //숫자로만 이루어져있어야하며 중간에 공백이 있으면 안된다.
                        System.out.println("!오류 : 재고수량 입력 형식이 맞지 않습니다. 다시 입력해주세요.");
                    } else {
                        pAmount = Integer.parseInt(amount_input);
                        if (pAmount >= 0) { //0이상의 자연수면서, 공백이 없는 입력
                            change[3]=String.valueOf(pAmount);
                            break;
                        } else {
                            System.out.println("!오류 : 재고수량 입력 형식이 맞지 않습니다. 다시 입력해주세요.");
                        }
                    }
                } catch (Exception e) {
                    System.out.println("예외 발생 : " + e.getMessage() + "\n");
                }
            }
        }else if(selNum==2){
            //가격 예외처리
            System.out.println("변경하실 가격을 입력해주세요");
            while (true) {
                try {
                    System.out.print("AShoppingMall > ");
                    String check_price = scanner.nextLine(); //앞뒤 공백 확인하는
                    String price_input = check_price.trim();

                    //상품 수량 입력에 앞뒤 공백이 있었던 경우
                    if (check_price.length() != price_input.length()) {
                        System.out.println("!오류 : 가격 입력 형식이 맞지 않습니다. 다시 입력해주세요.");
                    } else if (!price_input.matches("^[1-9][0-9]*$")) {
                        //숫자로만 이루어져있어야하며 중간에 공백이 있으면 안된다.
                        System.out.println("!오류 : 가격 입력 형식이 맞지 않습니다. 다시 입력해주세요.");
                    } else {
                        pPrice = Integer.parseInt(price_input);
                        if (pPrice >= 1) { //0이상의 자연수면서, 공백이 없는 입력
                            change[2]=String.valueOf(pPrice);
                            break;
                        } else {
                            System.out.println("!오류 : 가격 입력 형식이 맞지 않습니다. 다시 입력해주세요.");
                        }
                    }
                } catch (Exception e) {
                    System.out.println("예외 발생 : " + e.getMessage() + "\n");
                }
            }
        }

        //데이터 수정
        String modify=String.join("/",change);
        lines.set(checkIndex,modify);

        //파일에 수정된 데이터 저장
        try(BufferedWriter writer=new BufferedWriter(new FileWriter(filepath))){
            for(String line: lines){
                writer.write(line);
                writer.newLine();
            }
        }catch(IOException e){
            System.out.println("예외 발생: "+e.getMessage()+"\n");
        }

        //productlist.txt에 변경된 상품 정보를 반영하고 pName에 상품명 대입
        pName=change[1];

        //수정완료
        System.out.println();

        if(selNum==1){
            System.out.printf("%d. %s 잔여수량이 %d으로 변경되었습니다.\n",pNum,pName,pAmount);
        }else if(selNum==2){
            System.out.printf("%d. %s 가격이 %d으로 변경되었습니다.\n",pNum,pName,pPrice);
        }


        //사용자 엔터키 입력 대기
        System.out.println();
        System.out.println("엔터키를 입력하시면 관리자 첫 메뉴로 돌아갑니다.");
        System.out.print("AShoppingMall > ");
        scanner.nextLine(); //엔터키 입력 대기

        System.out.println();

    }


    public void manageProduct(){

        int selNum;

        //제품 데이터 읽기
        List<String> lines = new ArrayList<>();
        try(BufferedReader reader= new BufferedReader(new FileReader(filepath))){
            String line;
            while((line=reader.readLine())!=null){
                lines.add(line);
            }
        }catch(IOException e){
            System.out.println("예외 발생 : "+e.getMessage()+"\n");
        }
        //존재했던 상품번호 중 가장 큰 상품번호
        int maxNumber=Integer.parseInt(lines.get(0));

        System.out.println();
        System.out.println("[상품 추가 및 삭제] 실행할 메뉴를 선택해주세요");
        System.out.println("1. 상품 추가");
        System.out.println("2. 상품 삭제");
        System.out.println();

        while(true){
            try{
                System.out.print("AShoppingMall > ");

                String input=scanner.nextLine().trim();
                if(input.matches("[1-9][0-9]*")&&input.length()>=1){
                    selNum=Integer.parseInt(input);
                    if(selNum!=1 && selNum!=2){ //1이나 2를 입력하지 않은 경우. 비정상 입력
                        System.out.println("!오류: 잘못된 입력입니다. 다시 입력해주세요.");
                    }else break;
                }else {
                    System.out.println("!오류: 잘못된 입력입니다. 다시 입력해주세요.");
                }
            }catch (NumberFormatException e) {
                System.out.println("!오류: 잘못된 입력입니다. 다시 입력해주세요.");
            }
        }

        if(selNum==1){
            int pNum=maxNumber+1;
            String pName="";
            int pAmount=-1;
            int pPrice=-1;


            System.out.println();
            System.out.println("[상품 추가]");
            System.out.println("추가하길 원하는 상품명을 입력해주세요.");
            //상품명 입력
            while(true){
                boolean flag=false;

                System.out.print("AShoppingMall > ");

                String input=scanner.nextLine();
                String check_input=input.trim();

                //앞뒤 공백있는지 확인
                if(input.length()!=check_input.length()){
                    System.out.println("!오류 : 상품명 입력 형식이 맞지 않습니다. 다시 입력해주세요.");
                }else if(input.length()<2 || input.length()>30){ //상품명 길이가 2이상 30이하가 안되는 경우
                    System.out.println("!오류 : 상품명 입력 형식이 맞지 않습니다. 다시 입력해주세요.");
                }else if(input.matches("^[a-zA-Z가-힣]+$")) {
                    //문자열로만 구성되어있어고 공백도 없는

                    //다른 상품명과 중복되는지 확인
                    for (int i = 1; i < lines.size(); i++) {
                        String[] check_repeat = lines.get(i).split("/");
                        if (check_repeat[1].equals(input)) {
                            flag = true;
                            break;
                        }
                    }
                    if (flag){
                        System.out.println("!오류 : 이미 존재하는 상품명입니다. 다시 입력해주세요.");
                    }else{
                        pName = input;
                        System.out.println();
                        break;
                    }
                }else{
                    System.out.println("!오류 : 상품명 입력 형식이 맞지 않습니다. 다시 입력해주세요.");
                }
            }


            System.out.println("추가하길 원하는 상품의 가격을 입력해주세요.");
            //가격 입력
            while(true){
                System.out.print("AShoppingMall > ");
                String check_price = scanner.nextLine(); //앞뒤 공백 확인하는
                String price_input = check_price.trim();

                //상품 수량 입력에 앞뒤 공백이 있었던 경우
                if (check_price.length() != price_input.length()) {
                    System.out.println("!오류 : 가격 입력 형식이 맞지 않습니다. 다시 입력해주세요.");
                } else if (!price_input.matches("^[1-9][0-9]*$")) {
                    //숫자로만 이루어져있어야하며 중간에 공백이 있으면 안된다.
                    System.out.println("!오류 : 가격 입력 형식이 맞지 않습니다. 다시 입력해주세요.");
                } else {
                    if (Integer.parseInt(price_input) >= 1) { //0이상의 자연수면서, 공백이 없는 입력
                        pPrice = Integer.parseInt(price_input);
                        System.out.println();
                        break;
                    } else {
                        System.out.println("!오류 : 가격 입력 형식이 맞지 않습니다. 다시 입력해주세요.");
                    }
                }
            }


            System.out.println("추가하길 원하는 상품의 재고수량을 입력해주세요.");
            //재고 수량 입력
            while (true) {
                System.out.print("AShoppingMall > ");
                String check_amount = scanner.nextLine(); //앞뒤 공백 확인하는
                String amount_input = check_amount.trim();

                //상품 수량 입력에 앞뒤 공백이 있었던 경우
                if (check_amount.length() != amount_input.length()) {
                    System.out.println("!오류 : 재고수량 입력 형식이 맞지 않습니다. 다시 입력해주세요.");
                } else if (!amount_input.matches("^[0-9]+$")) {
                    //숫자로만 이루어져있어야하며 중간에 공백이 있으면 안된다.
                    System.out.println("!오류 : 재고수량 입력 형식이 맞지 않습니다. 다시 입력해주세요.");
                } else {
                    if (Integer.parseInt(amount_input) >= 0) { //0이상의 자연수면서, 공백이 없는 입력
                        pAmount = Integer.parseInt(amount_input);
                        System.out.println();
                        break;
                    } else {
                        System.out.println("!오류 : 재고수량 입력 형식이 맞지 않습니다. 다시 입력해주세요.");
                    }
                }
            }

            String[] change=new String[4];
            change[0]=String.valueOf(pNum);
            change[1]=pName;
            change[2]=String.valueOf(pPrice);
            change[3]=String.valueOf(pAmount);

            //데이터 수정
            String modify=String.join("/",change);
            lines.set(0,String.valueOf(pNum)); //지금까지의 상품번호 중 가장 큰 상품번호
            lines.add(modify); //상품 추가

            System.out.println("상품 추가가 완료됐습니다!");
            System.out.printf("%d. %s %d %d",pNum,pName,pPrice,pAmount);
            System.out.println();

        }else if(selNum==2){

            int pNum;
            String pName="";


            System.out.println();
            System.out.println("[상품 삭제]");
            System.out.println("삭제하길 원하는 상품번호을 입력해주세요.");

            //상품번호 예외처리
            int checkIndex=0; //수정할 배열 번호
            while(true){
                try{
                    System.out.print("AShoppingMall > ");

                    String num_input = scanner.nextLine().trim(); // 앞뒤 공백은 허용!
                    //입력에 공백이 포함되지 않고 길이가 1이상인지 확인
                    if(!num_input.contains(" ")&& num_input.length()>=1){
                        //입력이 0을 포함 안하는 경우
                        if(num_input.matches("[1-9][0-9]*")){
                            pNum=Integer.parseInt(num_input);

                            if(maxNumber<pNum){
                                System.out.println("!오류 : 잘못된 입력입니다. 다시 입력해주세요.");
                                continue;
                            }
                            for (int i = 1; i < lines.size(); i++) {
                                String[] arr = lines.get(i).split("/");
                                if (Integer.parseInt(arr[0]) == pNum) { // arr[0]가 pNum과 같은지 확인
                                    checkIndex=i; // 일치하는 행을 변수에 저장
                                    break; // 일치하는 행을 찾았으므로 반복문 종료
                                }
                            }
                            // 일치하는 행이 있을때
                            if (checkIndex>0) {
                                break;
                            }
                            else{ //pNum에 해당하는 행을 찾지 못한 경우에 대한 처리
                                System.out.println("!오류 : 잘못된 입력입니다. 다시 입력해주세요.");
                            }
                        }else if(num_input.startsWith("0")){ //0선행
                            System.out.println("!오류 : 잘못된 입력입니다. 다시 입력해주세요.");
                        }
                    }else{ //공백이 포함된 문법오류
                        System.out.println("!오류 : 잘못된 입력입니다. 다시 입력해주세요.");
                    }


                }catch(Exception e) {
                    System.out.println("예외 발생 : "+e.getMessage()+"\n");
                }
            }

            //productlist.txt에 변경된 상품 정보를 반영하고 pName에 상품명 대입
            pName= lines.get(checkIndex).split("/")[1];

            //데이터 수정
            lines.remove(checkIndex);

            System.out.println();
            System.out.printf("%d. %s 상품이 삭제되었습니다!",pNum,pName);
            System.out.println();

        }

        //파일에 수정된 데이터 저장
        try(BufferedWriter writer=new BufferedWriter(new FileWriter(filepath))){
            for(String line: lines){
                writer.write(line);
                writer.newLine();
            }
        }catch(IOException e){
            System.out.println("예외 발생: "+e.getMessage()+"\n");
        }


        //사용자 엔터키 입력 대기
        System.out.println();
        System.out.println("엔터키를 입력하시면 관리자 첫 메뉴로 돌아갑니다.");
        System.out.print("AShoppingMall > ");
        scanner.nextLine(); //엔터키 입력 대기

        System.out.println();

    }

}
