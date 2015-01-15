import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class HLTVXMLReader {
	
	public HLTVShort[] GetGames(boolean Hotgames) throws ParserConfigurationException, IOException, SAXException {

		// or if you prefer DOM:
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		
		URL url;
		if(Hotgames == true){
			url = new URL("http://www.hltv.org/hltv.rss.php?pri=15");
		}else{
			url = new URL("http://www.hltv.org/hltv.rss.php");
		}
		
		HttpURLConnection connection = (HttpURLConnection) url.openConnection(); 
		connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_5_8; en-US) AppleWebKit/532.5 (KHTML, like Gecko) Chrome/4.0.249.0 Safari/532.5");
		connection.setRequestMethod("GET");
		
		org.w3c.dom.Document doc = db.parse(connection.getInputStream());
		doc.getDocumentElement().normalize();
	    NodeList nList = doc.getElementsByTagName("item");
	    List<HLTVShort> Games = new ArrayList<HLTVShort>();
	    
	    for (int temp = 0; temp < nList.getLength(); temp++) {
	        Node nNode = nList.item(temp);

	        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	        	HLTVShort match = new HLTVShort();
	        	org.w3c.dom.Element eElement = (org.w3c.dom.Element) nNode;

	            match.setName(eElement.getElementsByTagName("title").item(0).getTextContent());
	            match.setAddr(eElement.getElementsByTagName("link").item(0).getTextContent());
	            match.setEvent(eElement.getElementsByTagName("description").item(0).getTextContent());
	            match.setDateTime(eElement.getElementsByTagName("pubDate").item(0).getTextContent());                
	            Games.add(match);
	        }
	    }
	    
	    Object[] ObjectList = Games.toArray();
	    HLTVShort[] GamesAr = Arrays.copyOf(ObjectList,ObjectList.length,HLTVShort[].class);
	    return GamesAr;
	  
	}
}
