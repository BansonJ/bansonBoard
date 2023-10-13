package com.practice.boardpratice.Weather;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class Weather {
    public static List<String> lookUpWeather() throws IOException, ParseException {
        String nx = "57";    //위도
        String ny = "124";    //경도
        String baseDate;    //조회하고싶은 날짜
        String baseTime = null;    //조회하고싶은 시간
        String type = "json";    //조회하고 싶은 type(json, xml 중 고름)

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        String Dnow = format.format(date);
        baseDate = Dnow;

        List<String> result1 = new ArrayList<>();

        LocalTime localTime = LocalTime.now();
        String Tnow = localTime.toString().substring(0,2);
        int time = Integer.parseInt(Tnow);

        if (time == 0 || time == 1) {
            baseTime = "02";
        } else if (time == 24) {
            baseTime = "23";
        } else if (time % 3 == 2) {
            baseTime = String.valueOf(time);
        } else if (time % 3 == 1) {
            baseTime = String.valueOf(time-2);
        } else if (time % 3 == 0) {
            baseTime = String.valueOf(time-1);
        }


//		참고문서에 있는 url주소
        String apiUrl = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst";
//         홈페이지에서 받은 키
        String serviceKey = "sdX40IrfRCRbZYMRwkzpoUArDZuvCfPBVWf3vtODLYNKQ1JJJsbUbDhencXtgcukktr4dJDPK5WZA0TANdvCjw%3D%3D";

            StringBuilder urlBuilder = new StringBuilder(apiUrl);
            urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "=" + serviceKey);
            urlBuilder.append("&" + URLEncoder.encode("nx", "UTF-8") + "=" + URLEncoder.encode(nx, "UTF-8")); //경도
            urlBuilder.append("&" + URLEncoder.encode("ny", "UTF-8") + "=" + URLEncoder.encode(ny, "UTF-8")); //위도
            urlBuilder.append("&" + URLEncoder.encode("base_date", "UTF-8") + "=" + URLEncoder.encode(baseDate, "UTF-8")); /* 조회하고싶은 날짜*/
            urlBuilder.append("&" + URLEncoder.encode("base_time", "UTF-8") + "=" + URLEncoder.encode(baseTime+"00", "UTF-8")); /* 조회하고싶은 시간 AM 02시부터 3시간 단위 */
            urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8"));    /* 타입 */
            /*
             * GET방식으로 전송해서 파라미터 받아오기
             */
            URL url = new URL(urlBuilder.toString());

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            System.out.println("Response code: " + conn.getResponseCode());

            BufferedReader rd;
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }

            rd.close();
            conn.disconnect();
            String result = sb.toString();

            //이 밑에 부터는 json에서 데이터 파싱해 오는 부분

            // response 키를 가지고 데이터를 파싱

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(result);

            JSONObject response = (JSONObject) jsonObject.get("response");
            JSONObject body = (JSONObject) response.get("body");
            JSONObject items = (JSONObject) body.get("items");
            JSONArray item = (JSONArray) items.get("item");

            for (int j = 0; j < item.size(); j++) {

                JSONObject weather = (JSONObject) item.get(j);

                //온도와 강수만 빼서 씀
                String T1H = null;
                String PCP = null;
                if (weather.get("category").equals("TMP")) {
                    T1H = (String) weather.get("fcstValue");
                    result1.add(T1H);
                } else if (weather.get("category").equals("PCP")) {
                    PCP = (String) weather.get("fcstValue");
                    result1.add(PCP);
                }
            }
            result1.add(baseTime);
        return result1;
    }
}
