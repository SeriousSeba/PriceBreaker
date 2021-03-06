package pl.lazyteam.pricebreaker.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.lazyteam.pricebreaker.crawler.products.ProductUpdater;
import pl.lazyteam.pricebreaker.dao.ProductDAO;
import pl.lazyteam.pricebreaker.dao.ProductFlagsDao;
import pl.lazyteam.pricebreaker.entity.ProductFlags;
import pl.lazyteam.pricebreaker.entity.ProductInfo;
import pl.lazyteam.pricebreaker.entity.User;
import pl.lazyteam.pricebreaker.service.EmailService;
import pl.lazyteam.pricebreaker.service.UserServiceImpl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Component
public class ProductsScheduler {

    private static final Logger log = LoggerFactory.getLogger(ProductsScheduler.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Autowired
    ProductDAO productDAO;

    @Autowired
    EmailService emailService;

    @Autowired
    Environment env;

    @Autowired
    ProductFlagsDao productFlagsDao;

    @Autowired
    UserServiceImpl userService;



    @Scheduled(fixedDelay = 5000)
    public void reportCurrentTime() {

        List<ProductInfo>productInfos=productDAO.findAll();
        for(ProductInfo productInfo:productInfos)
        {
            System.out.println(dateFormat.format(productInfo.getLastUpdate()));
            if(!ProductUpdater.updateProduct(productInfo))
                productDAO.delete(productInfo.getId());
            productInfo.setLastUpdate(new Date());
            productDAO.save(productInfo);
            ProductFlags productFlags = productFlagsDao.getOne(productInfo.getId());
        }
    }

}
