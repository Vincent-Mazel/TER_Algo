import java.util.ArrayList;
import java.util.List;

public class Test {
    private static List<String> test;

    private static void test(List<String> list) {
        list.add("salut");
    }

    public static void main(final String[] args) throws ClassNotFoundException {
        test = new ArrayList<String>();

        test(test);

        System.out.println(test);
    }
}