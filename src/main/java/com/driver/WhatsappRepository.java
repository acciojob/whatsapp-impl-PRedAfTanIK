package com.driver;

import java.time.LocalDate;
import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class WhatsappRepository {

    //Assume that each user belongs to at most one group
    //You can use the below mentioned hashmaps or delete these and create your own.
//    private HashMap<Group, List<User>> groupUserMap;
//    private HashMap<Group, List<Message>> groupMessageMap;
//    private HashMap<Message, User> senderMap;
//    private HashMap<Group, User> adminMap;
//    private HashSet<String> userMobile;

    private HashMap<String,User> userDb; //mob no. is key
    private HashMap<Group,List<User>> groupUserDb,chatUserDb; //for 2+ users and p chats
    private HashMap<User,Group> userIsPresentInGroup;
    private HashMap<Group, User> adminMap;
    private HashMap<Message,Message> messageDb;
    private List<Message> messageList;
    private HashMap<Message, User> senderMap;
    private HashMap<Group, List<Message>> groupMessageMap;
    private int customGroupCount;
    private int messageId;

    public WhatsappRepository(){
        userDb = new HashMap<>();
        groupUserDb = new HashMap<>();
        chatUserDb = new HashMap<>();
        userIsPresentInGroup = new HashMap<>();
        adminMap = new HashMap<>();
        messageDb = new HashMap<>();
        messageList = new ArrayList<>();
        senderMap = new HashMap<>();
        groupMessageMap = new HashMap<>();
        customGroupCount = 0;
        messageId = 0;
    }

    public boolean addUser(User user){
        if(userDb.containsKey(user.getMobile())) return false;
        userDb.put(user.getMobile(),user);
        return true;
    }
    public Group addGroup(List<User> list){
        if(list.size() == 2) return createChat(list);

        for(User user:list)
            if(userIsPresentInGroup.containsKey(user))
                return new Group();

        customGroupCount+=1;
        Group group = new Group("Group "+customGroupCount,list.size());
        adminMap.put(group,list.get(0));
        groupUserDb.put(group,list);
        for(User user:list) userIsPresentInGroup.put(user,group);
        return group;
    }
    public Group createChat(List<User> list){
        Group group = new Group(list.get(1).getName(),2);
        chatUserDb.put(group,list);
        adminMap.put(group,list.get(0));
        return group;
    }
    public int addMessage(String content){
        messageId+=1;
        Message message = new Message(messageId,content,new Date());
        messageDb.put(message,message);
        messageList.add(message);
        return messageId;
    }
    public int sendMessage(Message message, User sender, Group group){
        if(!groupUserDb.containsKey(group) && !chatUserDb.containsKey(group)) return -1;
        boolean isSenderInGroup = false;
        for(User user:groupUserDb.get(group)){
            if(user.equals(sender)){
                isSenderInGroup = true;
                break;
            }
        }
        if(!isSenderInGroup) return -2;
        groupMessageMap.putIfAbsent(group,new ArrayList<>());
        groupMessageMap.get(group).add(message);
        senderMap.put(message,sender);
        return groupMessageMap.get(group).size();
    }
    public int changeAdmin(User approver, User user, Group group){
        if(!adminMap.get(group).equals(approver)) return -1;
        boolean isUserInGroup = false;
        for(User member:groupUserDb.get(group)){
            if(member.equals(user)){
                isUserInGroup = true;
                break;
            }
        }
        if(!isUserInGroup) return -2;
        adminMap.put(group,user);
        return 1;
    }

}
