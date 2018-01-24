package pl.lazyteam.pricebreaker.crawler.products;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import pl.lazyteam.pricebreaker.entity.ProductInfo;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Static class which helps other components to access
 * certain infromations from saved product infromations.
 * Mostly working on their urls and gathers data from
 * hrml sources
 */
public class ProductUpdater {

    /**
     * Update given products price in givne ProductInfo reference
     * Works on product reference and changes its values
     * @param productInfo ProductInfo class to be updated
     * @return True if succes false otherwise
     */
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
