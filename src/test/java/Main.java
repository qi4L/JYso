import java.util.HashMap;
import java.util.Map;

public class Main {
    private static Map<Character, String[]> bytesMap = new HashMap<>();

    public static void main(String[] args) {
        char[] keys = new char[]{'$', '.', ';', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '[', ']', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
        for (char k : keys) {
            bytesMap.put(k, CharSplitToThressBytes(k));
        }
        printMap(bytesMap);
    }

    public static void printMap(Map<Character, String[]> map) {
        System.out.println("-------------开始打印map-------------");
        System.out.println("Map<Character,int[]> bytesMap=new HashMap<>();");
        for (Map.Entry<Character, String[]> entry : map.entrySet()) {
            String row = "bytesMap.put('{c}', new int[]{{b1},{b2},{b3}});";
            String[] bytes = entry.getValue();
            row = row.replace("{c}", String.valueOf(entry.getKey()));
            row = row.replace("{b1}", bytes[0]);
            row = row.replace("{b2}", bytes[1]);
            row = row.replace("{b3}", bytes[2]);
            System.out.println(row);
        }
    }

    /**
     * 将一个字符拆分为三个字节的表现形式
     *
     * @param c:要拆分的字符
     * @return:字符拆分后的三个字节 b1  11100000
     * b2  100000+字符前两位
     * b3  10+字符后六位
     */
    public static String[] CharSplitToThressBytes(char c) {
        System.out.println("-------------" + c + "-------------");
        int b1, b2, b3;
        byte t = (byte) c;
        b1 = Integer.parseInt("11100000", 2);
        //0xc0:11000000
        //0x80:10000000
        b2 = ((t & 0xc0) >>> 6) | 0x80;
        //0x3f:00111111
        b3 = (t & 0x3f) | 0x80;
        String b10x = "0x" + Integer.toHexString(b1);
        String b20x = "0x" + Integer.toHexString(b2);
        String b30x = "0x" + Integer.toHexString(b3);
        System.out.println("b1: " + b10x);
        System.out.println("b2: " + b20x);
        System.out.println("b3: " + b30x);
        if (TestThreeByteCode(b1, b2, b3) != c) {
            System.out.println("字符c： " + c + " 表示错误!");
            System.exit(0);
        }
        System.out.println("字符" + c + " 表示无误");
        return new String[]{b10x, b20x, b30x};
    }

    //3个字节表示一个字符
    public static char TestThreeByteCode(int b1, int b2, int b3) {
        return (char) (((b1 & 0x0F) << 12) |
                ((b2 & 0x3F) << 6) |
                ((b3 & 0x3F) << 0));
    }
}
