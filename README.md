# RecyclerMuitleAfapter
RecyclerView 的 单重，多重通用适配器


use sample:

LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        //通用适配器
        RecyclerCommonAdapter adapter = new RecyclerCommonAdapter(this);
        recyclerView.setAdapter(adapter);

        //布局类型1
        //RecyclerCommonAdapter.CommonItem<MyBean> 一般用于网络数据item展示, convert 中设置数据，点击事件等
        RecyclerCommonAdapter.CommonItem<String> item = new RecyclerCommonAdapter.CommonItem<String>(R.layout.cell_main_recycler_item) {
            @Override
            protected void convert(RecyclerCommonAdapter.ViewHolder holder, int position, int positionAtTotal, String s) {
                holder.setText(R.id.tv_main_recycler_item, s);
                //  getScopeStartPosition();----获取 该布局类型 起始position
                //  getScopeEndPosition()----获取 该布局类型 结束position
                //  getAdapter() 获取adapter
                //  getCount() 获取该类型布局 （注入的数据的总长度）


            }
        };
        //布局类型2  同一种item设置不用的布局資源也是不同的类型
        RecyclerCommonAdapter.CommonItem<Integer> item2 = new RecyclerCommonAdapter.CommonItem<Integer>(R.layout.cell_main_recycler_item2) {
            @Override
            protected void convert(RecyclerCommonAdapter.ViewHolder holder, int position, int positionAtTotal, Integer i) {
                holder.setText(R.id.tv_main_recycler_item2, "數據 = " + i);

            }
        };

        //布局类型3
        //RecyclerCommonAdapter.FixItem 一般用于固定长度布局，也可以用于网络数据布局 通过setCount改变该类型item个数
        RecyclerCommonAdapter.FixItem item1 = new RecyclerCommonAdapter.FixItem(R.layout.cell_my_layout, 2) {
            @Override
            protected void convert(RecyclerCommonAdapter.ViewHolder holder, final int position, final int positionAtTotal) {
                super.convert(holder, position, positionAtTotal);
                //設置點擊事件
                holder.getView(R.id.btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "相对位置 " + position + " 绝对位置 " + positionAtTotal, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };


        //布局类型3.... 等等等，可自定义，几乎可 用以上两个 item 完成全部布局


        //注册布局1，注册布局2
        adapter.registerItem(item).registerItem(item1).registerItem(item2);

        // 布局1 注入 数据， 数据类型声明泛型 RecyclerCommonAdapter.CommonItem<String> ，可以是任何类型
        item.addData(Arrays.asList(titles));

        //调用adapter 刷新界面   或者 adapter.notifyDataSetChanged(); 都行
        //item.notifyDataSetChanged();

        // 只刷新该类型全部的Item，其他类型不刷新
        //item.notifyRangeSetChanged();
        
        item2.addData(Arrays.asList(ints));

        item2.notifyDataSetChanged();