package pl.lazyteam.pricebreaker.crawler.products;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import pl.lazyteam.pricebreaker.entity.ProductInfo;

import java.io.IOException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProductUpdater {

     public static boolean updateProduct(ProductInfo productInfo){
         String url=productInfo.getProductUrl();

         Connection.Response response= null;
         Document document=null;
         try {
             Connection connection= Jsoup.connect(url).
                     timeout(0).
                     ignoreContentType(true);
             response = connection.execute();
             document=response.parse();
         } catch (IOException e) {
             e.printStackTrace();
             return false;
         }


         String html=document.outerHtml();
         Pattern pattern=Pattern.compile("\"minPrice\",\"(.*)\"");
         Matcher matcher=pattern.matcher(html);
         String string=null;
         while (matcher.find()){
             string=matcher.group(1);
         }

         double result=Double.parseDouble(string);

         productInfo.setProductBottom(result);
         return true;
    }

}
