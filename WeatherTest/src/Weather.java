import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
//체감온도, 현재온도, 바람, 습도, 미세먼지, 강수확률

public class Weather {
	private String humidity = "";//습도
	private String city = "";//시
	private String county = "";//구
	private String village = "";//동
	private String sky_name = "";//하늘상태
	private String temperature_tc = "";//현재기온
	private String temperature_tmax = "";//오늘의 최고날씨
	private String temperature_tmin = "";//오늘의 최저날씨
	private String wdir = "";//풍향(degree)
	private String wspd = "";//풍속(m/s)
	private String temprature_wc = ""; //체감온도
	
	public String getHumidity() {return this.humidity;}
	public String getCity() {return this.city;}
	public String getCounty() {return this.county;}
	public String getVillage() {return this.village;}
	public String getSkyName() {return this.sky_name;}
	public String getTemperatureTc() {return this.temperature_tc;}
	public String getTemperatureTmax() {return this.temperature_tmax;}
	public String getTemperatureTmin() {return this.temperature_tmin;}
	public String getWdir() {return this.wdir;}
	public String getWspd() {return this.wspd;}

	public void setHumidity(String humidity) {this.humidity = humidity;}
	public void setCity(String city) {this.city = city;}
	public void setCounty(String county) {this.county = county;}
	public void setVillage(String village) {this.village = village;}
	public void setSkyName(String sky_name) {this.sky_name = sky_name;}
	public void setTemperatureTc(String temperature_tc) {this.temperature_tc = temperature_tc;}
	public void setTempertaureTmax(String temperature_tmax) {this.temperature_tmax = temperature_tmax;}
	public void setTempertaureTmin(String temperature_tmin) {this.temperature_tmin = temperature_tmin;}
	public void setWdir(String wdir) {this.wdir = wdir;}
	public void setWspd(String wspd) {this.wspd = wspd;}
	
	void currentWeatherHourly(double lat, double lon){
		String url_string = "https://api2.sktelecom.com/weather/current/hourly?";
		String url_params = "lat=" + String.valueOf(lat) + "&lon=" + String.valueOf(lon);
		
		HttpsURLConnection conn = null;
	    BufferedReader br = null;
  
	    //String city = "서울";
	    //String county = "강남구";
	    //String village = "삼성동";	    
	    try {
	    	//city = URLEncoder.encode(city,"UTF-8");
	    	//county = URLEncoder.encode(county,"UTF-8");
	    	//village = URLEncoder.encode(village,"UTF-8");
	    	//url_string = url_string + "city=" +city + "&county=" + county + "&village=" + village;
	    	URL url = new URL(url_string+url_params);
			System.out.println("URL:" + url.toExternalForm());
			
			conn = (HttpsURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Content-Type", "charset=UTF-8");
			conn.setRequestProperty("appKey", "");
			
	        int responseCode = conn.getResponseCode();
	        if(responseCode == 200) {
	        	br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	        }else {
	        	br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
	        }
	        
	        String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();
            
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject)jsonParser.parse(response.toString());
            jsonObject = (JSONObject) jsonObject.get("weather");
            JSONArray jsonArray = (JSONArray) jsonObject.get("hourly");
            jsonObject = (JSONObject) jsonArray.get(0);
            
            setHumidity((String)jsonObject.get("humidity"));
            JSONObject grid = (JSONObject)jsonObject.get("grid");
            JSONObject sky = (JSONObject)jsonObject.get("sky");
            JSONObject temperature = (JSONObject)jsonObject.get("temperature");
            JSONObject wind = (JSONObject)jsonObject.get("wind");
            
            setCity((String)grid.get("city"));
            setCounty((String)grid.get("county"));
            setVillage((String)grid.get("village"));
            setSkyName((String) sky.get("name"));
            setTemperatureTc((String) temperature.get("tc"));
            setTempertaureTmax((String) temperature.get("tmax"));
            setTempertaureTmin((String) temperature.get("tmin"));
            setWdir((String)wind.get("wdir"));
            setWspd((String)wind.get("wspd")); 
            
	    }catch(IOException e) {
	    	System.out.println(e);
	    }catch(ParseException e) {
	    	System.out.println(e);
	    }
	}
	
	void windChillTemperatureIndex(double lat, double lon){
		String url_string = "https://api2.sktelecom.com/weather/index/wct?";
		String url_params = "lat=" + String.valueOf(lat) + "&lon=" + String.valueOf(lon);
		
		HttpsURLConnection conn = null;
	    BufferedReader br = null;
  
	    try {
	    	URL url = new URL(url_string+url_params);
			System.out.println("URL:" + url.toExternalForm());
			
			conn = (HttpsURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Content-Type", "charset=UTF-8");
			conn.setRequestProperty("appKey", "");
			
	        int responseCode = conn.getResponseCode();
	        if(responseCode == 200) {
	        	br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	        }else {
	        	br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
	        }      
	        String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();
            
            System.out.println(response.toString());
            //파싱 해야함
	    }catch(IOException e) {
	    	System.out.println(e);
	    }
	}
	    
	public static void main(String[] args){
		//37.496003, 126.954080
			Weather weather = new Weather();
			weather.currentWeatherHourly(37.496003, 126.954080);			
            System.out.println("위치: " + weather.getCity() +" " + weather.getCounty() + " " + weather.getVillage());
            System.out.println("하늘상태: " + weather.getSkyName());
            System.out.println("현재 기온: " + weather.getTemperatureTc() + "  오늘의 최고온도: " + weather.getTemperatureTmax() + "  오늘의 최저온도: " + weather.getTemperatureTmin());      
            System.out.println("습도: " + weather.getHumidity());
            System.out.println("풍향: " + weather.getWdir() + "  풍속: " + weather.getWspd());
            
            System.out.println("");
            weather.windChillTemperatureIndex(37.496003, 126.954080);
  	
	}

}
