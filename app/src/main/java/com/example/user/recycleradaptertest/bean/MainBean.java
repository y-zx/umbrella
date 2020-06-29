package com.example.user.recycleradaptertest.bean;

import java.util.List;

/**
 * @author: yangzhenxiang@km.com
 * @data: 2020-06-29 19:59
 * @descripton:
 * @version:
 */
public class MainBean {

    public SectionTitle section_title;

    public List<Book> books;


    public static class Book{
        public String image;
        public String title;
        public String tag;


        // 本地变量
        public String TYPE_LEFT="1";
        public String TYPE_RIGHT="2";

        public String type =TYPE_LEFT;
        public String section_type;
        public boolean lastOne;
    }

    public static class SectionTitle{
        public String title;
        public String type;
    }
}
