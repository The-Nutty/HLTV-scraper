package com.nutty.HLTV.data;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.xml.sax.SAXException;


public class Example {

	public static void main(String[] args){
		gethotmatches();
	}
	
	public static void gethotmatches(){
		HLTVXMLReader xml = new HLTVXMLReader();
		HLTVShort[] hotgames = null;
		try {
			hotgames = xml.GetGames(true);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Document doc = null;
		try {
			doc = Jsoup.connect(hotgames[1].getAddr()).userAgent("Mozilla").get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HLTVmatch game1 = new HLTVmatch(doc, hotgames[0].getEvent(), hotgames[0].getDateTime());
		game1.setallinfo();
		System.out.println(game1.getinfo());
		
		
	}
	
}
