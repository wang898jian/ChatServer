package com.yuanhong.listener;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONObject;

import com.yuanhong.util.MessageClass;
import com.yuanhong.util.MessageType;
import com.yuanhong.util.UserInfo;

//服务器窗口关闭时向所有用户发送消息
public class ServerWindowClosingListener extends WindowAdapter{
	private Map<String, UserInfo> allUserMap;
	
	public ServerWindowClosingListener(Map<String, UserInfo> allUserMap) {
		this.allUserMap = allUserMap;
	}
	
	@Override
	public void windowClosing(WindowEvent e) {
		super.windowClosing(e);
		for(Iterator ite = allUserMap.keySet().iterator();ite.hasNext();){
			MessageClass message;
			UserInfo userInfor_inner = null;
			try {						
				String theSendeUser = ite.next().toString();
				userInfor_inner = (UserInfo)allUserMap.get(theSendeUser);
				message = new MessageClass();		
				message.setMessType(MessageType.SERVERCLOSE);
				
				JSONObject json = new JSONObject(message.getJsonMap());
				
				Socket soc = new Socket(userInfor_inner.getAddress(), userInfor_inner.getPort());
				OutputStreamWriter output = new OutputStreamWriter(soc.getOutputStream());
				output.write(json.toString());
				output.close();
				soc.close();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
}
