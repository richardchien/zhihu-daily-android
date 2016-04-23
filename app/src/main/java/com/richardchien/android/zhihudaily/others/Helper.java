package com.richardchien.android.zhihudaily.others;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;

/**
 * ZhihuDaily
 * Created by richard on 16/1/14.
 */
public class Helper {
    public static String stringFromBytes(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        String line;
        String string = null;
        try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
             InputStreamReader isr = new InputStreamReader(bais);
             BufferedReader br = new BufferedReader(isr)) {
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            string = sb.toString();
        } catch (Exception ignored) {
        }

        return string;
    }
}
