package com.ymt.demo1.adapter;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ymt.demo1.R;
import com.ymt.demo1.beams.FullSearchItem;
import com.ymt.demo1.utils.BaseURLUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 2015/7/22
 */
public class AllSearchListAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<FullSearchItem> list = new ArrayList<>();
    private String keyWord;

    public void setList(List<FullSearchItem> list, String keyWord) {
        this.list = list;
        this.keyWord = keyWord;
        notifyDataSetChanged();
    }

    public AllSearchListAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_all_search_knowledge_result, null);
            holder = new ViewHolder();
            holder.resultTitle = (TextView) convertView.findViewById(R.id.result_title);
            holder.resultType = (TextView) convertView.findViewById(R.id.result_type);
            holder.resultFile = (TextView) convertView.findViewById(R.id.result_file);
            holder.resultTime = (TextView) convertView.findViewById(R.id.result_time);
            holder.resultContent = (WebView) convertView.findViewById(R.id.result_content);
            holder.resultDownload = (TextView) convertView.findViewById(R.id.result_download);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        final String fileName = list.get(position).getDoc_title() + ".pdf";

        if (TextUtils.isEmpty(list.get(position).getDoc_url_app())) {
            holder.resultDownload.setVisibility(View.INVISIBLE);
        } else {
            holder.resultDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "开始下载...", Toast.LENGTH_SHORT).show();
                    downloadBZGFFile(fileName, list.get(position).getDoc_url_app());
                }
            });
        }

        String type;//1、咨询； 2、知识；3、论坛；4、教育；5、商品，6资讯
        holder.resultTitle.setText(list.get(position).getDoc_title());
        switch (list.get(position).getDoc_type()) {
            case "1":
                type = "咨询";
                break;
            case "2":
                type = "知识";
                break;
            case "3":
                type = "论坛";
                break;
            case "4":
                type = "教育";
                break;
            case "5":
                type = "商品";
                break;
            case "6":
                type = "资讯";
                break;
            default:
                type = "全文检索";
                break;
        }
        holder.resultType.setText("分类：" + type);
        String filePath = list.get(position).getDoc_url_app();
        if ((!TextUtils.isEmpty(fileName)) && (!TextUtils.isEmpty(filePath))) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            holder.resultFile.setLayoutParams(params);
            holder.resultFile.setText("附件：" + fileName);
        } else {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
            holder.resultFile.setLayoutParams(params);
        }
        holder.resultTime.setText(list.get(position).getCreate_date() + "上传");

        int length = keyWord.length();
        int index = list.get(position).getDoc_content().indexOf(keyWord);
        if (index >= 0) {
            SpannableString spannableString = new SpannableString(list.get(position).getDoc_content());
            BackgroundColorSpan backgroundColorSpan = new BackgroundColorSpan(context.getResources().getColor(android.R.color.darker_gray));
            spannableString.setSpan(backgroundColorSpan, index, index + length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.resultContent.loadDataWithBaseURL(null, spannableString.toString(), "text/html", "utf-8", null);
        }

        return convertView;
    }

    class ViewHolder {
        TextView resultTitle;
        TextView resultType;
        TextView resultFile;
        TextView resultTime;
        WebView resultContent;
        TextView resultDownload;
    }

    protected void downloadBZGFFile(String name, String pdf_id) {
        StringBuilder fNameBuilder = new StringBuilder(name);

        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri;
        uri = Uri.parse(BaseURLUtil.getKnowledgeFileUrl(pdf_id));
//        Uri uri = Uri.parse("http://tingge.5nd.com/20060919/2015/2015-7-8/67322/1.Mp3");
        DownloadManager.Request request = new DownloadManager.Request(uri);

        //设置允许使用的网络类型，这里是移动网络和wifi都可以
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);

        //禁止发出通知，既后台下载，如果要使用这一句必须声明一个权限：android.permission.DOWNLOAD_WITHOUT_NOTIFICATION
        //request.setShowRunningNotification(false);

        //不显示下载界面
        //request.setVisibleInDownloadsUi(false);
        /*设置下载后文件存放的位置,如果sdcard不可用，那么设置这个将报错，因此最好不设置如果sdcard可用，下载后的文件        在/mnt/sdcard/Android/data/packageName/files目录下面，如果sdcard不可用,设置了下面这个将报错，不设置，下载后的文件在/cache这个  目录下面*/
        request.setDestinationInExternalFilesDir(context, null, fNameBuilder.toString());

        long id = downloadManager.enqueue(request);
        //TODO 把id保存好，在接收者里面要用，最好保存在Preferences里面
    }
}
