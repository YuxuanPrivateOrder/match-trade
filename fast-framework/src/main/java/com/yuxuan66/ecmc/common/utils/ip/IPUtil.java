package com.yuxuan66.ecmc.common.utils.ip;

import com.yuxuan66.ecmc.common.utils.entity.IPInfo;
import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.xdb.Searcher;

import java.util.concurrent.TimeUnit;

/**
 * @author Sir丶雨轩
 * @since 2023/9/7
 */
@Slf4j
public class IPUtil {

    /**
     * ip2region
     */
    private static Searcher searcher;

    static {
        byte[] cBuff = new byte[0];

        String dbPath = "/Users/yuxuan/Develop/Java/ef-match-trade/fast-framework/src/main/resources/ip2region/ip2region.xdb";
        try {
            cBuff = Searcher.loadContentFromFile(dbPath);
        } catch (Exception e) {
            // 换成logger
            log.error("Failed to load content from `{}`: {}", dbPath, e.getMessage());
        }
        try {
            searcher = Searcher.newWithBuffer(cBuff);
        } catch (Exception e) {
            log.error("failed to create content cached searcher: {}\n", e.getMessage());
        }
    }

    /**
     * 根据ip获取地址
     *
     * @param ip ip地址
     * @return 地址
     */
    public static IPInfo searcher(String ip) {
        try {
            long sTime = System.nanoTime();
            String region = searcher.search(ip);
            long cost = TimeUnit.NANOSECONDS.toMicros(System.nanoTime() - sTime);
            log.debug("{region: {}, ioCount: {}, took: {} μs}\n", region, searcher.getIOCount(), cost);
            String[] regionData = region.split("\\|");
            // 获取新创建的Bean
            return new IPInfo(ip,regionData[0], regionData[1], regionData[2], regionData[3], regionData[4]);
        } catch (Exception e) {
            log.error("failed to search({}): {}\n", ip, e.getMessage());
        }
        return new IPInfo();
    }

}
