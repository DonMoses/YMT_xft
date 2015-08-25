package com.ymt.demo1.main.file;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.ymt.demo1.R;
import com.ymt.demo1.baseClasses.BaseFloatActivity;

import java.io.File;
import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by 丹 on 2014/5/23
 */
public class SearchFileActivity extends BaseFloatActivity {
    private static final int SEARCH_START = 0;
    private static final int SEARCH_DONE = 1;
    private EditText mSearchTxt;
    private Button mPathBtn;
    private ArrayList<File> files = new ArrayList<>();
    private SearchAdapter adapter;
    private String inputStr;

    private ArrayList<File> myFiles = new ArrayList<>();
    private MyHandler myHandler = new MyHandler(this);
    private NativeImageLoader nativeImageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_file);
        nativeImageLoader = NativeImageLoader.getInstance();
        Collections.addAll(files, new File("/storage").listFiles());    //得到files
        intiView();

    }

    /**
     * 初始化控件
     * 设定事件
     */
    public void intiView() {
        mSearchTxt = (EditText) findViewById(R.id.file_search_txt);
        Button mSearchBtn = (Button) findViewById(R.id.file_search_button);
        ListView mFileLV = (ListView) findViewById(R.id.list_view_search_file);
        ProgressBar progressBar = new ProgressBar(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        progressBar.setLayoutParams(params);
        mFileLV.setEmptyView(progressBar);
        mPathBtn = (Button) findViewById(R.id.path_button);

        adapter = new SearchAdapter(SearchFileActivity.this);
        mFileLV.setAdapter(adapter);
        adapter.setData(files);

        mPathBtn.setText(new File("/storage").getPath());

        /**
         * 通过mPathBtn上的路径文本判断点击操作应该获得的新的路径
         */
        mPathBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = mPathBtn.getText().toString();
                if (path.equals("/storage")) {
                    files.clear();
                    Collections.addAll(files, new File("/storage").listFiles());
                    adapter.setData(files);
                    mPathBtn.setText("/storage");
                } else if (!path.equals("文件路径:") && !path.equals("/storage")) {
                    String parent = new File(path).getParent();
                    files.clear();
                    Collections.addAll(files, new File(parent).listFiles());
                    adapter.setData(files);
                    mPathBtn.setText(parent);
                }
            }
        });

        /**
         * 通过mSearchTxt上的关键字，mSearchBtn相应搜索的文件类型
         */
        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filePath = mPathBtn.getText().toString();
                inputStr = mSearchTxt.getText().toString();

                if (inputStr.equals("请输入要查找的文件后缀") || inputStr.equals("")) {
                    Toast.makeText(SearchFileActivity.this, "无效输入!", Toast.LENGTH_SHORT).show();
                } else {
                    mSearchTxt.setText("searching files for you...");
                    new SearchThread(filePath).start();

                }

//                myHandler.sendEmptyMessage(SEARCH_START);
            }

        });

        /**
         * 判断mFileLV 元素类型，如果item是文件夹，则点击打开文件夹，显示新的列表
         *    如果是文件，则调用相关方法打开文件或跳转到其他页面，或执行其他操作
         */
        mFileLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File theFile = files.get(position);
                if (theFile.isDirectory()) {
                    files.clear();
                    Collections.addAll(files, theFile.listFiles());
                    adapter.setData(files);
                    mPathBtn.setText(theFile.getAbsolutePath());
                } else {
                    /*
                   todo 上传选中文件【通过io流传输文件到服务端】
                    */

                }
            }
        });

    }

    /**
     * 这里shi适配器
     * 这里我直接使用了ListFilesActivity中适配器item的view布局
     */
    public class SearchAdapter extends BaseAdapter {
        ArrayList<File> mData = new ArrayList<>();
        LayoutInflater inflater;

        public SearchAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void setData(ArrayList<File> mData) {
            this.mData = mData;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final Mp3ViewHolder viewHolder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.view_file_list, null);
                viewHolder = new Mp3ViewHolder();
                viewHolder.mp3Img = (ImageView) convertView.findViewById(R.id.fileIcon);
                viewHolder.mp3Name = (TextView) convertView.findViewById(R.id.fileName);
                viewHolder.mp3Size = (TextView) convertView.findViewById(R.id.fileCount);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (Mp3ViewHolder) convertView.getTag();
            }
            if (mData.get(position).isFile()) {
                viewHolder.mp3Size.setText(sizeFile(mData.get(position)));
                if (mData.get(position).getName().endsWith(".mp3")
                        || mData.get(position).getName().endsWith(".MP3")) {
                    viewHolder.mp3Img.setImageResource(R.drawable.icon_file_mp3);
                } else if (mData.get(position).getName().endsWith(".pdf")
                        || mData.get(position).getName().endsWith(".PDF")) {
                    viewHolder.mp3Img.setImageResource(R.drawable.icon_file_pdf);
                } else if (mData.get(position).getName().endsWith(".txt")
                        || mData.get(position).getName().endsWith(".TXT")) {
                    viewHolder.mp3Img.setImageResource(R.drawable.icon_file_txt);
                } else if (mData.get(position).getName().endsWith(".ppt")
                        || mData.get(position).getName().endsWith(".PPT")) {
                    viewHolder.mp3Img.setImageResource(R.drawable.icon_file_ppt);
                } else if (mData.get(position).getName().endsWith(".jpg")
                        || mData.get(position).getName().endsWith(".png")
                        || mData.get(position).getName().endsWith(".JPG")
                        || mData.get(position).getName().endsWith(".PNG")) {
//                    nativeImageLoader.loadNativeImage(mData.get(position).getPath(),
//                            new Point(0, 0), new NativeImageLoader.NativeImageCallBack() {
//                                @Override
//                                public void onImageLoader(Bitmap bitmap, String path) {
//                                    viewHolder.mp3Img.setImageBitmap(bitmap);
//                                }
//                            });
                    Picasso.with(SearchFileActivity.this).load(mData.get(position).getAbsoluteFile()).into(viewHolder.mp3Img);
                } else {
                    Picasso.with(SearchFileActivity.this).load(R.drawable.icon_help).into(viewHolder.mp3Img);
                }

            } else {
                viewHolder.mp3Img.setImageResource(R.drawable.documents_folder);
                viewHolder.mp3Size.setText(countFiles(mData.get(position)) + "个文件");
            }

            viewHolder.mp3Name.setText(mData.get(position).getName());
            return convertView;
        }

        class Mp3ViewHolder {
            ImageView mp3Img;
            TextView mp3Name;
            TextView mp3Size;
        }
    }

    /**
     * 封装1个方法，计算文件大小
     */
    public String sizeFile(File file) {
        String str = null;
        if (file.isFile()) {
            Long byteLen = file.length();
            float kBLen = byteLen / 1024;
            float mBLen = kBLen / 1024;
            if (kBLen < 1) {
                str = String.valueOf(byteLen) + "B";
            } else if (mBLen < 1) {
                str = String.valueOf(kBLen) + "KB";
            } else {
                String strMath = new DecimalFormat("#.00").format(mBLen);
                str = strMath + "MB";
            }
        }
        return str;
    }

    /**
     * 封装1个方法，计算文件夹里的文件个数
     */
    public String countFiles(File file) {
        String str = "";
        int i;
        if (file.isDirectory()) {
            i = file.listFiles().length;
            return str + String.valueOf(i);
        }
        return str;
    }

    /**
     * 递归列出所有文件
     */
    public void listAllFile(File theFileDirPath) {
        if (theFileDirPath.isDirectory()) {
            for (File file : theFileDirPath.listFiles()) {
                if (file.isFile()) {
                    myFiles.add(file);

                } else {
                    listAllFile(file);
                }
            }
        }
    }


    /**
     * 封装1个方法，搜索文件， 得到list数据源
     * 要求没搜索得到一个适当的文件，就要传递对应的指导空间上显示
     * <p/>
     * 使用Handler  发送数据
     */
    private void searchFiles(String theFileDirPath) {

        listAllFile(new File(theFileDirPath));

        files.clear();
        for (File file : myFiles) {
            if (file.getName().endsWith(inputStr)) {
                files.add(file);

            }
        }
        //这里不再刷新数据，而是改为在mHandler中刷新

    }

    /**
     * 因为搜索耗时，创建一个搜索文件的线程类  ，用来操作文件搜索
     */
    class SearchThread extends Thread {
        //重写run方法    因为是搜索文件，u所以需要在构造中传入一个（文件夹）路径
        String filePath;

        public SearchThread(String filePath) {
            this.filePath = filePath;
        }

        /**
         * Handler Message Thread Looper
         * 这里是用Handler + Message
         * 子线程中发送消息，   主线程（UI）h线程中接收消息
         */
        @Override
        public void run() {
            searchFiles(filePath);
            myHandler.sendEmptyMessage(SEARCH_DONE);
        }

    }

    static class MyHandler extends Handler {
        private WeakReference<SearchFileActivity> reference;

        public MyHandler(SearchFileActivity activity) {
            reference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            SearchFileActivity activity = reference.get();
            if (activity != null) {
                switch (msg.what) {
                    case SEARCH_START:
                        //这里刷新数据，而不在搜索文件的方法中刷新
                        String path = (String) msg.obj;
                        activity.handleSearchTxt(path);
                        break;
                    case SEARCH_DONE:
                        activity.handleFileAdapter();
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private void handleSearchTxt(String path) {
        mSearchTxt.setText(path);
    }

    private void handleFileAdapter() {
        adapter.setData(files);
        mSearchTxt.setText("");
    }


}
