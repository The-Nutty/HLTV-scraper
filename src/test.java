import java.io.IOException;
import java.text.SimpleDateFormat;

import javax.xml.parsers.ParserConfigurationException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.xml.sax.SAXException;


public class test {
	
	public static void main(String[] args) throws IOException{
		
		Document doc = Jsoup.connect("http://www.hltv.org/match/2293871-nip-ggwp-sltv-starseries-xii").userAgent("Mozilla").get();
		HLTVmatch m1 = new HLTVmatch(doc, "event", "Thu, 08 Jan 2015 04:00:00 +0100");
		
		m1.setteams();
		m1.setCSGOLodds(Jsoup.connect("http://csgolounge.com/").userAgent("Mozilla").get());
		System.out.println(m1.getCSGLodd()[1]);
		
		//System.out.println(str);
		//SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d y H:m:s Z");
		//System.out.println("date : "+simpleDateFormat.format(m1.getDate()));
		
	
	}

}
