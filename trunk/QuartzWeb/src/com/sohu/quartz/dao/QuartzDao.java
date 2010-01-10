package com.sohu.quartz.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.StringUtils;
import com.sohu.Constant;

import javax.sql.DataSource;
import java.util.Map;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: SongJiao
 * Date: 2009-12-23
 * Time: 22:24:17
 * To change this template use File | Settings | File Templates.
 */
public class QuartzDao {


    private DataSource dataSource;

    @Autowired
    public void setDataSource(@Qualifier("dataSource") DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Map<String, Object>> getQrtzTriggers() {
        List<Map<String, Object>> results = getJdbcTemplate().queryForList("select * from QRTZ_TRIGGERS order by start_time");
        long val = 0;
        String temp = null;
        for (Map<String, Object> map : results) {
            temp = MapUtils.getString(map, "trigger_name");
            if(StringUtils.indexOf(temp, "&") != -1){
                map.put("display_name", StringUtils.substringBefore(temp, "&"));
            }else{
                map.put("display_name", temp);
            }

            val = MapUtils.getLong(map, "next_fire_time");
            if (val > 0) {
                map.put("next_fire_time", DateFormatUtils.format(val, "yyyy-MM-dd HH:mm:ss"));
            }

            val = MapUtils.getLong(map, "prev_fire_time");
            if (val > 0) {
                map.put("prev_fire_time", DateFormatUtils.format(val, "yyyy-MM-dd HH:mm:ss"));
            }

            val = MapUtils.getLong(map, "start_time");
            if (val > 0) {
                map.put("start_time", DateFormatUtils.format(val, "yyyy-MM-dd HH:mm:ss"));
            }

            val = MapUtils.getLong(map, "end_time");
            if (val > 0) {
                map.put("end_time", DateFormatUtils.format(val, "yyyy-MM-dd HH:mm:ss"));
            }

            map.put("statu", Constant.status.get(MapUtils.getString(map, "trigger_state")));
        }

        return results;
    }



    private JdbcTemplate getJdbcTemplate() {
        return new JdbcTemplate(this.dataSource);
    }

}
