package com.nutty.HLTV.data;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

//Make it with arguments in order : document, Eventname, Time(String)
public class HLTVmatch {
	
	private String Team1, Team2, map1, map2, map3, info;
	private String bestof = "1";
	private String map1s, map3s, map2s;
	private String HLTVodds1, HLTVodds2;
	private String csgolodds1 = null, csgolodds2 = null;
	private String ESportsgbets1, ESportsgbets2;
	private Document doc;
	private String[] streams;
	boolean isover;//done in constructior
	private Date Time; //only to be used it isover = false
	private String Event;//may be null if is over = false
	
	//init with only url, for example if not grabed from the xml
	public HLTVmatch(Document doctmp){
		doc = doctmp;
		Element elm = doc.select("#time").first();
		if(elm != null){
		String str = elm.text();
			if(str.equals("Match over")){
				isover = true;
			}else{
				isover = false;
			}
		
		}else{
			isover = true;
		}
		
	}
	
	//init with the url and event name>>prefrable option if game is over or dont know the time
	public HLTVmatch(Document doctmp, String Event){
		doc = doctmp;
		Element elm = doc.select("#time").first();
		if(elm != null){
		String str = elm.text();
			if(str.equals("Match over")){
				isover = true;
			}else{
				isover = false;
			}
		
		}else{
			isover = true;
		}
		this.Event = Event;
	}
	
	//init with url, event and time. prefrable option always
	public HLTVmatch(Document doctmp, String Event, String Time){
		doc = doctmp;		
		isover = false;
		try {
			this.Time = StringToDate(Time);
		} catch (ParseException e) {
			//standered pass in with no date
			e.printStackTrace();
			Element elm = doc.select("#time").first();
			if(elm != null){
			String str = elm.text();
				if(str.equals("Match over")){
					isover = true;
				}else{
					isover = false;
				}
			
			}else{
				isover = true;
			}
		}
		this.Event = Event;
	}
		
	//You may or may not want to set csgo odds because it require another page, this is if you do.
	public void setallinfo(Document csgl){
		setteams();
		setmaps();
		setscore();
		setinfoandbestof();
		setStream();
		setHLTVodds();
		setCSGOLodds(csgl);
		setEsportsbets();
	}
	
	//this is if you do not want to set csgl
	public void setallinfo(){
		setteams();
		setmaps();
		setscore();
		setinfoandbestof();
		setStream();
		setHLTVodds();
		setEsportsbets();
	}
	
	public void setteams(){
		Elements elms = doc.select(".nolinkstyle");
		int i = 0;
		
		for(Element em : elms){
			if(i == 0){
				Team1 = em.text();
			}else if(i == 1){
				Team2 = em.text();
			}
			i++;
		}
	}
	
	public void setmaps(){
		String[] ary = new String[5];
		int i = 0;
		Element link = doc.select("div.hotmatchbox").first();
		String str1 = link.html();
		Document doc2 = Jsoup.parse(str1);
		Elements media = doc2.select("[src]");
		for (Element src : media) {
			if (src.tagName().equals("img")) {
				String tmp;
				tmp = (src.attr("abs:src"));
				tmp = tmp.replace("http://static.hltv.org//images/hotmatch/","");
				tmp = tmp.replace(".png", "");
				ary[i] = tmp;
				i = i + 1;
			}
			
		}
		map1 = ary[0];
		map2 = ary[1];
		map3 = ary[2];
	}
	
	public void setscore(){
		Element link = doc.select("div.hotmatchbox").first();
		String str1 = link.html();
		Document doc2 = Jsoup.parse(str1);
		
		Elements elms = doc2.select("div.hotmatchbox");
		
		String[] maps = new String[3];
		int i = 0;
		for (Element div : elms) {
			maps[i] = div.html();
			i++;
		}

		for(int l = 0; l < maps.length; l++){
			if(maps[l] != null){
				maps[l] = maps[l].replaceAll("color:", "");
				maps[l] = maps[l].replaceAll("[^0-9:;()]", "");			
			}
		}
		map1s = maps[0];
		map2s = maps[1];
		map3s = maps[2];
	}

	public void setinfoandbestof(){
		
		Element link2 = doc.select("#mapformatbox").first();
		String str = link2.html();
		str = str.replace("<br> ", "");
		info = str
				.replace(
						"<div style=\"border-top: 1px solid darkgray;margin-bottom: 3px;margin-top:3px;\"></div>",
						"");
		this.info = info;
		
		int i = info.toLowerCase().indexOf("Best of") + 9;
		this.bestof = info.substring(i, i+1);
	}

	public void setStream(){
		String prefix;
		List<String> vodsStreams = new ArrayList<String>();
		Elements elms;
		if (isover == true) {
			elms = doc.select(".vod");
			prefix = "http://www.hltv.org/?pageid=113&clean=1&getVodEmbed=";
		}else{
			elms = doc.select(".stream");
			prefix = "http://www.hltv.org/?pageid=113&clean=1&getStreamEmbed=";
		}

		for (Element em : elms) {
			vodsStreams.add(prefix + em.id());
		}
		Object[] ObjectList = vodsStreams.toArray();
		streams = Arrays.copyOf(ObjectList,ObjectList.length,String[].class);
		//will be size 0 if no steams/vods exist 
	}
	
	public void setHLTVodds(){
		Element votes1 = doc.select("#voteteam1results").first();
		Element votes2 = doc.select("#voteteam2results").first();
		HLTVodds1 = votes1.text();
		HLTVodds2 = votes2.text();
		//will be null if match is over
	}
	
	public void setCSGOLodds(Document csgl){
		Element box = csgl.select(".box").get(1);
		Elements matches = box.select(".match:not(.notavailable)");
		for(Element em : matches){
			//em.text().toLowerCase());
			int pos1 = em.text().toLowerCase().indexOf(Team1.toLowerCase());//team1
			if(!(pos1 == -1)){
				int pos2 = em.text().toLowerCase().indexOf(Team2.toLowerCase());//team2
				if(!(pos2 == -1)){
					//team1 at pos1
					//team2 at pos2
					
					if(pos1 > pos2){
						csgolodds1 = em.text().toLowerCase().substring(pos1 + Team2.length() + 1, pos1 + Team2.length() + 3);
						csgolodds2 = em.text().toLowerCase().substring(pos2 + Team1.length() + 1, pos2 + Team1.length() + 3);
						
					}else if(pos1 < pos2){
						csgolodds2 = em.text().toLowerCase().substring(pos2 + 1 + Team2.length(), pos2 + 3 + Team2.length());
						csgolodds1 = em.text().toLowerCase().substring(pos1 + Team1.length() + 1, pos1 + Team1.length() + 3);
					}
					
				}
				
			}
			
		}
		
	}
	
	public void setEsportsbets(){
		Element box = doc.select("a[href^=http://egamingbets.com]").first();
		Element Team1 = box.select("[style=text-align: left;]").first();
		Element Team2 = box.select("[style=text-align: right;]").first();
		ESportsgbets1 = Team1.text();
		ESportsgbets2 = Team2.text();
	}
	/////getters now
	
	public String[] getStream(){
		
		return streams;
	}
	
	public String getscore(int map){
		
		if(map == 1){
			return map1s;
		}else if(map == 2){
			return map2s;
		}else if (map == 3){
			return map3s;
		}
		return null;
		
	}

	public String getinfo(){
		return info;
	}
	
	public String getbestof(){
		return bestof;
	}

	public String getmap(int map){
		
		if(map == 1){
			return map1;
		}else if(map == 2){
			return map2;
		}else if (map == 3){
			return map3;
		}

		return null;
	}

	public String[] getteams(){
		String[] stra = {Team1, Team2};
		return stra;
	}
	
	public boolean getisover(){
		
		return isover;
	}
	
	public Date getDate(){
		return this.Time;
		
	}
	
	public String[] getHLTVodds(){
		String[] ar = new String[]{HLTVodds1, HLTVodds2};
		return ar;
	}
	
	public String[] getCSGLodds(){
		String[] ar = new String[]{csgolodds1, csgolodds2};
		return ar;
	} 
	
	public String[] getESportsgbets(){
		String[] ar = new String[]{ESportsgbets1, ESportsgbets2};
		return ar;
	} 
	
	public String GetEventName(){
		return Event;
	}
	
	//other methods

	private Date StringToDate(String str) throws ParseException{
		//string must be in format : "Thu, 08 Jan 2015 04:00:00 +0100" as from hltv xml
		String s = str.replaceAll("[a-zA-Z,]", "");
		s = s.replaceFirst(" ", "");
		s = s.replaceFirst(" ", "");
		
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d y H:m:s Z");
        Date date = simpleDateFormat.parse(s);
        return date;
	}
}

/* 
 * WORKING:::: bestof and info and maps and score and teams and STREAM/VOD and date and odds
 */
