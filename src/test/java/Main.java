public class Main {
    public static void main(String[] args) {
        String input = "org/apache/myfaces/core/service/IoServiceListenerSupport";
        int lastIndex = input.lastIndexOf('/');
        if (lastIndex != -1) {
            String result = input.substring(lastIndex + 1);
            System.out.println(result);
        } else {
            // 如果没有'/'字符，则直接打印原字符串
            System.out.println(input);
        }
    }
}
