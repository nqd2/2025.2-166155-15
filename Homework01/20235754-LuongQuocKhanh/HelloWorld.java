import java.util.Scanner;

public class HelloWorld {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Nhap ten cua ban: ");
        String ten = sc.nextLine();

        System.out.println("Xin chao, " + ten + "!");
        sc.close();
    }
}
