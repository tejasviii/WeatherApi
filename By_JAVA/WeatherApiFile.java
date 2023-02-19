package org.example;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;


import java.util.Scanner;

public class WeatherApi {

    public static void callWeatherForecastApp() throws Exception{
        System.out.println("Enter the location at which you want to view the weather : ");
        Scanner sc = new Scanner(System.in);
        String location = sc.nextLine();

        URIBuilder builder = new URIBuilder("https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/weatherdata/forecast?");
        builder.setParameter("aggregateHours" , "24");
        builder.setParameter("contentType" , "json");
        builder.setParameter("unitGroup" , "metric");
        builder.setParameter("locationMode" , "single");
        builder.setParameter("key" , "1PYNQ6AWUDJE9AFERDCHJHSXK");
        builder.setParameter("locations" , location);

        HttpGet getData = new HttpGet(builder.build()); // an object to get data

        CloseableHttpClient httpClient = HttpClients.createDefault();

        CloseableHttpResponse response = httpClient.execute(getData);
        httpClient.close();

        if(response.getStatusLine().getStatusCode()==200){
            HttpEntity entityResponse = response.getEntity();
            String result = EntityUtils.toString(entityResponse);

            JSONObject responseObject = new JSONObject(result);

            // Formatting.............
            JSONObject locationObject = responseObject.getJSONObject("location");

            JSONArray valueArray = locationObject.getJSONArray("values");

            // datetimeStr , maxt , mint , visibility , humidity;

            System.out.println("dateTime \t \t \t \t\t\tmint \t maxt \t visibility \t humidity");
            for( int T = 0; T < valueArray.length(); T++ ) {
                JSONObject value = valueArray.getJSONObject(T);

                String dateTime = value.getString("datetimeStr");
                Double minTemp = value.getDouble("mint");
                Double maxTemp = value.getDouble("maxt");
                Double visibility = value.getDouble("visibility");
                Double humidity = value.getDouble("humidity");

                System.out.printf("%s \t %f \t %f \t %f \t %f \n" , dateTime , minTemp , maxTemp , visibility , humidity);
            }

        }else{
            System.out.println("Something went wrong please try again!!!!!!!!");
            return;
        }
    }

    public static void main(String[] args) {
        try{
            callWeatherForecastApp();
        }catch(Exception e){
            throw new RuntimeException();
        }
    }
}
