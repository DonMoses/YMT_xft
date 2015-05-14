package com.ymt.demo1.plates.exportConsult;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.ymt.demo1.R;
import com.ymt.demo1.adapter.ExportChatAdapter;
import com.ymt.demo1.beams.ChatBeam;
import com.ymt.demo1.beams.ChatMessage;
import com.ymt.demo1.beams.Export;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Dan on 2015/5/13
 */
public class ExportChatListFragment extends Fragment {

    public static final String FRAGMENT_TAG = "ExportChatFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_export_chat, container, false);
        initChatList(view);
        return view;
    }

    public static ExportChatListFragment newInstance(String emptyInfo) {
        ExportChatListFragment exportChatFragment = new ExportChatListFragment();
        Bundle args = new Bundle();
        args.putString("empty_info", emptyInfo);
        exportChatFragment.setArguments(args);
        return exportChatFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Bundle bundle = getArguments();
//        String emptyInfo = bundle.getString("empty_info");
    }

    /**
     * 初始化会话界面
     */
    protected void initChatList(View view) {
        ListView chatListView = (ListView) view.findViewById(R.id.export_chat_list_view);
        ExportChatAdapter chatAdapter = new ExportChatAdapter(getActivity());
        chatListView.setAdapter(chatAdapter);
        //todo 模拟会话数据
        ArrayList<ChatBeam> chatLists = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            ChatBeam chatBeam = new ChatBeam();
            /*
            添加专家
             */
            Export export = new Export();
            export.setName("Export" + String.valueOf(i));
            export.setBirthDay("1958年6月");
            export.setMajor("消防所干事");
            export.setIcon(BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.moses));
            chatBeam.setExport(export);
            /*
            添加会话
             */
            ArrayList<ChatMessage> messageArray = new ArrayList<>();
            //构造随机的消息数据
            for (int j = 0; j < 1 + new Random().nextInt(250 + 1); j++) {
                ChatMessage message = new ChatMessage();
                //todo 模拟msg类型
                if (j % 10 == 0) {
                    message.setMsgType(ChatMessage.ChatMsgType.IMG);
                } else if (j % 20 == 0) {
                    message.setMsgType(ChatMessage.ChatMsgType.MP3);
                } else {
                    message.setMsgType(ChatMessage.ChatMsgType.TXT);
                }

                switch (message.getMsgType()) {
                    case TXT:
                        message.setMsgTxt("this is example text for message" + j);
                        break;
                    case IMG:
                        message.setMsgImg(BitmapFactory.
                                decodeResource(getActivity().getResources(), R.drawable.moses));
                        break;
                    case MP3:
                        message.setMsgAudio(Environment.getExternalStorageDirectory().
                                getPath() + "xxf/msg/audio" + j + ".mp3");
                        break;
                    default:
                        break;
                }

                //todo 模拟已读、未读
                if ((j / 9) > 0 && (j % 9) == 0) {
                    message.setIsRead(false);
                } else {
                    message.setIsRead(true);
                }

                messageArray.add(message);

            }

            chatBeam.setMessages(messageArray);
            chatLists.add(chatBeam);
        }

        chatAdapter.setList(chatLists);

        /*
         * todo 会话列表点击事件
         */
        chatListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "export " + String.valueOf(position), Toast.LENGTH_SHORT).show();
            }
        });

    }

}
