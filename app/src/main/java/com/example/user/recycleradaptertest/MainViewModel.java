package com.example.user.recycleradaptertest;

import com.example.user.recycleradaptertest.bean.Constants;
import com.example.user.recycleradaptertest.bean.MainBean;
import com.example.user.recycleradaptertest.bean.MainResonpse;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: yangzhenxiang@km.com
 * @data: 2020-06-29 20:17
 * @descripton:
 * @version:
 */
public class MainViewModel {

    MainResonpse mainResonpse;

    public MainViewModel() {
        mainResonpse = new Gson().fromJson(Constants.DATA1, MainResonpse.class);
    }

    public List<Object> getData() {
        List<Object> mutiItemDataSource = new ArrayList<>();
        if (mainResonpse.sections != null && mainResonpse.sections.size() > 0) {
            for (MainBean section : mainResonpse.sections) {
                mutiItemDataSource.add(section.section_title);
                if (section.books != null && section.books.size() > 0) {
                    for (int i = 0; i < section.books.size(); i++) {
                        MainBean.Book book = section.books.get(i);
                        book.section_type = section.section_title.type;
                        if (i % 2 == 0) {
                            // 左边
                            book.type = book.TYPE_LEFT;
                        } else {
                            // 右边
                            book.type = book.TYPE_RIGHT;
                        }
                        book.lastOne = i == section.books.size() - 1;
                        mutiItemDataSource.add(book);
                    }
                }
            }
        }
        return mutiItemDataSource;
    }
}
