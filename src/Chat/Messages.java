package Chat;

import java.util.ArrayList;
import java.util.Iterator;

public class Messages {

	private static int MAX_READ_MESSAGES = 100;
	private static int MAX_SEND_MESSAGES = 30;
	private static ArrayList<Message> pastReadMessages;
	private static ArrayList<Message> recentSentMessages;
	private static ArrayList<Message> queMessage;
	
	public Messages(){
		init();
	}
	private void init(){
		pastReadMessages = new ArrayList<Message>();
		recentSentMessages = new ArrayList<Message>();
		queMessage = new ArrayList<Message>();
	}
	public static void AddPastReadMessages(String sender, String message){
		//hold the last x messages sent
		//uses fifo
		if(pastReadMessages.size()>=MAX_READ_MESSAGES){
			pastReadMessages.remove(0);
		}
		pastReadMessages.add(new Message(sender,message));
	}
	public static void AddRecentSentMessages(String sender, String message){
		recentSentMessages.add(new Message(sender, message));
	}
	public static boolean Contains(String message, ArrayList<String> messages){
		for(String msg: messages){
			if(msg.equals(message))return true;
		}
		return false;
	}
	public static boolean canSendMessage(){
		updateSentMessages();
		if(recentSentMessages.size()>MAX_SEND_MESSAGES){
			return false;
		}else{
			return true;
		}
	}
	private static void updateSentMessages(){
		//write a routine that removes messages sent more than 30 seconds ago
		
		Iterator<Message> iMessage = recentSentMessages.iterator();
		while(iMessage.hasNext()){
			Message nextMessage = iMessage.next();
			
			if((int) (System.currentTimeMillis()-nextMessage.getTimeStamp())>((int) 30*1000)){
				//if current time in mili > message.time in mili 
				System.out.println("Removing message: "+nextMessage.getMessage());
				iMessage.remove();
			}
		}
		System.out.println("Messages updated.");
	}
	public static void QueMessage(String sender, String msg){
		System.out.println(sender+"'s message: "+msg+" has been queued");
		if(queMessage==null)queMessage = new ArrayList<Message>();
		queMessage.add(new Message(sender,msg));
	}
	public static ArrayList<Message> getQueuedMessages(){
		//eliminate the que and send its contents
		if(queMessage==null)queMessage = new ArrayList<Message>();
		ArrayList<Message> tempMessages = new ArrayList<Message>();
		tempMessages.addAll(queMessage);
		queMessage=null;
		return tempMessages;
	}
	
	//test methods
	public static Integer getNumRecentSent(){
		return recentSentMessages.size();
	}
			
}
