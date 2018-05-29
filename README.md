# RecyclerDelegateAdapter
RecyclerView 代理适配器


use sample:

          RecyclerDelegateAdapter adapter = new RecyclerDelegateAdapter(this);
          recyclerView.setAdapter(adapter);

           //以下是 设置自己的item

          //item的个数 随数据源而定，布局为一种
          CommonItem<String> commonItem = new CommonItem<String>(R.layout.cell_main_recycler_item2) {
              @Override
              protected void convert(CommonViewHolder holder, int position, int positionAtTotal, String s) {
                  holder.setText(R.id.tv_main_recycler_item2, s);
              }
          };
          commonItem.setData(Arrays.asList(titles));

          //item 的个数 随数据源而定，布局为多种
          CommonMultipleItem<Integer> commonMultipleItem = new CommonMultipleItem<>();
          commonMultipleItem.registerMultileChildItem(commonMultipleItem.new MultipleChildItem(R.layout.cell_my_layout) {
              @Override
              protected boolean handleItem(Integer integer) {
                  return integer < 7;
              }

              @Override
              protected void convert(CommonViewHolder holder, int position, int positionAtTotal, Integer integer) {
                  holder.setText(R.id.btn, integer + "");

              }
          }).registerMultileChildItem(commonMultipleItem.new MultipleChildItem(R.layout.cell_my_layout2) {
              @Override
              protected boolean handleItem(Integer integer) {
                  return integer >= 7;
              }

              @Override
              protected void convert(CommonViewHolder holder, int position, int positionAtTotal, Integer integer) {
                  holder.setText(R.id.btn2, integer + "");
              }
          });
          commonMultipleItem.addData(Arrays.asList(ints));

          FooterItem footerItem = new FooterItem(R.layout.cell_my_footer) {
              @Override
              protected void convert(CommonViewHolder holder) {
                  holder.itemView.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                          adapter.setFooterStatusLoading();
                      }
                  });
              }

              @Override
              public FooterStatusChangedListener setFooterStatusChangedListener() {
                  return new FooterStatusChangedListener() {
                      @Override
                      public void loadComplete(CommonViewHolder holder) {
                          holder.setText(R.id.tv_footer_text, "加载更多")
                                  .setViewVisible(R.id.pb_footer_progress, View.GONE);
                      }

                      @Override
                      public void loading(CommonViewHolder holder) {
                          holder.setText(R.id.tv_footer_text, "正在加载")
                                  .setViewVisible(R.id.pb_footer_progress, View.VISIBLE);
                      }

                      @Override
                      public void loadError(CommonViewHolder holder) {
                          holder.setText(R.id.tv_footer_text, "网络异常")
                                  .setViewVisible(R.id.pb_footer_progress, View.GONE);
                      }

                      @Override
                      public void noMore(CommonViewHolder holder) {
                          holder.setText(R.id.tv_footer_text, "没有更多")
                                  .setViewVisible(R.id.pb_footer_progress, View.GONE);
                      }
                  };
              }
          };

          adapter.registerItem(new FixItem(R.layout.cell_main_recycler_item, 1)) //固定一个item
                  .registerItem(commonItem)
                  .registerItem(commonMultipleItem)
                  .registerItem(footerItem);

          adapter.notifyDataSetChanged();



        gradle库依赖： compile 'com.yzx.adapter:umbrella:1.0.3'