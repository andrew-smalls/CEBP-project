package Tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class NameReader {

    private static String username = null;
    private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public static String readName()
    {
        System.out.println("Enter a valid name: ");
        try{
            username = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return username;
    }
}
