import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class sqli_detector {

    public static void main(String[] args) {
        System.out.println("--------------------------------------------------");
        System.out.println("==============SQL Injection Detector==============");
        System.out.println("--------------------------------------------------\n");
        System.out.println("Enter your link : eg ~> http://target.com/news.php?id=1");
        String link = new Scanner(System.in).next();

        HttpURLConnection httpURLConnection = null;

        try {
            new URL(link);

            link += "'";

            if (!link.contains("?")) {
                System.out.println("Site is not vulnerable");
            }
            else {
                URL url = new URL(link);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

                String input;
                StringBuilder stringBuilder = new StringBuilder();

                while ( (input = bufferedReader.readLine()) != null) {
                    stringBuilder.append(input);
                }

                String content = stringBuilder.toString();

                if (content.contains("mysql_fetch_array()") || content.contains("You have an error in your SQL syntax") || content.length() <=0) {
                    System.out.println("\n" + link + " <~ This site is vulnerable");
                }else {
                    System.out.println("\n" + link + " <~ This site not vulnerable");
                }
                bufferedReader.close();
            }
        } catch (MalformedURLException e) {
            System.out.println("Try to enter valid link");
        } catch (IOException e) {

            assert httpURLConnection != null;

            try {
                int res_code =httpURLConnection.getResponseCode();

                if (res_code == 500) {
                    System.out.println("\n" + link + " <~ This site is vulnerable");
                }else {
                    System.out.println("\n" + link + " <~ This site not vulnerable");
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
}