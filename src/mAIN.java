import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class mAIN {

	public static void main(String[] args) throws ParseException,
			MalformedURLException, SAXException, IOException,
			ParserConfigurationException {

		String csgolodds1 = null, csgolodds2 = null;
		
		Document csgl = Jsoup.connect("http://csgolounge.com/").userAgent("Mozilla").get();
		
		Element box = csgl.select(".box").get(1);
		Elements matches = box.select(".match:not(.notavailable)");
		for(Element em : matches){
			//em.text().toLowerCase());
			int pos1 = em.text().toLowerCase().indexOf("nip".toLowerCase());//team1
			if(!(pos1 == -1)){
				int pos2 = em.text().toLowerCase().indexOf("epsilon".toLowerCase());//team2
				if(!(pos2 == -1)){
					//team1 at pos1
					//team2 at pos2
					
					if(pos1 > pos2){
						csgolodds1 = em.text().toLowerCase().substring(pos1 + "epsilon".length() + 1, pos1 + "epsilon".length() + 3);
						csgolodds2 = em.text().toLowerCase().substring(pos2 + "nip".length() + 1, pos2 + "nip".length() + 3);
						
					}else if(pos1 < pos2){
						csgolodds2 = em.text().toLowerCase().substring(pos2 + 1 + "epsilon".length(), pos2 + 3 + "epsilon".length());
						csgolodds1 = em.text().toLowerCase().substring(pos1 + "nip".length() + 1, pos1 + "nip".length() + 3);
					}
					///team1 % = pos1-3 team2 % = (pos2 + length +1)
					System.out.println(csgolodds1 + csgolodds2);
					
				}
				
			}
			
		}

		
		//for (Element em : elms) {
		//	vodsStreams.add(prefix + em.id());
		//}

		//System.out.println(vodsStreams.toString());

		// Document doc2 = Jsoup.parse(str1);
		// Element link2 = doc.select("#mapformatbox").first();
		// String str = link2.html();
		// System.out.println(str);

	}// use the output and sertch with in tha again

	public static String getinfo(Document doc) throws IOException {
		Element link2 = doc.select("#mapformatbox").first();
		String str = link2.html();
		str = str.replace("<br> ", "");
		return str = str
				.replace(
						"<div style=\"border-top: 1px solid darkgray;margin-bottom: 3px;margin-top:3px;\"></div>",
						"");
		// System.out.println(getinfo(doc));
	}

	public static String[] getmaps(Document doc) throws IOException {
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
				tmp = tmp.replace("http://static.hltv.org//images/hotmatch/",
						"");
				tmp = tmp.replace(".png", "");
				ary[i] = tmp;
				i = i + 1;
			}
		}

		return ary;

		// String[] ary = getmaps(doc);
		// System.out.println(ary[0]);
		// System.out.println(ary[1]);
		// System.out.println(ary[2]);
	}

}
