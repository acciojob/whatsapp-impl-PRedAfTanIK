package com.driver;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WhatsappService {
    WhatsappRepository repository = new WhatsappRepository();

    public boolean createUser(String name, String mobile){
        return repository.addUser(new User(name,mobile));
    }
    public Group createGroup(List<User> users){
        return repository.addGroup(users);
    }
    public int createMessage(String content){
        return repository.addMessage(content);
    }
    public int sendMessage(Message message, User sender, Group group){
        return repository.sendMessage(message,sender,group);
    }
    public int  changeAdmin(User approver, User user, Group group){
        return repository.changeAdmin(approver,user,group);
    }
}
