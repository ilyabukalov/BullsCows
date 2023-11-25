package game.bullcow;
import game.bullcow.Chislo;
import game.bullcow.Answer;
import game.bullcow.BullCow;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        BullCow bc = new BullCow((byte) 4);
        Scanner sc= new Scanner(System.in);
        while(!bc.askNew()) {
            System.out.println(bc.getNumber());

            System.out.print("Enter bulls:");
            String line = sc.nextLine();
            byte bull = (byte)Integer.parseInt(line);

            System.out.print("Enter cows:");
            line = sc.nextLine();
            byte cow = (byte)Integer.parseInt(line);

            bc.answer((byte)bull,(byte)cow);
        }
        System.out.print("Your number: " + bc.getNumber());
        System.out.println();
    }
}