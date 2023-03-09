package com.example.user.recycleradaptertest.bean;

import java.util.List;
import java.util.Objects;

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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Book book = (Book) o;
            return lastOne == book.lastOne
                    && Objects.equals(image, book.image)
                    && Objects.equals(title, book.title)
                    && Objects.equals(tag, book.tag)
                    && Objects.equals(TYPE_LEFT, book.TYPE_LEFT)
                    && Objects.equals(TYPE_RIGHT, book.TYPE_RIGHT)
                    && Objects.equals(type, book.type)
                    && Objects.equals(section_type, book.section_type);
        }

        @Override
        public int hashCode() {
            return Objects.hash(image, title, tag, TYPE_LEFT, TYPE_RIGHT, type, section_type, lastOne);
        }
    }

    public static class SectionTitle{
        public String title;
        public String type;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SectionTitle that = (SectionTitle) o;
            return Objects.equals(title, that.title) && Objects.equals(type, that.type);
        }

        @Override
        public int hashCode() {
            return Objects.hash(title, type);
        }
    }
}
