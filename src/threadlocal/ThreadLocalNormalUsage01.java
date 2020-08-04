package threadlocal;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 十个线程分别打印出生日日期
 */
public class ThreadLocalNormalUsage01 {
    public String date(int seconds){
        // 参数单位是毫秒,从1970.1.1 00:00:00 GMT计时
        Date date = new Date(1000L * seconds);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return simpleDateFormat.format(date);
    }

    public static void main(String[] args) {

        for (int i = 0; i < 10; i++) {
            int finalI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String date = new ThreadLocalNormalUsage01().date(finalI);
                    System.out.println(Thread.currentThread().getName()+":"+date);
                }
            }).start();
        }

    }

}
